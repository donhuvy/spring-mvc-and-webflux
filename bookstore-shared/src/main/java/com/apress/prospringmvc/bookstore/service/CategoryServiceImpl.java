package com.apress.prospringmvc.bookstore.service;

import com.apress.prospringmvc.bookstore.domain.Category;
import com.apress.prospringmvc.bookstore.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Marten Deinum
 * @see CategoryService
 */
@Service
@Transactional(readOnly = true)
class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Category findById(long id) {
		return categoryRepository.findById(id).orElse(null);
	}

	@Override
	public Iterable<Category> findAll() {
		return this.categoryRepository.findAll();
	}

	@Override
	public void addCategory(Category category) {
		categoryRepository.save(category);
	}
}
