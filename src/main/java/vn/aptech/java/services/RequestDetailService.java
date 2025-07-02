package vn.aptech.java.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.aptech.java.dtos.CreateRequestDetailDTO;
import vn.aptech.java.dtos.UpdateRequestDetailDTO;
import vn.aptech.java.models.RequestDetail;

import java.util.List;
import java.util.Optional;

public interface RequestDetailService {

    /**
     * Lấy tất cả request details
     */
    List<RequestDetail> getAllRequestDetails();

    /**
     * Lấy request details với phân trang
     */
    Page<RequestDetail> getAllRequestDetails(Pageable pageable);

    /**
     * Tìm kiếm request details với phân trang
     */
    Page<RequestDetail> searchRequestDetails(String search, Pageable pageable);

    /**
     * Lấy request detail theo ID
     */
    Optional<RequestDetail> getRequestDetailById(Long id);

    /**
     * Lấy request detail theo ID hoặc throw exception
     */
    RequestDetail getRequestDetailByIdOrThrow(Long id);

    /**
     * Lấy tất cả request details theo request ID
     */
    List<RequestDetail> getRequestDetailsByRequestId(Long requestId);

    /**
     * Lấy tất cả request details theo part ID
     */
    List<RequestDetail> getRequestDetailsByPartId(Long partId);

    /**
     * Tạo mới request detail
     */
    RequestDetail createRequestDetail(CreateRequestDetailDTO createRequestDetailDTO);

    /**
     * Cập nhật request detail
     */
    RequestDetail updateRequestDetail(Long id, UpdateRequestDetailDTO updateRequestDetailDTO);

    /**
     * Xóa request detail
     */
    void deleteRequestDetail(Long id);

    /**
     * Xóa tất cả request details của một request
     */
    void deleteRequestDetailsByRequestId(Long requestId);

    /**
     * Tính tổng giá trị của request
     */
    Double getTotalValueByRequestId(Long requestId);

    /**
     * Tính tổng số lượng part đã sử dụng
     */
    Long getTotalQuantityByPartId(Long partId);

    /**
     * Kiểm tra part có đang được sử dụng không
     */
    boolean isPartInUse(Long partId);

    /**
     * Lấy top parts được sử dụng nhiều nhất
     */
    List<Object[]> getTopUsedParts(int limit);

    /**
     * Kiểm tra và cập nhật số lượng part trong kho
     */
    void validateAndUpdatePartQuantity(Long partId, Integer quantityUsed);

    /**
     * Khôi phục số lượng part khi xóa request detail
     */
    void restorePartQuantity(Long partId, Integer quantityToRestore);
}
