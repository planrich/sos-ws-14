package sos.agent;

import jade.core.Agent;
import sos.agent.behaviour.*;

import java.util.logging.Logger;

/**
 * @author rich
 * 24.11.14.
 */
public class Prisoner extends Agent {

    private static Logger logger = Logger.getLogger(Prisoner.class.getSimpleName());

    private void usage() {
        System.out.println();
        System.out.println("usage: ./gradlew prisoner [-Drounds=X] -Dagent=\"prisonerName:sos.agent.Prisoner(<modus>, <param>, <param>, ...)\"");
        System.out.println();
        System.out.println("       rounds: default 10");
        System.out.println("       modus 'static': param 1: one of silent|accuse");
        System.out.println("       modus 'random'. random silent|accuse");
        System.out.println("       modus 'titfortat'. replays the previous decision of the other prisoner");
        System.out.println("       modus 'aggressive': param 1: <window>, param 2: <threshold in [0.0..1.0]>");
        System.out.println("                          param 3: modus (one of static, random, ...) and the parameters");
        System.out.println("       modus 'optimist':  param 1: <trust level> [param 2: <reset count>]");
        System.out.println("       modus 'bayes':     param 1: <window for model> param 2: modus (one of static, random, ...) and the parameters");
        System.exit(-1);
    }

    @Override
    protected void setup() {
        Object[] args = getArguments();

        logger.info("starting prisoner: " + getAID());

        if (args == null || args.length == 0) {
            usage();
        }

        String roundsString = System.getProperty("rounds","10");
        int rounds = Integer.parseInt(roundsString);

        addBehaviour(getBehaviour(args, 0, rounds));
    }

    private BasePrisonerBehaviour getBehaviour(Object[] args, int i, int rounds) {
        String modus = args[i].toString();
        if (modus.equals("static")) {
            ensureParam(args, i+1);
            return new StaticBehaviour(this, rounds, args[i+1].toString());
        } else if (modus.equals("random")) {
            return new RandomBehaviour(this, rounds);
        } else if (modus.equals("titfortat")) {
            return new TitForTatBehaviour(this, rounds);
        } else if (modus.equals("aggressive")) {
            ensureParam(args, i+1);
            ensureParam(args, i+2);
            ensureParam(args, i+3);
            int history = Integer.parseInt(args[i + 1].toString());
            float threshold = Float.parseFloat(args[i + 2].toString());
            return new AggressiveBehaviour(this, rounds, history, threshold, getBehaviour(args, i+3, rounds));
        } else if (modus.equals("optimist")) {
            ensureParam(args, i+1);
            int fooledCount = Integer.parseInt(args[i+1].toString());
            int resetCount = fooledCount;
            if (args.length > i+2) {
                resetCount = Integer.parseInt(args[i+2].toString());
            }
            return new OptimisticBehaviour(this, rounds, fooledCount, resetCount);
        } else if (modus.equals("bayes")) {
            ensureParam(args, i+1);
            ensureParam(args, i+2);
            int count = Integer.parseInt(args[i+1].toString());
            BasePrisonerBehaviour other = getBehaviour(args, i+2, rounds);
            return new BayesBehaviour(this, rounds, count, other);
        } else {
            usage();
        }
        throw new IllegalArgumentException("not possible. usage should system exit -1");
    }

    private void ensureParam(Object[] args, int i) {
        try {
            Object obj = args[i];
        } catch (IndexOutOfBoundsException e) {
            usage();
        }
    }

    @Override
    protected void takeDown() {
        // shut down the jvm. the platform connection is not terminated automatically.
        // there might  be some other way to do this more gracefully...
        System.exit(0);
    }
}
