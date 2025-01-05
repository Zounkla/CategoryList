package org.example.tp1.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateCategoryDTO  {

    private String name;

    private String oldName;

    private String parentName;
}
