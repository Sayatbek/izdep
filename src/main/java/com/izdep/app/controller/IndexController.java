package com.izdep.app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController {

    @Value("${kaz.search}")
    String search;

    @Value("${kaz.images}")
    String images;

    @Value("${kaz.request}")
    String request;
    @Value("${kaz.sitename}")
    String sitename;

	@RequestMapping(value = "/")
	public String home(ModelMap modelMap) {
        modelMap.addAttribute("sitename", sitename);
        modelMap.addAttribute("search", search);
        modelMap.addAttribute("images", images);
        modelMap.addAttribute("request", request);
		return "search";
	}

	@RequestMapping(value = "/imageIndex")
	public String imageIndex(ModelMap modelMap){
        modelMap.addAttribute("sitename", sitename);
        modelMap.addAttribute("search", search);
        modelMap.addAttribute("images", images);
        modelMap.addAttribute("request", request);
        return "imageIndex";
	}
}