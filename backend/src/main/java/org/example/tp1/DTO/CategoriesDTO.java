package org.example.tp1.DTO;

import java.util.List;

public class CategoriesDTO {

    private final List<CategoryDTO> categories;
    private final int totalItems;

    public CategoriesDTO(List<CategoryDTO> categories, int totalItems) {
        this.categories = categories;
        this.totalItems = totalItems;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }
    public int getTotalItems() {
        return totalItems;
    }
}
