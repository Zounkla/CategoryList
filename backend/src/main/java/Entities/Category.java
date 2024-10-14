package Entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private Long id;

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
        this.children = new ArrayList<Category>();
        this.parent = null;
        this.creationDate = new Date();
    }

    public Category() throws Exception {
        throw new Exception("Category must have at least a name");
    }

    public Long getId() {
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
