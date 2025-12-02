package com.example.hogwartsartifactsonline.hogwartsuser;

import com.example.hogwartsartifactsonline.hogwartsuser.converter.UserDtoToUserConverter;
import com.example.hogwartsartifactsonline.hogwartsuser.converter.UserToUserDtoConverter;
import com.example.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import com.example.hogwartsartifactsonline.system.Result;
import com.example.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {
    private final UserService userService;
    private final UserToUserDtoConverter userConverter;
    private final UserDtoToUserConverter userDtoConverter;

    @GetMapping("/{userId}")
    public Result getUserById(@PathVariable Integer userId) {
        HogwartsUser user = userService.getById(userId);
        UserDto userDto = userConverter.convert(user);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
    }

    @GetMapping
    public Result getAllUsers() {
        var users = userService.getAllUsers();
        var userDtos = users.stream().map(userConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", userDtos);
    }

    @PostMapping
    public Result createUser(@Valid @RequestBody HogwartsUser user) {
        HogwartsUser savedUser = userService.save(user);
        UserDto savedUserDto = userConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedUserDto);
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDto userDto) {
        HogwartsUser user = userDtoConverter.convert(userDto);
        HogwartsUser updatedUser = userService.update(userId, user);
        UserDto updatedUserDto = userConverter.convert(updatedUser);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedUserDto);
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId) {
        userService.delete(userId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success", null);
    }


}
