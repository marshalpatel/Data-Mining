package marshalp.qlearning.com;

public class QLearningMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Double reward = Double.parseDouble(args[0]);

		Domain domain = new Domain(5, 4, reward);

		//domain.printDomain();
		
		domain.qLearning(0.1, 0.5, 0.9);
		domain.printQMatrix();

		//domain.printPath();
		

	}

}
