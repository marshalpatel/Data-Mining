/**
 * Created by vraj on 11/14/15.
 */
public class Domain {
    
    private State[][] states;
    private Action[] actions;

    private int rows;
    private int columns;
    
    public Domain(int rows, int columns, double reward) {
        this.rows = rows;
        this.columns = columns;

        states = new State[rows][columns];
        actions = new Action[4];
        
        initializeStates(reward);
        initializeActions();
    }
    
    public void initializeStates(double reward) {
        for (int i = 0; i < this.states.length; i++) {
            for (int j = 0; j < this.states[0].length; j++) {
                this.states[i][j] = new State(reward);
            }
        }
    }

    public State getNextState(int i, int j, int actionType) {
        switch (actionType) {
            case Constants.LEFT: {
                j = j - 1;
                break;
            }
            case Constants.RIGHT: {
                j = j + 1;
                break;
            }
            case Constants.UP: {
                i = i + 1;
                break;
            }
            case Constants.DOWN: {
                i = i - 1;
                break;
            }
        }

        if ((i < 0 || i > this.rows - 1) || (j < 0 || j > this.columns - 1))
            return null;

        return this.states[i][j];
    }

    public void changeStateReward(int i, int j, int newReward) {
        this.states[i][j].setReward(newReward);
    }

    public State getState(int i, int j) {
        if ((i < 0 || i >this.rows) || (j < 0 || j > this.columns))
            return null;

        return this.states[i][j];
    }

    public Action getACtion(int action) {
        return actions[action];
    }

    public void initializeActions() {
        this.actions[Constants.LEFT] = new Action(Constants.LEFT, 1.0);
        this.actions[Constants.RIGHT] = new Action(Constants.RIGHT, 1.0);
        this.actions[Constants.UP] = new Action(Constants.UP, 1.0);
        this.actions[Constants.DOWN] = new Action(Constants.DOWN, 1.0);
    }

    public void setActionProbability(int action, double probability) {
        this.actions[action].setTransitionProbability(probability);
    }

    public Action[] getActions() {
        return actions;
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
    }

    public State[][] getStates() {
        return states;
    }

    public void setStates(State[][] states) {
        this.states = states;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }
}
