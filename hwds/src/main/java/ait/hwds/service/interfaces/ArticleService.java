package ait.hwds.service.interfaces;



import ait.hwds.model.dto.ArticleDto;
import ait.hwds.model.dto.CreateArticleDto;
import ait.hwds.model.entity.Article;

import java.time.LocalDate;
import java.util.List;

public interface ArticleService {




    Article getArticleOrThrow(long id);

    ArticleDto getArticleById(Long id);

    List<ArticleDto> getAllArticles();

    void deleteArticleById(Long id);

    List<ArticleDto> getAvailableArticles(LocalDate entryDate, LocalDate departureDate);

    ArticleDto saveArticle(CreateArticleDto createArticleDto);
}
