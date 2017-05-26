import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Driver {

	public final static Random r = new Random();

	public static void main(String[] args) throws IOException {
		TextDivisor delimiter = null;
		Map<String,Double> texts = new HashMap<>();
		int n = 0;
		int chainLen = 0;
		for (int i = 0; i < args.length; i += 2) {
			String next = args[i + 1];
			switch (args[i]) {
				case "-t":
					File file = new File(next);
					if (file.isDirectory()) {
						File[] listOfFiles = file.listFiles();
						for (File tempFile : listOfFiles) {
							if (!tempFile.getName().endsWith(".txt")) continue;
							String tempText = readFileToString(tempFile.toString());
							Pattern pattern = Pattern.compile("<WEIGHT: \\d+.\\d+>");
							Matcher matcher = pattern.matcher(tempText);
							double tempWeight = 1.0;
							if (matcher.find()) {
								String w = matcher.toMatchResult().group();
								w = w.replace(">","");
								w = w.substring(9);
								tempWeight = Double.parseDouble(w);
							}
							tempText = tempText.replace(matcher.toMatchResult().group() + "\n","");
							texts.put(tempText, tempWeight);
						}
					}
					else {
						texts.put(readFileToString(file.toString()),1.0);
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
		MarkovModel mm = getNGramModel(n, texts);

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

	public static MarkovModel getNGramModel(int order, Map<String,Double> texts) {
		List<Pair<String[], Double>> allNgrams= new ArrayList<>();
		BiMap<Integer,Object> allTokensAndIds = HashBiMap.create();
		List<String> allWords = new ArrayList<>();
		int i = 0;
		for (Map.Entry<String,Double> text : texts.entrySet()) {
			String cleanedText = cleanText(text.getKey());
			String tokenizedText = addTokens(cleanedText, order);
			String[] words = tokenizedText.split("\\s");
			allWords.addAll(Arrays.asList(words));
			Set<Object> tokens = new HashSet<>(Arrays.asList(words));

			Set<Object> tempTokens = getTokenIdMap(tokens);
			for (Object tempToken : tempTokens) {
				if (allTokensAndIds.containsValue(tempToken)) continue;
				allTokensAndIds.put(i, tempToken);
				i++;
			}

			//separate words into nGrams of n words
			List<Pair<String, Double>> weightedWords = new ArrayList<>();
			for (String s : words) {
				weightedWords.add(new Pair<>(s,text.getValue()));
			}
			List<Pair<String[], Double>> nGrams = getNGrams(weightedWords, order);
			allNgrams.addAll(nGrams);
		}
		return getMarkovModel(order, allNgrams, allTokensAndIds, allWords);
	}

	public static List<Pair<String[], Double>> getNGrams(List<Pair<String,Double>> words, int order) {
		String[] tempNGram = new String[order];
		List<Pair<String[], Double>> nGrams = new ArrayList<>();
		for (int i = 0; i < words.size(); i++) {
			double tempWeight = 1.0;
			for (int j = 0; j < order; j++) {
				if (i + j < words.size() && !words.get(i + j).getKey().equals("")) {
					if (j < order - 1 && words.get(i + j).getKey().equals("<e>")) break;
					tempNGram[j] = (words.get(i + j).getKey());
					tempWeight = words.get(i + j).getValue();
				}
			}
			if (tempNGram.length == order && tempNGram[order - 1] != null && tempNGram[0] != null) {
				nGrams.add(new Pair<>(tempNGram, tempWeight));
			}
			tempNGram = new String[order];
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

	public static Set<Object> getTokenIdMap(Set<Object> tokens) {
		Set<Object> tokensField = new HashSet<>();
		for (Object o : tokens) {
			if (o.equals("")) {
				System.out.println("skipped empty string!");
				continue;
			}
			tokensField.add(o);
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

	public static MarkovModel getMarkovModel(int order, List<Pair<String[], Double>> nGrams, BiMap<Integer,Object> tokensAndIds, List<String> words) {
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

	public static String cleanText(String text) {
		text = text.replaceAll("\"", "");
		text = text.replaceAll("\\(", "");
		text = text.replaceAll("\\)", "");
		return text;
	}

	public static String addTokens(String text, int order) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < order - 1; i++) {
			if (i == 0) {
				sb.append(" <s> ");
			}
			else {
				sb.append("<s> ");
			}
		}
		String endToken = " <e>";
		String startTokens = sb.toString();
		String tokens = endToken + startTokens;
		sb = new StringBuilder(text);
		sb.insert(0,startTokens);
		text = sb.toString();
		text = text.replaceAll("\\.\\s", "." + tokens);
		text = text.replaceAll("\\?\\s", "?" + tokens);
		text = text.replaceAll("!\\s", "!" + tokens);
		text = text.replaceAll(";\\s", ";" + tokens);
		text = text.replaceAll(":\\s", ":" + tokens);
		return text;
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
































