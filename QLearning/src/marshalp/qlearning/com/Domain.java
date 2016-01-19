package marshalp.qlearning.com;

import marshalp.qlearning.com.Action.Directions;

import java.text.DecimalFormat;

public class Domain {

    int row;
    int col;
    //	State[][] states;
    State[][] q_matrix;

    public Domain(int r, int c, double reward) {

        this.col = c;
        this.row = r;

//		this.states = new State[this.row][this.col];
        this.q_matrix = new State[this.row][this.col];

        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {

                // q_matrix[i][j] = new State(false, false, 0, i, j);

                if (i == 4 && j == 3) {
//					states[i][j] = new State(true, false, 10, i, j);
                    q_matrix[i][j] = new State(true, false, 10, i, j);
                    // q_matrix[i][j] = new State(false, false, 0, i, j);

                } else if ((i == 1 && j == 1) || (i == 1 && j == 3)) {
//					states[i][j] = new State(false, true, 0, i, j);
                    q_matrix[i][j] = new State(false, true, 0, i, j);

                } else if (i == 3 && j == 1) {
//					states[i][j] = new State(false, false, -50, i, j);
                    q_matrix[i][j] = new State(false, false, -50, i, j);

                } else {
//					states[i][j] = new State(false, false, -1, i, j);
                    q_matrix[i][j] = new State(false, false, reward, i, j);

                }
            }
        }

    }

    public void qLearning(double alpha, double eps, double gamma) {

        State currentState;
        State nextState;
        boolean reachedGoal = false;
        int episodes = 0;

        while (episodes < 5000) {

            currentState = q_matrix[0][0];
            reachedGoal = false;
            while (!reachedGoal) {

                if (shouldExplore(eps)) {
                    Action rndAction = currentState.getRandomAction();
                    nextState = updateQMatrix(currentState, rndAction, alpha,
                            gamma);

                } else {
                    Action maxValAction = currentState.getMaxValAction();
                    nextState = updateQMatrix(currentState, maxValAction,
                            alpha, gamma);
                }

                if (currentState.isGoalState) {
                    reachedGoal = true;
                }

                currentState = q_matrix[nextState.i][nextState.j];

            }

            episodes++;

            if (episodes % 10 == 0) {
                eps = ((1 - eps) / (2 - eps));
            }
        }
    }

    private boolean shouldExplore(double eps) {

        if (Math.random() > eps) {
            return false;
        } else
            return true;
    }

    private State updateQMatrix(State currentState, Action a, double alpha,
                                double gamma) {
//        System.out.println(a.direction);

        int i = currentState.i;
        int j = currentState.j;
        State nextState = null;
        if (a.direction == Directions.TOP) {

            nextState = ((i - 1) >= 0 && !q_matrix[i - 1][j].isBlockingState) ? q_matrix[i - 1][j]
                    : q_matrix[i][j];
            q_matrix[i][j].go_top.value += alpha
                    * (currentState.immediateReward + (gamma
                    * nextState.getMaxValAction().value) - q_matrix[i][j].go_top.value);

        } else if (a.direction == Directions.RIGHT) {

            nextState = ((j + 1) < this.col && !q_matrix[i][j + 1].isBlockingState) ? q_matrix[i][j + 1]
                    : q_matrix[i][j];
            q_matrix[i][j].go_right.value += alpha
                    * (currentState.immediateReward + (gamma
                    * nextState.getMaxValAction().value) - q_matrix[i][j].go_right.value);

        } else if (a.direction == Directions.BOTTOM) {

            nextState = ((i + 1) < this.row && !q_matrix[i + 1][j].isBlockingState) ? q_matrix[i + 1][j]
                    : q_matrix[i][j];
            q_matrix[i][j].go_bottom.value += alpha
                    * (currentState.immediateReward + (gamma
                    * nextState.getMaxValAction().value) - q_matrix[i][j].go_bottom.value);

        } else if (a.direction == Directions.LEFT) {

            nextState = ((j - 1) >= 0 && !q_matrix[i][j - 1].isBlockingState) ? q_matrix[i][j - 1]
                    : q_matrix[i][j];
            q_matrix[i][j].go_left.value += alpha
                    * (currentState.immediateReward + (gamma
                    * nextState.getMaxValAction().value) - q_matrix[i][j].go_left.value);
        }
//        printQMatrix();
        return nextState;
    }

    public void printDomain() {

        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {

                System.out.print(q_matrix[i][j].immediateReward + "\t");
            }

            System.out.println();
        }
    }

    public void printQMatrix() {

        DecimalFormat df = new DecimalFormat("##.##");
        System.out.println();
        for (int j = 0; j < 32 * 4; j++) {
            System.out.print("-");
        }
        System.out.println();
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {

                String stringValue = "" + df.format(q_matrix[this.row - i - 1][j].go_bottom.value);
                int addSpace = stringValue.length();

                System.out.print("\t\t\t");
                System.out.print(" " + stringValue);
                while (addSpace <= 8) {
                    System.out.print(" ");
                    addSpace++;
                }
                System.out.print("\t\t   |");
            }
            System.out.println();
            for (int j = 0; j < this.col; j++) {

                String stringValue = "" + df.format(q_matrix[this.row - i - 1][j].go_left.value);
                int addSpace = stringValue.length();

                System.out.print("\t" + stringValue);
                while (addSpace <= 8) {
                    System.out.print(" ");
                    addSpace++;
                }
                System.out.print("\t\t  ");

                stringValue = "" + df.format(q_matrix[this.row - i - 1][j].go_right.value);
                addSpace = stringValue.length();

                System.out.print(stringValue);
                while (addSpace <= 8) {
                    System.out.print(" ");
                    addSpace++;
                }
                System.out.print("|");
            }
            System.out.println();
            for (int j = 0; j < this.col; j++) {

                String stringValue = "" + df.format(q_matrix[this.row - i - 1][j].go_top.value);
                int addSpace = stringValue.length();

                System.out.print("\t\t\t");
                System.out.print(" "+stringValue);
                while (addSpace <= 8) {
                    System.out.print(" ");
                    addSpace++;
                }
                System.out.print("\t\t   |");
            }

            System.out.println();
            for (int j = 0; j < 32 * 4; j++) {
                System.out.print("-");
            }
            System.out.println();
        }

    }

    public void printPath() {

        int i = 0;
        int j = 0;

        State currentState = q_matrix[i][j];

        while (!currentState.isGoalState) {

            Action a = currentState.getMaxValAction();
            System.out.print(a.direction + " --> ");

            switch (a.direction) {

                case TOP:
                    currentState = q_matrix[i - 1][j];
                    break;
                case RIGHT:
                    currentState = q_matrix[i][j + 1];
                    break;
                case BOTTOM:
                    currentState = q_matrix[i + 1][j];
                    break;
                case LEFT:
                    currentState = q_matrix[i][j - 1];
                    break;

            }

        }

    }

}
