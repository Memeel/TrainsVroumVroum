package train;

/**
 * Représentation d'une section de voie ferrée. C'est une sous-classe de la
 * classe {@link Element}.
 *
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public class Section extends Element {
    public Section(String name) {
        super(name, 1); // Capacité de 1 train max
    }

    @Override
    public synchronized void enter(Direction d, Element from) throws InterruptedException {
        if (from instanceof Station) {
            Station dest = getDestinationStation(d);
            Direction opposite = (d == Direction.LR) ? Direction.RL : Direction.LR;
            
            synchronized(railway) {
                railway.addWaitingTrain(d);
                try {
                    while (railway.getNbRunningTrains(opposite) > 0 || 
                       dest.getMaxCapacity() < (railway.countTrainsInSections() + railway.getNbWaitingTrains(d))) {
                    railway.wait();
                    }
                } finally {
                    railway.removeWaitingTrain(d);
                }
            }
        }

        synchronized(this) {
            while (currentOccupancy >= getMaxCapacity()) {
                wait();
            }
            currentOccupancy++;
        }

        synchronized(railway) {
            railway.addRunningTrain(d);
            railway.notifyAll();
        }
    }

    @Override
    public synchronized void leave(Direction d) {
        synchronized(this) {
            currentOccupancy--;
            notifyAll();
        }
        synchronized(railway) {
            railway.removeRunningTrain(d);
            railway.notifyAll();
        }
    }
}