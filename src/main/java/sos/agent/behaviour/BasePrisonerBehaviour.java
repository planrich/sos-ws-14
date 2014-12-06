package sos.agent.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sos.agent.Constants;
import sos.agent.Round;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author rich
 * 24.11.14.
 *
 * This is the base behaviour. It abstracts away the communication.
 * A new behaviour should only implement the decide method.
 */
public abstract class BasePrisonerBehaviour extends Behaviour {
    private static Logger logger = Logger.getLogger(BasePrisonerBehaviour.class.getSimpleName());

    private int countTimes;
    private List<Round> roundList = new ArrayList<Round>();
    private boolean sent = false;
    private AID judgeAID = new AID("judge@dilemma", true);
    private String decision = null;

    public BasePrisonerBehaviour(Agent agent, int rounds) {
        super(agent);
        this.countTimes = rounds;
    }

    protected abstract String decide(List<Round> rounds);

    @Override
    public void action() {
        if (!sent) {
            decision = decide(roundList);
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setSender(myAgent.getAID());
            msg.addReceiver(judgeAID);
            msg.setOntology("Prisoners-Dilemma-Ontology");
            msg.setContent(decision);
            myAgent.send(msg);
            sent = true;
            logger.info("sent message to judge: " + msg.toString());
        }

        ACLMessage message = myAgent.receive(MessageTemplate.MatchSender(judgeAID));
        if (message == null) {
            block(); // no message from the judge yet. block
            logger.info("waiting for judge to respond!");
            return; // make blocking effective
        }
        String outCome = message.getContent();
        Integer year = Integer.parseInt(outCome.split(" ")[1]);
        addRound(roundList.size() + 1, year, decision);
        sent = false;

        if (done()) {
            debugPrintStats();
            myAgent.doDelete();
        }
    }

    private void debugPrintStats() {
        // print out stats!
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("rounds: " + roundList.size() + " round(s)");
        System.out.println("MY behaviour: " + getClass().getSimpleName());
        System.out.println();
        System.out.println("MY years:\n  0: ");
        // print my years
        for (int i = 0; i < roundList.size(); i++) {
            Round round = roundList.get(i);
            int otherYears = round.getYears();
            System.out.print(Integer.toString(otherYears)+"y,");
            if (i % 20 == 0 && i != 0) {
                System.out.print(String.format("\n% 3d: ", i));
            }
        }
        System.out.println();
        System.out.println("OTHER years:\n  0: ");
        // print other years
        for (int i = 0; i < roundList.size(); i++) {
            Round round = roundList.get(i);
            System.out.print(Integer.toString(round.getOtherYears())+"y,");
            if (i % 20 == 0 && i != 0) {
                System.out.print(String.format("\n% 3d: ", i));
            }
        }
        int years = 0;
        int otherYears = 0;
        int rYears = 0;
        int rOtherYears = 0;
        System.out.println();
        // print sum of years
        System.out.println();
        for (int i = 0; i < roundList.size(); i++) {
            Round round = roundList.get(i);
            years += round.getYears();
            otherYears += round.getOtherYears();
            rYears += round.getYears();
            rOtherYears += round.getOtherYears();
            if (i % 9 == 0 && i != 0 || i == roundList.size()-1) {
                System.out.println(String.format("Round: % 3d MY sum: % 3d|% 3d, OTHER sum: % 3d|% 3d, BOTH sum: % 3d|% 3d",
                        i+1, years, rYears, otherYears, rOtherYears, (years+otherYears), (rYears + rOtherYears)));
                rYears = 0;
                rOtherYears = 0;
            }
        }
        System.out.println();
        System.out.println("   my years sum (grand total): " + years);
        System.out.println("other years sum (grand total): " + otherYears);
        System.out.println("both years  sum (grand total): " + (otherYears + years));
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }


    protected void addRound(int round, int year, String decision) {
        String otherDecision = Constants.SILENT;
        int otherYear = 0;
        if (year == 3) {
            otherDecision = Constants.ACCUSE_OTHER;
            otherYear = 0;
        } else if (year == 2) {
            otherDecision = Constants.ACCUSE_OTHER;
            otherYear = 2;
        } else if (year == 1) {
            otherDecision = Constants.SILENT;
            otherYear = 1;
        } else if (year == 0) {
            otherDecision = Constants.SILENT;
            otherYear = 3;
        }
        roundList.add(new Round(round, year, otherYear, decision, otherDecision));
    }


    public Round getLastRound() {
        if (roundList.size() == 0) {
            return null;
        }
        return roundList.get(roundList.size() - 1);
    }

    @Override
    public boolean done() {
        return roundList.size() >= countTimes;
    }
}
