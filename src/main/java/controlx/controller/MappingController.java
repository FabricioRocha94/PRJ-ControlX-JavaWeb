package controlx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MappingController {
	@GetMapping("/menu")
	public String menu() {
		return "menu";
	}
	
	@GetMapping("/index")
	public String index() {
		return "index";
	}
	
	@GetMapping("/")
	public String principal() {
		return "index";
	}
	
	@GetMapping("/vender")
	private String vender() {
		return "vender";

	}
}
