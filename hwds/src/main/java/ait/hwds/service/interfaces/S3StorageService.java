package ait.hwds.service.interfaces;


import ait.hwds.model.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3StorageService {

    void uploadFile(String s3Path, MultipartFile file);

    String getImageUrl(String s3Path);

    List<String> getImageUrl(List<Image> articleImages);

    void deleteFile(String s3Path);
}
