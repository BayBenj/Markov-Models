import com.google.common.collect.BiMap;

import java.util.List;

public abstract class MarkovModel {

	private int order;
	private BiMap<Integer,Object> tokens;//all possible tokens and their id
	private List<List<String>> nGrams;

	public MarkovModel(int order, BiMap<Integer, Object> tokens, List<List<String>> nGrams) {
		this.setOrder(order);
		this.setTokens(tokens);
		this.setnGrams(nGrams);
	}

	protected abstract String generateChain(int n);

	protected String chainToString(List<String> words) {
		StringBuilder sb = new StringBuilder();
		for (String s : words) {
			sb.append(s);
			sb.append(" ");
		}
		return sb.toString();
	}

	protected BiMap<Integer, Object> getTokens() {
		return tokens;
	}

	protected void setTokens(BiMap<Integer, Object> tokens) {
		this.tokens = tokens;
	}

	protected int getOrder() {
		return order;
	}

	protected void setOrder(int order) {
		this.order = order;
	}

	public List<List<String>> getnGrams() {
		return nGrams;
	}

	public void setnGrams(List<List<String>> nGrams) {
		this.nGrams = nGrams;
	}
}
