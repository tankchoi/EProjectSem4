package vn.aptech.java.services;

import vn.aptech.java.models.RequestImg;

import java.util.List;

public interface RequestImgService {
    RequestImg createRequestImg(RequestImg requestImg);

    List<RequestImg> getRequestImgByRequestId(Long requestId);

    void deleteRequestImg(RequestImg requestImg);
}
