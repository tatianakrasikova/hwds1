package ait.hwds.service.interfaces;


import ait.hwds.model.dto.UserDto;
import ait.hwds.model.entity.User;

public interface UserService {

    UserDto findUserByEmailOrThrow(String userEmail);

    User getUserByEmailOrThrow(String userEmail);

    UserDto confirmUser(String confirmationToken);
}
