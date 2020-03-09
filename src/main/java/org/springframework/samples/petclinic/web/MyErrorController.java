package org.springframework.samples.petclinic.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class MyErrorController {

    @GetMapping("")
    public String handleError() {
	return "exception";
}
}