package com.denys.library.some.DTO;

import com.denys.library.some.models.User;
import lombok.Getter;

@Getter
public class ShortUserDTO {
    private final Long id;
    private final String firstName;
    private final String lastName;

    public ShortUserDTO(User user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
    }
}
