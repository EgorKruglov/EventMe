package ru.practicum.category.service;

import ru.practicum.category.model.dto.CategoryDto;

public interface AdminCategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);

    void deleteCategory(Long id);

    CategoryDto updateCategory(Long id, CategoryDto categoryDto);
}
