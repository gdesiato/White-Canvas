package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.dto.DtoMapper;
import com.desiato.whitecanvas.dto.UserRequestDto;
import com.desiato.whitecanvas.dto.UserResponseDto;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.service.UserService;
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
    private final DtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) throws Exception {
        User createdUser = userService.createUser(userRequestDto.email(), userRequestDto.password());
        UserResponseDto userResponseDto = dtoMapper.toUserResponseDTO(createdUser);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(dtoMapper.toUserResponseDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> userResponseDtoList = userService.getAllUsers().stream()
                .map(dtoMapper::toUserResponseDTO)
                .toList();

        return new ResponseEntity<>(userResponseDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDto userRequestDto) {

        User updatedUser = userService.updateUser(id, userRequestDto);

        UserResponseDto userResponseDto = dtoMapper.toUserResponseDTO(updatedUser);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
}
