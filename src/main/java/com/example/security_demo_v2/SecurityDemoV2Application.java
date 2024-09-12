package com.example.security_demo_v2;

import com.example.security_demo_v2.Models.Post;
import com.example.security_demo_v2.Models.User;
import com.example.security_demo_v2.Repository.PostRepository;
import com.example.security_demo_v2.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SecurityDemoV2Application {

	public static void main(String[] args) {
		SpringApplication.run(SecurityDemoV2Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(PostRepository posts, UserRepository users, PasswordEncoder encoder){

		return  args -> {
			users.save(new User("user", encoder.encode("password"), "ROLE_USER"));
			users.save(new User("admin", encoder.encode("password"), "ROLE_USER,ROLE_ADMIN"));
			posts.save(new Post("Hey There", "hey-there", "This is a blog post", "Obi-wan Kenobe"));
		};

	}

}
