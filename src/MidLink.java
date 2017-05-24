import com.google.common.collect.BiMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MidLink extends Link<Link> {
	public void putNgram(LinkedList<Integer> ids) {
		int firstId = ids.pop();
		if (this.containsKey(firstId)) {
			this.get(firstId).putNgram(ids);
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












