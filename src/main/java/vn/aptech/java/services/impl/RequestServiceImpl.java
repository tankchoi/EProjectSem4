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
import vn.aptech.java.services.UserService;
import vn.aptech.java.utils.ImgUploadUtil;
import java.util.Date;
import java.util.Collections;
import java.io.IOException;
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

    @Autowired
    private UserService userService;

    @Override
    public Request createScheduleRequest(WarrantyRequestDTO dto, MultipartFile[] images) throws IOException {
        Request request = new Request();
        request.setFullname(dto.getFullname());
        request.setPhone(dto.getPhone());
        request.setEmail(dto.getEmail());
        request.setAddress(dto.getAddress());
        request.setDescription(dto.getDescription());
        request.setStatus(Request.Status.PENDING);

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
                    if (!ImgUploadUtil.isValidImageFormat(file)) {
                        throw new IllegalArgumentException(
                                "Định dạng ảnh không hợp lệ. Chỉ chấp nhận jpg, jpeg, png, webp.");
                    }
                    String imgUrl = ImgUploadUtil.saveFile(file, "requests");
                    RequestImg requestImg = new RequestImg();
                    requestImg.setRequest(request);
                    requestImg.setImgUrl(imgUrl);
                    requestImgService.createRequestImg(requestImg);
                }
            }
        }
        return request;
    }

    @Override
    public List<Request> getHistoryByCustomerId(Long customerId) {
        return requestRepository.getHistoryByCustomerId(customerId);
    }

    @Override
    @Transactional
    public Request createRequest(CreateRequestDTO dto) {
        try {
            Request request = new Request();
            request.setFullname(dto.getFullname());
            request.setPhone(dto.getPhone());
            request.setEmail(dto.getEmail());
            request.setAddress(dto.getAddress());
            request.setDescription(dto.getDescription());
            request.setBookingDate(dto.getBookingDate());
            request.setTechnician(userService.findById(dto.getTechnicianId())
                    .orElseThrow(() -> new IllegalArgumentException("Kỹ thuật viên không tồn tại.")));
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
                        throw new IllegalArgumentException(
                                "Định dạng ảnh không hợp lệ. Chỉ chấp nhận jpg, jpeg, png, webp.");
                    }
                    RequestImg requestImg = new RequestImg();
                    requestImg.setRequest(request);
                    requestImg.setImgUrl(ImgUploadUtil.saveFile(image, "requests"));
                    requestImgService.createRequestImg(requestImg);
                }
            }
            return request;
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
            if (dto.getStatus() != Request.Status.PENDING) {
                if (dto.getTechnicianId() == null) {
                    throw new IllegalArgumentException(
                            "Phải chọn kỹ thuật viên khi cập nhật trạng thái khác chờ xử lý.");
                }
                request.setTechnician(userService.findById(dto.getTechnicianId())
                        .orElseThrow(() -> new IllegalArgumentException("Kỹ thuật viên không tồn tại.")));
            }
            request.setBookingDate(dto.getBookingDate());
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
                        throw new IllegalArgumentException(
                                "Định dạng ảnh không hợp lệ. Chỉ chấp nhận jpg, jpeg, png, webp.");
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

    @Override
    public List<Request> getRequests(String fullname, String phone, String email, String serialNumber,
            Long technicianId, Request.Status status) {
        String statusStr = status != null ? status.name() : null;
        return requestRepository.filterRequests(fullname, phone, email, serialNumber, technicianId, statusStr);
    }

    @Override
    public void deleteRequest(Long id) {
        try {
            Optional<Request> requestOpt = requestRepository.findById(id);
            if (requestOpt.isEmpty()) {
                throw new IllegalArgumentException("Không tìm thấy yêu cầu bảo hành với ID: " + id);
            }
            List<RequestImg> images = requestImgService.getRequestImgByRequestId(id);
            for (RequestImg img : images) {
                requestImgService.deleteRequestImg(img);
            }
            requestRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa yêu cầu bảo hành: " + e.getMessage(), e);
        }
    }

    @Override
    public long count() {
        return requestRepository.countAllRequests();
    }

    @Override
    public long countByStatus(Request.Status status) {
        return requestRepository.countByStatus(status);
    }
}
