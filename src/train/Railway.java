package train;


/**
 * Représentation d'un circuit constitué d'éléments de voie ferrée : gare ou
 * section de voie
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public class Railway {
	private final Element[] elements;
	private int nbWaitingLR = 0;
	private int nbWaitingRL = 0;
	private int nbRunningLR = 0;
	private int nbRunningRL = 0;

	public Railway(Element[] elements) {
		if (elements == null)
			throw new NullPointerException();
		
		this.elements = elements;
		for (int i = 0; i < elements.length; i++) {
			elements[i].setRailway(this);
			if (i < elements.length - 1) {
				elements[i].nextElementLR = elements[i + 1];
			} else {
				elements[i].nextElementLR = null;
			}
			if (i > 0) {
				elements[i].nextElementRL = elements[i - 1];
			} else {
				elements[i].nextElementRL = null;
			}
		}
		
	}

	public synchronized void addWaitingTrain(Direction d) {
		if (d == Direction.LR) {
			nbWaitingLR++;
		} else {
			nbWaitingRL++;
		}
	}

	public synchronized void removeWaitingTrain(Direction d) {
		if (d == Direction.LR) {
			nbWaitingLR--;
		} else {
			nbWaitingRL--;
		}
	}

	public synchronized int getNbWaitingTrains(Direction d) {
		if (d == Direction.LR) {
			return nbWaitingLR;
		} else {
			return nbWaitingRL;
		}
	}

	public synchronized void addRunningTrain(Direction d) {
		if (d == Direction.LR) {
			nbRunningLR++;
		} else {
			nbRunningRL++;
		}
	}

	public synchronized void removeRunningTrain(Direction d) {
		if (d == Direction.LR) {
			nbRunningLR--;
		} else {
			nbRunningRL--;
		}
	}

	public synchronized int getNbRunningTrains(Direction d) {
		if (d == Direction.LR) {
			return nbRunningLR;
		} else {
			return nbRunningRL;
		}
	}

	public synchronized int countTrainsInSections() {
		return elements[0].countRunningRecursive();
	}

	public synchronized void globalNotify() {
		notifyAll();
	}

	public synchronized Element getNextElement(Element current, Direction d) {
        int index = -1;
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == current) {
                index = i;
                break;
            }
        }
        if (index == -1) return null;

        if (d == Direction.LR) {
            return (index + 1 < elements.length) ? elements[index + 1] : null;
        } else {
            return (index - 1 >= 0) ? elements[index - 1] : null;
        }
    }
	

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Element e : this.elements) {
			if (first)
				first = false;
			else
				result.append("--");
			result.append(e);
		}
		return result.toString();
	}

	public Element[] getElements() {
		return elements;
	}
}
