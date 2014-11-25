package sos.agent.behaviour;

import jade.core.Agent;
import sos.agent.Round;

import java.util.List;

/**
 * @author rich
 * 24.11.14.
 */
public class StaticBehaviour extends BasePrisonerBehaviour {

    private String decide;

    public StaticBehaviour(Agent a, int countTimes, String decide) {
        super(a, countTimes);
        this.decide = decide;
    }

    @Override
    protected String decide(List<Round> rounds) {
        return decide;
    }
}
