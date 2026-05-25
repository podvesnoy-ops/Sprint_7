package ru.yandex.praktikum.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Courier {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String color;
}