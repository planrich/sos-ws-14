package sos.agent;

/**
 * @author rich
 * 24.11.14.
 */
public class Round {
    private int round;
    private int year;
    private String descicion;

    public Round(int round, int year, String decision) {
        this.round = round;
        this.year = year;
        this.descicion = decision;
    }

    public int getRound() {
        return round;
    }

    public int getYears() {
        return year;
    }

    public String getDescicion() {
        return descicion;
    }
}
