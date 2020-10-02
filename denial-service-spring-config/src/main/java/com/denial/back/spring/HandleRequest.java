package com.denial.back.spring;

import com.back.api.IDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/com/denial")
public class HandleRequest {
    @Autowired
    IDomain domain;
    @PostMapping("/process")
    ResponseEntity<String> processPost(@RequestParam("clientId") int id)
    {
        HttpStatus status = (domain.process(id))?HttpStatus.OK:HttpStatus.SERVICE_UNAVAILABLE;
        return new ResponseEntity(status);

    }

}
