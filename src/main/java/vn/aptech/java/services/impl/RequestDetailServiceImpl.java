package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.aptech.java.dtos.admin.CreateRequestDetailDTO;
import vn.aptech.java.dtos.admin.UpdateRequestDetailDTO;
import vn.aptech.java.models.Part;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.RequestDetail;
import vn.aptech.java.repositories.RequestDetailRepository;
import vn.aptech.java.services.PartService;
import vn.aptech.java.services.RequestDetailService;
import vn.aptech.java.services.RequestService;

import java.util.List;
import java.util.Optional;

@Service
public class RequestDetailServiceImpl implements RequestDetailService {
    @Autowired
    private RequestDetailRepository requestDetailRepository;
    @Autowired
    private PartService partService;
    @Autowired
    private RequestService requestService;

    private boolean checkExistRequestDetail(Long requestId, Long partId) {
        return requestDetailRepository.existsByRequestIdAndPartId(requestId, partId);
    }

    @Override
    public Optional<RequestDetail> getRequestDetailById(Long id) {
        return requestDetailRepository.findById(id);
    }

    @Override
    @Transactional
    public void createRequestDetail(CreateRequestDetailDTO dto) {
        RequestDetail requestDetail = new RequestDetail();
        requestDetail.setRequest(
                requestService.getRequestById(dto.getRequestId())
                        .orElseThrow(() -> new IllegalArgumentException("Yêu cầu không tồn tại.")));
        Part part = partService.lockPartById(dto.getPartId())
                .orElseThrow(() -> new IllegalArgumentException("Linh kiện không tồn tại."));
        if (part.getQuantity() < dto.getQuantity()) {
            throw new IllegalArgumentException("Số lượng linh kiện không đủ.");
        }
        if (checkExistRequestDetail(dto.getRequestId(), dto.getPartId())) {
            throw new IllegalArgumentException("Linh kiện đã tồn tại trong yêu cầu. Vui lòng cập nhật số lượng.");
        }
        part.setQuantity(part.getQuantity() - dto.getQuantity());
        requestDetail.setPart(part);
        requestDetail.setQuantity(dto.getQuantity());
        requestDetail.setPrice(part.getPrice());
        requestDetailRepository.save(requestDetail);
    }

    @Override
    @Transactional
    public void updateRequestDetail(UpdateRequestDetailDTO dto) {
        RequestDetail requestDetail = requestDetailRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Chi tiết yêu cầu không tồn tại."));
        Part oldPart = partService.lockPartById(requestDetail.getPart().getId())
                .orElseThrow(() -> new IllegalArgumentException("Linh kiện cũ không tồn tại."));
        Request request = requestService.getRequestById(dto.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Yêu cầu không tồn tại."));
        if (!dto.getPartId().equals(oldPart.getId())) {
            oldPart.setQuantity(oldPart.getQuantity() + requestDetail.getQuantity());
            Part newPart = partService.lockPartById(dto.getPartId())
                    .orElseThrow(() -> new IllegalArgumentException("Linh kiện mới không tồn tại."));
            if (newPart.getQuantity() < dto.getQuantity()) {
                throw new IllegalArgumentException("Số lượng linh kiện mới không đủ.");
            }
            newPart.setQuantity(newPart.getQuantity() - dto.getQuantity());
            requestDetail.setPart(newPart);
            requestDetail.setQuantity(dto.getQuantity());
            requestDetail.setPrice(newPart.getPrice());
            requestDetail.setRequest(request);
        } else {
            int oldQty = requestDetail.getQuantity();
            int newQty = dto.getQuantity();
            if (newQty > oldQty) {
                int diff = newQty - oldQty;
                if (oldPart.getQuantity() < diff) {
                    throw new IllegalArgumentException("Số lượng linh kiện không đủ để tăng.");
                }
                oldPart.setQuantity(oldPart.getQuantity() - diff);
            } else if (newQty < oldQty) {
                int diff = oldQty - newQty;
                oldPart.setQuantity(oldPart.getQuantity() + diff);
            }
            requestDetail.setQuantity(newQty);
            requestDetail.setPrice(oldPart.getPrice());
            requestDetail.setRequest(request);
        }
        requestDetailRepository.save(requestDetail);
    }

    @Override
    public List<RequestDetail> getRequestDetails() {
        return requestDetailRepository.findAll(Sort.by(Sort.Direction.DESC, "requestId"));
    }

    @Override
    @Transactional
    public void deleteRequestDetail(Long id) {
        RequestDetail requestDetail = requestDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chi tiết yêu cầu không tồn tại."));
        Part part = partService.lockPartById(requestDetail.getPart().getId())
                .orElseThrow(() -> new IllegalArgumentException("Linh kiện không tồn tại."));
        part.setQuantity(part.getQuantity() + requestDetail.getQuantity());
        requestDetailRepository.delete(requestDetail);
    }

    @Override
    public List<RequestDetail> getRequestDetailsByRequestId(Long requestId) {
        return requestDetailRepository.findByRequestId(requestId);
    }

}
