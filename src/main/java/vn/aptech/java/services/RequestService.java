package vn.aptech.java.services;

import vn.aptech.java.dtos.client.WarrantyRequestDTO;
import vn.aptech.java.models.Request;
import java.util.List;

public interface RequestService {
    void createScheduleRequest(WarrantyRequestDTO dto);
    List<Request> getHistoryByCustomerId(Long customerId);
}
