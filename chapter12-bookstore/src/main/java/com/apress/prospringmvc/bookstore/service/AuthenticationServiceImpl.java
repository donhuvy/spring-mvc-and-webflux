package com.apress.prospringmvc.bookstore.service;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.repository.AccountRepository;
import com.apress.prospringmvc.bookstore.repository.RoleRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Iuliana Cosmina
 * @date 14/06/2020
 */
@Primary
@Service
@Transactional(readOnly = true)
public class AuthenticationServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	private final RoleRepository roleRepository;

	public AuthenticationServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository) {
		this.accountRepository = accountRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	@Transactional(readOnly = false)
	public Account save(Account account) {
		account.setRoles(List.of(roleRepository.findByRole("ROLE_USER"))); // default role
		return this.accountRepository.save(account);
	}

	@Override
	public Account getAccount(String username) {
		return this.accountRepository.findByUsername(username);
	}

	// not needed
	@Override
	public Account login(String username, String password) throws AuthenticationException {
		return null;
	}
}
