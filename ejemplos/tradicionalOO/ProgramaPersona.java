package ejemplos.tradicionalOO;

public class ProgramaPersona {
    
    public static void main(String[] args) {
        Persona persona = new Persona("Juan", "Perez", 30);
        persona.subscribirse(new MiPersonaListener());
        persona.setNombre("Pedro");
    }
}
