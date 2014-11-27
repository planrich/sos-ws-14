package sos.agent;

/**
 * @author rich
 * 24.11.14.
 */
public class Round {
    private int round;
    private int year;
    private String descicion;
    private String otherDecision;

    public Round(int round, int year, String decision, String otherDecision) {
        this.round = round;
        this.year = year;
        this.descicion = decision;
        this.otherDecision = otherDecision;
    }

    public String getOtherDecision() {
        return otherDecision;
    }

    public int getRound() {
        return round;
    }

    public int getYears() {
        return year;
    }

    public String getDecision() {
        return descicion;
    }
}
