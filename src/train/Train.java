package train;

import java.util.Random;

/**
 * Représentation d'un train. Un train est caractérisé par deux valeurs :
 * <ol>
 *   <li>
 *     Son nom pour l'affichage.
 *   </li>
 *   <li>
 *     La position qu'il occupe dans le circuit (un élément avec une direction) : classe {@link Position}.
 *   </li>
 * </ol>
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Mayte segarra <mt.segarra@imt-atlantique.fr>
 * Test if the first element of a train is a station
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 * @version 0.3
 */
public class Train implements Runnable {
	private final String name;
	private Position pos;

	public Train(String name, Position p) throws BadPositionForTrainException {
        if (name == null || p == null) 
			throw new NullPointerException();
        if (!(p.getPos() instanceof Station)) 
			throw new BadPositionForTrainException(name);
        
        this.name = name;
        this.pos = p.clone();
        
        try {
            this.pos.getPos().enter();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

	public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                move();
                // Simulation du temps de trajet (entre 0.5 et 1.5 sec)
                Thread.sleep(500 + new Random().nextInt(1000));
            }
        } catch (InterruptedException e) {
            System.out.println("Train " + name + " arrêté.");
        }
    }

	private void move() throws InterruptedException {
        Element currentElement = pos.getPos();
        Direction currentDir = pos.getDirection();
        Railway railway = currentElement.railway; // Supposez l'accès (getter ou package)

        Element nextElement = railway.getNextElement(currentElement, currentDir);

        if (nextElement == null) {
            Direction newDir = (currentDir == Direction.LR) ? Direction.RL : Direction.LR;
            this.pos = new Position(currentElement, newDir);
            System.out.println(">>> " + name + " change de sens (reste sur " + currentElement + ")");
            return;
        }

        System.out.println(name + " attend pour entrer sur " + nextElement);
        
        nextElement.enter();
        
        System.out.println(name + " avance : " + currentElement + " -> " + nextElement);
        this.pos = new Position(nextElement, currentDir);

        currentElement.leave();
    }

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("Train[");
		result.append(this.name);
		result.append("]");
		result.append(" is on ");
		result.append(this.pos);
		return result.toString();
	}
}
