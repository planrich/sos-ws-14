package sos.agent.behaviour;

import jade.core.Agent;
import sos.agent.Constants;
import sos.agent.Round;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * @author rich
 * 25.11.14.
 */
public class RandomBehaviour extends BasePrisonerBehaviour {

    private Random random = new SecureRandom();

    public RandomBehaviour(Agent a, int countTimes) {
        super(a, countTimes);
    }

    @Override
    protected String decide(List<Round> rounds) {
        return random.nextBoolean() ? Constants.ACCUSE_OTHER : Constants.SILENT;
    }
}
