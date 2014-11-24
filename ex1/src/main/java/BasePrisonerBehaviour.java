import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rich on 24.11.14.
 */
public abstract class BasePrisonerBehaviour extends Behaviour {
    private int countTimes;
    private List<Round> roundList = new ArrayList<Round>();

    public BasePrisonerBehaviour(Agent a, int countTimes) {
        super(a);
        this.countTimes = countTimes;
    }

    protected abstract String decide(List<Round> rounds);

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("judge", false));
        msg.setContent(decide(roundList));
        myAgent.send(msg);

        ACLMessage message = myAgent.receive(MessageTemplate.MatchSender(new AID("judge", false)));
        String outCome = message.getContent();
        Integer year = Integer.parseInt(outCome.split(" ")[1]);
        addRound(roundList.size() + 1, year);
    }

    protected void addRound(int round, int year) {
        roundList.add(new Round(round, year));
    }

    @Override
    public boolean done() {
        return roundList.size() >= countTimes;
    }
}
