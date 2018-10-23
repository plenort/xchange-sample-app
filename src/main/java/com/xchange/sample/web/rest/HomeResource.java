package com.xchange.sample.web.rest;

import com.xchange.sample.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class HomeResource {

    @Autowired
    private AlertService alertService;


    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("alertsNumber", alertService.listAlerts().size());
        return "index";
    }
}
