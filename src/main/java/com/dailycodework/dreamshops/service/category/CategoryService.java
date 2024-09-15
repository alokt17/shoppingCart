package com.dailycodework.dreamshops.service.category;

import com.dailycodework.dreamshops.exception.AlreadyExistsException;
import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new AlreadyExistsException(category.getName() + " already exists");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete, ()-> {
                    throw new ResourceNotFoundException("Category not found");
        });

    }
}
