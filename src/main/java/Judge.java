import jade.core.Agent;

import java.util.logging.Logger;

/**
 * @author rich
 * 24.11.2014
 */
public class Judge extends Agent {
    private static Logger logger = Logger.getLogger(Prisoner.class.getSimpleName());
    protected void setup() {
        logger.info("starting judge agent: " + getAID());
        addBehaviour(new JudgeBehaviour());
    }
}
