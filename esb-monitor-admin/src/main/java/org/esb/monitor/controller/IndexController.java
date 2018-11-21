package org.esb.monitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class IndexController extends BaseController{

	@RequestMapping("/index")
	public String indexPage(){
		System.out.println(111);
		return "index";
	}
}
