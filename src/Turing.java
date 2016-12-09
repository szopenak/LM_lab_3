import java.util.ArrayList;
import java.util.List;

public class Turing {
	private List<State> states = new ArrayList<State>();
	private State actualState = null;
	public Turing() {

		states.add(new State("Q2,1,P","Q1,0,L","-,-,-","Q0"));
		states.add(new State("Q0,-,-","Q0,-,-","Q2,1,P","Q1"));
		states.add(new State("Q2,-,P","Q2,-,P","Q3,-,L","Q2"));
		states.add(new State("Q4,-,L","Q4,-,L","Q4,-,L","Q3"));
		states.add(new State("Q5,1,-","Q4,0,L","Q5,1,-","Q4"));
		states.add(new State("Q5,-,-","Q5,-,-","Q5,-,-","Q5"));
		actualState = states.get(0);
	}
	String getActualState() {
		return actualState.getName();
	}
	
	String Next(String s) {
		String  response = actualState.Next(s);	
		actualState = states.get(Integer.valueOf(response.substring(1, 2)));
		return response;
	}
}

