import java.util.HashMap;
import java.util.Map;

public class State {
	private String name;
	private Map<String, String> values = new HashMap<String, String>();
	public State(String zero, String one, String none, String name) {
		values.put("0", zero);
		values.put("1", one);
		values.put("#", none);
		this.name = name;
	}
	public String Next(String s) {
		if (values.containsKey(s)) {
			return values.get(s);
		}
		else return null;
	}
	public String getName() {
		return name;
	}
}
