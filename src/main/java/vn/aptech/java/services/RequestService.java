package vn.aptech.java.services;

import vn.aptech.java.dtos.client.WarrantyRequestDTO;
import vn.aptech.java.models.Request;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface RequestService {
    void createScheduleRequest(WarrantyRequestDTO dto, MultipartFile[] images) throws IOException;
    List<Request> getHistoryByCustomerId(Long customerId);
}
