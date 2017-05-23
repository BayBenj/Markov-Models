//import com.google.common.collect.BiMap;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class MM5Gram extends VariableOrderMM {
//
//	private Map<Integer,Map<Integer,Map<Integer,Map<Integer,Double>>>> priors;//p of starting with each <token><token>
//	private Map<Integer,Map<Integer,Map<Integer,Map<Integer,Map<Integer,Double>>>>> transitions;//<token><token> to <token> transition p
//
//	public MM5Gram(BiMap<Integer,Object> states, Map<Integer,Map<Integer,Map<Integer,Map<Integer,Double>>>> priors, Map<Integer,Map<Integer,Map<Integer,Map<Integer,Map<Integer,Double>>>>> transitions) {
//		super(5, states);
//		this.setPriors(priors);
//		this.setTransitions(transitions);
//	}
//
//	public String generateChain(int n) {
//		List<String> words = new ArrayList<>();
//		//start with an n-gram of order - 1 words
//		double total = 0;
//		for (Map.Entry<Integer,Map<Integer,Map<Integer,Map<Integer,Double>>>> entry : this.getPriors().entrySet()) {
//			for (Map.Entry<Integer,Map<Integer,Map<Integer,Double>>> entry2 : entry.getValue().entrySet()) {
//				for (Map.Entry<Integer,Map<Integer,Double>> entry3 : entry2.getValue().entrySet()) {
//					for (Map.Entry<Integer,Double> entry4 : entry3.getValue().entrySet()) {
//						total += entry4.getValue();
//					}
//				}
//			}
//		}
//		double rnd = Driver.r.nextInt((int)total);
//		double cumulativeTotal = 0;
//		for (Map.Entry<Integer,Map<Integer,Map<Integer,Map<Integer,Double>>>> entry : this.getPriors().entrySet()) {
//			for (Map.Entry<Integer,Map<Integer,Map<Integer,Double>>> entry2 : entry.getValue().entrySet()) {
//				for (Map.Entry<Integer,Map<Integer,Double>> entry3 : entry2.getValue().entrySet()) {
//					for (Map.Entry<Integer,Double> entry4 : entry3.getValue().entrySet()) {
//						cumulativeTotal += entry4.getValue();
//						if (cumulativeTotal > rnd) {
//							words.add((String) (this.getTokens().get(entry.getKey())));
//							break;
//						}
//					}
//				}
//			}
//		}
//
//		for (int i = 0; i < n - (this.getOrder() - 1); i++) {
//			int i1 = this.getTokens().inverse().get(words.get(0 + i));
//			int i2 = this.getTokens().inverse().get(words.get(1 + i));
//			int i3 = this.getTokens().inverse().get(words.get(2 + i));
//			int i4 = this.getTokens().inverse().get(words.get(3 + i));
//			int i5 = this.getTokens().inverse().get(words.get(4 + i));
//			Map<Integer,Double> transitions = this.getTransitions().get(i1).get(i2).get(i3).get(i4);
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
//	public Map<Integer, Map<Integer, Map<Integer,Map<Integer,Double>>>> getPriors() {
//		return priors;
//	}
//
//	public void setPriors(Map<Integer, Map<Integer, Map<Integer,Map<Integer,Double>>>> priors) {
//		this.priors = priors;
//	}
//
//	public Map<Integer, Map<Integer, Map<Integer, Map<Integer,Map<Integer,Double>>>>> getTransitions() {
//		return transitions;
//	}
//
//	public void setTransitions(Map<Integer, Map<Integer, Map<Integer, Map<Integer,Map<Integer,Double>>>>> transitions) {
//		this.transitions = transitions;
//	}
//}
