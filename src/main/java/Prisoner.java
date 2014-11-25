import jade.core.Agent;

import java.util.logging.Logger;

/**
 * @author rich
 * 24.11.14.
 */
public class Prisoner extends Agent {

    private static Logger logger = Logger.getLogger(Prisoner.class.getSimpleName());

    private void usage() {
        System.out.println();
        System.out.println("usage: ./gradlew ex1:run [-Drounds=X] prisonerName:Prisoner(<modus>, <param>)");
        System.out.println();
        System.out.println("       rounds: default 10");
        System.out.println("       modus 'static': param should be one of "+Constants.ACCUSE_OTHER+"|"+Constants.SILENT);
        System.out.println("       modus 'random': no param. will always gamble");
        System.exit(-1);
    }

    @Override
    protected void setup() {
        Object[] args = getArguments();

        logger.info("starting prisoner: " + getAID());

        if (args == null || args.length == 0) {
            usage();
        }
        String modus = args[0].toString();
        String param = null;
        if (args.length > 1) {
            param = args[1].toString();
        }

        String roundsString = System.getProperty("rounds","10");
        int rounds = Integer.parseInt(roundsString);

        if (modus.equals("static")) {
            if (param == null) {
                usage();
            }
            addBehaviour(new StaticBehaviour(this, rounds, param));
        } else if (modus.equals("random")) {
            addBehaviour(new RandomBehaviour(this, rounds));
        } else {
            usage();
        }
    }

    @Override
    protected void takeDown() {
        System.exit(0);
    }
}
