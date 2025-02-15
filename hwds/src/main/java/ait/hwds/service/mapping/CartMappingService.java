package ait.hwds.service.mapping;


import ait.hwds.model.dto.CartDto;
import ait.hwds.model.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMappingService.class, CartItemArticleMappingService.class})
public interface CartMappingService {

    @Mapping(target = "userDto", source = "user")
    @Mapping(target = "articles", source = "cartItemArticles")
    CartDto mapEntityToDto(Cart entity);
}
