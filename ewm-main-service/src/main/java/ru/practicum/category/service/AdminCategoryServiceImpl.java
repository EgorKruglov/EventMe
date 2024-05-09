package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.extraExceptions.ConflictException;
import ru.practicum.exceptions.extraExceptions.NotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        log.info("Запрос на добавление категории мероприятий");
        Category category = categoryRepository.save(CategoryMapper.toCategory(categoryDto));
        return CategoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        log.info("Запрос на удаление категории мероприятий");
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Категория мероприятий не найдена Id:" + id);
        }
        if (eventRepository.existsByCategoryId(id)) {
            throw new ConflictException("Нельзя удалить категорию мероприятий при наличии мероприятий этой категории");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        log.info("Запрос на обновление категории мероприятий");
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Категория не найдена Id:" + id));
        category.setName(categoryDto.getName());
        return CategoryMapper.toDto(category);
    }
}
