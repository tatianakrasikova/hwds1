package ait.hwds.service.mapping;


import ait.hwds.model.dto.CreateOrUpdateDepartamentDto;
import ait.hwds.model.dto.DepartamentDto;
import ait.hwds.model.entity.Departament;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import software.amazon.awssdk.services.chime.model.Room;

@Mapper(componentModel = "spring", uses = ArticleMappingService.class)
public interface DepartamentMappingService {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "articles", ignore = true)
    @Mapping(target = "images", ignore = true)
    Departament mapDtoToEntity(CreateOrUpdateDepartamentDto departamentDto);

    @Mapping(target = "price", ignore = true)
    @Mapping(target = "imageUrls", ignore = true)
    DepartamentDto mapEntityToDto(Departament entity);
}
