package vn.aptech.java.services;

import vn.aptech.java.dtos.admin.CreateRequestDTO;
import vn.aptech.java.dtos.admin.UpdateRequestDTO;
import vn.aptech.java.dtos.client.WarrantyRequestDTO;
import vn.aptech.java.models.Request;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface RequestService {
    void createScheduleRequest(WarrantyRequestDTO dto, MultipartFile[] images) throws IOException;

    List<Request> getHistoryByCustomerId(Long customerId);

    void createRequest(CreateRequestDTO dto);

    Optional<Request> getRequestById(Long id);

    void updateRequest(UpdateRequestDTO dto);

    List<Request> getRequests(String fullname, String phone, String email, String serialNumber, Long technicianId,
            Request.Status status);

    void deleteRequest(Long id);

    long count();

    long countByStatus(Request.Status status);
}
