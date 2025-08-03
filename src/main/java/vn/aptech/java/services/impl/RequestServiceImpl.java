package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.dtos.client.WarrantyRequestDTO;
import vn.aptech.java.models.CustomerLaptop;
import vn.aptech.java.models.Request;
import vn.aptech.java.repositories.CustomerLaptopRepository;
import vn.aptech.java.repositories.RequestRepository;
import vn.aptech.java.services.RequestService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private CustomerLaptopRepository customerLaptopRepository;

    @Override
    public void createScheduleRequest(WarrantyRequestDTO dto) {
        Request request = new Request();
        request.setFullname(dto.getFullname());
        request.setPhone(dto.getPhone());
        request.setEmail(dto.getEmail());
        request.setAddress(dto.getAddress());
        request.setDescription(dto.getDescription());
        request.setBookingDate(Date.valueOf(LocalDate.now()));
        request.setStatus("Chờ Xử Lý");

        if (dto.getSerialNumber() != null && !dto.getSerialNumber().isBlank()) {
            CustomerLaptop cl = customerLaptopRepository.findBySerial(dto.getSerialNumber().trim());

            if (cl == null) {
                throw new IllegalArgumentException("Số sê-ri không tồn tại hoặc không hợp lệ.");
            }

            request.setCustomerLaptop(cl);
        }

        requestRepository.save(request);
    }

    @Override
    public List<Request> getHistoryByCustomerId(Long customerId) {
        return requestRepository.getHistoryByCustomerId(customerId);
    }
}
