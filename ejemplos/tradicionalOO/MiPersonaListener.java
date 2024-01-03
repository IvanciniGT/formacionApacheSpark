package ejemplos.tradicionalOO;

public class MiPersonaListener implements PersonaListener {
    @Override
    public void onValueChanged(Persona persona) {
        System.out.println("La persona ha cambiado: " + persona.getNombre());
    }
    
}
