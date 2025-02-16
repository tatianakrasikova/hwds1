package ait.hwds.service.mapping;


import ait.hwds.model.dto.CartItemArticleDto;
import ait.hwds.model.entity.CartItemArticle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ArticleMappingService.class})
public interface CartItemBedMappingService {

    CartItemArticleDto mapEntityToDto(CartItemArticle entity);
}
