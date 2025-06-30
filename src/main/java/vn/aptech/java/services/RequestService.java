package vn.aptech.java.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.aptech.java.dtos.CreateRequestDTO;
import vn.aptech.java.models.Request;

import java.util.List;
import java.util.Optional;

public interface RequestService {
    List<Request> getAllRequests();

    Page<Request> getAllRequests(String status, Pageable pageable);

    Page<Request> searchRequests(String search, String status, Pageable pageable);

    Optional<Request> getRequestById(Long id);

    Request getRequestByIdOrThrow(Long id);

    Request createRequest(CreateRequestDTO createRequestDTO);

    Request updateRequest(Long id, CreateRequestDTO updateRequestDTO);

    Request updateRequest(Request request);

    void deleteRequest(Long id);

    List<Request> searchRequests(String keyword);

    List<Request> getRequestsByStatus(String status);

    List<Request> getRequestsByTechnician(Long technicianId);

    List<Request> getApprovedRequestsWithoutInvoice();

    Request assignTechnician(Long requestId, Long technicianId);

    Request updateStatus(Long requestId, String status);
}
