package model;

import javafx.scene.image.Image;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by devNinja on 7.1.16..
 */
public class Player extends Thread
{
    private String playerName, playerSurname;
    private Date dateOfBirth; // todo TEST and TRY date conversion from SQL
    private HashMap<String, Short> stats;
    private Position position;
    private Image playerImage;

    public Player(String name, String surname, String pos, java.sql.Date date, HashMap<String, Short> stats)
    {
        this.playerName = name;
        this.playerSurname = surname;
        this.position = new Position(pos);
        this.dateOfBirth = new Date(date.getTime()); // TODO check
        this.stats = new HashMap<>(stats);
    }

    public Player(String name, String surname, String pos, java.sql.Date date, HashMap<String, Short> stats, Image img)
    {
        this.playerName = name;
        this.playerSurname = surname;
        this.position = new Position(pos);
        this.dateOfBirth = new Date(date.getTime()); // TODO check
        this.stats = new HashMap<>(stats);
        this.playerImage = img;
    }

    @Override
    public void run()
    {
        super.run();
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

    public HashMap<String, Short> getStats()
    {
        return stats;
    }

    public void setStats(HashMap<String, Short> stats)
    {
        this.stats = stats;
    }

    public Position getPosition()
    {
        return position;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

    public Image getPlayerImage()
    {
        return playerImage;
    }

    public int getPlayerAge()
    {
        // TODO implement this homie
        return 25;
    }

    @Override
    public String toString()
    {
        String msg = playerName + " " + playerSurname + " " + dateOfBirth.toString() + "\n";

        return msg;
    }
}
