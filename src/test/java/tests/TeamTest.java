package tests;
import no.ntnu.idatt1002.k204.tasystem.model.Player;
import no.ntnu.idatt1002.k204.tasystem.model.Team;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TeamTest {
    private Team team;
    private Player player;
    private Team team_2;
    @BeforeEach
    public void testData() {
        ArrayList<Player> teamList = new ArrayList<Player>();
        this.team = new Team(teamList, "Fnatic");
        this.player=new Player("Phase","Iron");

    }
    @Nested
    public class generalTest{

        @Test
        @DisplayName("Adds player to team")
        public void addPlayerToTeam() {
            team.addPlayer(player);
            assertTrue(team.getPlayers().contains(player));
        }

        @Test
        @DisplayName("Set team name")
        public void setTeamName() {
            team.setTeamName("Team Liquid");
            assertEquals("Team Liquid", team.getTeamName());
        }
    }
    @Nested
    public class inputNotSupported{
        @Test
        public void emptyName(){
            assertThrows(IllegalArgumentException.class,()->new Team(""));

        }
        @Test
        public void emptyArrayList(){
            assertEquals("Fnatic",team.toString());
        }
    }

}
