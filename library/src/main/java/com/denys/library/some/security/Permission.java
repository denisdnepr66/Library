package com.denys.library.some.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permission {
    USER_READ("users:read"),
    USER_WRITE("users:write"),
    BOOK_READ("books:read"),
    BOOK_WRITE("books:write");


    private final String permission;

}
