package vn.aptech.java.services;

import vn.aptech.java.dtos.admin.CreateRequestDetailDTO;
import vn.aptech.java.dtos.admin.UpdateRequestDetailDTO;
import vn.aptech.java.models.RequestDetail;

import java.util.List;
import java.util.Optional;

public interface RequestDetailService {
    RequestDetail createRequestDetail(CreateRequestDetailDTO dto);

    void updateRequestDetail(UpdateRequestDetailDTO dto);

    Optional<RequestDetail> getRequestDetailById(Long id);

    List<RequestDetail> getRequestDetails();

    void deleteRequestDetail(Long id);

    List<RequestDetail> getRequestDetailsByRequestId(Long requestId);

    long countByRequestId(Long requestId);

    double getTotalValueByRequestId(Long requestId);
}
