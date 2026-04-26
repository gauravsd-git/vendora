package com.gaurav.vendora.service.impl;

import com.gaurav.vendora.configuration.JwtProvider;
import com.gaurav.vendora.mapper.CategoryMapper;
import com.gaurav.vendora.model.Category;
import com.gaurav.vendora.model.Store;
import com.gaurav.vendora.model.User;
import com.gaurav.vendora.payload.dto.CategoryDto;
import com.gaurav.vendora.repository.CategoryRepository;
import com.gaurav.vendora.repository.UserRepository;
import com.gaurav.vendora.service.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(CategoryDto dto, String jwt) throws Exception {

        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);

        Store store = user.getStore();

        if (store == null) {
            throw new RuntimeException("User not assigned to store");
        }

        Category category = categoryMapper.toEntity(dto);
        category.setStore(store);

        Category saved = categoryRepository.save(category);

        return categoryMapper.toDto(saved);
    }

    @Override
    public List<CategoryDto> getAllCategories(String jwt) throws Exception {

        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);

        Store store = user.getStore();

        List<Category> categories = categoryRepository.findByStoreId(store.getId());

        return categories.stream()
                .map(categoryMapper::toDto)
                .toList();
    }
}