package sos.agent.behaviour;

import jade.core.Agent;
import sos.agent.Constants;
import sos.agent.Round;

import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author rich
 *         27.11.14.
 */
public class SelfishAttackOtherBehaviour extends BasePrisonerBehaviour {

    private Deque<Round> lastSteps = new LinkedBlockingDeque<Round>();
    private int thresholdHistory;
    private float thresholdToAttack;
    private BasePrisonerBehaviour basePrisonerBehaviour;

    public SelfishAttackOtherBehaviour(Agent a, int countTimes, int thresholdHistory, float thresholdToAttack, BasePrisonerBehaviour behaviour) {
        super(a, countTimes);
        this.thresholdHistory = thresholdHistory;
        this.thresholdToAttack = thresholdToAttack;
        this.basePrisonerBehaviour = behaviour;
    }

    @Override
    protected String decide(List<Round> rounds) {
        if (rounds.size() != 0) {
            lastSteps.push(rounds.get(rounds.size() - 1));
        }
        if (lastSteps.size() > thresholdHistory) {
            lastSteps.removeLast();
        }

        int myYears = 0;
        for (Round round : lastSteps) {
            myYears += round.getYears();
        }
        int maxYears = thresholdHistory * 3;

        if (myYears <= thresholdToAttack * maxYears) {
            return Constants.ACCUSE_OTHER;
        }

        return basePrisonerBehaviour.decide(rounds);
    }
}
