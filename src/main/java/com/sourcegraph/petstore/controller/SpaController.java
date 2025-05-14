package com.sourcegraph.petstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    // Forward all routes not matching API paths to the React app
    @RequestMapping(value = "/{path:[^\\.]*}")
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}
