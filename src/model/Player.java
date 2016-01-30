package model;

import javafx.scene.image.Image;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by devNinja on 7.1.16..
 */
public class Player extends Thread
{
    private String playerName, playerSurname;
    private Date dateOfBirth;
    private HashMap<String, Integer> stats;
    private String position;
    private Image playerImage;  //TODO add player image
    private Integer playerAge;

    public Player(ResultSet s)
    {
        try {
            this.playerName = s.getString("name");
            this.playerSurname = s.getString("surname");
            this.position = s.getString("position");
            this.dateOfBirth = s.getDate("date_of_birth");
            DateTime now = new DateTime();
            DateTime birhaday = new DateTime(this.dateOfBirth);
            Period period = new Period(birhaday,now);
            this.playerAge = period.getYears();
            stats = new HashMap<>();
            for(int i = 8 ; i <= 23; i++)
                stats.put( s.getMetaData().getColumnName(i) ,  s.getInt(i));
        }
        catch (SQLException e) {
            System.out.println("Pravljenje objekta player EXP!");
        }
    }

    @Override
    public void run()
    {
        super.run();
    }

    public Integer getPlayerStats(String s)
    {
        return stats.get(s);
    }

    public HashMap<String, Integer> getStats()
    {
        return stats;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }

    public String getPlayerSurname()
    {
        return playerSurname;
    }

    public String getFullPlayerName()
    {
        return playerName + " " + playerSurname;
    }

    public void setPlayerSurname(String playerSurname)
    {
        this.playerSurname = playerSurname;
    }

    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPosition()
    {
        return position;
    }

    public Image getPlayerImage()
    {
        return playerImage;
    }

    public int getPlayerAge()
    {
        return this.playerAge;
    }

    @Override
    public String toString()
    {
        String msg = playerName + " " + playerSurname + " " + getPosition() + " " + getPlayerAge() + "\n";
        return msg;
    }
}
