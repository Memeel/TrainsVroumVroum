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
    public Element nextElementLR;
    public Element nextElementRL;
    
    // État interne pour la synchronisation
    private final int maxCapacity;
    protected int currentOccupancy = 0;

    protected Element(String name, int maxCapacity) {
        if (name == null) throw new NullPointerException();
        this.name = name;
        this.maxCapacity = maxCapacity;
    }

    public Element getNextElement(Direction d) {
        if (d == Direction.LR) {
            return nextElementLR;
        } else {
            return nextElementRL;
        }
    }

    public Station getDestinationStation(Direction d) {
        Element next = this.getNextElement(d);
        if (next == null) return null;
        if (next instanceof Station) return (Station) next;
        return next.getDestinationStation(d);
    }

    public int countRunningRecursive() {
        int count = (this instanceof Section) ? currentOccupancy : 0;
        if (nextElementLR == null) return count;
        return count + nextElementLR.countRunningRecursive();
    }

    public abstract void enter(Direction d, Element from) throws InterruptedException;
    public abstract void leave(Direction d);

    public int getMaxCapacity() {return maxCapacity;}
    public int getCurrentOccupancy() {return currentOccupancy;}

    @Override 
    public String toString() {
        return name + "[" + currentOccupancy + "]";
    }

    public void setRailway(Railway r) {
        if (r == null) throw new NullPointerException();
        this.railway = r;
    }

    /**
     * INVARIANT DE SÛRETÉ
     * Vérifie que l'état de l'élément est toujours valide.
     * Doit être appelé à l'intérieur d'un bloc synchronized.
     *


    public boolean invariant(int occupancy, Direction currentDir) {
        return (occupancy >= 0 && occupancy <= getMaxCapacity()); 
    }

    public synchronized void enter(Direction currentDir) throws InterruptedException {
        while (!invariant(currentOccupancy + 1,currentDir)) {
            wait();
        }
        currentOccupancy++;
    }

    /**
     * Sortie sécurisée (notifie les autres)
     *
    public synchronized void leave() {
        if(invariant(currentOccupancy - 1, null)){
            currentOccupancy--;        
            notifyAll();
        }
    }

    @Override
    public String toString() {
        return this.name + " [" + currentOccupancy + "/" + maxCapacity + "]";
    }

    public void setOccupancy(int occupancy) {
        if(!invariant(occupancy))
            throw new IllegalArgumentException("Invalid occupancy value");
        this.currentOccupancy = occupancy;
    }

    public Railway getRailway() {
        return railway;
    }
    */
}