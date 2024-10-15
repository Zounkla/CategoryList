package org.example.tp1.services;

import org.example.tp1.entities.Category;
import org.example.tp1.repositories.CategoryRepository;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public String createError(HttpStatus status, String message) {
        Map<String, String> map = new HashMap<>();
        map.put("status", String.valueOf(status.value()));
        map.put("message", message);
        JSONObject jo = new JSONObject(map);
        return jo.toString();
    }

    public String createCategoryJSON(Category category) {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(category.getId()));
        map.put("name", category.getName());
        map.put("children", category.getChildren().toString());
        Category parent = category.getParent();
        map.put("parent", parent == null ? "null" : parent.getName());
        map.put("date", category.getCreationDate().toString());
        JSONObject jo = new JSONObject(map);
        return jo.toString();
    }

    public boolean categoryAlreadyExists(String name) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name);
        return optionalCategory.isPresent();
    }

    public Category insertCategory(String name) {
        Category category = new Category(name);
        categoryRepository.save(category);
        return category;
    }
}
