package org.example.tp1.DTO;

import java.util.List;

public class CategoriesDTO {

    private final List<CategoryDTO> categories;
    private final int totalPages;

    public CategoriesDTO(List<CategoryDTO> categories, int totalPages) {
        this.categories = categories;
        this.totalPages = totalPages;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }
    public int getTotalPages() {
        return totalPages;
    }
}
