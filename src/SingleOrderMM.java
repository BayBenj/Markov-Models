import com.google.common.collect.BiMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleOrderMM extends MarkovModel {

	private Map<Integer,Double> frequencies;//token frequency

	public SingleOrderMM(BiMap<Integer, Object> states, List<String> words) {
		super(1, states, null);
		this.setFrequencies(determineFrequencies(states, words));
	}

	public static Map<Integer,Double> determineFrequencies(BiMap<Integer, Object> states, List<String> words) {
		return null;
	}

	public String generateChain(int n) {
		List<String> words = new ArrayList<>();
		//start with an n-gram of order - 1 words
		double total = 0;
		for (Map.Entry<Integer,Double> entry : frequencies.entrySet()) {
			total += entry.getValue();
		}
		for (int i = 0; i < n; i++) {
			double rnd = Driver.r.nextInt((int)total);
			double cumulativeTotal = 0;
			for (Map.Entry<Integer,Double> entry : frequencies.entrySet()) {
				cumulativeTotal += entry.getValue();
				if (cumulativeTotal > rnd) {
					words.add((String)(this.getTokens().get(entry.getKey())));
					break;
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for (String s : words) {
			sb.append(s);
			sb.append(" ");
		}
		return sb.toString();
	}

	protected Map<Integer, Double> getFrequencies() {
		return frequencies;
	}

	protected void setFrequencies(Map<Integer, Double> frequencies) {
		this.frequencies = frequencies;
	}

}
