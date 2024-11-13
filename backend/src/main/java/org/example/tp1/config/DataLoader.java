package org.example.tp1.config;

import org.example.tp1.entities.Category;
import org.example.tp1.repositories.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataLoader(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Override
    public void run(String... args) {
        Category DC = new Category("DC", null);
        Category marvel = new Category("Marvel", null);
        categoryRepository.insertIfNotExists(DC);
        categoryRepository.insertIfNotExists(marvel);
        categoryRepository.insertIfNotExists(new Category("Batman", DC));
        categoryRepository.insertIfNotExists(new Category("Superman", DC));
        categoryRepository.insertIfNotExists(new Category("Wonder Woman", DC));
        Category xMen = new Category("X-MEN", marvel);
        categoryRepository.insertIfNotExists(xMen);
        categoryRepository.insertIfNotExists(new Category("Wolverine", xMen));
        categoryRepository.insertIfNotExists(new Category("Magneto", xMen));
        categoryRepository.insertIfNotExists(new Category("Iron Man", marvel));
        categoryRepository.insertIfNotExists(new Category("Thor", marvel));
    }
}
