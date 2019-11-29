package com.handicapper.handicapper.proto1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
	@RequestMapping("/")
	public String index() {
		return "Finally Spring boooted";
	}
}
