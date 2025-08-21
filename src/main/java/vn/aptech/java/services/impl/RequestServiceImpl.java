package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.java.dtos.admin.CreateRequestDTO;
import vn.aptech.java.dtos.admin.UpdateRequestDTO;
import vn.aptech.java.dtos.client.WarrantyRequestDTO;
import vn.aptech.java.models.CustomerLaptop;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.RequestImg;
import vn.aptech.java.repositories.CustomerLaptopRepository;
import vn.aptech.java.repositories.RequestRepository;
import vn.aptech.java.services.RequestImgService;
import vn.aptech.java.services.RequestService;
import vn.aptech.java.utils.ImgUploadUtil;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private CustomerLaptopRepository customerLaptopRepository;

    @Autowired
    private RequestImgService requestImgService;

    @Override
    public void createScheduleRequest(WarrantyRequestDTO dto) {
        Request request = new Request();
        request.setFullname(dto.getFullname());
        request.setPhone(dto.getPhone());
        request.setEmail(dto.getEmail());
        request.setAddress(dto.getAddress());
        request.setDescription(dto.getDescription());
        request.setBookingDate(Date.valueOf(LocalDate.now()));
        request.setStatus(Request.Status.PENDING);

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

    @Override
    @Transactional
    public void createRequest(CreateRequestDTO dto) {
        try {
            Request request = new Request();
            request.setFullname(dto.getFullname());
            request.setPhone(dto.getPhone());
            request.setEmail(dto.getEmail());
            request.setAddress(dto.getAddress());
            request.setDescription(dto.getDescription());
            request.setBookingDate(Date.valueOf(LocalDate.now()));
            request.setStatus(dto.getStatus());
            if (dto.getSerialNumber() != null && !dto.getSerialNumber().isBlank()) {
                CustomerLaptop cl = customerLaptopRepository.findBySerial(dto.getSerialNumber().trim());
                if (cl == null) {
                    throw new IllegalArgumentException("Số sê-ri không tồn tại hoặc không hợp lệ.");
                }
                request.setCustomerLaptop(cl);
            }
            requestRepository.save(request);
            if (dto.getImages() != null && dto.getImages().stream().anyMatch(file -> !file.isEmpty())) {
                for (MultipartFile image : dto.getImages()) {
                    if (!ImgUploadUtil.isValidImageFormat(image)) {
                        throw new IllegalArgumentException("Định dạng ảnh không hợp lệ. Chỉ chấp nhận jpg, jpeg, png, webp.");
                    }
                    RequestImg requestImg = new RequestImg();
                    requestImg.setRequest(request);
                    requestImg.setImgUrl(ImgUploadUtil.saveFile(image, "requests"));
                    requestImgService.createRequestImg(requestImg);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo yêu cầu bảo hành: " + e.getMessage(), e);
        }
    }
    @Override
    public Optional<Request> getRequestById(Long id) {
        return requestRepository.findById(id);
    }

    @Override
    @Transactional
    public void updateRequest(UpdateRequestDTO dto) {
        try {
            Optional<Request> optionalRequest = requestRepository.findById(dto.getId());
            if (optionalRequest.isEmpty()) {
                throw new IllegalArgumentException("Không tìm thấy yêu cầu bảo hành với ID: " + dto.getId());
            }
            
            Request request = optionalRequest.get();
            request.setFullname(dto.getFullname());
            request.setPhone(dto.getPhone());
            request.setEmail(dto.getEmail());
            request.setAddress(dto.getAddress());
            request.setDescription(dto.getDescription());
            request.setStatus(dto.getStatus());
            
            if (dto.getSerialNumber() != null && !dto.getSerialNumber().isBlank()) {
                CustomerLaptop cl = customerLaptopRepository.findBySerial(dto.getSerialNumber().trim());
                if (cl == null) {
                    throw new IllegalArgumentException("Số sê-ri không tồn tại hoặc không hợp lệ.");
                }
                request.setCustomerLaptop(cl);
            }
            
            requestRepository.save(request);
            List<RequestImg> oldImages = requestImgService.getRequestImgByRequestId(dto.getId());
            List<String> existingUrls = Optional.ofNullable(dto.getExistingImageUrls())
                    .orElse(Collections.emptyList());
            for (RequestImg img : oldImages) {
                String imgUrl = img.getImgUrl();
                if (!existingUrls.contains(imgUrl)) {
                    requestImgService.deleteRequestImg(img);
                }
            }
            if (dto.getNewImages() != null && dto.getNewImages().stream().anyMatch(file -> !file.isEmpty())) {
                for (MultipartFile image : dto.getNewImages()) {
                    if (!ImgUploadUtil.isValidImageFormat(image)) {
                        throw new IllegalArgumentException("Định dạng ảnh không hợp lệ. Chỉ chấp nhận jpg, jpeg, png, webp.");
                    }
                    RequestImg requestImg = new RequestImg();
                    requestImg.setRequest(request);
                    requestImg.setImgUrl(ImgUploadUtil.saveFile(image, "requests"));
                    requestImgService.createRequestImg(requestImg);
                }
            }




        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật yêu cầu bảo hành: " + e.getMessage(), e);
        }
    }
}

