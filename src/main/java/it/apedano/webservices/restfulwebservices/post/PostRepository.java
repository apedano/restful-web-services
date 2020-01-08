/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.apedano.webservices.restfulwebservices.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Alessandro
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    
}
