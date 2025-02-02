package com.apress.prospringmvc.bookstore.service;

import com.apress.prospringmvc.bookstore.document.Account;
import com.apress.prospringmvc.bookstore.document.Book;
import com.apress.prospringmvc.bookstore.document.Cart;
import com.apress.prospringmvc.bookstore.document.Order;
import com.apress.prospringmvc.bookstore.util.BookSearchCriteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Iuliana Cosmina on 27/07/2020
 */
public interface BookstoreService {

	Flux<Book> findBooksByCategory(String category);

	Mono<Book> findBook(String id);

	Flux<Book> findRandomBooks();

	Mono<Order> findOrder(String id);

	Mono<List<Order>> findOrdersForAccountId(String accountId);

	Flux<Book> findBooks(BookSearchCriteria bookSearchCriteria);

	Mono<Order> createOrder(Cart cart, Account account);

	Mono<Book> addBook(Book book);

	Mono<Void> deleteBook(String bookIsbn);

	Mono<Book> findBookByIsbn(String isbn);

	Mono<Void> updateByIsbn(String isbn, Mono<Book> bookMono);

}
