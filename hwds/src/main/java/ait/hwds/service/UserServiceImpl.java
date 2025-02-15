package ait.hwds.service;


import ait.hwds.exception.RestException;
import ait.hwds.model.dto.UserDto;
import ait.hwds.model.entity.ConfirmationCode;
import ait.hwds.model.entity.User;
import ait.hwds.repository.UserRepository;
import ait.hwds.service.interfaces.ConfirmationService;
import ait.hwds.service.interfaces.UserService;
import ait.hwds.service.mapping.UserMappingService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMappingService userMappingService;
    private final ConfirmationService confirmationService;

    public UserServiceImpl(UserRepository userRepository,
                           UserMappingService userMappingService,
                           ConfirmationService confirmationService) {
        this.userRepository = userRepository;
        this.userMappingService = userMappingService;
        this.confirmationService = confirmationService;
        System.out.println("UserServiceImpl initialized");
    }

    @Override
    public UserDto findUserByEmailOrThrow(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User with email: " + userEmail + " not found"));
        return userMappingService.mapEntityToDto(user);
    }

    @Override
    public User getUserByEmailOrThrow(String userEmail) {
        return userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User with email: " + userEmail + " not found"));
    }

    @Override
    public UserDto confirmUser(String confirmationToken) {
        ConfirmationCode confirmationCode = confirmationService.validateToken(confirmationToken);

        User user = confirmationCode.getUser();
        if (null != user) {
            user.setIsConfirmed(true);
            userRepository.save(user);
            return userMappingService.mapEntityToDto(user);
        } else {
            throw new RestException("User for this confirmation code not found");
        }
    }
}
