package org.example.tp1.repositories;

import org.example.tp1.entities.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    List<Category> findBy();

    Optional<Category> findByName(String name);
}
