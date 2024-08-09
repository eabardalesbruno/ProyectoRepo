package com.proriberaapp.ribera.Api.controllers.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {

    @GetMapping("/error")
    public HttpStatus handleNotFound() {
        return HttpStatus.NOT_FOUND;
    }
}
