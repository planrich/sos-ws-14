package sos.agent.behaviour;

import jade.core.Agent;
import sos.agent.Constants;
import sos.agent.Round;

import java.util.List;

/**
 * @author rich
 * 27.11.14
 * Take This for That! Behaviour that replays the last round. In
 * the first round it plays silent!
 */
public class TitForTatBehaviour extends BasePrisonerBehaviour {
    public TitForTatBehaviour(Agent a, int countTimes) {
        super(a, countTimes);
    }

    @Override
    protected String decide(List<Round> rounds) {
        String decision = Constants.SILENT;

        if (rounds.size() > 1) {
            decision = rounds.get(rounds.size()-1).getOtherDecision();
        }

        return decision;
    }
}
