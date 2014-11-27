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
 */
public abstract class BasePrisonerBehaviour extends Behaviour {
    private static Logger logger = Logger.getLogger(BasePrisonerBehaviour.class.getSimpleName());

    private int countTimes;
    private List<Round> roundList = new ArrayList<Round>();
    private boolean sent = false;
    private AID judgeAID = new AID("judge@dilemma", true);
    private String decision = null;

    public BasePrisonerBehaviour(Agent a, int countTimes) {
        super(a);
        this.countTimes = countTimes;
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
            // print out stats!
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println(" prisoner stats: " + roundList.size() + " round(s)");
            System.out.print("       my years: ");
            int sum = 0;
            for (int i = 0; i < roundList.size(); i++) {
                Round round = roundList.get(i);
                System.out.print(Integer.toString(round.getYears())+"y,");
                sum += round.getYears();
            }
            System.out.println();
            System.out.print("    other years: ");
            int otherSum = 0;
            for (int i = 0; i < roundList.size(); i++) {
                Round round = roundList.get(i);
                int otherYears = round.getYears();
                if (otherYears == 0) {
                    otherYears = 3;
                } else if (otherYears == 1) {
                    otherYears = 1;
                } else if (otherYears == 2) {
                    otherYears = 2;
                } else if (otherYears == 3) {
                    otherYears = 0;
                }
                System.out.print(Integer.toString(otherYears)+"y,");
                otherSum += otherYears;
            }
            System.out.println();
            System.out.println("   my years sum: " + sum);
            System.out.println("other years sum: " + otherSum);
            System.out.println("both years  sum: " + (otherSum + sum));
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            myAgent.doDelete();
        }
    }

    protected void addRound(int round, int year, String decision) {
        String otherDecision = Constants.SILENT;
        if (year == 3) {
            otherDecision = Constants.ACCUSE_OTHER;
        } else if (year == 2) {
            otherDecision = Constants.ACCUSE_OTHER;
        } else if (year == 1) {
            otherDecision = Constants.SILENT;
        } else if (year == 0) {
            otherDecision = Constants.SILENT;
        }
        roundList.add(new Round(round, year, decision, otherDecision));
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
