package org.example.tp1.services;

import org.example.tp1.DTO.CategoriesDTO;
import org.example.tp1.DTO.CategoryDTO;
import org.example.tp1.entities.Category;
import org.example.tp1.repositories.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CategoryService {

    private static final int CATEGORY_PER_PAGE = 2;

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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
        if (!categoryName.equals(newName) && categoryAlreadyExists(newName)) {
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

    public CategoriesDTO getListDTO(List<Category> categories, int totalItems) {
        List<CategoryDTO> result = new ArrayList<>();
        for (Category category : categories) {
            result.add(getDTO(category));
        }
        totalItems = (int) Math.ceil((double) totalItems / CATEGORY_PER_PAGE);
        return new CategoriesDTO(result, totalItems);
    }

    private Page<Category> getCategoriesPage(int page, List<Category> categories) {
        List<String> names = new ArrayList<>();
        for (Category category : categories) {
            names.add(category.getName());
        }
        return categoryRepository.findAllByName(names, PageRequest.of(page, CATEGORY_PER_PAGE));
    }
}
