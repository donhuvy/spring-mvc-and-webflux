package com.apress.prospringmvc.bookstore.service;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.domain.Book;
import com.apress.prospringmvc.bookstore.domain.BookSearchCriteria;
import com.apress.prospringmvc.bookstore.domain.Cart;
import com.apress.prospringmvc.bookstore.domain.Category;
import com.apress.prospringmvc.bookstore.domain.Order;

import java.util.List;
import java.util.Optional;

/**
 * Main entry point for the bookstore.
 *
 * @author Marten Deinum
 */
public interface BookstoreService {

	List<Book> findBooksByCategory(Category category);

	Book findBook(long id);

	Optional<Order> findOrder(long id);

	List<Book> findRandomBooks();

	/**
	 * Find the {@link Order}s for a given {@link Account}.
	 *
	 * @param account the customer
	 * @return the list of orders (never <code>null</code>).
	 */
	List<Order> findOrdersForAccount(Account account);

	/**
	 * Persist or update the given {@link Order} in the database.
	 *
	 * @param order the order to store.
	 * @return the persisted order.
	 */
	Order store(Order order);

	/**
	 * Search for {@link Book}s that meet the given {@link BookSearchCriteria}.
	 *
	 * @param bookSearchCriteria the search criteria.
	 * @return list of books (never <code>null</code>).
	 */
	List<Book> findBooks(BookSearchCriteria bookSearchCriteria);

	/**
	 * Create an actual {@link Order} for the given {@link Account} based on the content of their {@link Cart}.
	 *
	 * @param cart    the cart
	 * @param account the customer
	 * @return an {@link Order}
	 */
	Order createOrder(Cart cart, Account account);

	/**
	 * Find all the categories available.
	 *
	 * @return list with all the categories
	 */
	Iterable<Category> findAllCategories();

	Category findCategory(long id);

	/**
	 * Store a book in the repository.
	 *
	 * @param book the book to store.
	 */
	void addBook(Book book);
}
