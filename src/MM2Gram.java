//import com.google.common.collect.BiMap;
//
//import java.util.*;
//
//public class MM2Gram extends VariableOrderMM {
//
//	private Map<Integer,Double> priors;//p of starting with each <token>
//	private Map<Integer,Map<Integer,Double>> transitions;//<token> to <token> transition p
//
//	public MM2Gram(BiMap<Integer,Object> states, Map<Integer,Double> priors, Map<Integer,Map<Integer,Double>> transitions) {
//		super(2, states);
//		this.setPriors(priors);
//		this.setTransitions(transitions);
//	}
//
//	public static Map<Integer,Map<Integer,Double>> determineTransitions(List<List<String>> _2grams, BiMap<Integer,Object> tokensAndIds) {
//		final double ONE = 1.0;
//
//		Map<Integer,Map<Integer,Double>> result = new HashMap<>();
//
//		List<String> _2gram;
//		for (int i = 0; i < _2grams.size(); i++) {
//			_2gram = _2grams.get(i);
//			int id1 = tokensAndIds.inverse().get(_2gram.get(0));
//			int id2 = tokensAndIds.inverse().get(_2gram.get(1));
//			if (result.containsKey(id1)) {
//				if (result.get(id1).containsKey(id2)) {
//					result.get(id1).get(id2);
//				}
//				else {
//					Map<Integer,Double> map = result.get(id1);
//					map.put(id2, ONE);
//					result.put(id1, map);
//				}
//			}
//			else {
//				Map<Integer,Double> map = new HashMap<>();
//				map.put(id2, ONE);
//				result.put(id1, map);
//			}
//		}
//		return result;
//	}
//
//	public String generateChain(int n) {
//		List<String> words = new ArrayList<>();
//		//start with an n-gram of order - 1 words
//		double total = 0;
//		for (Map.Entry<Integer,Double> entry : this.getPriors().entrySet()) {
//			total += entry.getValue();
//		}
//		double rnd = Driver.r.nextInt((int)total);
//		double cumulativeTotal = 0;
//		for (Map.Entry<Integer,Double> entry : this.getPriors().entrySet()) {
//			cumulativeTotal += entry.getValue();
//			if (cumulativeTotal > rnd) {
//				words.add((String)(this.getTokens().get(entry.getKey())));
//				break;
//			}
//		}
//
//		for (int i = 0; i < n - (this.getOrder() - 1); i++) {
//			int i1 = this.getTokens().inverse().get(words.get(0 + i));
//			Map<Integer,Double> transitions = this.getTransitions().get(i1);
//			cumulativeTotal = 0;
//			total = 0;
//			for (Map.Entry<Integer,Double> entry : transitions.entrySet()) {
//				total += entry.getValue();
//			}
//			rnd = Driver.r.nextInt((int)total);
//
//			for (Map.Entry<Integer,Double> entry : transitions.entrySet()) {
//				cumulativeTotal += entry.getValue();
//				if (cumulativeTotal > rnd) {
//					words.add((String)(this.getTokens().get(entry.getKey())));
//					break;
//				}
//			}
//		}
//		return chainToString(words);
//	}
//
//	public Map<Integer, Double> getPriors() {
//		return priors;
//	}
//
//	public void setPriors(Map<Integer, Double> priors) {
//		this.priors = priors;
//	}
//
//	public Map<Integer, Map<Integer, Double>> getTransitions() {
//		return transitions;
//	}
//
//	public void setTransitions(Map<Integer, Map<Integer, Double>> transitions) {
//		this.transitions = transitions;
//	}
//}
