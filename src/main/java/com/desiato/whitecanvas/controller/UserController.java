package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.mapper.UserMapper;
import com.desiato.whitecanvas.dto.UserRequestDto;
import com.desiato.whitecanvas.dto.UserResponseDTO;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.service.UserService;
import com.desiato.whitecanvas.validator.UserRequestValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserRequestValidator validator;
    private final UserMapper dtoMapper;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDto userRequestDto) {
        validator.validate(userRequestDto);
        User createdUser = userService.createUser(userRequestDto.email(), userRequestDto.password());
        UserResponseDTO userResponseDto = dtoMapper.toUserResponseDTO(createdUser);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(dtoMapper.toUserResponseDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> userResponseDTOList = userService.getAllUsers().stream()
                .map(dtoMapper::toUserResponseDTO)
                .toList();

        return new ResponseEntity<>(userResponseDTOList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDto userRequestDto) {

        validator.validate(userRequestDto);

        User updatedUser = userService.updateUser(id, userRequestDto);

        UserResponseDTO userResponseDto = dtoMapper.toUserResponseDTO(updatedUser);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
}
