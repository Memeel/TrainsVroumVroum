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

	public Railway(Element[] elements) {
		if(elements == null)
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

	public Element[] getElements() {
		return elements;
	}
}
