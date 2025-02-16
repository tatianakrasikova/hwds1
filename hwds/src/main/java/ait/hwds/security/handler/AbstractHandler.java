package ait.hwds.security.handler;


import ait.hwds.security.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;


public abstract class AbstractHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void fillResponse(HttpServletResponse response, int statusCode, String message) {

        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String jsonResponse = null;
        try {
            jsonResponse = objectMapper.writeValueAsString(new AuthResponse(message));
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
