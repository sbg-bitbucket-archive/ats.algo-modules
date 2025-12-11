package ats.algo.inplayGS;

import ats.algo.genericsupportfunctions.MethodName;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import ats.algo.inplayGS.Selection.Team;

public class DemarginTest {

    private static Selection[] firstToScore = {new Selection(Team.A, "Andre Silva", 3.4),
            new Selection(Team.A, "Otavio", 9.5), new Selection(Team.A, "Hector Herrera", 2001),
            new Selection(Team.A, "Danilo Pereira", 15), new Selection(Team.A, "Yacine Brahimi", 2001),
            new Selection(Team.A, "Jesus Corona", 2001), new Selection(Team.A, "Ivan Marcano", 26),
            new Selection(Team.A, "Diogo Jota", 2001), new Selection(Team.A, "Willy Boly", 51),
            new Selection(Team.A, "Ruben Neves", 2001), new Selection(Team.A, "Oliver Torres", 8),
            new Selection(Team.A, "Rui Pedro", 2001), new Selection(Team.A, "Felipe", 29),
            new Selection(Team.A, "Evandro Goebel", 2001), new Selection(Team.A, "Andre Andre", 12),
            new Selection(Team.A, "Alex Telles", 29), new Selection(Team.A, "Maximiliano Pereira", 29),
            new Selection(Team.A, "Joao Carlos Teixeira", 2001), new Selection(Team.A, "Miguel Layun", 9),
            new Selection(Team.B, "William", 2001), new Selection(Team.B, "Rafael Lopes", 2001),
            new Selection(Team.B, "Pedro Queiros", 101), new Selection(Team.B, "Fabio Martins", 29),
            new Selection(Team.B, "Rodrigo Battaglia", 2001), new Selection(Team.B, "Bruno Braga", 2001),
            new Selection(Team.B, "Felix Mathaus", 101), new Selection(Team.B, "Leandro Freire", 67),
            new Selection(Team.B, "Rafael Assis", 2001), new Selection(Team.B, "Gustavo Souza", 26),
            new Selection(Team.B, "Joao Patrao", 26), new Selection(Team.B, "Fabio Santos", 91),
            new Selection(Team.B, "Carlos Ponck", 51), new Selection(Team.B, "Alioune Fall", 15),
            new Selection(Team.B, "Perdigao", 2001), new Selection(Team.B, "Paulinho", 101),
            new Selection(Team.B, "Nelson Lenho", 91), new Selection(Team.B, "Simon Vukcevic", 2001),
            new Selection(Team.B, "Nemanja Petrovic", 2001), new Selection(Team.NEITHER, "No Score", 13)};

    @Test
    public void test() {
        MethodName.log();
        Map<String, Selection> firstToScorePrices = new LinkedHashMap<String, Selection>();
        for (int i = 0; i < firstToScore.length; i++)
            firstToScorePrices.put(firstToScore[i].name, firstToScore[i]);
        MarginManager.demargin(firstToScorePrices, 1.0);
    }

}
