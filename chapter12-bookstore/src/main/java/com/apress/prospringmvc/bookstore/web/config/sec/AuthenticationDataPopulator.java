package com.apress.prospringmvc.bookstore.web.config.sec;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.domain.Address;
import com.apress.prospringmvc.bookstore.domain.Role;
import com.apress.prospringmvc.bookstore.repository.AccountRepository;
import com.apress.prospringmvc.bookstore.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * This class is needed to create accounts for the users in the AuthenticationManager in-memory database.
 *
 * @author Iuliana Cosmina
 * @date 07/06/2020
 */
@Service
public class AuthenticationDataPopulator {
	private Logger logger = LoggerFactory.getLogger(AuthenticationDataPopulator.class);

	private AccountRepository accountRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public AuthenticationDataPopulator(AccountRepository accountRepository, RoleRepository roleRepository) {
		this.accountRepository = accountRepository;
		this.roleRepository = roleRepository;
	}

	@PostConstruct
	private void init() {
		logger.info(" -->> Starting authentication data initialization...");
		roleRepository.saveAll(List.of(new Role("ROLE_USER"), new Role("ROLE_ADMIN")));

		Address address = new Address();
		address.setStreet("Liberty Street");
		address.setCity("of angels");
		address.setCountry("Europe");

		Account john = new Account();
		john.setFirstName("john");
		john.setUsername("john");
		john.setLastName("doe");
		john.setEmailAddress("john@doe.com");
		john.setPassword(passwordEncoder.encode("doe"));
		john.setAddress(address);
		john.setRoles(List.of(roleRepository.findByRole("ROLE_USER")));
		accountRepository.save(john);

		Account jane = new Account();
		jane.setFirstName("jane");
		jane.setLastName("doe");
		jane.setUsername("jane");
		jane.setEmailAddress("jane@doe.com");
		jane.setPassword(passwordEncoder.encode("doe"));
		jane.setAddress(address);
		jane.setRoles(List.of(roleRepository.findByRole("ROLE_USER"), roleRepository.findByRole("ROLE_ADMIN")));
		accountRepository.save(jane);

		Account admin = new Account();
		admin.setFirstName("admin");
		admin.setLastName("admin");
		admin.setUsername("admin");
		admin.setEmailAddress("admin@admin.com");
		admin.setPassword(passwordEncoder.encode("admin"));
		admin.setAddress(address);
		admin.setRoles(List.of(roleRepository.findByRole("ROLE_ADMIN")));
		accountRepository.save(admin);
		logger.info(" -->> Authentication data initialization completed.");
	}

}
