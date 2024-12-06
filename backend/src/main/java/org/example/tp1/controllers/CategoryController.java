package org.example.tp1.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.tp1.entities.Category;
import org.example.tp1.examples.JsonExample;
import org.example.tp1.services.CategoryService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = JsonExample.CATEGORY_EXAMPLE))),
            @ApiResponse(responseCode = "412", description = "Category already exists or " +
                    "cannot be parent of itself or parent does not exist",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = JsonExample.CATEGORY_ERROR_PRECOND_EXAMPLE)))
    })
    @Operation(summary = "Create category", description = "Inserts or update a category into database")
    @RequestMapping(value = "/category", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> insert(@RequestBody (
            description = "New Category to insert",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(example = JsonExample.INSERT_EXAMPLE))) String nameJSON) {
        JSONObject jsonObject = new JSONObject(nameJSON);
        if (jsonObject.isNull("name")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "A name is required")
            );
        }
        String name = jsonObject.getString("name");
        String parentName = "";
        if (!jsonObject.isNull("parentName")) {
            parentName = jsonObject.getString("parentName");
        }

        if (name == null || name.isEmpty()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "A name is required")
            );
        }
        String oldName = "";
        if (!jsonObject.isNull("oldName")) {
            oldName = jsonObject.getString("oldName");
        }

        if (!oldName.isEmpty() && categoryService.categoryAlreadyExists(oldName)) {
            Category category;
            try {
                category = categoryService.updateCategory(oldName, name, parentName);
            } catch (UnsupportedOperationException e) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                        categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                                "Cannot be parent of itself")
                );
            } catch (InvalidParameterException e) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                        categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                                "Category already exists")
                );
            }
            return ResponseEntity.ok(categoryService.createCategoryJSON(category));
        }
        if (!parentName.equals("None") && !categoryService.categoryAlreadyExists(parentName)) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "Parent category does not exist")
            );
        }
        Category parent = parentName.equals("None") ? null : categoryService.getCategory(parentName);
        Category category;
        try  {
            category = categoryService.insertCategory(name, parent);
        } catch (InvalidParameterException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "Category already exists")
            );
        }
        return ResponseEntity.ok(categoryService.createCategoryJSON(category));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(example = JsonExample.CATEGORIES_EXAMPLE)))
    })
    @Operation(summary = "Return all the categories", description = "Select all the categories on database " +
            "filtered by criterias")
    @RequestMapping(value = "/category/search", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCategories(@Parameter(
                                                name =  "isRoot",
                                                description  = "categories matching this state",
                                                example = "false"
                                                ) @RequestParam Optional<Boolean> isRoot,
                                                @Parameter(
                                                name =  "beforeDate",
                                                description  = "categories before this date",
                                                example = "2024-12-31"
                                                ) @RequestParam Optional<String> beforeDate,
                                                @Parameter(
                                                name =  "afterDate",
                                                description  = "categories after this date",
                                                example = "2025-12-31"
                                                )@RequestParam Optional<String> afterDate,
                                                @Parameter(
                                                name =  "beforeDate",
                                                description  = "categories before this date",
                                                example = "2024-12-31"
                                                )@RequestParam Optional<Integer> page,
                                                @Parameter(
                                                name =  "parentName",
                                                description  = "categories having this parent",
                                                example = "DC"
                                                )@RequestParam Optional<String> parentName,
                                                @Parameter(
                                                name =  "orderByName",
                                                example = "true"
                                                )@RequestParam Optional<Boolean> orderByName,
                                                @Parameter(
                                                name =  "orderByCreationDate",
                                                example = "false"
                                                )@RequestParam Optional<Boolean> orderByCreationDate,
                                                @Parameter(
                                                name =  "orderByChildrenNumber",
                                                example = "true"
                                                )@RequestParam Optional<Boolean> orderByChildrenNumber
    ) {
        List<Category> categories = this.categoryService.getCategories();
        int pageNb = 0;
        if (page.isPresent()) {
            pageNb = page.get();
        }
        if (isRoot.isPresent()) {
            if (isRoot.get()) {
                categories.removeIf(category -> category.getParent() != null);
            } else {
                categories.removeIf(category -> category.getParent() == null);
            }
        }
        if (parentName.isPresent()) {
            categories.removeIf(category -> category.getParent() == null
                    || !category.getParent().getName().equals(parentName.get()));
        }

        if (beforeDate.isPresent()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date;
            try {
                date = formatter.parse(beforeDate.get());
            } catch (ParseException e) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                        categoryService.createError(HttpStatus.NOT_FOUND,
                                "Dates must be formatted as 'yyyy-MM-dd'")
                );
            }
            categories.removeIf(category -> category.getCreationDate().compareTo(date) > 0);
        }
        if (afterDate.isPresent()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date;
            try {
                date = formatter.parse(afterDate.get());
            } catch (ParseException e) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                        categoryService.createError(HttpStatus.NOT_FOUND,
                                "Dates must be formatted as 'yyyy-MM-dd'")
                );
            }
            categories.removeIf(category -> category.getCreationDate().compareTo(date) < 0);
        }
        if (orderByName.isPresent() && orderByName.get()) {
            categories.sort(Comparator.comparing(Category::getName));
        } else {
            if (orderByName.isPresent()) {
                categories.sort((e1, e2) -> e2.getName().compareTo(e1.getName()));
            }
        }

        if (orderByCreationDate.isPresent() && orderByCreationDate.get()) {
            categories.sort(Comparator.comparing(Category::getCreationDate));
        } else {
            if (orderByCreationDate.isPresent()) {
                categories.sort((e1, e2) -> e2.getCreationDate().compareTo(e1.getCreationDate()));
            }
        }

        if (orderByChildrenNumber.isPresent() && !orderByChildrenNumber.get()) {
            categories.sort(Comparator.comparingInt(e -> e.getChildren().size()));
        } else {
            if (orderByChildrenNumber.isPresent()) {
                categories.sort((e1, e2) -> e2.getChildren().size() - e1.getChildren().size());
            }
        }
        int numberOfCategories = categories.size();
        categories = this.categoryService.getByPage(categories, pageNb);
        return ResponseEntity.ok(categoryService.createCategoriesJSON(categories, numberOfCategories));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = JsonExample.CATEGORIES_EXAMPLE)))
    })
    @Operation(summary = "Returns all the categories", description = "Selects all the categories on database and " +
            "returns it in JSON format")
    @RequestMapping(value = "/category/all", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCategories() {
        List<Category> categories = categoryService.getCategories();
        return ResponseEntity.ok(categoryService.createCategoriesJSON(categories, categories.size()));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = JsonExample.CATEGORY_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = JsonExample.CATEGORY_NOT_FOUND_EXAMPLE))),
            @ApiResponse(responseCode = "412", description = "Must provide a name",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = JsonExample.CATEGORY_ERROR_PRECOND_EXAMPLE))),
    })
    @Operation(summary = "Delete a category", description = "Delete a category from database")
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping(value = "/category/deleteCategory")
    public ResponseEntity<String> deleteCategory(@RequestParam String categoryName) {
        try {
            Category category = categoryService.deleteCategory(categoryName);
            return ResponseEntity.ok(categoryService.createCategoryJSON(category));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    categoryService.createError(HttpStatus.NOT_FOUND,
                            "Category does not exists")
            );
        }
    }
}
