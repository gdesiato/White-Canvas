package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.dto.UserDTO;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @Operation(summary = "Get user message", description = "Returns a message from the UserController")
    public ResponseEntity<String> getUserMessage() {
        return ResponseEntity.ok("Hello, this is a message from the UserController!");
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class),
                            examples = @ExampleObject(value = "{ \"id\": 1, \"email\": \"user@example.com\" }"))),
            @ApiResponse(responseCode = "400", description = "Invalid input, password cannot be null or empty"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<Object> createUser(@RequestBody User newUser) throws Exception {
        if (newUser.getPassword() == null || newUser.getPassword().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Password cannot be null or empty");
        }

        if (userService.findByEmail(newUser.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("User already exists");
        }

        User createdUser = userService.createUser(newUser.getEmail(), newUser.getPassword());
        UserDTO createdUserDTO = new UserDTO(createdUser.getId(), createdUser.getEmail());
        return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns a user based on their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserDTO.class),
                            examples = @ExampleObject(value = "{ \"id\": 1, \"email\": \"user@example.com\" }"))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> new ResponseEntity<>(new UserDTO(user.getId(), user.getEmail()), HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/list")
    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
            content = @Content(schema = @Schema(implementation = UserDTO.class)))
    public List<UserDTO> getUsers() {
        return userService.getAllUsers().stream()
                .map(user -> new UserDTO(user.getId(), user.getEmail()))
                .toList();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID", description = "Deletes a user based on their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"id\": 1, " +
                                    "\"email\": \"updateduser@example.com\" " +
                                    "}"))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(new UserDTO(updatedUser.getId(), updatedUser.getEmail()), HttpStatus.OK);
    }
}
