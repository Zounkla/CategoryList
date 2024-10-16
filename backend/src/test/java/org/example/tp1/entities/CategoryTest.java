package org.example.tp1.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CategoryTest {

    @Test
    void contextLoads() {
    }
    @Test
    void setNameShouldBeEqual() {
        String name = "plane";
        Category category = new Category();
        category.setName(name);
        assertEquals(category.getName(), name);
    }

    @Test
    void setNameShouldNotBeEqual() {
        String name = "plane";
        String notGoodName = "avion";
        Category category = new Category();
        category.setName(name);
        assertNotEquals(category.getName(), notGoodName);
    }
}