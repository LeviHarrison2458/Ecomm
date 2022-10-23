package com.tts.Ecommerce.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tts.Ecommerce.model.ChargeRequest;
import com.tts.Ecommerce.model.Product;
import com.tts.Ecommerce.model.User;
import com.tts.Ecommerce.service.ProductService;
import com.tts.Ecommerce.service.UserService;


@Controller
public class CartController {
	@Autowired
	ProductService productService;

	@Autowired
	UserService userService;

	@ModelAttribute("loggedInUser")
	public User loggedInUser() {
		return userService.getLoggedInUser();
	}

	@ModelAttribute("cart")
	public Map<Product, Integer> cart() {
		User user = loggedInUser();
		if (user == null)
			return null;
		System.out.println("Getting Cart");
		return user.getCart();
	}

	@ModelAttribute("list")
	public List<Float> list() {
		return new ArrayList<>();
	}

	@GetMapping("/cart")
	public String showCart() {
		return "cart";
	}

	@PostMapping("/cart")
	public String addToCart(@RequestParam Long id) {
		Product p = productService.findById(id);
		setQuantity(p, cart().getOrDefault(p, 0) + 1);
		return "cart";
	}

	@PatchMapping("/cart")
	public String updateQuantities(@RequestParam Long[] id, @RequestParam int[] quantity) {
		for (int i = 0; i < id.length; i++) {
			Product p = productService.findById(id[i]);
			setQuantity(p, quantity[i]);
		}
		return "cart";
	}

	@DeleteMapping("/cart")
	public String removeFromCart(@RequestParam Long id) {
		Product p = productService.findById(id);
		setQuantity(p, 0);
		return "cart";
	}

	private void setQuantity(Product p, int quantity) {
		if (quantity > 0) {
			cart().put(p, quantity);
		} else {
			cart().remove(p);
		}
		userService.updateCart(cart());

	}

//CHECKOUT SECTION
	
	public int calculateTotal(Map<Product, Integer> currentCart) {
		float runningTotal = 0;
		for(Map.Entry<Product, Integer> entry: currentCart.entrySet()) {
			float itemPrice = entry.getKey().getPrice();
			Integer quantity = entry.getValue();
			runningTotal += itemPrice*quantity;
		}
		
		return (int) runningTotal * 100;
		
	}
	
	@Value("${STRIPE_PUBLIC_KEY}")
	private String stripePublicKey;

	@RequestMapping("/checkout")
	public String checkout(Model model) {
		User user = userService.getLoggedInUser();
		int amount = calculateTotal(user.getCart());
		
		model.addAttribute("amount", amount); // in cents
		model.addAttribute("stripePublicKey", stripePublicKey);
		model.addAttribute("currency", ChargeRequest.Currency.USD);
		return "checkout";

	}

}