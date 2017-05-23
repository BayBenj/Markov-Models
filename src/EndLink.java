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
		return 1;
	}

	public int randomNgram(double cumulativeTotal, double rndTarget, BiMap<Integer, Object> tokens, List<String> words) {
		for (Map.Entry<Integer,Double> entry : this.entrySet()) {
			cumulativeTotal += entry.getKey();
			if (cumulativeTotal > rndTarget) {
				words.add((String) (tokens.get(entry.getKey())));
				break;
			}
		}
	}

}
