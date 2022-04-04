/*
 * Copyright 2016-2022 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

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
