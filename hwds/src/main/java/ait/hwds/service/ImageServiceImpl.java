package ait.hwds.service;


import ait.hwds.exception.RestException;
import ait.hwds.model.entity.Article;
import ait.hwds.model.entity.Departament;
import ait.hwds.model.entity.Image;
import ait.hwds.repository.ArticleRepository;
import ait.hwds.repository.DepartamentRepository;
import ait.hwds.repository.ImageRepository;
import ait.hwds.service.interfaces.ImageService;
import ait.hwds.service.interfaces.S3StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.chime.model.Room;

import java.util.List;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final S3StorageService s3StorageService;
    private final ArticleRepository articleRepository;
    private final DepartamentRepository departamentRepository;

    @Value("${s3.bucketName}")
    private String bucketName;

    public ImageServiceImpl(ImageRepository imageRepository, S3StorageService s3StorageService, ArticleRepository articleRepository, DepartamentRepository departamentRepository) {
        this.imageRepository = imageRepository;
        this.s3StorageService = s3StorageService;
        this.articleRepository = articleRepository;
        this.departamentRepository = departamentRepository;
    }


    @Override
    public String uploadImageForArticle(MultipartFile file, Article article) {
        String fileOriginalFilename = file.getOriginalFilename();
        try {
            String s3Path = "articles/" + article.getId() + "/images/" + UUID.randomUUID() + "_" + fileOriginalFilename;
            s3StorageService.uploadFile(s3Path, file);

            Image image = new Image(s3Path, fileOriginalFilename, bucketName, article);
            imageRepository.save(image);
            return s3StorageService.getImageUrl(s3Path);
        } catch (Exception e) {
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while uploading image", e);
        }
    }

    @Override
    public String uploadImageForDepartament(MultipartFile file, Departament departament) {
        String fileOriginalFilename = file.getOriginalFilename();
        try {
            String s3Path = "departaments/" + departament.getId() + "/images/" + UUID.randomUUID() + "_" + fileOriginalFilename;
            s3StorageService.uploadFile(s3Path, file);

            Image image = new Image(s3Path, fileOriginalFilename, bucketName, departament);
            imageRepository.save(image);
            return s3StorageService.getImageUrl(s3Path);
        } catch (Exception e) {
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while uploading image for departament", e);
        }
    }

    @Override
    public List<Image> getImagesByArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Article by id: " + articleId + " not found"));

        return imageRepository.findAllByArticle(article);
    }

    @Override
    public List<Image> getImagesByDepartament(Long departamentId) {
        Departament departament = departamentRepository.findById(departamentId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Departament by id: " + departamentId + " not found"));

        return imageRepository.findAllByDepartament(departament);
    }

    @Override
    public void deleteImage(Long id) {
        // Находим изображение по ID
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Image by id: " + id + " not found"));

        s3StorageService.deleteFile(image.getS3Path());

        imageRepository.delete(image);
    }


    @Override
    public String getImageUrl(Long imageId) {

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Image by id: " + imageId + " not found"));

        return s3StorageService.getImageUrl(image.getS3Path());
    }
}
