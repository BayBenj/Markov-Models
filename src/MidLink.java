import com.google.common.collect.BiMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MidLink extends Link<Link> {
	public void putNgram(LinkedList<Integer> ids) {
		int firstId = ids.pop();
		if (this.containsKey(firstId)) {
			Link temp = this.get(firstId);
			temp.putNgram(ids);
		}
		else {
			Link temp = null;
			if (ids.size() == 1) {
				temp = new EndLink();
			}
			else if (ids.size() > 1) {
				temp = new MidLink();
			}
			temp.putNgram(ids);
			this.put(firstId, temp);
		}
	}

	public int count() {
		int result = 0;
		for (Map.Entry<Integer,?> entry : this.entrySet()) {
			Link temp;
			if (entry.getValue() instanceof MidLink) {
				temp = (MidLink) entry.getValue();
				result += temp.count();
			}
			else {
				temp = (EndLink) entry.getValue();
				result += temp.count();
			}
		}
		return result;
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
