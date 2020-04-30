package com.example.Oauthdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * Created by Suriyanarayanan K
 * on 25/03/20 9:57 PM.
 */
@RestController
public class OauthController {

// Employee newEmployee = new Employee("superadmin","U2VjcmV0QDEyMw==");
    //http://13.126.204.39:8180/#!/login
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String getProductList(@RequestBody Employee user) {
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Employee> entity = new HttpEntity<Employee>(user,headers);
        return restTemplate.exchange("http://13.126.204.39:9000/auth/login", HttpMethod.POST, entity, String.class).getBody();
    }
}
