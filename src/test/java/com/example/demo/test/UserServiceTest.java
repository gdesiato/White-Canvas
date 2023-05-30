package com.example.demo.test;

import com.example.demo.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testPrintUserRoles() {
        // use the username of an existing user
        System.out.println("///////////////////////////////////////////////////////");
        System.out.println("///////////////////////////////////////////////////////");
        userService.printUserRoles("UserAdmin");
    }
}
