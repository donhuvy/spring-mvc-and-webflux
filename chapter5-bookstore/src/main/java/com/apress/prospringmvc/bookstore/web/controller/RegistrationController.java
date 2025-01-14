package com.apress.prospringmvc.bookstore.web.controller;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.service.AccountService;
import com.apress.prospringmvc.bookstore.validation.AccountValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Controller to handle the registration of a new {@code Account}
 *
 * @author Marten Deinum
 */
@Controller
@RequestMapping("/customer/register")
public class RegistrationController {

	private final AccountService accountService;

	public RegistrationController(AccountService accountService) {
		this.accountService = accountService;
	}

	@ModelAttribute("countries")
	public Map<String, String> countries(Locale currentLocale) {
		var countries = new TreeMap<String, String>();
		for (var locale : Locale.getAvailableLocales()) {
			countries.put(locale.getCountry(), locale.getDisplayCountry(currentLocale));
		}
		return countries;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id");
		binder.setValidator(new AccountValidator());
	}

	@GetMapping
	@ModelAttribute
	public Account register(Locale currentLocale) {
		var account = new Account();
		account.getAddress().setCountry(currentLocale.getCountry());
		return account;
	}

	@PostMapping
	@PutMapping
	public String handleRegistration(@Valid @ModelAttribute Account account, BindingResult result) {
		if (result.hasErrors()) {
			return "customer/register";
		}
		this.accountService.save(account);
		return "redirect:/customer/account/" + account.getId();
	}
}
