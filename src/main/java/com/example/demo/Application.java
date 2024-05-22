package com.example.demo;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class Application {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;


	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
	}

	@PostConstruct
	public void run() throws Exception {


		// ROLES

		Role roleAdmin;
		// check if it exists
		if (roleRepository.existsByName("ADMIN")) {
			// fetch it
			roleAdmin = roleRepository.findRoleByName("ADMIN");
		} else {
			roleAdmin = roleRepository.save(Role.builder().name("ADMIN").build());
		}

		Role roleUser;
		// check if it exists
		if (roleRepository.existsByName("USER")) {
			// fetch it
			roleUser = roleRepository.findRoleByName("USER");
		} else {
			roleUser = roleRepository.save(Role.builder().name("USER").build());
		}


		// USERS

		User userAdmin = userRepository.findByUsername("UserAdmin");
		// if it is not there we create it
		if (userAdmin == null) {
			userAdmin = userRepository.save(User.builder()
					.username("UserAdmin")
					.password(passwordEncoder.encode("useradmin")) //encoding password
					.roles(Arrays.asList(roleAdmin, roleUser))
					.build());
		}

		User userUser = userRepository.findByUsername("UserUser");
		if (userUser == null) {
			userUser = userRepository.save(User.builder()
					.username("UserUser")
					.password(passwordEncoder.encode("useruser")) //encoding password
					.roles(Collections.singletonList(roleUser))
					.build());
		}
	}
}
