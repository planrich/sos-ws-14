package sos.agent;

/**
 * @author rich
 * 24.11.14.
 *
 * A round saved by the agent. Might be used to predict behaviour of the
 * other guy.
 */
public class Round {
    private int round;
    private int year;
    private int otherYear;
    private String descicion;
    private String otherDecision;

    public Round(int round, int year, int otherYear, String decision, String otherDecision) {
        this.round = round;
        this.year = year;
        this.otherYear = otherYear;
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

    public int getOtherYears() {
        return otherYear;
    }
}
