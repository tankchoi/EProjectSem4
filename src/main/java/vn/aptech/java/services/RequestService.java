package vn.aptech.java.services;

import vn.aptech.java.dtos.admin.CreateRequestDTO;
import vn.aptech.java.dtos.admin.UpdateRequestDTO;
import vn.aptech.java.dtos.client.WarrantyRequestDTO;
import vn.aptech.java.models.Request;
import java.util.List;
import java.util.Optional;

public interface RequestService {
    void createScheduleRequest(WarrantyRequestDTO dto);
    List<Request> getHistoryByCustomerId(Long customerId);

    void createRequest(CreateRequestDTO dto);
    Optional<Request> getRequestById(Long id);
    void updateRequest(UpdateRequestDTO dto);
}
