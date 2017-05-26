import com.google.common.collect.BiMap;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EndLink extends Link<Double> {
	public void putNgram(LinkedList<Pair<Integer,Double>> idsAndWeights) {
		Pair<Integer,Double> first = idsAndWeights.pop();
//		try {
			int firstId = first.getKey();
			double firstWeight = first.getValue();
			if (this.containsKey(firstId)) {
				this.put(firstId, this.get(firstId) + firstWeight);
			}
			else {
				this.put(firstId, firstWeight);
			}
//		}
//		catch (NullPointerException e) {
//			e.printStackTrace();
//		}
	}

	public double count() {
		double total = 0;
		for (Map.Entry<Integer,Double> entry : this.entrySet()) {
			total += entry.getValue();
		}
		return total;
	}

	public List<String> randomNgram(Double cumulativeTotal, double rndTarget, BiMap<Integer, Object> tokens, List<String> chain) {
		for (Map.Entry<Integer,Double> entry : this.entrySet()) {
			cumulativeTotal += entry.getKey();
			if (cumulativeTotal > rndTarget) {
				chain.add((String) (tokens.get(entry.getKey())));
				break;
			}
		}
		return chain;
	}

//	public List<String> getChain(int lastId, BiMap<Integer, Object> tokens) {
//		List<String> chain = new ArrayList<>();
//		chain.add((String)tokens.get(this.get(lastId)));
//
//	}

}
