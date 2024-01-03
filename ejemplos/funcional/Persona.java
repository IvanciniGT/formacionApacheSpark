package ejemplos.funcional;

import java.util.*; 
import java.util.function.Consumer;
public class Persona {

    String nombre;
    String apellidos;
    int edad;
    private List<Consumer<Persona>> listeners = new ArrayList<>();

    public Persona(String nombre, String apellidos, int edad) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        notificarCambio();
    }
    public String getNombre() {
        return this.nombre;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
        notificarCambio();    
    }
    public String getApellidos() {
        return this.apellidos;
    }
    public void setEdad(int edad) {
        this.edad = edad;
        notificarCambio();
    }
    public int getEdad() {
        return this.edad;
    }
    public void subscribirse(Consumer<Persona> consumer) {
        System.out.println("Me he suscrito a la persona");
        listeners.add(consumer);
    }

    private void notificarCambio() {
        for (Consumer<Persona> listener : listeners) {
            listener.accept(this);
        }
    }
}