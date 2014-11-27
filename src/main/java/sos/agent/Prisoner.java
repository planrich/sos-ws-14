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
        System.out.println("usage: ./gradlew ex1:run [-Drounds=X] prisonerName:sos.agent.Prisoner(<modus>, <param>)");
        System.out.println();
        System.out.println("       rounds: default 10");
        System.out.println("       modus 'static': param should be one of "+ Constants.ACCUSE_OTHER+"|"+Constants.SILENT);
        System.out.println("       modus 'random': no param. will always gamble");
        System.out.println("       modus 'titfortat': no param. replays the previous decision of the other prisoner");
        System.out.println("       modus 'selfish': param 1: <history count>, param 2: <threshold in [0.0..1.0]>");
        System.out.println("                        param 3: modus default behaviour, param 4 optional depending on behaviour");
        System.out.println("       modus 'optimist': param 1: <fooled count>");
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
        } else if (modus.equals("selfish")) {
            ensureParam(args, i+1);
            ensureParam(args, i+2);
            ensureParam(args, i+3);
            int history = Integer.parseInt(args[i + 1].toString());
            float threshold = Float.parseFloat(args[i + 2].toString());
            return new SelfishAttackOtherBehaviour(this, rounds, history, threshold, getBehaviour(args, i+3, rounds));
        } else if (modus.equals("optimist")) {
            ensureParam(args, i+1);
            int fooledCount = Integer.parseInt(args[i+1].toString());
            return new OptimisticBehaviour(this, rounds, fooledCount);
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
