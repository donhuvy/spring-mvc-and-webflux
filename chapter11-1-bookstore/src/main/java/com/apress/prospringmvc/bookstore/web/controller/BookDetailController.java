package com.apress.prospringmvc.bookstore.web.controller;

import com.apress.prospringmvc.bookstore.service.BookstoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to handle book detail requests.
 *
 * @author Marten Deinum
 */
@Controller
public class BookDetailController {

	@Autowired
	private BookstoreService bookstoreService;

	/**
	 * Method used to prepare our model and select the view to show the details of the selected book.
	 *
	 * @param bookId the id of the book
	 * @param model  the implicit model
	 * @return view name to render (book/detail)
	 */
	@RequestMapping(value = "/book/detail/{bookId}")
	public String details(@PathVariable("bookId") long bookId, Model model) {
		var book = this.bookstoreService.findBook(bookId);
		model.addAttribute(book);
		return "book/detail";
	}
}
