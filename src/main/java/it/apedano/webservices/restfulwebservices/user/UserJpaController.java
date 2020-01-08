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

    /*
    Content negotiation based on the Accpet request header
    By simply adding the jackson xml serializer to the pom we are able to give xml as response

    curl --location --request GET 'http://localhost:8080/users' \
--header 'Accept: application/json' \

    [
    {
        "id": 1,
        "name": "Ciccio",
        "birthDate": "2020-01-06T08:41:14.019+0000"
    },
    {
        "id": 2,
        "name": "Pasticcio",
        "birthDate": "2020-01-06T08:41:14.019+0000"
    },
    {
        "id": 3,
        "name": "Pasticcione",
        "birthDate": "2020-01-06T08:41:14.019+0000"
    }
]

    curl --location --request GET 'http://localhost:8080/users' \
--header 'Accept: application/xml' \
    <List>
    <item>
        <id>1</id>
        <name>Ciccio</name>
        <birthDate>2020-01-06T08:41:31.534+0000</birthDate>
    </item>
    <item>
        <id>2</id>
        <name>Pasticcio</name>
        <birthDate>2020-01-06T08:41:31.534+0000</birthDate>
    </item>
    <item>
        <id>3</id>
        <name>Pasticcione</name>
        <birthDate>2020-01-06T08:41:31.534+0000</birthDate>
    </item>
</List>

    curl --location --request GET 'http://localhost:8080/users' \
--header 'Accept: application/ecmascript' \

    Response 406 Not Acceptable

     */
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

    Content negotiation with XML posted data
    curl --location --request POST 'http://localhost:8080/users' \
--header 'Accept: application/xml' \
--header 'Content-Type: application/xml' \
--data-raw '<item>
        <name>New User</name>
        <birthDate>2020-01-06T08:45:35.732+0000</birthDate>
    </item>'


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
