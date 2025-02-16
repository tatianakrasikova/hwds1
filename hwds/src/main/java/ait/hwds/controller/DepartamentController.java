package ait.hwds.controller;


import ait.hwds.model.dto.CreateOrUpdateDepartamentDto;
import ait.hwds.model.dto.DepartamentDto;
import ait.hwds.service.interfaces.DepartamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/departaments")
@Tag(name = "Departament", description = "Controller for operations with departaments")

public class DepartamentController {

    private final DepartamentService departamentService;

    public DepartamentController(DepartamentService departamentService) {
        this.departamentService = departamentService;
    }


    @Operation(summary = "Get all departaments", description = "Retrieve a list of all departaments",
            security = @SecurityRequirement(name = ""))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DepartamentDto.class))),
                            @Content(mediaType = "application/xml",
                                    array = @ArraySchema(schema = @Schema(implementation = DepartamentDto.class)))
                    }
            )
    })

    /**
     * Получить список всех комнат (GET /rooms).
     */
    @GetMapping
    public List<DepartamentDto> getAllDepartaments() {
        return departamentService.getAllDepartament();
    }


    @Operation(summary = "Get departament by id", description = "Retrieve departament details by its ID",
            security = @SecurityRequirement(name = ""))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepartamentDto.class)),
                            @Content(mediaType = "application/xml",
                                    schema = @Schema(implementation = DepartamentDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Departament not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class, example = "Departament not found"))
            )
    })
    /**
     * Найти комнату по ID (GET /Departaments/{id}).
     */
    @GetMapping("/{id}")
    public DepartamentDto getDepartamentById(@PathVariable Long id) {
        return departamentService.getDepartamentById(id);
    }


    @Operation(summary = "Create departament", description = "Add a new departament")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Departament created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepartamentDto.class)),
                            @Content(mediaType = "application/xml",
                                    schema = @Schema(implementation = DepartamentDto.class))
                    }),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class, example = "User not authenticated"))
            ),
            @ApiResponse(responseCode = "403", description = "User doesn't have rights",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class, example = "User doesn't have rights"))
            )
    })
    /**
     * Создать новую комнату (POST/rooms).
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public DepartamentDto createDepartament(@RequestBody CreateOrUpdateDepartamentDto departamentDto) {
        return departamentService.createDepartament(departamentDto);
    }


    @Operation(summary = "Delete departament", description = "Delete departament by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Departament deleted",
                    content = @Content
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class, example = "User not authenticated"))
            ),
            @ApiResponse(responseCode = "403", description = "User doesn't have rights",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class, example = "User doesn't has rights"))
            ),
            @ApiResponse(responseCode = "404", description = "Departament not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class, example = "Departament not found"))
            )
    })
    /**
     * Удалить комнату (DELETE /rooms/{id}).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDepartament(@PathVariable Long id) {
        departamentService.deleteDepartament(id);
    }


    @Operation(summary = "Get total departament price", description = "Retrieve the total price of articles in the specified departament",
            security = @SecurityRequirement(name = ""))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(type = "number", example = "120.99")),
                            @Content(mediaType = "application/xml",
                                    schema = @Schema(type = "number", example = "120.99"))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Departament not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class, example = "Departament not found"))
            )
    })
    /**
     * Получить общую стоимость кроватей по комнате (GET /rooms/{id}/total_price).
     */
    @GetMapping("/{id}/total_price")
    public BigDecimal getTotalArticlePrice(@PathVariable Long id) {
        return departamentService.getTotalArticlePriceForDepartament(id);
    }
}
