package com.apress.prospringmvc.bookstore.config;

import com.apress.prospringmvc.bookstore.domain.Book;
import com.apress.prospringmvc.bookstore.domain.Category;
import com.apress.prospringmvc.bookstore.repository.BookRepository;
import com.apress.prospringmvc.bookstore.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class DbPopulator {

	private Logger logger = LoggerFactory.getLogger(DbPopulator.class);

	private BookRepository bookRepository;

	private CategoryRepository categoryRepository;

	public DbPopulator(BookRepository bookRepository, CategoryRepository categoryRepository) {
		this.bookRepository = bookRepository;
		this.categoryRepository = categoryRepository;
	}

	@PostConstruct
	private void init() {
		logger.info(" -->> Starting database initialization...");
		List<Category> categories = List.of(new Category("Spring"), new Category("Java"), new Category("Web"));
		categoryRepository.saveAll(categories);

		List<Book> books = List.of(
				new Book("Spring Boot 2 Recipes", BigDecimal.valueOf(37.44), 2017, "Marten Deinum", "9781484227893", categoryRepository.findByName("Spring").get()),
				new Book("Pivotal Certified Professional Core Spring 5 Developer Exam", BigDecimal.valueOf(54.99), 2018, "Iuliana Cosmina", "9781484251355", categoryRepository.findByName("Spring").get()),
				new Book("Java for Absolute Beginners", BigDecimal.valueOf(24.99), 2020, "Iuliana Cosmina", "9781484237779", categoryRepository.findByName("Java").get())
		);

		bookRepository.saveAll(books);
		logger.info(" -->> Database initialization completed.");
	}

}
