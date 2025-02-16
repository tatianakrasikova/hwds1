package ait.hwds.service.mapping;


import ait.hwds.model.dto.RoleDto;
import ait.hwds.model.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMappingService {

    @Mapping(target = "name", source = "title")
    RoleDto toDto(Role role);
}
