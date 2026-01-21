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
	private int nbTrainsRL = 0;
	private int nbTrainsLR = 0;

	public Railway(Element[] elements) {
		if(elements == null)
			throw new NullPointerException();
		
		this.elements = elements;
		for (Element e : elements)
			e.setRailway(this);
	}

	public Element getNextElement(Element current, Direction d) {
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

	public int getNbTrainsLR() {
		return nbTrainsLR;
	}
	public int getNbTrainsRL() {
		return nbTrainsRL;
	}

	public synchronized void setNbTrainsLR(int nb) {
		this.nbTrainsLR = nb;
		notifyAll();
	}
	public synchronized void setNbTrainsRL(int nb) {
		this.nbTrainsRL = nb;
		notifyAll();
	}

	public Element[] getElements() {
		return elements;
	}
}
