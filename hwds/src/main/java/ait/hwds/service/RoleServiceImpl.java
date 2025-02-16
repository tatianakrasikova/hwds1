package ait.hwds.service;


import ait.hwds.exception.RestException;
import ait.hwds.model.entity.Role;
import ait.hwds.repository.RoleRepository;
import ait.hwds.service.interfaces.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findRoleByTitleOrThrow(String title) {
        return roleRepository.findRoleByTitleIgnoreCase(title)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Role not found"));
    }
}
