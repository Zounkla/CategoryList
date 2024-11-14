package org.example.tp1.repositories;

import org.example.tp1.entities.Category;
import org.example.tp1.services.CategoryService;
import org.hibernate.service.spi.InjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    List<Category> findBy();

    Page<Category> findBy(Pageable pageable);

    Page<Category> findByParentIsNotNull(Pageable pageable);

    Page<Category> findAllByParentIsNull(Pageable pageable);

    Page<Category> findAllByParent(Category parent, Pageable pageable);

    Optional<Category> findByName(String name);

    Page<Category> findByParent(Category category, Pageable pageable);

    List<Category> findByParent(Category category);

    default void insertIfNotExists(Category category) {
        if (findByName(category.getName()).isEmpty()) {
            save(category);
        }
    }

    default void saveAllIfNotExist(List<Category> categories) {
        categories.forEach(this::insertIfNotExists);
    }
}
