package com.example.demo.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotEmpty(message = "username maydoni bo'sh qolmasin")
    private String username;

    @NotEmpty(message = "paeol maydoni bo'sh qolmasin")
    @Size(min = 4, message = "Parol uzunligi 4 dan katta bo'lsin")
    private String password;
}
