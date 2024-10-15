package org.example.tp1.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.tp1.entities.Category;
import org.example.tp1.services.CategoryService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CategoryController {

    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category created"),
            @ApiResponse(responseCode = "412", description = "A name is required"),
            @ApiResponse(responseCode = "412", description = "This category already exists"),

    })
    @Operation(summary = "Create category", description = "Inserts a new category into Database")
    @RequestMapping(value="/category", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> insert(@RequestBody String nameJSON) {

        JSONObject jsonObject = new JSONObject(nameJSON);

        if (jsonObject.isNull("name")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "A name is required")
            );
        }

        String name = jsonObject.getString("name");

        if (name == null || name.isEmpty()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "A name is required")
            );
        }

        if (categoryService.categoryAlreadyExists(name)) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "This category already exists")
            );
        }

        Category category = categoryService.insertCategory(name);
        return ResponseEntity.ok(categoryService.createCategoryJSON(category));
    }
}
