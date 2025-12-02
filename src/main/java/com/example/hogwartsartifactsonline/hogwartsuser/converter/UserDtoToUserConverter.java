package com.example.hogwartsartifactsonline.hogwartsuser.converter;

import com.example.hogwartsartifactsonline.hogwartsuser.HogwartsUser;
import com.example.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, HogwartsUser> {
    @Override
    public HogwartsUser convert(UserDto source) {
        HogwartsUser user = new HogwartsUser();
        user.setUsername(source.username());
        user.setEnabled(source.enabled());
        user.setRoles(source.roles());
        return user;

    }
}
