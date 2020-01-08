/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.apedano.webservices.restfulwebservices.user;

import it.apedano.webservices.restfulwebservices.post.Post;
import it.apedano.webservices.restfulwebservices.post.PostRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
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
public class UserJpaController {


    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PostRepository postRepository;


    @GetMapping(path = "/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/jpa/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) throws NoSuchMethodException {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id:" + id);
        }
        EntityModel<User> entityModel = new EntityModel<>(userOptional.get());
        //Methods of the WebMvcLinkBuilder class the linkTo takes a method as input to be presented in the
        //output with the name in the withRel method
        Link link = linkTo(methodOn(UserJpaController.class).retrieveAllUsers()).withRel("all-users");
        entityModel.add(link);
        Link selfLink = linkTo(methodOn(UserJpaController.class).retrieveUser(id)).withSelfRel();
        entityModel.add(selfLink);
        return entityModel;
    }
    
    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrievePosts(@PathVariable int id) throws NoSuchMethodException {
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()) {
            throw new UserNotFoundException("id: " + id);
        }
        return userOptional.get().getPosts();
    }

    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id) {
       userRepository.deleteById(id);
    }

 
    @PostMapping("/jpa/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);
        URI savedUserUri = ServletUriComponentsBuilder.fromCurrentRequest() // /users
                .path("/{id}") //append the id part
                .buildAndExpand(savedUser.getId())
                .toUri();
        //the created response code is 201 that is the correct one for a newly created entity
        //in the response headers we will find also
        // Location: http://localhost:8080/users/4
        return ResponseEntity.created(savedUserUri).build();
    }
    
    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable int id, @RequestBody Post post) {
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()) {
            throw new UserNotFoundException("id: " + id);
        }
        User user = userOptional.get();
        post.setUser(user);
        postRepository.save(post);
        URI savedUserUri = ServletUriComponentsBuilder.fromCurrentRequest() // /users
                .path("/{id}") //append the id part
                .buildAndExpand(post.getId())
                .toUri();
        //the created response code is 201 that is the correct one for a newly created entity
        //in the response headers we will find also
        // Location: http://localhost:8080/users/4
        return ResponseEntity.created(savedUserUri).build();
    }

}
