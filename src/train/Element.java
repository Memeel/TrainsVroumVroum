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
    protected int currentOccupancy = 0;

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


    public boolean invariant(int occupancy) {
        return occupancy >= 0 && occupancy <= maxCapacity;
    }

    public synchronized void enter() throws InterruptedException {
        while (!invariant(currentOccupancy + 1)) {
            wait();
        }
        currentOccupancy++;
    }

    /**
     * Sortie sécurisée (notifie les autres)
     */
    public synchronized void leave() {
        if(invariant(currentOccupancy - 1)){
            currentOccupancy--;        
            notifyAll();
        }
    }

    @Override
    public String toString() {
        return this.name + " [" + currentOccupancy + "/" + maxCapacity + "]";
    }

    public int getOccupancy() {
        return currentOccupancy;
    }

    public void setOccupancy(int occupancy) {
        if(!invariant(occupancy))
            throw new IllegalArgumentException("Invalid occupancy value");
        this.currentOccupancy = occupancy;
    }

    public Railway getRailway() {
        return railway;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}