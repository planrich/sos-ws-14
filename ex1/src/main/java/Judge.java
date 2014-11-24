import jade.core.Agent;

public class Judge extends Agent {
    protected void setup() {
        addBehaviour(new JudgeBehaviour());
    }
}
