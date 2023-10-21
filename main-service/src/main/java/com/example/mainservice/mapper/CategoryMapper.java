package com.example.mainservice.mapper;

import com.example.mainservice.model.Category;
import com.example.mainservice.model.NewCategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public Category mapToCategory(NewCategoryDto newCategoryDto) {
        return new Category(null, newCategoryDto.getName());
    }

}
