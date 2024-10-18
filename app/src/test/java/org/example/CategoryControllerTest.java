package org.example;

import org.example.entity.Category;
import org.example.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void getAllCategoriesTest() throws Exception {
        List<Category> categories = new ArrayList<>();
        Category category = new Category();
        category.setName("Test Category");
        category.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        categories.add(category);
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        when(categoryService.getAllCategories(any(UUID.class))).thenReturn(categories);

        mockMvc.perform(get("/api/v1/categories/all")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Category"));
    }

    @Test
    public void addCustomCategoryTest() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        mockMvc.perform(post("/api/v1/categories/add")
                        .param("name", "New Category")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Category added"));
    }

    @Test
    public void getCategoryByNameTest() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        Category category = new Category();
        category.setName("food");
        category.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        when(categoryService.getCategoryByName(eq("food"), any(UUID.class)))
                .thenReturn(Optional.of(category));

        mockMvc.perform(get("/api/v1/categories/food")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("food"));
    }
}

