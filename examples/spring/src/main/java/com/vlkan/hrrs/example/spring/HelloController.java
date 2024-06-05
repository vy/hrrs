/*
 * Copyright 2016-2024 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

package com.vlkan.hrrs.example.spring;

import com.vlkan.hrrs.servlet.HrrsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/hello", produces = {MediaType.TEXT_PLAIN_VALUE})
public class HelloController {

    @Autowired
    private HrrsFilter hrrsFilter;

    @GetMapping("/recording")
    public ResponseEntity<String> isRecordingEnabled() {
        boolean enabled = hrrsFilter.isEnabled();
        String responseBody = String.valueOf(enabled);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/recording")
    public ResponseEntity<Void> enableRecording(@RequestParam("enabled") boolean enabled) {
        hrrsFilter.setEnabled(enabled);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/say")
    public ResponseEntity<String> sayHello(@RequestParam("name") String name, @RequestBody byte[] requestBody) {
        String responseBody = String.format("Hello, %s! (%d)%n", name, requestBody.length);
        return ResponseEntity.ok(responseBody);
    }

}
