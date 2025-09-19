package com.ecom.controller;

import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Log
@Controller
@RequestMapping(value = {"", "/"})
public class ViewController {

	@GetMapping()
	public ModelAndView modelAndView() {
		return new ModelAndView("signup-login/login");
	}

}
 