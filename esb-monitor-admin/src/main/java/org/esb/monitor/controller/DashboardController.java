package org.esb.monitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
@Controller
public class DashboardController extends BaseController {

    @RequestMapping("/dashboard.html")
    public String dashboardPage() {
        return "dashboard";
    }
}
