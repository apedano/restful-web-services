/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.apedano.webservices.restfulwebservices;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Alessandro
 */
@RestController
public class HelloWorldController {
    
    //@RequestMapping(method = RequestMethod.GET, path = "/hello-world")
    /*
    Alternative annotation
    */
    @GetMapping(path = "/hello-world")
    public String helloWorld() {
        return "Hello world";
    }
    
    
}
