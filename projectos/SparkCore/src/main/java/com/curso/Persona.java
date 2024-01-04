package com.curso;

import lombok.*;

import java.util.Optional;


@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Persona {
    @Getter @Setter private String nombre;
    @Getter @Setter private String apellidos;
    @Getter @Setter private long edad;
    @Getter @Setter private String cp;
    @Getter @Setter private String email;
    @Getter private String dni;
    private Integer numeroDni;
    private String letraDni;
    private boolean dniValido;

    public Optional<Integer> getNumeroDni(){
        return Optional.ofNullable(numeroDni);
    }
    public Optional<String> getLetraDni(){
        return Optional.ofNullable(letraDni);
    }

    public void setDni(String dni){
        this.dni = dni;
        validarDni(dni);
    }

    private static final String LETRAS_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";

    private void validarDni(String dni){
        // Debe validar el dni
        // Y rellenar las variables:
        // - numeroDni
        // - letraDni
        // - dniValido
    }

    public Optional<String> normalizarDni(boolean puntosDeMiles, boolean cerosIzquierda, String separadorLetra){
        if(!dniValido){
            return Optional.empty();
        }
        String dniNormalizado = "";
        // Aqui haré la normalización
        return Optional.of(dniNormalizado);
    }
}
