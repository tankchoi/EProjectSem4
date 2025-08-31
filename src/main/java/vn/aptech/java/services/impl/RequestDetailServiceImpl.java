package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.aptech.java.dtos.admin.CreateRequestDetailDTO;
import vn.aptech.java.dtos.admin.UpdatePartDTO;
import vn.aptech.java.models.Part;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.RequestDetail;
import vn.aptech.java.repositories.RequestDetailRepository;
import vn.aptech.java.services.PartService;
import vn.aptech.java.services.RequestDetailService;
import vn.aptech.java.services.RequestService;

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
    @Transactional
    public void createRequestDetail(CreateRequestDetailDTO dto) {
        RequestDetail requestDetail = new RequestDetail();
        requestDetail.setRequest(
                requestService.getRequestById(dto.getRequestId())
                        .orElseThrow(() -> new IllegalArgumentException("Yêu cầu không tồn tại."))
        );
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

}
