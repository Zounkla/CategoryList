package org.example.tp1.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tp1.entities.Category;
import org.example.tp1.repositories.CategoryRepository;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    public String createCategoriesJSON(List<Category> categories) {
        HashMap<String, Map<String, String>> jsonMap = new LinkedHashMap<>();
        for (Category category : categories) {
            SortedMap<String, String> map = new TreeMap<>();
            map.put("id", String.valueOf(category.getId()));
            map.put("name", category.getName());
            map.put("children", category.getChildrenNames().toString());
            Category parent = category.getParent();
            map.put("parent", parent == null ? "null" : parent.getName());
            map.put("date", category.getCreationDate().toString());
            jsonMap.put(category.getName(), map);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public boolean categoryAlreadyExists(String name) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name);
        return optionalCategory.isPresent();
    }

    public Category insertCategory(String name, Category parent) {
        Category category = new Category(name, parent);
        categoryRepository.save(category);
        categoryRepository.save(parent);
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
        return new ArrayList<>(categoryPage.getContent());
    }

    public List<Category> getRootPaginatedCategories(int page, String parentName) {
        Page<Category> categoryPage = getRootCategoryPage(page, parentName);
        return new ArrayList<>(categoryPage.getContent());
    }

    public List<Category> getNonRootPaginatedCategories(int page, String parentName) {
        Page<Category> categoryPage = getNonRootCategoryPage(page, parentName);
        return new ArrayList<>(categoryPage.getContent());
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
        if (parentName.isEmpty()) {
            return categoryRepository.findBy(PageRequest.of(page, CATEGORY_PER_PAGE));
        }
        Optional<Category> optionalCategory = categoryRepository.findByName(parentName);
        Category parent = optionalCategory.orElse(null);
        if (parent == null) {
            return categoryRepository.findAllByParentIsNull(PageRequest.of(page, CATEGORY_PER_PAGE));
        }
        return categoryRepository.findAllByParent(parent, PageRequest.of(page, CATEGORY_PER_PAGE));
    }

    private Page<Category> getRootCategoryPage(int page, String parentName) {
        if (!parentName.isEmpty()) {
            return Page.empty();
        }
        return categoryRepository.findByParent(null, PageRequest.of(page, CATEGORY_PER_PAGE));
    }

    private Page<Category> getNonRootCategoryPage(int page, String parentName) {
        if (parentName.isEmpty()) {
            return categoryRepository.findByParentIsNotNull(PageRequest.of(page, CATEGORY_PER_PAGE));
        }
        Optional<Category> optionalCategory = categoryRepository.findByName(parentName);
        Category parent = optionalCategory.orElse(null);
        return categoryRepository.findByParent(parent,
                    PageRequest.of(page, CATEGORY_PER_PAGE));
    }
}
