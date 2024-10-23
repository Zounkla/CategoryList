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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parent associated"),
            @ApiResponse(responseCode = "412", description = "A parent name is required"),
            @ApiResponse(responseCode = "412", description = "A child name is required"),
            @ApiResponse(responseCode = "412", description = "Parent category does not exists"),
            @ApiResponse(responseCode = "412", description = "Child category does not exists"),

    })
    @Operation(summary = "Associate parent and child", description = "Modify parent and children fields in database")
    @RequestMapping(value="/category/updateParent", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateParent(@RequestBody String paramJSON) {
        JSONObject jsonObject = new JSONObject(paramJSON);

        if (jsonObject.isNull("parent")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "A name is required")
            );
        }

        String parent = jsonObject.getString("parent");

        if (parent == null || parent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "A parent name is required")
            );
        }

        if (jsonObject.isNull("child")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "A name is required")
            );
        }

        String child = jsonObject.getString("child");

        if (child == null || child.isEmpty()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "A child name is required")
            );
        }

        try {
            Category category = categoryService.updateParent(parent, child);
            return ResponseEntity.ok(categoryService.createCategoryJSON(category));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "Category does not exists")
            );
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Changed category name"),
            @ApiResponse(responseCode = "412", description = "Category does not exists"),

    })
    @Operation(summary = "Update category name", description = "Modify name field in database")
    @RequestMapping(value="/category/updateName", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateName(@RequestBody String paramJSON) {
        JSONObject jsonObject = new JSONObject(paramJSON);

        if (jsonObject.isNull("oldName")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "A name is required")
            );
        }

        String name = jsonObject.getString("oldName");

        if (name == null || name.isEmpty()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "A name is required")
            );
        }

        if (jsonObject.isNull("newName")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "A name is required")
            );
        }

        String newName = jsonObject.getString("newName");

        if (newName == null || newName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "A name is required")
            );
        }

        try {
            Category category = categoryService.updateName(name, newName);
            return ResponseEntity.ok(categoryService.createCategoryJSON(category));
        } catch (ResponseStatusException e) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                        "Category does not exists")
        );
    }



    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found"),

    })
    @Operation(summary = "Return a category", description = "Select the category on database and return it in JSON format")
    @RequestMapping(value="/category/getCategory", method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCategory(@RequestBody String paramJSON) {
        JSONObject jsonObject = new JSONObject(paramJSON);

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

        try {
            Category category = categoryService.getCategory(name);
            return ResponseEntity.ok(categoryService.createCategoryJSON(category));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    categoryService.createError(HttpStatus.NOT_FOUND,
                            "Category does not exists")
            );
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),

    })
    @Operation(summary = "Return all the categories", description = "Select all the categories on database and " +
            "return it in JSON format")
    @RequestMapping(value="/category/getCategories", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCategories() {
        List<Category> categories = categoryService.getCategories();
        return ResponseEntity.ok(categoryService.createCategoriesJSON(categories));
    }

}
