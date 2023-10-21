package com.example.mainservice.controller;

import com.example.mainservice.model.NewUserRequest;
import com.example.mainservice.model.UserDto;
import com.example.mainservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    Set<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                          @RequestParam(defaultValue = "0") int from,
                          @RequestParam(defaultValue = "10") int size) {
        return userService.getUser(ids, from / size, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto addUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        return userService.addUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
