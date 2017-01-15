package com.vlkan.hrrs.example.spring;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.core.MediaType;

@Controller
@RequestMapping(path = "/hello")
public class HelloController {

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = {MediaType.TEXT_PLAIN},
            produces = {MediaType.TEXT_PLAIN})
    public ResponseEntity<String> sayHello(@RequestParam String name, @RequestBody byte[] requestBody) {
        String responseBody = String.format("Hello, %s! (%d)%n", name, requestBody.length);
        return ResponseEntity.ok(responseBody);
    }

}
