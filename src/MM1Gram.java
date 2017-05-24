//import com.google.common.collect.BiMap;
//
//import java.util.*;
//
//public class MM1Gram extends SingleOrderMM {
//
//	public MM1Gram(BiMap<Integer, Object> states, Map<Integer,Double> frequencies) {
//		super(states, frequencies);
//	}
//
//	public static Map<Integer,Double> determineFrequencies(List<List<String>> _1grams, Set<Object> tokens) {
//		double pStartEven = 1.0 / _1grams.size();
//		Map<Integer,Double> priors = new HashMap<>();
//		for (int i = 0; i < tokens.size(); i++) {
//			priors.put(i,pStartEven);
//		}
//		return priors;
//	}
//
//	public String generateChain(int n) {
//		List<String> words = new ArrayList<>();
//		//start with an n-gram of order - 1 words
//		double total = 0;
//		for (Map.Entry<Integer,Double> entry : this.getFrequencies().entrySet()) {
//			total += entry.getValue();
//		}
//		for (int i = 0; i < n; i++) {
//			double rnd = Driver.r.nextInt((int)total);
//			double cumulativeTotal = 0;
//			for (Map.Entry<Integer,Double> entry : this.getFrequencies().entrySet()) {
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
//}
