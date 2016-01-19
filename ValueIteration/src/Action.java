import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vraj on 11/14/15.
 */
public class Action {
    int type;
    double transitionProbability;

//    HashMap<Action, Double> probabilityMap;
    ArrayList<Action> conditionalActions;

    public Action(int type, double transitionProbability) {
        this.type = type;
        this.transitionProbability = transitionProbability;

//        probabilityMap = new HashMap<>();
        conditionalActions = new ArrayList<>();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getTransitionProbability() {
        return transitionProbability;
    }

    public void setTransitionProbability(double transitionProbability) {
        this.transitionProbability = transitionProbability;
    }

    public ArrayList<Action> getConditionalActions() {
        return conditionalActions;
    }

    public void setConditionalActions(ArrayList<Action> conditionalActions) {
        this.conditionalActions = conditionalActions;
    }

    public void addAction(Action action) {
        this.conditionalActions.add(action);
    }
}
