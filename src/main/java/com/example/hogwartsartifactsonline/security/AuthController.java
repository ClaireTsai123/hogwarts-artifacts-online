package com.example.hogwartsartifactsonline.security;

import com.example.hogwartsartifactsonline.system.Result;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.base-url}/users")
public class AuthController {

    private final AuthService authService;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public Result getLoginInfo(Authentication authentication) {
        logger.debug("Authentication name: {}", authentication.getName());
        return new Result(true, 200, "User Info and JSON Web Token", authService.createLoginInfo(authentication));
    }
}
