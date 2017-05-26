import com.google.common.collect.BiMap;
import javafx.util.Pair;

import java.util.*;

public class VariableOrderMM extends MarkovModel {

	private MidLink transitions;
	private Link priors;
	private List<String> startTokens;

	public VariableOrderMM(int order, BiMap<Integer, Object> tokens, List<Pair<String[], Double>> nGrams, List<String> words) {
		super(order, tokens, nGrams);
//		this.setPriors(determinePriors(words, tokens, order));
		this.setTransitions(determineTransitions(nGrams, tokens));
		this.setStartTokens(this.getOrder());
	}

	public static MidLink determineTransitions(List<Pair<String[], Double>> nGrams, BiMap<Integer,Object> tokensAndIds) {
		MidLink result = new MidLink();

		Pair<String[],Double> nGram;
		for (int i = 0; i < nGrams.size(); i++) {
			nGram = nGrams.get(i);
			LinkedList<Pair<Integer,Double>> ids = new LinkedList<>();
			for (int j = 0; j < nGram.getKey().length; j++) {
				Pair<Integer,Double> tempIdAndWeight = new Pair<>(tokensAndIds.inverse().get(nGram.getKey()[j]), nGram.getValue());
				ids.add(tempIdAndWeight);
			}
			result.putNgram(ids);
		}
		return result;
	}

//	public static Link determinePriors(List<String> words, BiMap<Integer,Object> tokensAndIds, int order) {
//		Link endLink = new EndLink();
//		endLink.put(tokensAndIds.inverse().get(words.get(order - 1)), 1.0);
//		Link lastMidLink = endLink;
//		Link midLink = new MidLink();
//		for (int j = 1; j < order; j++) {
//			midLink.put(tokensAndIds.inverse().get(words.get(order - 1 - j)), lastMidLink);
//			lastMidLink = midLink;
//			midLink = new MidLink();
//		}
//		return lastMidLink;
//	}

	public String generateChain(int n) {
		//start with an n-gram of order - 1 words
		double total = this.getPriors().count();
		double rndTarget = Driver.r.nextDouble() * total;
		double cumulativeTotal = 0.0;
		List<String> chain = new ArrayList<>();
		chain.addAll(this.getStartTokens());

		for (int i = 0; i < n - (this.getOrder() - 1); i++) {
			LinkedList<Integer> ids = new LinkedList<>();
//			System.out.print("IDS: ");
			for (int j = 0; j < this.getOrder() - 1; j++) {
				int tempId = this.getTokens().inverse().get(chain.get(i + j));
				ids.add(tempId);
//				System.out.print(tempId + " ");
			}
//			System.out.print("\n");
			Map<Integer,Double> transitions = this.getTransitions().getEndLink(ids);
//			if (transitions == null) {
//				System.out.println("Pause for error!!!!");
//			}
			cumulativeTotal = 0;
			total = 0;
			for (Map.Entry<Integer,Double> entry : transitions.entrySet()) {
				total += entry.getValue();
			}
			rndTarget = Driver.r.nextDouble() * total;

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

	public List<String> getStartTokens() {
		return startTokens;
	}

	public void setStartTokens(int order) {
		List<String> starts = new ArrayList<>();
		for (int i = 0; i < order - 1; i++) {
			starts.add("<s>");
		}
		this.startTokens = starts;
	}
}
