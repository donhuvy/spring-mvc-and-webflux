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
package com.apress.prospringmvc.account.service;

import com.apress.prospringmvc.account.AccountRepository;
import com.apress.prospringmvc.account.document.Account;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Iuliana Cosmina on 27/07/2020
 */
@Service
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public Mono<Account> create(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public Mono<Account> findOne(String username) {
		return accountRepository.findByUsername(username);
	}

	@Override
	public Mono<Account> getAccountLight(String username) {
		return accountRepository.findLightByUsername(username);
	}

	@Override
	public Mono<Void> update(String username, Account account) {
		return accountRepository.findByUsername(username).doOnNext(
				original ->
				{
					original.setAddress(account.getAddress());
					original.setFirstName(account.getFirstName());
					original.setLastName(account.getLastName());
					// this op should be enabled only for admin users
					//original.setUsername(account.getUsername());
					original.setPassword(account.getPassword());
					original.setEmailAddress(account.getEmailAddress());
				}
		).then(Mono.empty());
	}

	@Override
	public Mono<Void> deleteByUsername(String username) {
		return accountRepository.findLightByUsername(username).doOnNext(accountRepository::delete).then(Mono.empty());
	}

	@Override
	public Flux<Account> findAll() {
		return accountRepository.findAll();
	}
}
