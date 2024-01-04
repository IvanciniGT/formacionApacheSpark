package com.curso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Persona {
    private String nombre;
    private String apellidos;
    private long edad;
    private String cp;
    private String email;
    private String dni;
}
