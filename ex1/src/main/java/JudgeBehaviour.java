import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by rich on 24.11.14.
 */
public class JudgeBehaviour extends CyclicBehaviour {

    @Override
    public void action() {
        MessageTemplate p1Template = MessageTemplate.MatchSender(new AID("p1",false));
        ACLMessage p1Message = myAgent.receive(p1Template);
        MessageTemplate p2Template = MessageTemplate.MatchSender(new AID("p2", false));
        ACLMessage p2Message = myAgent.receive(p2Template);

        String p1 = p1Message.getContent();
        String p2 = p2Message.getContent();
        int p1Years = 0;
        int p2Years = 0;

        if (p1.equals(Constants.ACCUSE_OTHER) && p2.equals(Constants.ACCUSE_OTHER)) {
            p1Years = 2;
            p2Years = 2;
        } else if (p1.equals(Constants.ACCUSE_OTHER) && p2.equals(Constants.SILENT))  {
            p1Years = 0;
            p2Years = 3;
        } else if (p1.equals(Constants.SILENT) && p2.equals(Constants.ACCUSE_OTHER))  {
            p1Years = 3;
            p2Years = 0;
        } else if (p1.equals(Constants.SILENT) && p2.equals(Constants.SILENT))  {
            p1Years = 1;
            p2Years = 1;
        }

        ACLMessage judgeResultP1 = new ACLMessage(ACLMessage.INFORM);
        judgeResultP1.setContent("years " + Integer.toString(p1Years));
        judgeResultP1.addReceiver(new AID("p1", false));
        myAgent.send(judgeResultP1);

        ACLMessage judgeResultP2 = new ACLMessage(ACLMessage.INFORM);
        judgeResultP2.setContent("years " + Integer.toString(p2Years));
        judgeResultP2.addReceiver(new AID("p2", false));
        myAgent.send(judgeResultP2);
    }
}
