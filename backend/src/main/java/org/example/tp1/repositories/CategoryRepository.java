package org.example.tp1.repositories;

import org.example.tp1.entities.Category;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    List<Category> findBy();

    Page<Category> findAllByParentIsNull(Pageable pageable);

    Page<Category> findAllByParent(Category parent, Pageable pageable);

    Optional<Category> findByName(String name);

    List<Category> findByParent(Category category);
}
