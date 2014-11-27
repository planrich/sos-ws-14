package sos.agent.behaviour;

import jade.core.Agent;
import sos.agent.Constants;
import sos.agent.Round;

import java.util.List;

/**
 * @author rich
 *         27.11.14.
 */
public class OptimisticBehaviour extends BasePrisonerBehaviour {

    int fooledThreshold;

    public OptimisticBehaviour(Agent a, int countTimes, int fooledThreshold) {
        super(a, countTimes);
        this.fooledThreshold = fooledThreshold;
    }

    @Override
    protected String decide(List<Round> rounds) {
        Round round = getLastRound();

        if (round != null && round.getOtherDecision().equals(Constants.ACCUSE_OTHER)) {
            fooledThreshold--;
        } else {
            fooledThreshold++;
        }

        if (fooledThreshold < 0) {
            return Constants.ACCUSE_OTHER;
        }
        return Constants.SILENT;
    }

}
