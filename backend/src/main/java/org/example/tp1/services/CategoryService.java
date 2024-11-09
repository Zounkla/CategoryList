package org.example.tp1.services;

import org.example.tp1.entities.Category;
import org.example.tp1.repositories.CategoryRepository;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public String createCategoriesJSON(List<Category> categories) {
        Map<String, Map<String, String>> jsonMap = new HashMap<>();
        for (Category category : categories) {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(category.getId()));
            map.put("name", category.getName());
            map.put("children", category.getChildrenNames().toString());
            Category parent = category.getParent();
            map.put("parent", parent == null ? "null" : parent.getName());
            map.put("date", category.getCreationDate().toString());
            jsonMap.put(category.getName(), map);
        }
        JSONObject jo = new JSONObject(jsonMap);
        return jo.toString();
    }

    public boolean categoryAlreadyExists(String name) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name);
        return optionalCategory.isPresent();
    }

    public Category insertCategory(String name, Category parent) {
        Category category = new Category(name, parent);
        categoryRepository.save(category);
        return category;
    }

    public Category updateParent(String parentName, String childName) {
        Optional<Category> optionalParentCategory = categoryRepository.findByName(parentName);
        Optional<Category> optionalChildCategory = categoryRepository.findByName(childName);
        if (optionalParentCategory.isEmpty() || optionalChildCategory.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        Category parent = optionalParentCategory.get();
        Category child = optionalChildCategory.get();
        Category oldParent = child.getParent();
        if (oldParent != null) {
            oldParent.removeChildren(child);
            categoryRepository.save(oldParent);
        }
        child.setParent(parent);
        parent.addChildren(child);
        categoryRepository.save(child);
        categoryRepository.save(parent);
        return parent;
    }

    public Category updateName(String oldName, String newName) {
        Optional<Category> optionalCategory = categoryRepository.findByName(oldName);
        if (optionalCategory.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        Category category = optionalCategory.get();
        category.setName(newName);
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

    public List<Category> getRootCategories() {
        return categoryRepository.findByParentIsNull();
    }

    public List<Category> getNotRootCategories() {
        return categoryRepository.findByParentIsNotNull();
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
            if (parent != null) {
                parent.addChildren(child);
            }
        }
        categoryRepository.delete(category);
        return category;
    }

    public List<Category> getChildren(Category category) {
        return this.categoryRepository.findByParent(category);
    }

    public List<Category> getPaginatedCategories(int page, String parentName) {
        Page<Category> categoryPage = getCategoryPage(page, parentName);
        return categoryPage.getContent();
    }

    public int getPageCount(String parentName) {
        Page<Category> categoryPage = getCategoryPage(0, parentName);
        return categoryPage.getTotalPages();
    }

    public String createPageCountJSON(Integer pageCount) {
        Map<String, Integer> jsonMap = new HashMap<>();
        jsonMap.put("id", pageCount);
        JSONObject jo = new JSONObject(jsonMap);
        return jo.toString();
    }

    private Page<Category> getCategoryPage(int page, String parentName) {
        Optional<Category> optionalCategory = categoryRepository.findByName(parentName);
        Category parent = optionalCategory.orElse(null);
        if (parent == null) {
            return categoryRepository.findAllByParentIsNull(PageRequest.of(page, CATEGORY_PER_PAGE));
        }
        return categoryRepository.findAllByParent(parent, PageRequest.of(page, CATEGORY_PER_PAGE));
    }
}
