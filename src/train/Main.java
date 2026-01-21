package train;

/**
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 */

public class Main {
    public static void main(String[] args) {
        Station A = new Station("GareA", 3);
        Station D = new Station("GareD", 3);
        
        Section AB = new Section("AB");
        Section BC = new Section("BC");
        Section CD = new Section("CD");
        
        Railway r = new Railway(new Element[] { A, AB, BC, CD, D });
        
        System.out.println("The railway is:");
        System.out.println("\t" + r);
        System.out.println("--------------------------------------------------");


        Position p = new Position(A, Direction.LR);
        Position p2 = new Position(D, Direction.RL);

        try {
            Train t1 = new Train("TGV_1", p);
            Train t2 = new Train("TER_2", p);
            Train t3 = new Train("Fret_3", p2);

            System.out.println("Lancement des trains...");

            new Thread(t1).start();
            new Thread(t2).start();
            new Thread(t3).start();

            // Ajoutons un 4ème train qui part de l'autre côté (Gare D)
            // Attention : Sans gestion avancée des interblocages, ce train et les autres
            // risquent de se bloquer mutuellement s'ils se rencontrent sur une section !

            /*
            Position pInverse = new Position(D, Direction.RL);
            Train t4 = new Train("ContreSens_4", pInverse);
            new Thread(t4).start();
            */

        } catch (BadPositionForTrainException e) {
            System.out.println("Erreur de positionnement : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}