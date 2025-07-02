package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.aptech.java.dtos.CreateRequestDTO;
import vn.aptech.java.models.CustomerLaptop;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.CustomerLaptopRepository;
import vn.aptech.java.repositories.RequestRepository;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.RequestService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private CustomerLaptopRepository customerLaptopRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    public Page<Request> getAllRequests(String status, Pageable pageable) {
        if (status != null && !status.trim().isEmpty() && !"ALL".equals(status)) {
            return requestRepository.findByStatus(status, pageable);
        }
        return requestRepository.findAll(pageable);
    }

    @Override
    public Page<Request> searchRequests(String search, String status, Pageable pageable) {
        if (status != null && "ALL".equals(status)) {
            status = null;
        }
        return requestRepository.findBySearchAndStatus(search, status, pageable);
    }

    @Override
    public Optional<Request> getRequestById(Long id) {
        return requestRepository.findById(id);
    }

    @Override
    public Request getRequestByIdOrThrow(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu với ID: " + id));
    }

    @Override
    public Request createRequest(CreateRequestDTO createRequestDTO) {
        // Validate customer laptop exists
        CustomerLaptop customerLaptop = customerLaptopRepository.findById(createRequestDTO.getCustomerLaptopId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy laptop khách hàng"));

        // Validate technician if provided
        User technician = null;
        if (createRequestDTO.getTechnicianId() != null) {
            technician = userRepository.findById(createRequestDTO.getTechnicianId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kỹ thuật viên"));

            if (!User.Role.STAFF.equals(technician.getRole())) {
                throw new IllegalArgumentException("Chỉ có thể gán kỹ thuật viên có vai trò STAFF");
            }
        }

        Request newRequest = new Request();
        newRequest.setCustomerLaptop(customerLaptop);
        newRequest.setFullname(createRequestDTO.getFullname());
        newRequest.setEmail(createRequestDTO.getEmail());
        newRequest.setPhone(createRequestDTO.getPhone());
        newRequest.setAddress(createRequestDTO.getAddress());
        newRequest.setDescription(createRequestDTO.getDescription());
        newRequest.setBookingDate(createRequestDTO.getBookingDate());
        newRequest.setStatus(createRequestDTO.getStatus() != null ? createRequestDTO.getStatus() : "PENDING");
        newRequest.setTechnician(technician);

        return requestRepository.save(newRequest);
    }

    @Override
    public Request updateRequest(Long id, CreateRequestDTO updateRequestDTO) {
        Request existingRequest = getRequestByIdOrThrow(id);

        // Validate customer laptop exists
        CustomerLaptop customerLaptop = customerLaptopRepository.findById(updateRequestDTO.getCustomerLaptopId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy laptop khách hàng"));

        // Validate technician if provided
        User technician = null;
        if (updateRequestDTO.getTechnicianId() != null) {
            technician = userRepository.findById(updateRequestDTO.getTechnicianId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kỹ thuật viên"));

            if (!User.Role.STAFF.equals(technician.getRole())) {
                throw new IllegalArgumentException("Chỉ có thể gán kỹ thuật viên có vai trò STAFF");
            }
        }

        existingRequest.setCustomerLaptop(customerLaptop);
        existingRequest.setFullname(updateRequestDTO.getFullname());
        existingRequest.setEmail(updateRequestDTO.getEmail());
        existingRequest.setPhone(updateRequestDTO.getPhone());
        existingRequest.setAddress(updateRequestDTO.getAddress());
        existingRequest.setDescription(updateRequestDTO.getDescription());
        existingRequest.setBookingDate(updateRequestDTO.getBookingDate());
        existingRequest.setStatus(updateRequestDTO.getStatus());
        existingRequest.setTechnician(technician);

        return requestRepository.save(existingRequest);
    }

    @Override
    public Request updateRequest(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public void deleteRequest(Long id) {
        getRequestByIdOrThrow(id); // Validation to ensure request exists
        requestRepository.deleteById(id);
    }

    @Override
    public List<Request> searchRequests(String keyword) {
        return requestRepository.findByKeyword(keyword);
    }

    @Override
    public List<Request> getRequestsByStatus(String status) {
        return requestRepository.findByStatus(status);
    }

    @Override
    public List<Request> getRequestsByTechnician(Long technicianId) {
        return requestRepository.findByTechnicianId(technicianId);
    }

    @Override
    public List<Request> getApprovedRequestsWithoutInvoice() {
        return requestRepository.findApprovedRequestsWithoutInvoice();
    }

    @Override
    public Request assignTechnician(Long requestId, Long technicianId) {
        Request request = getRequestByIdOrThrow(requestId);
        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kỹ thuật viên"));

        if (!User.Role.STAFF.equals(technician.getRole())) {
            throw new IllegalArgumentException("Chỉ có thể gán kỹ thuật viên có vai trò STAFF");
        }

        request.setTechnician(technician);
        return requestRepository.save(request);
    }

    @Override
    public Request updateStatus(Long requestId, String status) {
        Request request = getRequestByIdOrThrow(requestId);
        request.setStatus(status);
        return requestRepository.save(request);
    }
}
