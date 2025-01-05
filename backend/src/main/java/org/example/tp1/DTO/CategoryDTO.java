package org.example.tp1.DTO;

import java.util.Date;
import java.util.List;

public class CategoryDTO {
    private final Integer id;
    private final String name;
    private final String parentName;
    private final List<String> childrenNames;
    private final Date creationDate;

    public CategoryDTO(Integer id, String name, String parentName, List<String> childrenNames, Date creationDate) {
        this.id = id;
        this.name = name;
        this.parentName = parentName;
        this.childrenNames = childrenNames;
        this.creationDate = creationDate;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getParentName() {
        return parentName;
    }

    public List<String> getChildrenNames() {
        return childrenNames;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
