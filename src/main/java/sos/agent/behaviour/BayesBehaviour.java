package sos.agent.behaviour;

import jade.core.Agent;
import sos.agent.Constants;
import sos.agent.Round;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rich
 * 05.12.14.
 *
 * This behaviour trains a model how the other behaves.
 * It calculates the probabilities to predict what he is going
 * do in the next move.
 */
public class BayesBehaviour extends BasePrisonerBehaviour {

    private BasePrisonerBehaviour behaviour;
    private int learnRoundCount;
    private List<Round> modelRounds = new ArrayList<Round>();

    public BayesBehaviour(Agent a, int rounds, int learnRounds, BasePrisonerBehaviour behaviour) {
        super(a, rounds);

        this.behaviour = behaviour;
        this.learnRoundCount = learnRounds;
    }

    @Override
    protected String decide(List<Round> rounds) {

        if (modelRounds.size() < learnRoundCount) {
            if (getLastRound() != null) {
                modelRounds.add(getLastRound());
            }
            return behaviour.decide(rounds);
        }

        double [][] frequency = new double[2][2];

        // initialize to avoid zero index problem
        frequency[0][0] = 1;
        frequency[1][0] = 1;
        frequency[0][1] = 1;
        frequency[1][1] = 1;

        double myAccuses = 1;
        double otherAccuses = 1;
        double mySilent = 1;
        double otherSilent = 1;
        for (int i = 0; i < modelRounds.size(); i++) {
            Round round = modelRounds.get(i);
            String myD = round.getDecision();
            String otherD = round.getOtherDecision();
            if (myD.equals(Constants.ACCUSE_OTHER) && otherD.equals(Constants.ACCUSE_OTHER)) {
                frequency[0][0] += 1;
                myAccuses += 1;
                otherAccuses += 1;
            } else if (myD.equals(Constants.ACCUSE_OTHER) && otherD.equals(Constants.SILENT)) {
                frequency[1][0] += 1;
                myAccuses += 1;
                otherSilent += 1;
            } else if (myD.equals(Constants.SILENT) && otherD.equals(Constants.ACCUSE_OTHER)) {
                frequency[0][1] += 1;
                mySilent += 1;
                otherAccuses += 1;
            } else { // both silent
                frequency[1][1] += 1;
                mySilent += 1;
                otherSilent += 1;
            }
        }
        double total = modelRounds.size();

        double ltAA = frequency[0][0] / otherAccuses;
        double ltSA = frequency[0][1] / otherAccuses;
        double ltAS = frequency[1][0] / otherSilent;
        double ltSS = frequency[1][1] / otherSilent;

        double totalMyA = (frequency[0][0] + frequency[1][0]) / total;
        double totalMyS = (frequency[0][1] + frequency[1][1]) / total;

        double totalOtherA = (frequency[0][0] + frequency[0][1]) / total;
        double totalOtherS = (frequency[1][0] + frequency[1][1]) / total;

        double youAccuseIamSilent = ltSA * (totalOtherA/totalMyS);
        double youAccuseIAccuse = ltAA * (totalOtherA/totalMyA);
        double youSilentIamSilent = ltSS * (totalOtherS/totalMyS);
        double youSilentIAccuse = ltAS * (totalOtherS/totalMyA);

        String s = String.format(" P(S|A) %.2f, P(S|S) %.2f, P(A|S) %.2f P(A|A) %.2f", youSilentIAccuse, youSilentIamSilent, youAccuseIamSilent, youAccuseIAccuse);
        System.out.println(s);
        if (youSilentIAccuse < youAccuseIAccuse) {
            // this would mean 2 years -> try silent
            if (youAccuseIamSilent > youSilentIamSilent) {
                // take the minimum. The other one is more likely to accuse me
                return Constants.ACCUSE_OTHER;
            }
            return Constants.SILENT;
        } else {
            return Constants.ACCUSE_OTHER;
        }
    }
}
