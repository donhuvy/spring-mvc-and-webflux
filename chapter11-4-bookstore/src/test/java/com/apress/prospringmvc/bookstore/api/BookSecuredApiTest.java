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
package com.apress.prospringmvc.bookstore.api;

import com.apress.prospringmvc.bookstore.document.Book;
import com.apress.prospringmvc.bookstore.util.BookSearchCriteria;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by Iuliana Cosmina on 31/07/2020
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookSecuredApiTest {
	private static Logger logger = LoggerFactory.getLogger(BookSecuredApiTest.class);

	@Autowired
	private WebTestClient testClient;

	@WithMockUser(roles = "USER")
	@Test
	void shouldFindByIsbn() {
		testClient.get()
				.uri(uriBuilder -> uriBuilder.path("/book/by/{isbn}").build("9781484230042"))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(Book.class)
				.consumeWith(responseEntity -> {
					logger.debug("Response: {}", responseEntity);
					Book book = responseEntity.getResponseBody();
					assertAll("book", () ->
					{
						assertNotNull(book);
						assertAll("book",
								() -> assertNotNull(book.getTitle()),
								() -> assertEquals("Iuliana Cosmina", book.getAuthor()));
					});
				});
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldCreateABook() {
		Book book = new Book();
		book.setTitle("TDD for dummies");
		book.setAuthor("Test User");
		book.setPrice(BigDecimal.valueOf(40.99));
		book.setIsbn("12232434324");
		book.setCategory("test");

		testClient.post().uri("/book/create")
				.body(Mono.just(book), Book.class)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectHeader().exists("Location")
				.expectBody(Book.class)
				.consumeWith(responseEntity -> {
					logger.debug("Response: {}", responseEntity);
					assertAll("book", () ->
					{
						assertNotNull(book);
						assertAll("book",
								() -> assertNotNull(book.getIsbn()),
								() -> assertEquals("test", book.getCategory()));
					});
				});
	}

	@WithMockUser(roles = "ADMIN")
	@Test
	void shouldDeleteByIsbn() {
		testClient.delete()
				.uri(uriBuilder -> uriBuilder.path("/book/delete/{isbn}").build("9781484230042"))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNoContent();
	}

	@Test
	public void shouldReturnTwoBooks() {
		BookSearchCriteria criteria = new BookSearchCriteria();
		criteria.setCategory(Book.Category.JAVA);

		testClient.post()
				.uri("/book/search")
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(criteria), BookSearchCriteria.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Book.class)
				.hasSize(2);
	}

	@Test
	void test() {
		String[] in = {"x-service", "y-mvc-bookstore", "x-z-bookstore"};
		for (String it : in) {
			if (
					(it.endsWith("service") || it.endsWith("bookstore")) && !(it.contains("mvc"))
			) {
				System.out.println("add boot: " + it);
			}
		}

	}
}
