package org.example.tp1.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany
    private List<Category> children;

    @OneToOne
    private Category parent;

    private Date creationDate;

    public Category(String name, List<Category> children, Category parent) {
        this.name = name;
        this.children = children;
        this.parent = parent;
        this.creationDate = new Date();
    }

    public Category(String name) {
        this.name = name;
        this.children = new ArrayList<>();
        this.parent = null;
        this.creationDate = new Date();
    }

    protected Category() {}

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getChildren() {
        return children;
    }

    public List<String> getChildrenNames() {
        List<String> list = new ArrayList<>();
        for (Category child : children) {
            list.add(child.getName());
        }
        return list;
    }

    public void addChildren(Category child) {
        if(child == this) {
            throw new IllegalArgumentException("Cannnot be child of itself");
        }
        this.children.add(child);
    }

    public void removeChildren(Category child) {
        this.children.remove(child);
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        if(parent == this) {
            throw new IllegalArgumentException("Cannnot be parent of itself");
        }
        this.parent = parent;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
