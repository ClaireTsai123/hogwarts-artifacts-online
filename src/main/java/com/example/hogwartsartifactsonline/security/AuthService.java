package com.example.hogwartsartifactsonline.security;

import com.example.hogwartsartifactsonline.hogwartsuser.HogwartsUser;
import com.example.hogwartsartifactsonline.hogwartsuser.MyUserPrincipal;
import com.example.hogwartsartifactsonline.hogwartsuser.converter.UserDtoToUserConverter;
import com.example.hogwartsartifactsonline.hogwartsuser.converter.UserToUserDtoConverter;
import com.example.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserToUserDtoConverter userConverter;

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        // create user info
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        HogwartsUser user = principal.getUser();
        UserDto userDto = userConverter.convert(user);
        //create JWT token
        String token = jwtProvider.createToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("userInfo", userDto);
        loginResultMap.put("token", token);
        return loginResultMap;
    }
}
