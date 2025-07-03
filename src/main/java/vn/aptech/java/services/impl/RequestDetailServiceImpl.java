package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.aptech.java.dtos.CreateRequestDetailDTO;
import vn.aptech.java.dtos.UpdateRequestDetailDTO;
import vn.aptech.java.models.Part;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.RequestDetail;
import vn.aptech.java.repositories.PartRepository;
import vn.aptech.java.repositories.RequestDetailRepository;
import vn.aptech.java.repositories.RequestRepository;
import vn.aptech.java.services.InvoiceService;
import vn.aptech.java.services.RequestDetailService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RequestDetailServiceImpl implements RequestDetailService {

    @Autowired
    private RequestDetailRepository requestDetailRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Override
    public List<RequestDetail> getAllRequestDetails() {
        return requestDetailRepository.findAll();
    }

    @Override
    public Page<RequestDetail> getAllRequestDetails(Pageable pageable) {
        return requestDetailRepository.findAll(pageable);
    }

    @Override
    public Page<RequestDetail> searchRequestDetails(String search, Pageable pageable) {
        return requestDetailRepository.findBySearch(search, pageable);
    }

    @Override
    public Optional<RequestDetail> getRequestDetailById(Long id) {
        return requestDetailRepository.findById(id);
    }

    @Override
    public RequestDetail getRequestDetailByIdOrThrow(Long id) {
        return requestDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết yêu cầu với ID: " + id));
    }

    @Override
    public List<RequestDetail> getRequestDetailsByRequestId(Long requestId) {
        return requestDetailRepository.findByRequestId(requestId);
    }

    @Override
    public List<RequestDetail> getRequestDetailsByPartId(Long partId) {
        return requestDetailRepository.findByPartId(partId);
    }

    @Override
    public RequestDetail createRequestDetail(CreateRequestDetailDTO createRequestDetailDTO) {
        // Validate request exists
        Request request = requestRepository.findById(createRequestDetailDTO.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy yêu cầu với ID: " + createRequestDetailDTO.getRequestId()));

        // Validate part exists
        Part part = partRepository.findById(createRequestDetailDTO.getPartId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy linh kiện với ID: " + createRequestDetailDTO.getPartId()));

        // Check if this combination already exists
        RequestDetail existingDetail = requestDetailRepository.findByRequestIdAndPartId(
                createRequestDetailDTO.getRequestId(),
                createRequestDetailDTO.getPartId());

        if (existingDetail != null) {
            throw new IllegalArgumentException(
                    "Linh kiện này đã được thêm vào yêu cầu. Vui lòng cập nhật số lượng thay vì tạo mới.");
        }

        // Validate part quantity availability
        if (part.getQuantity() < createRequestDetailDTO.getQuantity()) {
            throw new IllegalArgumentException("Không đủ số lượng linh kiện trong kho. Có sẵn: " + part.getQuantity()
                    + ", Yêu cầu: " + createRequestDetailDTO.getQuantity());
        }

        // Update part quantity
        validateAndUpdatePartQuantity(part.getId(), createRequestDetailDTO.getQuantity());

        // Create new request detail
        RequestDetail newRequestDetail = new RequestDetail();
        newRequestDetail.setRequest(request);
        newRequestDetail.setPart(part);
        newRequestDetail.setQuantity(createRequestDetailDTO.getQuantity());

        RequestDetail savedRequestDetail = requestDetailRepository.save(newRequestDetail);

        // Tự động cập nhật hóa đơn nếu có
        updateInvoiceIfExists(createRequestDetailDTO.getRequestId());

        return savedRequestDetail;
    }

    @Override
    public RequestDetail updateRequestDetail(Long id, UpdateRequestDetailDTO updateRequestDetailDTO) {
        RequestDetail existingDetail = getRequestDetailByIdOrThrow(id);

        // Validate request exists
        Request request = requestRepository.findById(updateRequestDetailDTO.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy yêu cầu với ID: " + updateRequestDetailDTO.getRequestId()));

        // Validate part exists
        Part part = partRepository.findById(updateRequestDetailDTO.getPartId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy linh kiện với ID: " + updateRequestDetailDTO.getPartId()));

        // If part or request changed, check for duplicates
        if (!existingDetail.getRequest().getId().equals(updateRequestDetailDTO.getRequestId()) ||
                !existingDetail.getPart().getId().equals(updateRequestDetailDTO.getPartId())) {

            RequestDetail duplicateDetail = requestDetailRepository.findByRequestIdAndPartId(
                    updateRequestDetailDTO.getRequestId(),
                    updateRequestDetailDTO.getPartId());

            if (duplicateDetail != null && !duplicateDetail.getId().equals(id)) {
                throw new IllegalArgumentException("Linh kiện này đã tồn tại trong yêu cầu.");
            }
        }

        // Calculate quantity difference
        Integer oldQuantity = existingDetail.getQuantity();
        Integer newQuantity = updateRequestDetailDTO.getQuantity();
        Integer quantityDifference = newQuantity - oldQuantity;

        // If quantity increased, check availability
        if (quantityDifference > 0) {
            if (part.getQuantity() < quantityDifference) {
                throw new IllegalArgumentException("Không đủ số lượng linh kiện trong kho. Có sẵn: "
                        + part.getQuantity() + ", Cần thêm: " + quantityDifference);
            }
            validateAndUpdatePartQuantity(part.getId(), quantityDifference);
        }
        // If quantity decreased, restore to inventory
        else if (quantityDifference < 0) {
            restorePartQuantity(part.getId(), Math.abs(quantityDifference));
        }

        // Update request detail
        existingDetail.setRequest(request);
        existingDetail.setPart(part);
        existingDetail.setQuantity(newQuantity);

        // Update invoice if exists
        updateInvoiceIfExists(request.getId());

        return requestDetailRepository.save(existingDetail);
    }

    @Override
    public void deleteRequestDetail(Long id) {
        RequestDetail requestDetail = getRequestDetailByIdOrThrow(id);
        Long requestId = requestDetail.getRequest().getId();

        // Restore part quantity to inventory
        restorePartQuantity(requestDetail.getPart().getId(), requestDetail.getQuantity());

        requestDetailRepository.deleteById(id);

        // Tự động cập nhật hóa đơn nếu có
        updateInvoiceIfExists(requestId);
    }

    @Override
    public void deleteRequestDetailsByRequestId(Long requestId) {
        List<RequestDetail> requestDetails = getRequestDetailsByRequestId(requestId);

        // Restore all part quantities
        for (RequestDetail detail : requestDetails) {
            restorePartQuantity(detail.getPart().getId(), detail.getQuantity());
        }

        requestDetailRepository.deleteByRequestId(requestId);
    }

    @Override
    public Double getTotalValueByRequestId(Long requestId) {
        Double total = requestDetailRepository.getTotalValueByRequestId(requestId);
        return total != null ? total : 0.0;
    }

    @Override
    public Long getTotalQuantityByPartId(Long partId) {
        return requestDetailRepository.getTotalQuantityByPartId(partId);
    }

    @Override
    public boolean isPartInUse(Long partId) {
        return requestDetailRepository.existsByPartId(partId);
    }

    @Override
    public List<Object[]> getTopUsedParts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return requestDetailRepository.getTopUsedParts(pageable);
    }

    @Override
    public void validateAndUpdatePartQuantity(Long partId, Integer quantityUsed) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy linh kiện với ID: " + partId));

        if (part.getQuantity() < quantityUsed) {
            throw new IllegalArgumentException("Không đủ số lượng linh kiện trong kho. Có sẵn: " + part.getQuantity()
                    + ", Yêu cầu: " + quantityUsed);
        }

        part.setQuantity(part.getQuantity() - quantityUsed);
        partRepository.save(part);
    }

    @Override
    public void restorePartQuantity(Long partId, Integer quantityToRestore) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy linh kiện với ID: " + partId));

        part.setQuantity(part.getQuantity() + quantityToRestore);
        partRepository.save(part);
    }

    /**
     * Tự động cập nhật tổng tiền hóa đơn nếu có
     */
    private void updateInvoiceIfExists(Long requestId) {
        invoiceService.updateInvoiceTotalIfExists(requestId);
    }
}
