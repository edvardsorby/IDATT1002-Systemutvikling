package no.ntnu.idatt1002.k204.tasystem.model;

import no.ntnu.idatt1002.k204.tasystem.model.Player;
import no.ntnu.idatt1002.k204.tasystem.model.Team;
import no.ntnu.idatt1002.k204.tasystem.model.TournamentRegister;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

public class TournamentRegisterTest {
    private TournamentRegister register;
    private Team team1;

    @BeforeEach
    @DisplayName("Creates tournament register and fills a team with two players")
    public void TestData() {
        this.register = new TournamentRegister();
        Player player1 = new Player("Sjokoladepudden", "Silver");
        Player player2 = new Player("Phase", "Iron");
        ArrayList<Player> playerlist = new ArrayList<>();
        playerlist.add(player1);
        playerlist.add(player2);
        this.team1 = new Team(playerlist, "GeirSittLag");
    }

    @Nested
    @DisplayName("Perform positive tests")
    public class inputSupported {

        @Test
        @DisplayName("Tests if it is possible to add tournament to the tournament register")
        public void addTournament() {
            register.addTournament("SummonersRift", "Silver", "", LocalDateTime.of(2022, Month.APRIL, 28, 14, 00, 00));
            Assertions.assertEquals("[Tournament{name='SummonersRift', rankRequirement='Silver', dateTime=2022-04-28T14:00, teams=[]}]", register.getTournaments().toString());
        }

        @Test
        @DisplayName("Tests if it is possible to add a team to tournament")
        public void addTeamToTournament() {
            register.addTournament("SummonersRift", "Silver", "", LocalDateTime.of(2022, Month.APRIL, 28, 14, 00, 00));
            register.getTournamentByName("SummonersRift").addTeam(team1);
            Assertions.assertEquals(1, register.getTournamentByName("SummonersRift").getTeams().size());
        }
    }
}
