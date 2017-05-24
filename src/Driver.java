import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.*;
import java.util.*;

public class Driver {

	public final static Random r = new Random();

	public static void main(String[] args) throws IOException {
		String text = "";
		TextDivisor delimiter = null;
		int n = 0;
		int chainLen = 0;
		for (int i = 0; i < args.length; i += 2) {
			String next = args[i + 1];
			switch (args[i]) {
				case "-t":
					File file = new File(next);
					if (file.isDirectory()) {
						StringBuilder sb = new StringBuilder();
						File[] listOfFiles = file.listFiles();
						for (File tempText : listOfFiles) {
							sb.append(readFileToString(tempText.toString()));
						}
						text = sb.toString();
					}
					else {
						text = readFileToString(next);
					}
					break;
				case "-d":
					switch(next) {
						case "sentences":
							delimiter = TextDivisor.SENTENCE_PUNCT;
							break;
					}
					break;
				case "-n":
					n = Integer.parseInt(next);
					break;
				case "-c":
					chainLen = Integer.parseInt(next);
					break;
				default:
					printUsage();
					System.exit(0);
			}
		}

		System.out.println("Building Markov model");
		MarkovModel mm = getNGramModel(n, text);

		System.out.println("Generating Markov chain");
		String chain = mm.generateChain(chainLen);
		System.out.println(chain);

		String[] sentences = chain.split("[.!?;:] ");
		for (String s : sentences) {
			System.out.println(s);
		}
	}

	public static void printUsage() {
		System.out.println("Usage: -t [text] -d [divisor] -n [order] -c [chain length]");
	}

	public static MarkovModel getNGramModel(int order, String text) {
		String[] words = text.split("\\s");

		//separate words into nGrams of n words
		List<List<String>> nGrams = getNGrams(Arrays.asList(words), order);

		Set<Object> tokens = new HashSet<>(Arrays.asList(words));
		BiMap<Integer,Object> tokensAndIds = getTokenIdMap(tokens);

		return getNGram(order, nGrams, tokensAndIds, Arrays.asList(words));
	}

	public static List<List<String>> getNGrams(List<String> words, int order) {
		List<String> tempUnit = new ArrayList<>();
		List<List<String>> nGrams = new ArrayList<>();
		for (int i = 0; i < words.size(); i++) {
			for (int j = 0; j < order; j++) {
				if (i + j < words.size() && !words.get(i + j).equals("")) {
					tempUnit.add(words.get(i + j));
				}
			}
			if (tempUnit.size() == order) {
				nGrams.add(tempUnit);
				tempUnit = new ArrayList<>();
			}
		}
		return nGrams;
	}

	public static String readFileToString(String file) throws IOException {
		InputStream is = new FileInputStream(file);
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		String line = buf.readLine(); StringBuilder sb = new StringBuilder();
		while(line != null){
			sb.append(line).append("\n");
			line = buf.readLine();
		}
		String fileAsString = sb.toString();
		return fileAsString;
	}

	public static BiMap<Integer,Object> getTokenIdMap(Set<Object> tokens) {
		BiMap<Integer,Object> tokensField = HashBiMap.create();
		int i = 0;
		for (Object o : tokens) {
			if (o.equals("")) {
				continue;
			}
			tokensField.put(i,o);
			i++;
		}
		return tokensField;
	}

//	public static Map<Integer,Double> getPriors2(int order, List<List<String>> ngrams, Set<Object> tokens) {
//		double pStartEven = 1.0 / ngrams.size();
//		Map<Integer,Double> priors = new HashMap<>();
//		for (int i = 0; i < tokens.size(); i++) {
//			priors.put(i,pStartEven);
//		}
//		return priors;
//	}

	public static MarkovModel getNGram(int order, List<List<String>> nGrams, BiMap<Integer,Object> tokensAndIds, List<String> words) {
		MarkovModel mm;
		switch (order) {
			case 1:
				mm = new SingleOrderMM(tokensAndIds, words);
				break;
			default:
				mm = new VariableOrderMM(order, tokensAndIds, nGrams, words);
				break;
		}
		return mm;
//		switch (order) {
//			case 1: return (nGrams, tokensAndIds);
//			case 2: return _2Gram(nGrams, tokensAndIds, words);
//			case 3: return _3Gram(nGrams, tokensAndIds, words);
//			case 4: return _4Gram(nGrams, tokensAndIds, words);
//			case 5: return _5Gram(nGrams, tokensAndIds, words);
//			default: return null;
//		}
	}

//	public static MM1Gram _1Gram(List<List<String>> _1Grams, BiMap<Integer,Object> tokensAndIds) {
//		Map<Integer,Double> frequencies = MM1Gram.determineFrequencies(_1Grams, tokensAndIds.values());
//		return new MM1Gram(tokensAndIds, frequencies);
//	}
//
//	public static MM2Gram _2Gram(List<List<String>> _2Grams, BiMap<Integer,Object> tokensAndIds, List<String> words) {
//		//initialize priors TODO fix this
//		Map<Integer,Double> priors = MM2Gram.determinePriors(words, tokensAndIds, 2);
//		Map<Integer,Map<Integer,?>> transitions = MM2Gram.determineTransitions(_2Grams, tokensAndIds);
//		return new MM2Gram(tokensAndIds, priors, transitions);
//
//	}
//	public static MM3Gram _3Gram(List<List<String>> _3Grams, BiMap<Integer,Object> tokensAndIds, List<String> words) {
//		Map<Integer,Map<Integer,Double>> priors = MM3Gram.determinePriors(words, tokensAndIds, 3);
//		Map<Integer,Map<Integer,?>> transitions = MM3Gram.determineTransitions(_3Grams, tokensAndIds);
//		return new MM3Gram(tokensAndIds, priors, transitions);
//	}
//
//	public static MM4Gram _4Gram(List<List<String>> _4Grams, BiMap<Integer,Object> tokensAndIds, List<String> words) {
//		Map<Integer,Map<Integer,Map<Integer,Double>>> priors = MM4Gram.determinePriors(words, tokensAndIds, 4);
//		Map<Integer,Map<Integer,?>> transitions = MM4Gram.determineTransitions(_4Grams, tokensAndIds);
//		return new MM4Gram(tokensAndIds, priors, transitions);
//	}
//
//	public static MM5Gram _5Gram(List<List<String>> _5Grams, BiMap<Integer,Object> tokensAndIds, List<String> words) {
//		Map<Integer,Map<Integer,Map<Integer,Map<Integer,Double>>>> priors = MM5Gram.determinePriors(words, tokensAndIds, 5);
//		Map<Integer,Map<Integer,?>> transitions = MM5Gram.determineTransitions(_5Grams, tokensAndIds);
//		return new MM5Gram(tokensAndIds, priors, transitions);
//	}

}































