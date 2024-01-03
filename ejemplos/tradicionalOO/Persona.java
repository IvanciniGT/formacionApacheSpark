package ejemplos.tradicionalOO;

import java.util.*; 
public class Persona {

    String nombre;
    String apellidos;
    int edad;
    private List<PersonaListener> listeners = new ArrayList<PersonaListener>();

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
    public void subscribirse(PersonaListener listener) {
        System.out.println("Me he suscrito a la persona");
        listeners.add(listener);
    }

    private void notificarCambio() {
        for (PersonaListener listener : listeners) {
            listener.onValueChanged(this);
        }
    }
}