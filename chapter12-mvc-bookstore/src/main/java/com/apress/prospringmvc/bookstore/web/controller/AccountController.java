package com.apress.prospringmvc.bookstore.web.controller;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.repository.AccountRepository;
import com.apress.prospringmvc.bookstore.repository.OrderRepository;
import com.apress.prospringmvc.bookstore.service.FileStorageService;
import com.apress.prospringmvc.bookstore.util.WebFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author Marten Deinum
 * @author Koen Serneels
 */
@Controller
@RequestMapping("/customer/account")
public class AccountController {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private FileStorageService fileStorageService;

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
		binder.setRequiredFields("username", "emailAddress");
	}

	//@Secured( {"USER", "ADMIN"})
	//@RolesAllowed( {"USER", "ADMIN"})
	//@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public String index(Model model, Principal activeUser) {
		Account account = accountRepository.findByUsername(activeUser.getName());
		model.addAttribute("account", account);
		model.addAttribute("orders", this.orderRepository.findByAccount(account));
		model.addAttribute("fileOrders", getAsWebFiles());
		return "customer/account";
	}

	public List<WebFile> getAsWebFiles() {
		return fileStorageService.loadAll().map(
						path -> new WebFile(path.getFileName().toString(), MvcUriComponentsBuilder.fromMethodName(AccountController.class,
								"serveFileOrder", path.getFileName().toString()).build().toUri().toString()))
				.collect(Collectors.toList());

	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFileOrder(@PathVariable String filename) {

		Resource file = fileStorageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}


	@PostMapping
	@PutMapping
	public String update(@ModelAttribute Account account) {
		this.accountRepository.save(account);
		return "redirect:/customer/account";
	}

}
