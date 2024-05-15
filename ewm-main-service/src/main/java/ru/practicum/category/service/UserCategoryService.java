package ru.practicum.category.service;

import ru.practicum.category.model.dto.CategoryDto;

import java.util.List;


public interface UserCategoryService {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategory(Long id);
}
