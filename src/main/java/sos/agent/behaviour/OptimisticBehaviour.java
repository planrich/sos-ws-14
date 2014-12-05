package sos.agent.behaviour;

import jade.core.Agent;
import sos.agent.Constants;
import sos.agent.Round;

import java.util.List;

/**
 * @author rich
 * 27.11.14.
 *
 * This behaviour assumes that the other guy is interested in cooperation.
 * It keeps a threshold of trust.
 *    0  1  2  3
 * ------------+-------------
 *             ^
 *             |
 *             +-- trust level
 * In case of losing trust (other plays accuse) decrease trust level.
 * If it is below 0 this behaviour starts to send ACCUSE until the
 * other reearns the trust (by playing fair).
 * trust level = -300, other has to play fair 300 times until this
 * behaviour sends SILENT again!
 */
public class OptimisticBehaviour extends BasePrisonerBehaviour {

    final int originalTrustLevel;
    int trustLevel;
    int resetTrustAtRound;

    public OptimisticBehaviour(Agent a, int rounds, int fooledThreshold, int restCount) {
        super(a, rounds);
        this.trustLevel = fooledThreshold;
        this.originalTrustLevel = fooledThreshold;
        this.resetTrustAtRound = restCount;
    }

    @Override
    protected String decide(List<Round> rounds) {
        Round round = getLastRound();

        if (rounds.size() > resetTrustAtRound) {
            this.trustLevel = this.originalTrustLevel;
            resetTrustAtRound = Integer.MAX_VALUE;
        }

        if (round != null && round.getOtherDecision().equals(Constants.ACCUSE_OTHER)) {
            trustLevel--;
        } else {
            trustLevel++;
        }

        if (trustLevel < 0) {
            return Constants.ACCUSE_OTHER;
        }
        return Constants.SILENT;
    }

}
