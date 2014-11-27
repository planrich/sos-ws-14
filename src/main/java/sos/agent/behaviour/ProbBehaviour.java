package sos.agent.behaviour;

import jade.core.Agent;
import sos.agent.Constants;
import sos.agent.Round;

import java.util.List;

/**
 * @author rich
 *         27.11.14.
 */
public class ProbBehaviour extends BasePrisonerBehaviour {

    public ProbBehaviour(Agent a, int countTimes) {
        super(a, countTimes);
    }

    @Override
    protected String decide(List<Round> rounds) {

        if (rounds.size() == 0) {
            return Constants.SILENT;
        }




        return null;
    }
}
