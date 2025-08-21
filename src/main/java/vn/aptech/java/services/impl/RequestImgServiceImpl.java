package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.RequestImg;
import vn.aptech.java.repositories.RequestImgRepository;
import vn.aptech.java.services.RequestImgService;
import vn.aptech.java.utils.ImgUploadUtil;

import java.util.List;

@Service
public class RequestImgServiceImpl implements RequestImgService {
    @Autowired
    private RequestImgRepository requestImgRepository;
    @Override
    public void createRequestImg(RequestImg requestImg) {
        requestImgRepository.save(requestImg);
    }

    @Override
    public List<RequestImg> getRequestImgByRequestId(Long requestId) {
        return requestImgRepository.findRequestImgByRequestId(requestId);
    }

    @Override
    public void deleteRequestImg(RequestImg requestImg) {
        try{
            requestImgRepository.delete(requestImg);
            ImgUploadUtil.deleteFile(requestImg.getImgUrl());
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xóa ảnh yêu cầu: " + e.getMessage());
        }
    }


}
