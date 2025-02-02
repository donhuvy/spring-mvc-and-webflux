package com.apress.prospringmvc.bookstore.web.controller;

import com.apress.prospringmvc.bookstore.domain.BookSearchCriteria;
import com.apress.prospringmvc.bookstore.domain.Category;
import com.apress.prospringmvc.bookstore.service.BookstoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Controller to handle book search requests.
 *
 * @author Marten Deinum
 */
@Controller
public class BookSearchController {

	private final BookstoreService bookstoreService;

	public BookSearchController(BookstoreService bookstoreService) {
		this.bookstoreService = bookstoreService;
	}

	@ModelAttribute
	public BookSearchCriteria criteria() {
		return new BookSearchCriteria();
	}

	@ModelAttribute("categories")
	public Iterable<Category> getCategories() {
		return this.bookstoreService.findAllCategories();
	}

	/**
	 * This method searches our database for books based on the given {@link BookSearchCriteria}.
	 * Only books matching the criteria are returned.
	 *
	 * @param criteria the criteria used for searching
	 * @return the found books
	 */
	@GetMapping("/book/search")
	public String list(@ModelAttribute("bookSearchCriteria") BookSearchCriteria criteria, Model model) {
		model.addAttribute("bookList", this.bookstoreService.findBooks(criteria));
		return "book/search";
	}

}
