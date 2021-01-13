package com.jwang261.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "Hello Swagger";
    }

    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
