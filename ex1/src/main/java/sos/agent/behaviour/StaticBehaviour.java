package sos.agent.behaviour;

import jade.core.Agent;
import sos.agent.Round;

import java.util.List;

/**
 * @author rich
 * 24.11.14.
 *
 * Static behaviour. Never change the behaviour after creation.
 */
public class StaticBehaviour extends BasePrisonerBehaviour {

    private String decide;

    public StaticBehaviour(Agent agent, int rounds, String decide) {
        super(agent, rounds);
        this.decide = decide;
    }

    @Override
    protected String decide(List<Round> rounds) {
        return decide;
    }
}
