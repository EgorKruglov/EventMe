package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exceptions.extraExceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserCategoryServiceImpl implements UserCategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto getCategory(Long id) {
        log.info("Запрос на получение категории мероприятий id:{}", id);
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Категория не найдена Id:" + id));
        return CategoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        log.info("Запрос на получение категорий мероприятий");
        List<Category> categories = categoryRepository.findAllCategories(PageRequest.of(from, size, Sort.Direction.ASC, "id"));
        List<CategoryDto> result = new ArrayList<>();
        for (Category category : categories) {
            result.add(CategoryMapper.toDto(category));
        }
        return result;
    }
}
