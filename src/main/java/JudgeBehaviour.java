import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.logging.Logger;

/**
 * @author rich
 * 24.11.14.
 */
public class JudgeBehaviour extends CyclicBehaviour {

    private Logger logger = Logger.getLogger(JudgeBehaviour.class.getSimpleName());

    private ACLMessage prisoner1Answer = null;
    private ACLMessage prisoner2Answer = null;

    private AID p1AID = new AID("p1@dilemma", true);
    private AID p2AID = new AID("p2@dilemma", true);

    private MessageTemplate p1Template = MessageTemplate.MatchSender(p1AID);
    private MessageTemplate p2Template = MessageTemplate.MatchSender(p2AID);

    @Override
    public void action() {
        logger.info("judge on action");
        if (prisoner1Answer == null) {
            prisoner1Answer = myAgent.receive(p1Template);
            if (prisoner1Answer == null) {
                block(); // wait for prisoner 1 to provide his answer
                return; // block is effective after action() returned
            }
            logger.info("got message from prisoner 1: " + prisoner1Answer.getContent());
        }
        if (prisoner2Answer == null) {
            prisoner2Answer = myAgent.receive(p2Template);
            if (prisoner2Answer == null) {
                block(); // wait for prisoner 2 to provide his answer
                return; // block is effective after action() returned
            }
            logger.info("got message from prisoner 2: " + prisoner2Answer.getContent());
        }


        String p1 = prisoner1Answer.getContent();
        String p2 = prisoner2Answer.getContent();
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

        prisoner1Answer = null;
        prisoner2Answer = null;

        ACLMessage judgeResultP1 = new ACLMessage(ACLMessage.INFORM);
        judgeResultP1.setSender(myAgent.getAID());
        judgeResultP1.setOntology("Prisoners-Dilemma-Ontology");
        judgeResultP1.setContent("years " + Integer.toString(p1Years));
        judgeResultP1.addReceiver(p1AID);
        myAgent.send(judgeResultP1);

        ACLMessage judgeResultP2 = new ACLMessage(ACLMessage.INFORM);
        judgeResultP2.setSender(myAgent.getAID());
        judgeResultP2.setOntology("Prisoners-Dilemma-Ontology");
        judgeResultP2.setContent("years " + Integer.toString(p2Years));
        judgeResultP2.addReceiver(p2AID);
        myAgent.send(judgeResultP2);

        logger.info("decision is: p1="+p1Years+", p2="+p2Years);
    }
}
