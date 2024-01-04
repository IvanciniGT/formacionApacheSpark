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
        dni = dni.toUpperCase();
        this.dniValido = dni.matches("^(([0-9]{1,8})|((([0-9]{1,2}[.][0-9]{3})|([0-9]{1,3}))[.][0-9]{3}))[ -]?[A-Z]$");
        if(dniValido) {
            dni = dni.replaceAll("[. -]","");
            this.numeroDni = Integer.parseInt(dni.substring(0, dni.length() - 1));
            this.letraDni = dni.substring(dni.length() - 1);
            this.dniValido = LETRAS_DNI.charAt(numeroDni % 23) == letraDni.charAt(0);
        }
    }

    public Optional<String> normalizarDni(boolean puntosDeMiles, boolean cerosIzquierda, String separadorLetra){
        if(!dniValido){
            return Optional.empty();
        }
        String dniNormalizado = (separadorLetra == null ? "" : separadorLetra) + letraDni;
        String numeroDNINormalizado = ""+numeroDni;
        if(cerosIzquierda){
            numeroDNINormalizado = ("00000000" + numeroDNINormalizado).substring(numeroDNINormalizado.length());
        }
        if(puntosDeMiles){
            int posicion = numeroDNINormalizado.length() - 3;
            while(posicion > 0){
                numeroDNINormalizado = numeroDNINormalizado.substring(0, posicion) + "." + numeroDNINormalizado.substring(posicion);
                posicion -= 3;
            }
        }
        // Aqui haré la normalización
        return Optional.of(numeroDNINormalizado + dniNormalizado);
    }
}
