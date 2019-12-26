/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.apedano.webservices.restfulwebservices.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 *
 * @author Alessandro
 */
@Component
public class UserDaoService {
    
    private static List<User> USERS = new ArrayList<>();
    
    static {
        USERS.add(new User(1, "Ciccio", new Date()));
        USERS.add(new User(2, "Pasticcio", new Date()));
        USERS.add(new User(3, "Pasticcione", new Date()));
    }
    
    public List<User> findAll() {
        return USERS;
    } 
    
    public User save(User user) {
        if(user.getId() == null) {
            user.setId(USERS.size() + 1);
        }
        USERS.add(user);
        return user;
    }
    
    public Optional<User> findOne(int id) {
        return USERS.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }
}
