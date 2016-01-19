package marshalp.qlearning.com;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import marshalp.qlearning.com.Action.Directions;

public class State {

	Action go_top;
	Action go_right;
	Action go_bottom;
	Action go_left;

	boolean isGoalState;
	boolean isBlockingState;

	double immediateReward;
	
	int i;
	int j;

	private static final List<Directions> randomAction = Collections
			.unmodifiableList(Arrays.asList(Directions.values()));
	private static final Random rnd = new Random();
	private static final int size = randomAction.size();

	public State(boolean gs, boolean bs, double ir,int x,int y) {

		this.go_top = new Action(Directions.TOP);
		this.go_right = new Action(Directions.RIGHT);
		this.go_bottom = new Action(Directions.BOTTOM);
		this.go_left = new Action(Directions.LEFT);
		this.isGoalState = gs;
		this.isBlockingState = bs;
		this.immediateReward = ir;
		this.i=x;
		this.j=y;

	}
	
	public Action getMaxValAction(){
		
		
		if(go_top.value == go_right.value && go_top.value == go_bottom.value && go_top.value == go_left.value){
			
			return getRandomAction();
		}
		
		/*if(go_top.value > go_right.value && go_top.value > go_bottom.value && go_top.value > go_left.value)
			return go_top;
		else if(go_right.value > go_top.value && go_right.value > go_bottom.value && go_right.value > go_left.value)
			return go_top;
		else if(go_bottom.value > go_right.value && go_top.value < go_bottom.value && go_bottom.value > go_left.value)
			return go_top;
		else
			return go_left;*/

		return max(go_top,max(go_right,max(go_bottom,go_left)));
		
	}

	private Action max(Action a1, Action a2){

		return a1.value>a2.value?a1:a2;
	}

	public Action getRandomAction() {

		Directions d = randomAction.get(rnd.nextInt(size));
		Action action = null;

		switch (d) {
		case TOP:
			action = go_top;
			break;

		case RIGHT:
			action = go_right;
			break;

		case BOTTOM:
			action = go_bottom;
			break;

		case LEFT:
			action = go_left;
			break;
		}

		return action;

	}

}
