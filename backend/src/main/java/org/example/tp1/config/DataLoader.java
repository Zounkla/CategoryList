package org.example.tp1.config;

import org.example.tp1.entities.Category;
import org.example.tp1.repositories.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        Category batman = new Category("Batman", DC);
        categoryRepository.insertIfNotExists(batman);
        Category superman = new Category("Superman", DC);
        categoryRepository.insertIfNotExists(superman);
        Category wonderWoman = new Category("Wonder Woman", DC);
        categoryRepository.insertIfNotExists(wonderWoman);
        Category xMen = new Category("X-MEN", marvel);
        categoryRepository.insertIfNotExists(xMen);
        Category wolverine = new Category("Wolverine", xMen);
        categoryRepository.insertIfNotExists(wolverine);
        Category magneto = new Category("Magneto", xMen);
        categoryRepository.insertIfNotExists(magneto);
        Category ironMan = new Category("Iron Man", marvel);
        categoryRepository.insertIfNotExists(ironMan);
        Category thor = new Category("Thor", marvel);
        categoryRepository.insertIfNotExists(thor);
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(DC);
        categoryList.add(marvel);
        categoryList.add(batman);
        categoryList.add(superman);
        categoryList.add(wonderWoman);
        categoryList.add(xMen);
        categoryList.add(wolverine);
        categoryList.add(magneto);
        categoryList.add(ironMan);
        categoryList.add(DC);
        categoryRepository.saveAllIfNotExist(categoryList);
    }
}
