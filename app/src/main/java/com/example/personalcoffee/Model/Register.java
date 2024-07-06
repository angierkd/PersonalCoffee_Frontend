package com.example.personalcoffee.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Register {
    String nickname;
    String id;
    String password;
    String password2;
}
