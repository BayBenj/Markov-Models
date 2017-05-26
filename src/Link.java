import com.google.common.collect.BiMap;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class Link<T> extends HashMap<Integer,T> {

	public abstract void putNgram(LinkedList<Pair<Integer,Double>> idsAndWeights);

	public abstract double count();

	public abstract List<String> randomNgram(Double cumulativeTotal, double rndTarget, BiMap<Integer, Object> tokens, List<String> chain);

}
