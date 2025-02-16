package ait.hwds.service;


import ait.hwds.exception.RestException;
import ait.hwds.model.dto.ArticleDto;
import ait.hwds.model.dto.CreateArticleDto;
import ait.hwds.model.entity.Article;
import ait.hwds.model.entity.Departament;
import ait.hwds.model.entity.Image;
import ait.hwds.repository.ArticleRepository;
import ait.hwds.service.interfaces.ArticleService;
import ait.hwds.service.interfaces.DepartamentService;
import ait.hwds.service.interfaces.S3StorageService;
import ait.hwds.service.mapping.ArticleMappingService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMappingService articleMappingService;
    private final DepartamentService departamentService;
    private final S3StorageService s3StorageService;
    private List<Image> articleImages;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              ArticleMappingService articleMappingService,
                              @Lazy DepartamentService departamentService,
                              S3StorageService s3StorageService) {
        this.articleRepository = articleRepository;
        this.articleMappingService = articleMappingService;
        this.departamentService = departamentService;
        this.s3StorageService = s3StorageService;
    }

    @Override
    public ArticleDto saveArticle(CreateArticleDto createArticleDto) {
        Article article = articleMappingService.mapDtoToEntity(createArticleDto);
        Departament departament = departamentService.findByIdOrThrow(createArticleDto.getDepartamentId());
        article.setDepartament(departament);

        if (articleRepository.existsByDepartamentIdAndNumber(departament.getId(), article.getNumber())) {
            throw new RestException(HttpStatus.CONFLICT, "Article number already exists in this departament");
        }
        return articleMappingService.mapEntityToDto(articleRepository.save(article));
    }

    @Override
    public Article getArticleOrThrow(long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Article by id: " + id + " not found"));
    }

    @Override
    public ArticleDto getArticleById(Long id) {
        Article article = getArticleOrThrow(id);
        return mapArticleToDtoWithImages(article);
    }

    @Override
    public List<ArticleDto> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(this::mapArticleToDtoWithImages)
                .toList();
    }


    @Override
    public void deleteArticleById(Long id) {
        getArticleOrThrow(id);
        articleRepository.deleteById(id);
    }



    @Override
    public List<ArticleDto> getAvailableArticles(LocalDate entryDate, LocalDate departureDate) {
        return articleRepository.findAvailableBeds(entryDate, departureDate).stream()
                .map(this::mapArticleToDtoWithImages)
                .toList();
    }

    private ArticleDto mapArticleToDtoWithImages(Article article) {
        List<Image> ArticleImages = article.getImages();
        List<String> articleImagesUrls = s3StorageService.getImageUrl(articleImages);
        ArticleDto articleDto = articleMappingService.mapEntityToDto(article);
        articleDto.setImageUrls(articleImagesUrls);
        return articleDto;
    }
}
