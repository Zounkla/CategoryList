package org.example.tp1.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tp1.DTO.CategoryDTO;
import org.example.tp1.entities.Category;
import org.example.tp1.repositories.CategoryRepository;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidParameterException;
import java.util.*;

@org.springframework.stereotype.Service
public class CategoryService {

    private static final int CATEGORY_PER_PAGE = 2;

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
        map.put("children", category.getChildrenNames().toString());
        Category parent = category.getParent();
        map.put("parent", parent == null ? "null" : parent.getName());
        map.put("date", category.getCreationDate().toString());
        JSONObject jo = new JSONObject(map);
        return jo.toString();
    }

    public String createCategoriesJSON(List<Category> categories, int size) {
        HashMap<String, Map
                <String, Map<String, String>>> jsonMap = new LinkedHashMap<>();
        HashMap<String, Map<String, String>> categoriesMap = new LinkedHashMap<>();
        for (Category category : categories) {
            SortedMap<String, String> map = new TreeMap<>();
            map.put("id", String.valueOf(category.getId()));
            map.put("name", category.getName());
            map.put("children", category.getChildrenNames().toString());
            Category parent = category.getParent();
            map.put("parent", parent == null ? "null" : parent.getName());
            map.put("date", category.getCreationDate().toString());
            categoriesMap.put(category.getName(), map);
        }
        jsonMap.put("categories", categoriesMap);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            StringBuilder result = new StringBuilder(objectMapper.writeValueAsString(jsonMap));
            result.deleteCharAt(result.length() - 1);
            result.append(",\n\"pageCount\": \"").append((int) Math.ceil((double) size / CATEGORY_PER_PAGE)).append("\"").append("}");
            return result.toString();
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public boolean categoryAlreadyExists(String name) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name);
        return optionalCategory.isPresent();
    }

    public Category insertCategory(String name, Category parent) {
        if (categoryAlreadyExists(name)) {
            throw new InvalidParameterException("cartegory already exists");
        }
        Category category = new Category(name, parent);
        categoryRepository.save(category);
        if (parent != null) {
            categoryRepository.save(parent);
        }
        return category;
    }

    public Category updateCategory(String categoryName, String newName, String newParent) {
        Category category = categoryRepository.findByName(categoryName).get();
        Optional<Category> optionalParentCategory = categoryRepository.findByName(newParent);
        Category parent = null;
        if (optionalParentCategory.isPresent()) {
            parent = optionalParentCategory.get();
        }
        if (categoryAlreadyExists(newName)) {
            throw new InvalidParameterException("Category already exists");
        }
        if (category == parent) {
            throw new UnsupportedOperationException("cannot be parent of iteself");
        }
        if (category.getParent() != null) {
            category.getParent().removeChildren(category);
            categoryRepository.save(category.getParent());
        }
        category.setName(newName);
        categoryRepository.save(category);
        if (category.getParent() == parent) {
            return category;
        }
        category.setParent(parent);
        if (parent != null) {
            parent.addChildren(category);
            categoryRepository.save(parent);
        }
        categoryRepository.save(category);
        return category;
    }

    public Category getCategory(String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
        if (optionalCategory.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        return optionalCategory.get();
    }

    public List<Category> getCategories() {
        return categoryRepository.findBy();
    }

    public Category deleteCategory(String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
        if (optionalCategory.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        Category category = optionalCategory.get();
        Category parent = category.getParent();

        for (Category child : category.getChildren()) {
            child.setParent(parent);
            categoryRepository.save(child);
        }
        if (parent != null) {
            parent.removeChildren(category);
            categoryRepository.save(parent);
        }
        categoryRepository.delete(category);
        return category;
    }

    public List<Category> getByPage(List<Category> categories, int pageNb) {
        Page<Category> categoryPage = getCategoriesPage(pageNb, categories);
        return new ArrayList<>(categoryPage.getContent());
    }

    public CategoryDTO getDTO(Category category) {
        String parentName = category.getParent() == null ? "" : category.getParent().getName();
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                parentName,
                category.getChildrenNames(),
                category.getCreationDate()
        );
    }

    public List<CategoryDTO> getListDTO(List<Category> categories) {
        List<CategoryDTO> result = new ArrayList<>();
        for (Category category : categories) {
            result.add(getDTO(category));
        }
        return result;
    }

    private Page<Category> getCategoriesPage(int page, List<Category> categories) {
        List<String> names = new ArrayList<>();
        for (Category category : categories) {
            names.add(category.getName());
        }
        return categoryRepository.findAllByName(names, PageRequest.of(page, CATEGORY_PER_PAGE));
    }
}
