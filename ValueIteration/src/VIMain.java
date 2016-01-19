/**
 * Created by vraj on 11/14/15.
 */
public class VIMain {

    public static void main(String a[]) {

        Double reward = Double.parseDouble(a[0]);

        VIMain vi = new VIMain();

        Domain domain = new Domain(5, 4, reward);

        domain.getState(3, 1).setReward(-50.0);
        domain.getState(4, 3).setReward(10);

        domain.getState(1, 3).setIsBlocked(true);
        domain.getState(1, 1).setIsBlocked(true);

        domain.getACtion(Constants.RIGHT).setTransitionProbability(0.8);
        domain.getACtion(Constants.RIGHT).addAction(new Action(Constants.DOWN, 0.2));

        domain.getACtion(Constants.UP).setTransitionProbability(0.8);
        domain.getACtion(Constants.UP).addAction(new Action(Constants.LEFT, 0.2));

        int convergence = vi.performValueIteration(domain, 0.9);

        System.out.println("Converged at Timestamp: " + convergence);

        vi.printStates(domain);
    }

    public void printStates(Domain domain) {
        System.out.println();
        for (int j = 0; j < 15 * 4; j++) {
            System.out.print("-");
        }
        System.out.println();
        for (int i = 0; i < domain.getRows(); i++) {
            for (int j = 0; j < domain.getColumns(); j++) {
                State currentState = domain.getState(domain.getRows() - i - 1, j);
                double currentStateValue = currentState.getValue().get(currentState.getValue().size() - 1);

                String currentStringValue = "" + currentStateValue;
                int addSpace = currentStringValue.length();

                System.out.print("|");
                System.out.print("    " + currentStateValue);

                while (addSpace < 10) {
                    System.out.print(" ");
                    addSpace++;
                }
            }
            System.out.print("|");
            System.out.println();
            for (int j = 0; j < 15 * 4; j++) {
                System.out.print("-");
            }
            System.out.println();
        }
    }

    public int performValueIteration(Domain domain, double discountFactor) {

        int timestamp = 1;
        boolean isConverged = false;
        while (!isConverged) {
            isConverged = true;
            for (int i = 0; i < domain.getRows(); i++) {
                for (int j = 0; j < domain.getColumns(); j++) {
                    if (domain.getState(domain.getRows() - i - 1, j).isBlocked())
                        continue;

                    State currentState = domain.getState(domain.getRows() - i - 1, j);
                    double currentStateValue = currentState.getValue().get(timestamp - 1);

                    double newValue = 0.0;

                    double maxValue = -Double.MAX_VALUE;

                    for (Action action : domain.getActions()) {
                        State nextState = domain.getNextState(domain.getRows() - i - 1, j, action.getType());

                        double actionFactor = 0.0;

                        if (nextState != null && !nextState.isBlocked()) {
                            double nextStateValue = nextState.getValue().get(timestamp - 1);
                            actionFactor = action.getTransitionProbability() * nextStateValue;
                        } else {
                            actionFactor = action.getTransitionProbability() * currentStateValue;
                        }

                        for (Action condAction : action.getConditionalActions()) {
                            State tempNextState = domain.getNextState(domain.getRows() - i - 1, j, condAction.getType());

                            if (tempNextState == null || tempNextState.isBlocked()) {
                                actionFactor += condAction.getTransitionProbability() * currentStateValue;
                            } else {
                                double tempNextStateValue = tempNextState.getValue().get(timestamp - 1);
                                actionFactor += condAction.getTransitionProbability() * tempNextStateValue;
                            }
                        }
                        double actionValue = currentState.getReward() + (discountFactor * actionFactor);

                        if (actionValue > maxValue) {
                            maxValue = actionValue;
                            currentState.setAction(action);
                        }
                    }
                    if (maxValue != -Double.MAX_VALUE)
                        newValue = maxValue;

                    newValue = (double) Math.round(newValue * 100d) / 100d;

                    if (Double.compare(newValue, currentState.getValue().get(timestamp - 1)) != 0)
                        isConverged = false;

                    currentState.addValue(newValue);

//                    System.out.print(newValue + "[" + (domain.getRows() - i - 1) + "," + j + "]  ");
                }
//                System.out.println();
            }
//            System.out.println();
            timestamp++;
        }
        return timestamp;
    }
}
