package org.example.tp1.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.tp1.entities.Category;
import org.example.tp1.services.CategoryService;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

        String oldName = jsonObject.getString("oldName");
        if (oldName != null) {
            Category category = categoryService.updateName(oldName, name);
            return ResponseEntity.ok(categoryService.createCategoryJSON(category));
        }

        if (categoryService.categoryAlreadyExists(name)) {
            Category category = categoryService.updateParent(parentName, name);
            return ResponseEntity.ok(categoryService.createCategoryJSON(category));
        }

        if (!parentName.isEmpty() && !categoryService.categoryAlreadyExists(parentName)) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "Parent category does not exist")
            );
        }
        Category parent = parentName.isEmpty() ? null : categoryService.getCategory(parentName);
        Category category = categoryService.insertCategory(name, parent);
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
            @ApiResponse(responseCode = "412", description = "Must provide a name"),

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
    @Operation(summary = "Return all the categories", description = "Select all the categories on database " +
            "filtered by criterias")
    @RequestMapping(value="/category/search", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCategories(@RequestParam Optional<Boolean> isRoot,
        @RequestParam Optional<String> beforeDate,
        @RequestParam Optional<String> afterDate,
        @RequestParam Optional<Integer> page,
        @RequestParam Optional<String> parentName,
        @RequestParam Optional<Boolean> orderByName,
        @RequestParam Optional<Boolean> orderByCreationDate,
        @RequestParam Optional<Boolean> orderByChildrenNumber
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
            @ApiResponse(responseCode = "200", description = "Category found"),

    })
    @Operation(summary = "Return the count of pages", description = "Select all the parent matching categories" +
            " and count pages number")
    @RequestMapping(value="/category/getPageCount", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getPageCount(@RequestParam String parentName) {
        int size = categoryService.getPageCount(parentName);
        return ResponseEntity.ok(size);
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
        return ResponseEntity.ok(categoryService.createCategoriesJSON(categories, categories.size()));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "412", description = "Must provide a name"),

    })
    @Operation(summary = "Delete a category", description = "Delete a category from database")
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping(value="/category/deleteCategory")
    public ResponseEntity<String> deleteCategory(@RequestParam String categoryName) {
        System.out.println(categoryName);
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "412", description = "Must provide a name"),

    })
    @Operation(summary = "Retrieves all children names of a category", description = "Returns each child name of given category")
    @RequestMapping(value="/category/children", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getChildren(@RequestParam String name) {

        if (name.isEmpty()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                    categoryService.createError(HttpStatus.PRECONDITION_FAILED,
                            "Must provide a category name")
            );
        }

        if (!name.equals("None") && !categoryService.categoryAlreadyExists(name)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    categoryService.createError(HttpStatus.NOT_FOUND,
                            "Category does not exists")
            );
        }
        List<Category> children;
        if (name.equals("None")) {
            children = categoryService.getCategories();
        } else {
           Category category = categoryService.getCategory(name);
           children = categoryService.getChildren(category);
        }
        return ResponseEntity.ok(categoryService.createCategoriesJSON(children, children.size()));
    }
}
