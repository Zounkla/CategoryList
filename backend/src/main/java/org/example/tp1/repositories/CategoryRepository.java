package org.example.tp1.repositories;

import org.example.tp1.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findBy();

    Page<Category> findBy(Pageable pageable);

    Page<Category> findByParentIsNotNull(Pageable pageable);

    Page<Category> findAllByParentIsNull(Pageable pageable);

    Page<Category> findAllByParent(Category parent, Pageable pageable);

    Optional<Category> findByName(String name);

    Page<Category> findByParent(Category category, Pageable pageable);

    List<Category> findByParent(Category category);

    @Query("SELECT c FROM Category  c WHERE c.name IN :names")
    Page<Category> findAllByName(List<String> names, Pageable pageable);
}
