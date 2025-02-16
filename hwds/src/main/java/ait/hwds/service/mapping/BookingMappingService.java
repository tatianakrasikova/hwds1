package ait.hwds.service.mapping;


import ait.hwds.model.dto.BookingDto;
import ait.hwds.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ArticleMappingService.class, UserMappingService.class})
public interface BookingMappingService {

    @Mapping(target = "imageUrl", ignore = true)
    BookingDto mapEntityToDto(Booking entity);
}
