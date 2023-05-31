package com.example.demo.services;

import com.example.demo.models.CustomUserDetails;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import org.slf4j.Logger;


@Service
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @Lazy
    private CartService cartService;

    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    @Transactional(readOnly = true)
    public User getUserByUserId(Long userId) throws EntityNotFoundException {
        User user = userRepository.getById(userId);
        //call unproxy() to ensure all related entities are loaded—no lazy load exceptions.
        return (User) Hibernate.unproxy(user);
    }

    @Transactional(readOnly = true)
    public User getUser(String username) throws EntityNotFoundException  {
        return userRepository.findByUsername(username);
    }

    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setAddress(user.getAddress());

        // Update the password only if it has been changed
        if (!existingUser.getPassword().equals(user.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(existingUser);
    }

    private void checkPassword(String password) {
        if (password == null) {
            throw new IllegalStateException("You must set a password");
        }
        if (password.length() < 6) {
            throw new IllegalStateException("Password is too short. Must be longer than 6 characters");
        }
    }


    public void printUserRoles(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            Collection<Role> roles = user.getRoles();
            log.info("User: " + username + " Roles: " + roles);
        } else {
            log.info("User " + username + " not found");
        }
    }

    @Transactional
    public void createAdminUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            Role roleAdmin;
            if (roleRepository.existsByName("ADMIN")) {
                roleAdmin = roleRepository.findRoleByName("ADMIN");
            } else {
                roleAdmin = roleRepository.save(new Role("ADMIN"));
            }

            user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(Collections.singletonList(roleAdmin));

            userRepository.save(user);
        }
    }
}

