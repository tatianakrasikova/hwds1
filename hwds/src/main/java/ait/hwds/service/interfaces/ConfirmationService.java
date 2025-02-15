package ait.hwds.service.interfaces;


import ait.hwds.model.entity.ConfirmationCode;
import ait.hwds.model.entity.User;

public interface ConfirmationService {

    String generateConfirmationCode(User user);

    ConfirmationCode validateToken(String confirmationToken);

    ConfirmationCode findOrThrow(String code);
}
