//import com.google.common.collect.BiMap;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class MM4Gram extends VariableOrderMM {
//
//	private Map<Integer,Map<Integer,Map<Integer,Double>>> priors;//p of starting with each <token><token>
//	private Map<Integer,Map<Integer,Map<Integer,Map<Integer,Double>>>> transitions;//<token><token> to <token> transition p
//
//	public MM4Gram(BiMap<Integer,Object> states, Map<Integer,Map<Integer,Map<Integer,Double>>> priors, Map<Integer,Map<Integer,Map<Integer,Map<Integer,Double>>>> transitions) {
//		super(4, states);
//		this.setPriors(priors);
//		this.setTransitions(transitions);
//	}
//
//	public static Map<Integer,Map<Integer,?>> determineTransitions(List<List<String>> _4grams, BiMap<Integer,Object> tokensAndIds) {
//		final double ONE = 1.0;
//
//		Map<Integer,Map<Integer,Map<Integer,Map<Integer,Double>>>> result = new HashMap<>();
//
//		List<String> _4gram;
//		for (int i = 0; i < _4grams.size(); i++) {
//			_4gram = _4grams.get(i);
//			int id1 = tokensAndIds.inverse().get(_4gram.get(0));
//			int id2 = tokensAndIds.inverse().get(_4gram.get(1));
//			int id3 = tokensAndIds.inverse().get(_4gram.get(2));
//			int id4 = tokensAndIds.inverse().get(_4gram.get(3));
//			if (result.containsKey(id1)) {
//				if (result.get(id1).containsKey(id2)) {
//					if (result.get(id1).get(id2).containsKey(id3)) {
//						if (result.get(id1).get(id2).get(id3).containsKey(id4)) {
//							result.get(id1).get(id2).get(id3).put(id4, result.get(id1).get(id2).get(id3).get(id4) + ONE);
//						}
//						else {
//							Map<Integer,Double> map4 = new HashMap<>();
//							map4.put(id4, ONE);
//							result.get(id1).get(id2).get(id3).put(id4, ONE);
//						}
//					}
//					else {
//						Map<Integer,Double> map4 = new HashMap<>();
//						map4.put(id4, ONE);
//						Map<Integer,Map<Integer,Double>> map3 = new HashMap<>();
//						map3.put(id3, map4);
//						Map<Integer,Map<Integer,Map<Integer,Double>>> map2 = new HashMap<>();
//						map2.put(id2, map3);
//						result.put(id1, map2);
//					}
//				}
//				else {
//					Map<Integer,Double> map5 = new HashMap<>();
//					map5.put(id4, ONE);
//					Map<Integer,Map<Integer,Double>> map4 = new HashMap<>();
//					map4.put(id3, map5);
//					Map<Integer,Map<Integer,Map<Integer,Double>>> map3 = new HashMap<>();
//					map3.put(id2, map4);
//					Map<Integer,Map<Integer,Map<Integer,Map<Integer,Double>>>> map2 = new HashMap<>();
//					map2.put(id1, map3);
//					result.put(id1, map3);
//				}
//			}
//			else {
//				Map<Integer,Double> map4 = new HashMap<>();
//				map4.put(id4, ONE);
//				Map<Integer,Map<Integer,Double>> map2 = new HashMap<>();
//				map2.put(id2, map4);
//				result.put(id1, map2);
//			}
//		}
//		return result;
//	}
//
//	public String generateChain(int n) {
//		List<String> words = new ArrayList<>();
//		//start with an n-gram of order - 1 words
//		double total = 0;
//		for (Map.Entry<Integer,Map<Integer,Map<Integer,Double>>> entry : this.getPriors().entrySet()) {
//			for (Map.Entry<Integer,Map<Integer,Double>> entry2 : entry.getValue().entrySet()) {
//				for (Map.Entry<Integer,Double> entry4 : entry2.getValue().entrySet()) {
//					total += entry4.getValue();
//				}
//			}
//		}
//		double rnd = Driver.r.nextInt((int)total);
//		double cumulativeTotal = 0;
//		for (Map.Entry<Integer,Map<Integer,Map<Integer,Double>>> entry : this.getPriors().entrySet()) {
//			for (Map.Entry<Integer,Map<Integer,Double>> entry2 : entry.getValue().entrySet()) {
//				for (Map.Entry<Integer,Double> entry4 : entry2.getValue().entrySet()) {
//					cumulativeTotal += entry4.getValue();
//					if (cumulativeTotal > rnd) {
//						words.add((String) (this.getTokens().get(entry.getKey())));
//						break;
//					}
//				}
//			}
//		}
//
//		for (int i = 0; i < n - (this.getOrder() - 1); i++) {
//			int i1 = this.getTokens().inverse().get(words.get(0 + i));
//			int i2 = this.getTokens().inverse().get(words.get(1 + i));
//			int i4 = this.getTokens().inverse().get(words.get(2 + i));
//			Map<Integer,Double> transitions = this.getTransitions().get(i1).get(i2).get(i4);
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
//	public Map<Integer, Map<Integer, Map<Integer,Double>>> getPriors() {
//		return priors;
//	}
//
//	public void setPriors(Map<Integer, Map<Integer, Map<Integer,Double>>> priors) {
//		this.priors = priors;
//	}
//
//	public Map<Integer, Map<Integer, Map<Integer, Map<Integer,Double>>>> getTransitions() {
//		return transitions;
//	}
//
//	public void setTransitions(Map<Integer, Map<Integer, Map<Integer, Map<Integer,Double>>>> transitions) {
//		this.transitions = transitions;
//	}
//}
