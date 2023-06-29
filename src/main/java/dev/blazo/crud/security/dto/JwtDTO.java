package dev.blazo.crud.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtDTO {

    private String token;

    public JwtDTO(String token) {
        this.token = token;
    }
}
