package com.apress.prospringmvc.bookstore.web.controller;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.service.AccountService;
import com.apress.prospringmvc.bookstore.validation.AccountValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	@Autowired
	private AccountService accountService;

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

	@RequestMapping(method = RequestMethod.GET)
	@ModelAttribute
	public Account register(Locale currentLocale) {
		var account = new Account();
		account.getAddress().setCountry(currentLocale.getCountry());
		return account;
	}

	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
	public String handleRegistration(@Valid @ModelAttribute Account account, BindingResult result) {
		if (result.hasErrors()) {
			return "customer/register";
		}
		this.accountService.save(account);
		return "redirect:/login";
	}
}
