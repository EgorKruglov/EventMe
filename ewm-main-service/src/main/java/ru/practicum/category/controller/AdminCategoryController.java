package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Validator;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.service.AdminCategoryService;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    private final AdminCategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @Validated(Validator.Create.class) CategoryDto categoryDtoategory) {
        CategoryDto result = categoryService.addCategory(categoryDtoategory);
        log.info("Добавлена новая категория мероприятий {}", result);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable @Positive Long id) {
        categoryService.deleteCategory(id);
        log.info("Удалена категория мероприятий id:{}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Удалена категория мероприятий id:{}" + id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id,
                                                      @RequestBody @Validated(Validator.Update.class) CategoryDto categoryDto) {
        CategoryDto result = categoryService.updateCategory(id, categoryDto);
        log.info("Обновлена категория мероприятий {}", result);
        return ResponseEntity.ok().body(result);
    }
}
