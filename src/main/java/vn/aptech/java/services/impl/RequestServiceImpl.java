package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vn.aptech.java.dtos.client.WarrantyRequestDTO;
import vn.aptech.java.models.CustomerLaptop;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.RequestImg;
import vn.aptech.java.repositories.CustomerLaptopRepository;
import vn.aptech.java.repositories.RequestRepository;
import vn.aptech.java.repositories.RequestImgRepository;
import vn.aptech.java.services.RequestService;
import vn.aptech.java.utils.ImgUploadUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private CustomerLaptopRepository customerLaptopRepository;

    @Autowired
    private RequestImgRepository requestImgRepository;

    @Override
    public void createScheduleRequest(WarrantyRequestDTO dto, MultipartFile[] images) throws IOException {
        Request request = new Request();
        request.setFullname(dto.getFullname());
        request.setPhone(dto.getPhone());
        request.setEmail(dto.getEmail());
        request.setAddress(dto.getAddress());
        request.setDescription(dto.getDescription());
        request.setStatus("Chờ Xử Lý");

        if (dto.getBookingDate() != null) {
            request.setBookingDate(dto.getBookingDate());
        } else {
            request.setBookingDate(new Date());
        }

        if (dto.getSerialNumber() != null && !dto.getSerialNumber().isBlank()) {
            CustomerLaptop cl = customerLaptopRepository.findBySerial(dto.getSerialNumber().trim());
            if (cl == null) {
                throw new IllegalArgumentException("Số sê-ri không tồn tại hoặc không hợp lệ.");
            }
            request.setCustomerLaptop(cl);
        }

        requestRepository.save(request);

        if (images != null && images.length > 0) {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    String imgUrl = ImgUploadUtil.saveFile(file, "requests");
                    RequestImg requestImg = new RequestImg();
                    requestImg.setRequest(request);
                    requestImg.setImgUrl(imgUrl);
                    requestImgRepository.save(requestImg);
                }
            }
        }
    }

    @Override
    public List<Request> getHistoryByCustomerId(Long customerId) {
        return requestRepository.getHistoryByCustomerId(customerId);
    }
}
