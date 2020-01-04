/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.apedano.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author Alessandro
 */
@RestController
public class UserController {

    @Autowired
    private EntityLinks entityLinks;

    @Autowired
    private UserDaoService userDaoService;

    @GetMapping(path = "/users")
    public List<User> retrieveAllUsers() {
        return userDaoService.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) throws NoSuchMethodException {
        Optional<User> userOptional = userDaoService.findOne(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id:" + id);
        }
        EntityModel<User> entityModel = new EntityModel<>(userOptional.get());
        //Methods of the WebMvcLinkBuilder class the linkTo takes a method as input to be presented in the
        //output with the name in the withRel method
        Link link = linkTo(methodOn(UserController.class).retrieveAllUsers()).withRel("all-users");
        entityModel.add(link);
        Link selfLink = linkTo(methodOn(UserController.class).retrieveUser(id)).withSelfRel();
        entityModel.add(selfLink);
        /*
        {
    "id": 1,
    "name": "Ciccio",
    "birthDate": "2020-01-04T12:28:38.050+0000",
    "_links": {
        "all-users": {
            "href": "http://localhost:8080/users"
        },
        "self": {
            "href": "http://localhost:8080/users/1"
        }
    }
}       
         */

        return entityModel;

    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        Optional<User> userOptional = userDaoService.deleteById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id:" + id);
        }
    }

    /*
    @RequestBody will be part of the request body annotation
    Request:

    Content-Type: application/json
    {
        "name": "Alessandro",
        "birthDate": "1980-07-27T11:28:39.363+0000"
    }
     */
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userDaoService.save(user);
        URI savedUserUri = ServletUriComponentsBuilder.fromCurrentRequest() // /users
                .path("/{id}") //append the id part
                .buildAndExpand(savedUser.getId())
                .toUri();
        //the created response code is 201 that is the correct one for a newly created entity
        //in the response headers we will find also
        // Location: http://localhost:8080/users/4
        return ResponseEntity.created(savedUserUri).build();
    }

}
