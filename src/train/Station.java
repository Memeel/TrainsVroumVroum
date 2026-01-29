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
    public synchronized void enter(Direction d, Element from) throws InterruptedException {
        while (currentOccupancy >= getMaxCapacity()) {
            wait();
        }
        currentOccupancy++;
        synchronized(railway) { railway.notifyAll(); }
    }

    @Override
    public synchronized void leave(Direction d) {
        currentOccupancy--;
        notifyAll();
        synchronized(railway) { railway.notifyAll(); }
    }
}


