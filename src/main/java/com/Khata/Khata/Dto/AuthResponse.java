package com.Khata.Khata.Dto;

import com.Khata.Khata.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse
{
    private String token;
    private User user;
}
