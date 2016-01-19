package marshalp.qlearning.com;

public class Action {

	public enum Directions {
		TOP, RIGHT, BOTTOM, LEFT
	};


	Directions direction;
	double value;
	

	public Action(Directions d) {
		this.direction = d;
		this.value = 0.0;
		
	}
	
	
	
}
