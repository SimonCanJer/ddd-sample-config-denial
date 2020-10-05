package com.denial.back.spring;

import com.back.api.IDataHolder;
import com.back.api.IDomain;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/denial")
public class HandleRequest {
    @Autowired
    IDomain domain;
    @PostMapping("/process")
    ResponseEntity<String> processPost(@RequestParam("clientId") String id,@RequestParam("command") String command)
    {

        try {
            IDataHolder.VarResult res = IDomain.LAZY.get().process(id,command);
            int status = res.isResult() ? 200 : 503;
            Gson gson = new Gson();
            String jsonRes = gson.toJson(res);
            return ResponseEntity.status(status).body(jsonRes);
        }
        catch(Throwable t)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal exception");
        }

    }

}
