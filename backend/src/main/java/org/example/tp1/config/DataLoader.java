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
        if (categoryRepository.findBy().isEmpty()) {
            Category DC = new Category("DC", null);
            Category marvel = new Category("Marvel", null);
            categoryRepository.save(DC);
            categoryRepository.save(marvel);
            Category batman = new Category("Batman", DC);
            categoryRepository.save(batman);
            Category superman = new Category("Superman", DC);
            categoryRepository.save(superman);
            Category wonderWoman = new Category("Wonder Woman", DC);
            categoryRepository.save(wonderWoman);
            Category xMen = new Category("X-MEN", marvel);
            categoryRepository.save(xMen);
            Category wolverine = new Category("Wolverine", xMen);
            categoryRepository.save(wolverine);
            Category magneto = new Category("Magneto", xMen);
            categoryRepository.save(magneto);
            Category ironMan = new Category("Iron Man", marvel);
            categoryRepository.save(ironMan);
            Category thor = new Category("Thor", marvel);
            categoryRepository.save(thor);
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
            categoryList.add(thor);
            categoryRepository.saveAll(categoryList);
        }
    }
}
