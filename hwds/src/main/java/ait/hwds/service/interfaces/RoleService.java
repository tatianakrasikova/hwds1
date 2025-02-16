package ait.hwds.service.interfaces;


import ait.hwds.model.entity.Role;

public interface RoleService {
    Role findRoleByTitleOrThrow(String title);
}
