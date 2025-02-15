package ait.hwds.controller;


import ait.hwds.exception.RestException;
import ait.hwds.model.entity.Article;
import ait.hwds.model.entity.Departament;
import ait.hwds.model.entity.Image;
import ait.hwds.service.ArticleServiceImpl;
import ait.hwds.service.DepartamentServiceImpl;
import ait.hwds.service.interfaces.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private final ImageService imageService;
    private final ArticleServiceImpl articleService;
    private final DepartamentServiceImpl departamentService;

    public ImageController(ImageService imageService, ArticleServiceImpl articleService, DepartamentServiceImpl departamentService) {
        this.imageService = imageService;
        this.articleService = articleService;
        this.departamentService = departamentService;
    }

    @PostMapping(value = "/upload/article/{articleId}", consumes = "multipart/form-data", produces = "text/plain")
    @Operation(
            summary = "Upload an image for a specific article",
            description = "Allows the upload of an image for a specified article. Only PNG and JPEG formats are supported, and the maximum file size is 10 MB.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "File to upload",
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data"
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Image uploaded successfully. Returns a URL to the uploaded image.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(value = "https://example.com/images/article/12345.png")
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "File is not provided or is empty"),
                    @ApiResponse(responseCode = "415", description = "Invalid file format. Only PNG and JPEG are supported"),
                    @ApiResponse(responseCode = "413", description = "File size exceeds the allowed limit (10 MB)")
            }
    )
    public String uploadImageForArticle(@Parameter(description = "ID of the article to which the image will be uploaded", required = true)
                                    @PathVariable Long articleId,

                                    @Parameter(description = "Image file to upload. Only PNG and JPEG are accepted.", required = true)
                                    @RequestParam("file") MultipartFile file) {
        // 1. Check if the file is provided
        if (file == null || file.isEmpty()) {
            throw new RestException(HttpStatus.BAD_REQUEST, "File is not provided or is empty.");
        }

        // 2. Validate the file type (e.g., only images)
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/png") || contentType.equals("image/jpeg"))) {
            throw new RestException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Invalid file format. Only PNG and JPEG are supported.");
        }

        // 3. Validate the file size (e.g., no more than 10 MB)
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RestException(HttpStatus.PAYLOAD_TOO_LARGE, "File size exceeds the allowed limit (10 MB).");
        }

        Article article = articleService.getArticleOrThrow(articleId);
        return imageService.uploadImageForArticle(file,article);
    }

    /**
     * Загрузка изображения для комнаты (Departament)
     */
    @PostMapping(value = "/upload/departament/{departamentId}", consumes = "multipart/form-data", produces = "text/plain")
    @Operation(
            summary = "Upload an image for a specific article",
            description = "Allows the upload of an image for a specified departament. Only PNG and JPEG formats are supported, and the maximum file size is 10 MB.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "File to upload",
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data"
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Image uploaded successfully. Returns a URL to the uploaded image.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(value = "https://example.com/images/article/12345.png")
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "File is not provided or is empty"),
                    @ApiResponse(responseCode = "415", description = "Invalid file format. Only PNG and JPEG are supported"),
                    @ApiResponse(responseCode = "413", description = "File size exceeds the allowed limit (10 MB)")
            }
    )
    public String uploadImageForDepartament(@Parameter(description = "ID of the article to which the image will be uploaded", required = true)
                                     @PathVariable Long departamentId,

                                     @Parameter(description = "Image file to upload. Only PNG and JPEG are accepted.", required = true)
                                     @RequestParam("file")
                                     MultipartFile file) {
        // 1. Check if the file is provided
        if (file == null || file.isEmpty()) {
            throw new RestException(HttpStatus.BAD_REQUEST, "File is not provided or is empty.");
        }

        // 2. Validate the file type (e.g., only images)
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/png") || contentType.equals("image/jpeg"))) {
            throw new RestException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Invalid file format. Only PNG and JPEG are supported.");
        }

        // 3. Validate the file size (e.g., no more than 10 MB)
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RestException(HttpStatus.PAYLOAD_TOO_LARGE, "File size exceeds the allowed limit (10 MB).");
        }

        Departament departament = departamentService.findByIdOrThrow(departamentId);
        return imageService.uploadImageForDepartament(file, departament);
    }

    /**
     * Получение изображений для кровати
     */
    @GetMapping("/article/{articleId}")
    public List<Image> getImagesByArticle(@PathVariable Long articleId) {
        return imageService.getImagesByArticle(articleId);
    }

    /**
     * Получение изображений для комнаты
     */
    @GetMapping("/departament/{departamentId}")
    public List<Image> getImagesByDepartament(@PathVariable Long departamentId) {
        return imageService.getImagesByDepartament(departamentId);
    }

    /**
     * Удаление изображения по ID
     */
    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
    }

    /**
     * Получение Presigned URL для изображения по ID
     */
    @GetMapping("/presigned/{imageId}")
    public String getImageUrl(@PathVariable Long imageId) {
        return imageService.getImageUrl(imageId);
    }
}
