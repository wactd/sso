package com.dongly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@Controller
public class ClientBApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientBApplication.class, args);
	}


	@GetMapping(value = "/")
	public String index(HttpServletRequest request) {

		return "index";
	}
}
