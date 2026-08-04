package com.ham.invoiceapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class HelloController {

    private static final String template = "Hi, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/hello")
    public Hello hello(@RequestParam(defaultValue = "World") String name) {
        return new Hello(counter.incrementAndGet(), template.formatted(name));
    }
}
