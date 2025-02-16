package ait.hwds.service;


import ait.hwds.exception.RestException;
import ait.hwds.model.entity.ConfirmationCode;
import ait.hwds.model.entity.User;
import ait.hwds.repository.ConfirmationCodeRepository;
import ait.hwds.service.interfaces.ConfirmationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationCodeRepository repository;

    public ConfirmationServiceImpl(ConfirmationCodeRepository repository) {
        this.repository = repository;
    }

    @Override
    public String generateConfirmationCode(User user) {

        String code = UUID.randomUUID().toString();

        ConfirmationCode confirmationCode = new ConfirmationCode(
                code,
                LocalDateTime.now().plusDays(3),
                user
        );

        repository.save(confirmationCode);
        return code;
    }

    @Transactional
    @Override
    public ConfirmationCode validateToken(String confirmationTokenCode) {
        ConfirmationCode confirmationCode = findOrThrow(confirmationTokenCode);
        LocalDateTime tokenExpiration = confirmationCode.getExpired();

        if (tokenExpiration.isBefore(LocalDateTime.now())) {
            throw new RestException("Token expired");
        }
        repository.delete(confirmationCode);
        return confirmationCode;
    }

    @Transactional
    @Override
    public ConfirmationCode findOrThrow(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Confirmation code not found"));
    }
}
