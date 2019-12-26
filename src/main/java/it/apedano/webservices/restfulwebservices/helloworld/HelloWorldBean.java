/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.apedano.webservices.restfulwebservices.helloworld;

/**
 *
 * @author Alessandro
 */
public class HelloWorldBean {
    
    private final String message;

    public HelloWorldBean(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "HelloWorldBean{" + "message=" + message + '}';
    }
    
    
    
    
    
}
