package ait.hwds.security.service;



import ait.hwds.exception.RestException;
import ait.hwds.model.entity.Role;
import ait.hwds.model.entity.User;
import ait.hwds.repository.UserRepository;
import ait.hwds.security.dto.TokenResponseDTO;
import ait.hwds.service.interfaces.ConfirmationService;
import ait.hwds.service.interfaces.EmailService;
import ait.hwds.service.interfaces.RoleService;
import ait.hwds.service.mapping.UserMappingService;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
;import static ait.hwds.service.mapping.UserEmailUtils.normalizeUserEmail;

@Service
public class AuthService {

    public static final String DEFAULT_USER_ROLE = "ROLE_USER";

    private final Map<String, String> refreshStorage;
    private final BCryptPasswordEncoder passwordEncoder;

    private final UserMappingService userMappingService;
    private final TokenService tokenService;
    private final UserDetailsService userService;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final EmailService emailService;
    private final ConfirmationService confirmationService;

    public AuthService(
            BCryptPasswordEncoder passwordEncoder,
            UserMappingService userMappingService,
            TokenService tokenService,
            UserDetailsService userService,
            UserRepository userRepository,
            RoleService roleService,
            EmailService emailService,
            ConfirmationService confirmationService) {
        this.refreshStorage = new HashMap<>();
        this.passwordEncoder = passwordEncoder;
        this.userMappingService = userMappingService;
        this.tokenService = tokenService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.emailService = emailService;
        this.confirmationService = confirmationService;
    }


    public ait.hwds.security.dto.TokenResponseDTO login(ait.hwds.security.dto.@Valid LoginRequestDTO loginRequestDTO) {
        String username = normalizeUserEmail(loginRequestDTO.userEmail());
        UserDetails foundUser = userService.loadUserByUsername(username);

        if (passwordEncoder.matches(loginRequestDTO.password(), foundUser.getPassword())) {
            String accessToken = tokenService.generateAccessToken(foundUser);
            String refreshToken = tokenService.generateRefreshToken(foundUser);
            refreshStorage.put(username, refreshToken);

            return new TokenResponseDTO(accessToken, refreshToken);
        }

        throw new RestException(HttpStatus.UNAUTHORIZED, "Incorrect login and/or password");
    }

    public ait.hwds.security.dto.TokenResponseDTO refreshAccessToken(String refreshToken) {
        boolean isValid = tokenService.validateRefreshToken(refreshToken);
        if (!isValid) {
            throw new RestException(HttpStatus.FORBIDDEN, "Incorrect refresh token. Re login please");
        }
        Claims refreshClaims = tokenService.getRefreshClaimsFromToken(refreshToken);
        String username = refreshClaims.getSubject();

        String savedToken = refreshStorage.getOrDefault(username, null);
        boolean isSaved = savedToken != null && savedToken.equals(refreshToken);

        if (isSaved) {
            UserDetails foundUser = userService.loadUserByUsername(username);

            String accessToken = tokenService.generateAccessToken(foundUser);

            return new TokenResponseDTO(accessToken, refreshToken);
        }
        throw new RestException(HttpStatus.FORBIDDEN, "Incorrect refresh token. Re login please");
    }

    @Transactional
    public ait.hwds.model.dto.UserDto register(ait.hwds.security.dto.@Valid RegisterRequestDTO loginRequestDTO) {

        String hashPassword = passwordEncoder.encode(loginRequestDTO.password());
        String normalizedEmail = normalizeUserEmail(loginRequestDTO.userEmail());
        Optional<User> foundUser = userRepository.findUserByEmail(normalizedEmail);
        if (foundUser.isPresent()) {
            throw new RestException(HttpStatus.CONFLICT, "User with email {0} already exist", loginRequestDTO.userEmail());
        }

        Role userRole = roleService.findRoleByTitleOrThrow(DEFAULT_USER_ROLE);
        User user = new User(loginRequestDTO.firstName(),
                loginRequestDTO.lastName(),
                normalizedEmail,
                loginRequestDTO.phoneNumber(),
                hashPassword,
                userRole);

        userRepository.save(user);
        String code = confirmationService.generateConfirmationCode(user);

        emailService.sendConfirmationEmail(user, code);
        return userMappingService.mapEntityToDto(user);
    }
}
