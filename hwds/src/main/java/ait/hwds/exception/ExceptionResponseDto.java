package ait.hwds.exception;


import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "DTO for representing exception responses")
public record ExceptionResponseDto(
        @Schema(description = "Error message explaining the exception", example = "Invalid input data")
        String message,
        @Schema(description = "HTTP status code associated with the exception", example = "400")
        Integer statusCode
) {
}
