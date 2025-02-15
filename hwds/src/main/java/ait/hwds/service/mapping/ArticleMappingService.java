package ait.hwds.service.mapping;


import ait.hwds.model.dto.ArticleDto;
import ait.hwds.model.dto.CreateArticleDto;
import ait.hwds.model.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ArticleMappingService {

    @Mapping(target = "departament.id", source = "departamentId")
    @Mapping(target = "images", ignore = true)
    Article mapDtoToEntity(CreateArticleDto createArticleDto);

    @Mapping(target = "departamentId", source = "departament.id")
    @Mapping(target = "imageUrls", ignore = true)
    ArticleDto mapEntityToDto(Article entity);
}
