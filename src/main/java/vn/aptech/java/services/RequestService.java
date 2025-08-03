package vn.aptech.java.services;

import vn.aptech.java.dtos.client.WarrantyRequestDTO;

public interface RequestService {
    void createScheduleRequest(WarrantyRequestDTO dto);
}
