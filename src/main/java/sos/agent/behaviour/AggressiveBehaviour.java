package sos.agent.behaviour;

import jade.core.Agent;
import sos.agent.Constants;
import sos.agent.Round;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author rich
 * 27.11.14.
 *
 * This Behaviour keeps a sliding window of the last rounds.
 * It calculates the amount of years it got for this window and
 * if it is above the threshold (trust developed with other prisoner) he starts to attack.
 */
public class AggressiveBehaviour extends BasePrisonerBehaviour {

    private Deque<Round> lastSteps = new LinkedBlockingDeque<Round>();
    private int historyWindowCount;
    private float thresholdToAttach;
    private BasePrisonerBehaviour baseBehaviour;

    public AggressiveBehaviour(Agent a, int rounds, int historyWindow, float thresholdAttack, BasePrisonerBehaviour behaviour) {
        super(a, rounds);
        this.historyWindowCount = historyWindow;
        this.thresholdToAttach = thresholdAttack;
        this.baseBehaviour = behaviour;
    }

    @Override
    protected String decide(List<Round> rounds) {
        if (getLastRound() != null) {
            lastSteps.push(getLastRound());
        }
        if (lastSteps.size() > historyWindowCount) {
            lastSteps.removeLast();
        }

        int myYears = 0;
        for (Round round : lastSteps) {
            myYears += round.getYears();
        }
        int maxYears = historyWindowCount * 3;

        if (myYears <= thresholdToAttach * maxYears) {
            return Constants.ACCUSE_OTHER;
        }

        return baseBehaviour.decide(rounds);
    }
}
