package train;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

	// Gestion de la concurrence avec Locks
    private final Lock lock = new ReentrantLock(true); // true = équité (FIFO)
    private final Condition notFull = lock.newCondition();
    
    private final int maxCapacity;
    private int currentOccupancy = 0;

	protected Element(String name, int maxCapacity) {
        if(name == null) 
			throw new NullPointerException();

        this.name = name;
        this.maxCapacity = maxCapacity;
    }

	public void setRailway(Railway r) {
		if(r == null)
			throw new NullPointerException();
		
		this.railway = r;
	}

	/**
     * Tente d'entrer sur l'élément.
     * Bloque le thread si la capacité maximale est atteinte.
     */
    public void enter() throws InterruptedException {
        lock.lock(); 
        try {
            while (currentOccupancy >= maxCapacity) {
                notFull.await();
            }
            currentOccupancy++;
        } finally {
            lock.unlock(); 
        }
    }

    /**
     * Quitte l'élément et signale aux autres trains qu'une place est libre.
     */
    public void leave() {
        lock.lock();
        try {
            if (currentOccupancy > 0) {
                currentOccupancy--;
                notFull.signal(); 
            }
        } finally {
            lock.unlock();
        }
    }

	@Override
	public String toString() {
		return this.name;
	}
}
