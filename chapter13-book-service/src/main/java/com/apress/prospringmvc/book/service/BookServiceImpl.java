/*
Freeware License, some rights reserved

Copyright (c) 2020 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy 
of this software and associated documentation files (the "Software"), 
to work with the Software within the limits of freeware distribution and fair use. 
This includes the rights to use, copy, and modify the Software for personal use. 
Users are also allowed and encouraged to submit corrections and modifications 
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for 
commercial use in any way, or for a user's educational materials such as books 
or blog articles without prior permission from the copyright holder. 

The above copyright notice and this permission notice need to be included 
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospringmvc.book.service;

import com.apress.prospringmvc.book.Book;
import com.apress.prospringmvc.book.BookSearchCriteria;
import com.apress.prospringmvc.book.repository.BookRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Iuliana Cosmina on 27/07/2020
 */
@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {
	private static final int RANDOM_BOOKS = 2;

	private final BookRepository bookRepository;

	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public Flux<Book> findBooksByCategory(String category) {
		return this.bookRepository.findByCategory(category);
	}

	@Override
	public Mono<Book> findBook(String id) {
		return this.bookRepository.findById(id);
	}

	@Override
	public Mono<Book> findBookByIsbn(String isbn) {
		return this.bookRepository.findByIsbn(isbn);
	}

	@Override
	public Flux<Book> findRandomBooks() {
		PageRequest request = PageRequest.of(0, RANDOM_BOOKS);
		return this.bookRepository.findRandom(request);
	}

	@Override
	public Flux<Book> findBooks(BookSearchCriteria bookSearchCriteria) {
		if (bookSearchCriteria.isEmpty()) {
			return Flux.empty();
		}
		Query query = new Query();
		if (StringUtils.isNotEmpty(bookSearchCriteria.getTitle())) {
			query.addCriteria(Criteria.where("title").is(bookSearchCriteria.getTitle()));
		}
		if (StringUtils.isNotEmpty(bookSearchCriteria.getCategory())) {
			query.addCriteria(Criteria.where("category").is(bookSearchCriteria.getCategory()));
		}
		return bookRepository.findAll(query);
	}

	@Override
	public Mono<Book> addBook(Book book) {
		return this.bookRepository.save(book);
	}

	@Override
	public Mono<Void> deleteBook(String bookIsbn) {
		return bookRepository.findByIsbn(bookIsbn).doOnNext(bookRepository::delete).then(Mono.empty());
	}

	@Override
	public Mono<Void> updateByIsbn(String bookIsbn, Mono<Book> bookMono) {
		return bookRepository.findByIsbn(bookIsbn).doOnNext(
				original ->
						bookMono.doOnNext(
								updatedBook -> {
									original.setTitle(updatedBook.getTitle());
									original.setAuthor(updatedBook.getAuthor());
									original.setCategory(updatedBook.getCategory());
									original.setPrice(updatedBook.getPrice());
									original.setYear(updatedBook.getYear());
									// this op should be enabled only for admin users
									//original.setIsbn(updatedBook.getIsbn());
									original.setDescription(updatedBook.getDescription());
								})
		).then(Mono.empty());
	}
}
