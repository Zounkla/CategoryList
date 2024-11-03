package org.example.tp1.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CategoryTest {

    private Category category;

    @BeforeEach
    void init() {
        category = new Category();
    }

    @Test
    void constructorWithName() {
        //GIVEN
        String name = "name";
        //WHEN
        Category newCategory = new Category(name);
        //THEN
        assertThat(newCategory.getName()).isEqualTo(name);
    }

    @Test
    void constructorWithNameAndChildrenAndParent() {
        //GIVEN
        String name = "name";
        Category parent = new Category();
        List<Category> children = new ArrayList<>();
        children.add(new Category());
        children.add(new Category());
        //WHEN
        Category newCategory = new Category(name, children, parent);
        //THEN
        assertThat(newCategory.getName()).isEqualTo(name);
        assertThat(newCategory.getParent()).isEqualTo(parent);
        assertThat(newCategory.getChildren()).isEqualTo(children);
    }

    @Test
    void setName() {
        //GIVEN
        String newName = "avion";
        //WHEN
        category.setName(newName);
        //THEN
        assertThat(category.getName()).isEqualTo(newName);
    }

    @Test
    void getChildrenNames() {
        //GIVEN
        String name1 = "hello";
        String name2 = "test";
        Category child1 = new Category(name1);
        Category child2 = new Category(name2);
        category.addChildren(child1);
        category.addChildren(child2);
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add(name1);
        expectedNames.add(name2);
        //WHEN
        List<String> result = category.getChildrenNames();
        //THEN
        assertThat(result).isEqualTo(expectedNames);
    }

    @Test
    void addChildrenShouldFailBecauseOwnChild() {
        //GIVEN

        //THEN
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                //WHEN
                () -> category.addChildren(category)
        );
    }

    @Test
    void addChildrenShouldSucceed() {
        // GIVEN
        Category newCategory = new Category();
        //WHEN
        category.addChildren(newCategory);
        //THEN
        assertThat(category.getChildren().get(category.getChildren().size() - 1))
                .isEqualTo(newCategory);
    }

    @Test
    void removeChildren() {
        // GIVEN
        Category newCategory = new Category();
        category.addChildren(newCategory);
        //WHEN
        category.removeChildren(newCategory);
        //THEN
        assertThat(category.getChildren()).doesNotContain(newCategory);
    }

    @Test
    void setParentShouldFailBecauseOwnParent() {
        //GIVEN

        //THEN
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                //WHEN
                () -> category.setParent(category)
        );
    }

    @Test
    void setParentShouldSucceed() {
        // GIVEN
        Category newCategory = new Category();
        //WHEN
        category.setParent(newCategory);
        //THEN
        assertThat(category.getParent()).isEqualTo(newCategory);
    }
    @Test
    void setCreationDate() {
        // GIVEN
        Date date = new Date();
        //WHEN
        category.setCreationDate(date);
        //THEN
        assertThat(category.getCreationDate()).isEqualTo(date);
    }
}
