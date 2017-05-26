import com.google.common.collect.BiMap;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MidLink extends Link<Link> {
	public void putNgram(LinkedList<Pair<Integer,Double>> idsAndWeights) {
		try {
			int firstId = idsAndWeights.pop().getKey();
			if (this.containsKey(firstId)) {
				this.get(firstId).putNgram(idsAndWeights);
			} else {
				Link temp = null;
				if (idsAndWeights.size() == 1) {
					temp = new EndLink();
				} else if (idsAndWeights.size() > 1) {
					temp = new MidLink();
				}
				temp.putNgram(idsAndWeights);
				this.put(firstId, temp);
			}
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public double count() {
		int result = 0;
		for (Map.Entry<Integer,Link> entry : this.entrySet()) {
			Link temp = entry.getValue();
//			if (entry.getValue() instanceof MidLink) {
//				temp = (MidLink) entry.getValue();
//			}
//			else if (entry.getValue() instanceof EndLink) {
//				temp = (EndLink) entry.getValue();
//			}
			result += temp.count();
		}
		return result;
	}

	public List<String> randomNgram(Double cumulativeTotal, double rndTarget, BiMap<Integer, Object> tokens, List<String> chain) {
		for (Map.Entry<Integer,Link> entry : this.entrySet()) {
			String string = (String) tokens.get(entry.getKey());
			if (entry.getValue() instanceof MidLink) {
				chain = ((MidLink) entry.getValue()).randomNgram(cumulativeTotal, rndTarget, tokens, chain);
				if (!chain.isEmpty()) {
					chain.add(0, string);
					break;
				}
			}
			else {
				EndLink d = (EndLink) entry.getValue();
				cumulativeTotal += d.count();
				if (cumulativeTotal > rndTarget) {
					chain.add(0, string);
				}
			}

		}
		return chain;
	}

	public EndLink getEndLink(LinkedList<Integer> ids) {
		int tempId = ids.pop();
		if (this.get(tempId) instanceof  MidLink) {
			return ((MidLink)this.get(tempId)).getEndLink(ids);
		}
		else {
			return (EndLink) this.get(tempId);
		}
	}

}















