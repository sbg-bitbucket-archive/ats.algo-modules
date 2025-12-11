package ats.algo.sport.football;

import ats.algo.core.common.PlayerStatus;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.TeamSheet;

public class ExampleTeamSheets {

    public static TeamSheet getExampleTeamSheet() {
        TeamSheet teamSheet = new TeamSheet();
        teamSheet.addPlayer(TeamId.A, "Aiden O'Neill", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.A, "Andre Gray", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.A, "Ashley Barnes", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.A, "Ben Mee", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.A, "Dean Marney", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.A, "George Boyd", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.A, "James Tarkowski", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.A, "Jeff Hendrick", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.A, "Joey Barton", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.A, "Johann Gudmundssno", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.A, "John Flanagan", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.A, "Patrick Bamford", PlayerStatus.ON_THE_BENCH);
        teamSheet.addPlayer(TeamId.A, "Sam Vokes", PlayerStatus.ON_THE_BENCH);
        teamSheet.addPlayer(TeamId.A, "Scott Arfield", PlayerStatus.ON_THE_BENCH);
        teamSheet.addPlayer(TeamId.A, "Stephen Ward", PlayerStatus.ON_THE_BENCH);
        teamSheet.addPlayer(TeamId.A, "Steven Defour", PlayerStatus.ON_THE_BENCH);
        teamSheet.addPlayer(TeamId.A, "Tendayi Darikwa", PlayerStatus.ON_THE_BENCH);

        teamSheet.addPlayer(TeamId.B, "Cedric Soares", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.B, "Cuco Martina", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.B, "Dusan Tadic", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.B, "Florin Gardos", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.B, "Harrison Reed", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.B, "Jack Stephens", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.B, "James Ward-Prowse", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.B, "Jay Rodriguez", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.B, "Jordy Clasie", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.B, "Jose Fonte", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.B, "Josh Sims", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.B, "Pierre Hojbjerg", PlayerStatus.ON_THE_BENCH);
        teamSheet.addPlayer(TeamId.B, "Ryan Bertrand", PlayerStatus.ON_THE_BENCH);
        teamSheet.addPlayer(TeamId.B, "Sam McQueen", PlayerStatus.ON_THE_BENCH);
        teamSheet.addPlayer(TeamId.B, "Shane Long", PlayerStatus.ON_THE_BENCH);
        teamSheet.addPlayer(TeamId.B, "Steven Davis", PlayerStatus.ON_THE_BENCH);
        teamSheet.addPlayer(TeamId.B, "Virgil VanDijk", PlayerStatus.ON_THE_BENCH);
        return teamSheet;
    }


}
