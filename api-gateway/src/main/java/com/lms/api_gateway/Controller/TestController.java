package com.lms.api_gateway.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    // @GetMapping("/test")
    // public String test() {
    // return "Gateway Working";
    // }

    @GetMapping("/gateway-test")
    public String test() {
        return "Gateway Working";
    }

}
