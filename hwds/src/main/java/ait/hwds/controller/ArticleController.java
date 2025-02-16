package ait.hwds.controller;




import ait.hwds.model.dto.ArticleDto;
import ait.hwds.model.dto.CreateArticleDto;
import ait.hwds.service.interfaces.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.FutureOrPresent;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/articles")
@Tag(name = "Article", description = "Controller for operations with articles")

public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Operation(summary = "Create article", description = "Add new article")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Article created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ArticleDto.class)),
                            @Content(mediaType = "application/xml",
                                    schema = @Schema(implementation = ArticleDto.class))
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
     * POST /beds
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleDto saveArticle(@RequestBody CreateArticleDto createArticleDto) {

        return articleService.saveArticle(createArticleDto);
    }

    @Operation(summary = "Get article by id", description = "Retrieve article by its ID", security = @SecurityRequirement(name = ""))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArticleDto.class)),
                            @Content(mediaType = "application/xml",
                                    schema = @Schema(implementation = ArticleDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Article not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class,
                                    example = "Article not found")))
    })

    /**
     * GET /articles/id
     */
    @GetMapping("/{id}")
    public ArticleDto getById(@PathVariable("id") Long id) {
        return articleService.getArticleById(id);
    }


    @Operation(summary = "Get all articles", description = "Returns a list of all articles", security = @SecurityRequirement(name = ""))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of articles",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ArticleDto.class))),
                            @Content(mediaType = "application/xml",
                                    array = @ArraySchema(schema = @Schema(implementation = ArticleDto.class)))
                    }
            )
    })
    /**
     * GET /beds
     */
    @GetMapping()
    public List<ArticleDto> getAll() {
        return articleService.getAllArticles();
    }


    @Operation(summary = "Delete article", description = "Delete article by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Article deleted",
                    content = @Content
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class, example = "User not authenticated"))
            ),
            @ApiResponse(responseCode = "403", description = "User doesn't have rights",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class, example = "User doesn't have rights"))
            ),
            @ApiResponse(responseCode = "404", description = "Article not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class,
                                    example = "Article not found"))
            )
    })
    /**
     * DELETE  /beds/id
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        articleService.deleteArticleById(id);
    }

    @GetMapping("/available-articles")
    public List<ArticleDto> getAvailableArticles(@FutureOrPresent
                                         @DateTimeFormat(pattern = "yyyy-MM-dd")
                                         @Schema(description = "Entry date", type = "string", format = "date", example = "2025-02-10")
                                         @RequestParam LocalDate entryDate,

                                         @FutureOrPresent
                                         @DateTimeFormat(pattern = "yyyy-MM-dd")
                                         @Schema(description = "Departure date", type = "string", format = "date", example = "2025-02-15")
                                         @RequestParam LocalDate departureDate) {
        return articleService.getAvailableArticles(entryDate, departureDate);
    }
}
