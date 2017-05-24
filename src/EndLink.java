import com.google.common.collect.BiMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EndLink extends Link<Double> {
	public void putNgram(LinkedList<Integer> ids) {
		int firstId = ids.pop();
		if (this.containsKey(firstId)) {
			this.put(firstId, this.get(firstId) + 1.0);
		}
		else {
			this.put(firstId, 1.0);
		}
	}

	public int count() {
		return this.size();
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
