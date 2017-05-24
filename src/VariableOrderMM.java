import com.google.common.collect.BiMap;

import java.util.*;

public class VariableOrderMM extends MarkovModel {

	private MidLink transitions;
	private Link priors;

	public VariableOrderMM(int order, BiMap<Integer, Object> tokens, List<List<String>> nGrams, List<String> words) {
		super(order, tokens, nGrams);
		this.setPriors(determinePriors(words, tokens, order));
		this.setTransitions(determineTransitions(nGrams, tokens, order));
	}

	public static MidLink determineTransitions(List<List<String>> nGrams, BiMap<Integer,Object> tokensAndIds, int order) {
		MidLink result = new MidLink();

		List<String> nGram;
		for (int i = 0; i < nGrams.size(); i++) {
			nGram = nGrams.get(i);
			LinkedList<Integer> ids = new LinkedList<>();
			for (int j = 0; j < nGram.size(); j++) {
				int tempId = tokensAndIds.inverse().get(nGram.get(j));
				ids.add(tempId);
			}
			result.putNgram(ids);
		}
		return result;
	}

	public static Link determinePriors(List<String> words, BiMap<Integer,Object> tokensAndIds, int order) {
		Link endLink = new EndLink();
		endLink.put(tokensAndIds.inverse().get(words.get(order - 1)), 1.0);
		Link lastMidLink = endLink;
		Link midLink = new MidLink();
		for (int j = 1; j < order; j++) {
			midLink.put(tokensAndIds.inverse().get(words.get(order - 1 - j)), lastMidLink);
			lastMidLink = midLink;
			midLink = new MidLink();
		}
		return lastMidLink;
	}

	public String generateChain(int n) {
		//start with an n-gram of order - 1 words
		double total = this.getPriors().count();
		double rndTarget = Driver.r.nextInt((int)total);
		double cumulativeTotal = 0.0;
		List<String> chain = this.getPriors().randomNgram(cumulativeTotal, rndTarget, getTokens(), new ArrayList<>());

		for (int i = 0; i < n - (this.getOrder() - 1); i++) {
			LinkedList<Integer> ids = new LinkedList<>();
			for (int j = 0; j < this.getOrder() - 1; j++) {
				int tempId = this.getTokens().inverse().get(chain.get(i + j));
				ids.add(tempId);
			}
			Map<Integer,Double> transitions = this.getTransitions().getEndLink(ids);
			cumulativeTotal = 0;
			total = 0;
			for (Map.Entry<Integer,Double> entry : transitions.entrySet()) {
				total += entry.getValue();
			}
			rndTarget = Driver.r.nextInt((int)total);

			for (Map.Entry<Integer,Double> entry : transitions.entrySet()) {
				cumulativeTotal += entry.getValue();
				if (cumulativeTotal > rndTarget) {
					chain.add((String)(this.getTokens().get(entry.getKey())));
					break;
				}
			}
		}
		return chainToString(chain);
	}

	public MidLink getTransitions() {
		return transitions;
	}

	public void setTransitions(MidLink transitions) {
		this.transitions = transitions;
	}

	public Link getPriors() {
		return priors;
	}

	public void setPriors(Link priors) {
		this.priors = priors;
	}
}







