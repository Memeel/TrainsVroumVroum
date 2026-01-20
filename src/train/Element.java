package train;

/**
 * Cette classe abstraite est la représentation générique d'un élément de base d'un
 * circuit, elle factorise les fonctionnalitÃ©s communes des deux sous-classes :
 * l'entrée d'un train, sa sortie et l'appartenance au circuit.<br/>
 * Les deux sous-classes sont :
 * <ol>
 *   <li>La représentation d'une gare : classe {@link Station}</li>
 *   <li>La représentation d'une section de voie ferrée : classe {@link Section}</li>
 * </ol>
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */

public abstract class Element {
    private final String name;
    protected Railway railway;
    
    // État interne pour la synchronisation
    private final int maxCapacity;
    private int currentOccupancy = 0;

    protected Element(String name, int maxCapacity) {
        if(name == null) throw new NullPointerException();
        this.name = name;
        this.maxCapacity = maxCapacity;
    }

    public void setRailway(Railway r) {
        if(r == null) throw new NullPointerException();
        this.railway = r;
    }

    /**
     * INVARIANT DE SÛRETÉ
     * Vérifie que l'état de l'élément est toujours valide.
     * Doit être appelé à l'intérieur d'un bloc synchronized.
     */
    private void checkInvariant() {
        if (currentOccupancy < 0) {
            throw new IllegalStateException("INVARIANT VIOLÉ : Nombre de trains négatif sur " + name);
        }
        if (currentOccupancy > maxCapacity) {
            throw new IllegalStateException("INVARIANT VIOLÉ : Capacité dépassée sur " + name + 
                                            " (" + currentOccupancy + "/" + maxCapacity + ")");
        }
    }

    public synchronized void enter() throws InterruptedException {
        while (currentOccupancy >= maxCapacity) {
            wait();
        }
        
        currentOccupancy++;
        checkInvariant();
    }

    /**
     * Sortie sécurisée (notifie les autres)
     */
    public synchronized void leave() {
        currentOccupancy--;
        
        checkInvariant();        
        notifyAll();
    }

    @Override
    public String toString() {
        return this.name + " [" + currentOccupancy + "/" + maxCapacity + "]";
    }
}