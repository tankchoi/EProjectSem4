package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.java.models.RequestImg;
import java.util.List;

public interface RequestImgRepository extends JpaRepository<RequestImg, Long> {
    List<RequestImg> findRequestImgByRequestId(Long requestId);
}
