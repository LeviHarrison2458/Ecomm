package com.tts.Ecommerce.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tts.Ecommerce.model.Product;
import com.tts.Ecommerce.service.ProductService;

@Controller
public class ProductController {
	@Autowired
	ProductService productService;
	
	@GetMapping("/product/{id}")
	public String show(@PathVariable Long id, Model model) {
		Product product = productService.findById(id);
		model.addAttribute(product);
		return "product";
	}
	
	//Either implement admin controls or remove these methods
	
	
	@RequestMapping(value = "/product", method = {RequestMethod.POST, RequestMethod.PUT})
	public String createOrUpdate(@Valid Product product) {
		productService.save(product);
		return "redirect:/product/"+product.getId();
	}
}
