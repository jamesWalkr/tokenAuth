package com.example.security_demo_v2.Repository;

import com.example.security_demo_v2.Models.Post;
import org.springframework.data.repository.CrudRepository;


public interface PostRepository extends CrudRepository<Post, Long> {

}
