package ait.hwds.service;

import ait.hwds.exception.RestException;
import ait.hwds.model.dto.CreateOrUpdateDepartamentDto;
import ait.hwds.model.dto.DepartamentDto;
import ait.hwds.model.dto.ArticleDto;
import ait.hwds.model.entity.Article;
import ait.hwds.model.entity.Departament;
import ait.hwds.model.entity.Image;
import ait.hwds.repository.ArticleRepository;
import ait.hwds.repository.CartItemArticleRepository;
import ait.hwds.repository.DepartamentRepository;
import ait.hwds.service.interfaces.DepartamentService;
import ait.hwds.service.interfaces.ImageService;
import ait.hwds.service.interfaces.S3StorageService;
import ait.hwds.service.mapping.DepartamentMappingService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DepartamentServiceImpl implements DepartamentService {

    private final DepartamentRepository departamentRepository;
    private final DepartamentMappingService departamentMappingService;
    private final CartItemArticleRepository cartItemArticleRepository;
    private final ArticleRepository articleRepository;
    private final S3StorageService s3StorageService;
    private final ImageService imageService;

    public DepartamentServiceImpl(DepartamentRepository departamentRepository,
                                  DepartamentMappingService departamentMappingService,
                                  CartItemArticleRepository cartItemArticleRepository,
                                  ArticleRepository articleRepository,
                                  S3StorageService s3StorageService,
                                  ImageService imageService) {
        this.departamentRepository = departamentRepository;
        this.departamentMappingService = departamentMappingService;
        this.cartItemArticleRepository = cartItemArticleRepository;
        this.articleRepository = articleRepository;
        this.s3StorageService = s3StorageService;
        this.imageService = imageService;
    }

    @Override
    public DepartamentDto getDepartamentById(Long id) {
        Departament departament = findByIdOrThrow(id);
        return mapDepartamentToDtoWithImages(departament);
    }

    @Override
    public List<DepartamentDto> getAllDepartaments() {
        return departamentRepository.findAll()
                .stream()
                .map(this::mapDepartamentToDtoWithImages)
                .toList();
    }

    private DepartamentDto mapDepartamentToDtoWithImages(Departament departament) {
        List<Image> departamentImages = departament.getImages();
        List<String> departamentImagesUrls = s3StorageService.getImageUrl(departamentImages);

        BigDecimal priceDepartament = departament.getArticles()
                .stream()
                .map(Article::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        DepartamentDto departamentDto = departamentMappingService.mapEntityToDto(departament);
        departamentDto.setImageUrls(departamentImagesUrls);
        departamentDto.setPrice(priceDepartament);

        for (ArticleDto articleDto : departamentDto.getArticles()) {
            List<Image> articleImages = imageService.getImagesByArticle(articleDto.getId());
            List<String> imagesByArticle = s3StorageService.getImageUrl(articleImages);
            articleDto.setImageUrls(imagesByArticle);
        }
        return departamentDto;
    }

    @Override
    public DepartamentDto createDepartament(CreateOrUpdateDepartamentDto createDepartamentDto) {
        Departament departament = departamentMappingService.mapDtoToEntity(createDepartamentDto);
        return departamentMappingService.mapEntityToDto(departamentRepository.save(departament));
    }

    @Override
    @Transactional
    public void deleteDepartament(Long id) {
        Departament departament = findByIdOrThrow(id);

        for (Article article : departament.getArticles()) {
            cartItemArticleRepository.deleteArticleById(article.getId());
        }

        for (Article article : departament.getArticles()) {
            articleRepository.delete(article);
        }

        departamentRepository.delete(departament);
    }

    @Override
    public Departament findByIdOrThrow(Long id) {
        return departamentRepository.findById(id)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Departament by id: " + id + " not found"));
    }

    @Override
    public BigDecimal getTotalArticlePriceForDepartament(Long departamentId) {
        return findByIdOrThrow(departamentId)
                .getArticles()
                .stream()
                .map(Article::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalArticlePriceForDepartament(Departament departament) {
        return null;
    }

    @Override
    public List<DepartamentDto> getAllDepartament() {
        return List.of();
    }
}
