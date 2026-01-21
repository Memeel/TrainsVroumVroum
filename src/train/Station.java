package train;

/**
 * Représentation d'une gare. C'est une sous-classe de la classe {@link Element}.
 * Une gare est caractérisée par un nom et un nombre de quais (donc de trains
 * qu'elle est susceptible d'accueillir à un instant donné).
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */

public class Station extends Element {
    public Station(String name, int size) {
        super(name, size); 
        if (size <= 0) throw new NullPointerException();
    }

    @Override
    public boolean invariant(int occupancy) {
        return (occupancy >= 0 && occupancy <= getMaxCapacity())
               && (railway.getNbTrainsLR() == 0
               || railway.getNbTrainsRL() == 0);
    }

    @Override
    public synchronized void enter() throws InterruptedException {
        while (!invariant(currentOccupancy + 1)) {
            wait();
        }
        currentOccupancy++;
    }

    /**
     * Sortie sécurisée (notifie les autres)
     */
    @Override
    public synchronized void leave() {
        if(invariant(currentOccupancy - 1)){
            currentOccupancy--;        
            notifyAll();
        }
    }
}


