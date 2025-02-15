package ait.hwds.service.mapping;


import ait.hwds.model.dto.UserDto;
import ait.hwds.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {RoleMappingService.class})
public interface UserMappingService {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "password", ignore = true)
    User mapDtoToEntity(UserDto userDto);

    UserDto mapEntityToDto(User entity);

}
