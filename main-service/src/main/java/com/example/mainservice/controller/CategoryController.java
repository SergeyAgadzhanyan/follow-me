package com.example.mainservice.controller;

import com.example.mainservice.model.Category;
import com.example.mainservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getCategories(@RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return categoryService.getCategories(from / size, size);
    }

    @GetMapping("/{catId}")
    public Category getCategoryById(@PathVariable Long catId) {
        return categoryService.getCategoryById(catId);
    }

}
