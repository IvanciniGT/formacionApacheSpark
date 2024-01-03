package ejemplos.funcional;

public class ProgramaPersona {
    
    public static void main(String[] args) {
        Persona persona = new Persona("Juan", "Perez", 30);
        persona.subscribirse(personaCambiada-> System.out.println("La persona ha cambiado: " + personaCambiada.getNombre()));
        persona.setNombre("Pedro");
    }
}
