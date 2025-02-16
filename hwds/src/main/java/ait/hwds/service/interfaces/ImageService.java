package ait.hwds.service.interfaces;


import ait.hwds.model.entity.Article;
import ait.hwds.model.entity.Departament;
import ait.hwds.model.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImageService {

    String uploadImageForArticle(MultipartFile file, Article article);

    String uploadImageForDepartament(MultipartFile file, Departament departament);

    List<Image> getImagesByArticle(Long articleId);

    List<Image> getImagesByDepartament(Long departamentId);

    void deleteImage(Long id);

    String getImageUrl(Long imageId);


}
