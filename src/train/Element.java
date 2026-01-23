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
        if (name == null) throw new NullPointerException();
        this.name = name;
        this.maxCapacity = maxCapacity;
    }

    public void setRailway(Railway r) {
        if (r == null) throw new NullPointerException();
        this.railway = r;
    }

    /**
     * INVARIANT DE SÛRETÉ
     * Vérifie que l'état de l'élément est toujours valide.
     * Doit être appelé à l'intérieur d'un bloc synchronized.
     */
    public boolean invariant(int occupancy) {
        boolean check = (occupancy >= 0 && occupancy <= maxCapacity);
        
        if (this instanceof Station) {
            return check;
        }

        return check && (railway.getNbTrainsLR() == 0 || railway.getNbTrainsRL() == 0);
    }

    public void enter() throws InterruptedException {
        synchronized (railway) {
            while (!invariant(currentOccupancy + 1)) {
                railway.wait();
            }
            currentOccupancy++;
            railway.notifyAll();
        }
    }

    /**
     * Sortie sécurisée (notifie les autres)
     */
    public void leave() {
        synchronized (railway) {
            if (invariant(currentOccupancy - 1)){
                currentOccupancy--;        
                railway.notifyAll();
            }
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
}