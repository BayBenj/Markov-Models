import com.google.common.collect.BiMap;

import java.util.*;

public class VariableOrderMM extends MarkovModel {

	private Link transitions;
	private Link priors;

	public VariableOrderMM(int order, BiMap<Integer, Object> tokens, List<List<String>> nGrams, List<String> words) {
		super(order, tokens, nGrams);
		this.setPriors(determinePriors(words, tokens, order));
		this.setTransitions(determineTransitions(nGrams, tokens, order));
	}

	public static Link determineTransitions(List<List<String>> nGrams, BiMap<Integer,Object> tokensAndIds, int order) {
		Link result = new MidLink();

		List<String> nGram;
		for (int i = 0; i < nGrams.size(); i++) {
			nGram = nGrams.get(i);
			LinkedList<Integer> ids = new LinkedList<>();
			for (int j = 0; j < order; j++) {//TODO: if the nGram is not smaller than the order...
				int tempId = tokensAndIds.inverse().get(nGram.get(j));
				ids.add(tempId);
			}
			result.putNgram(ids);
		}
		return result;
	}

//	public static Link determineTransitions(List<List<String>> nGrams, BiMap<Integer,Object> tokensAndIds, int order) {
//		final double ONE = 1.0;
//
//		Map<Integer,Map<Integer,Map<Integer,Double>>> result = new HashMap<>();
//
//		List<String> nGram;
//		for (int i = 0; i < nGrams.size(); i++) {
//			nGram = nGrams.get(i);
//			for (int j = 0; j < order; j++) {
//				int tempId = tokensAndIds.inverse().get(nGram.get(j));
//				if (result.containsKey(tempId)) {
//
//				}
//			}
//			int id1 = tokensAndIds.inverse().get(nGram.get(0));
//			int id2 = tokensAndIds.inverse().get(nGram.get(1));
//			int id3 = tokensAndIds.inverse().get(nGram.get(2));
//			if (result.containsKey(id1)) {
//				if (result.get(id1).containsKey(id2)) {
//					if (result.get(id1).get(id2).containsKey(id3)) {
//						result.get(id1).get(id2).put(id3, result.get(id1).get(id2).get(id3) + ONE);
//					}
//					else {
//						result.get(id1).get(id2).put(id3, ONE);
//					}
//				}
//				else {
//					Map<Integer,Double> map3 = new HashMap<>();
//					map3.put(id3, ONE);
//					Map<Integer,Map<Integer,Double>> map2 = result.get(id1);
//					map2.put(id2, map3);
//					result.put(id1, map2);
//				}
//			}
//			else {
//				Map<Integer,Double> map3 = new HashMap<>();
//				map3.put(id3, ONE);
//				Map<Integer,Map<Integer,Double>> map2 = new HashMap<>();
//				map2.put(id2, map3);
//				result.put(id1, map2);
//			}
//		}
//		return result;
//	}

	public static Link determinePriors(List<String> words, BiMap<Integer,Object> tokensAndIds, int order) {
		Link endLink = new EndLink();
		endLink.put(tokensAndIds.inverse().get(words.get(1)), 1.0);
		Map<Integer,Object> lastMidLink = endLink;
		Link midLink = new MidLink();
		for (int j = 0; j < order; j++) {
			midLink.put(tokensAndIds.inverse().get(words.get(order - j)), lastMidLink);
			lastMidLink = midLink;
		}
		return midLink;
	}

	public String generateChain(int n) {
		List<String> words = new ArrayList<>();
		//start with an n-gram of order - 1 words
		double total = this.getPriors().count();
		double rnd = Driver.r.nextInt((int)total);
		double cumulativeTotal = 0;
		for (Map.Entry<Integer,Map<Integer,Double>> entry : this.getPriors().entrySet()) {
			for (Map.Entry<Integer,Double> entry2 : entry.getValue().entrySet()) {
				cumulativeTotal += entry2.getValue();
				if (cumulativeTotal > rnd) {
					words.add((String) (this.getTokens().get(entry.getKey())));
					break;
				}
			}
		}

		for (int i = 0; i < n - (this.getOrder() - 1); i++) {
			int i1 = this.getTokens().inverse().get(words.get(0 + i));
			int i2 = this.getTokens().inverse().get(words.get(1 + i));
			Map<Integer,Double> transitions = this.getTransitions().get(i1).get(i2);
			cumulativeTotal = 0;
			total = 0;
			for (Map.Entry<Integer,Double> entry : transitions.entrySet()) {
				total += entry.getValue();
			}
			rnd = Driver.r.nextInt((int)total);

			for (Map.Entry<Integer,Double> entry : transitions.entrySet()) {
				cumulativeTotal += entry.getValue();
				if (cumulativeTotal > rnd) {
					words.add((String)(this.getTokens().get(entry.getKey())));
					break;
				}
			}
		}
		return chainToString(words);
	}

	public Link getTransitions() {
		return transitions;
	}

	public void setTransitions(Link transitions) {
		this.transitions = transitions;
	}

	public Link getPriors() {
		return priors;
	}

	public void setPriors(Link priors) {
		this.priors = priors;
	}
}


