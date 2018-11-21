package org.esb.monitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardController extends BaseController{

	@RequestMapping("/dashboard.html")
	public String dashboardPage(){
		return "dashboard";
	}
}
