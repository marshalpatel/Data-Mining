import java.util.ArrayList;

/**
 * Created by vraj on 11/14/15.
 */
public class State {
    private Action action;
    private double reward;
    private ArrayList<Double> value;
    private boolean isBlocked;

    public State(double reward) {
        this.reward = reward;


        this.value = new ArrayList<>();
        this.value.add(0.0);
//        this.action = new Action();
    }

    public void addValue(double value) {
        this.value.add(value);
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public ArrayList<Double> getValue() {
        return value;
    }

    public void setValue(ArrayList<Double> value) {
        this.value = value;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }
}
