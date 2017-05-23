import com.google.common.collect.BiMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class Link<T> extends HashMap<Integer,T> {

	public abstract void putNgram(LinkedList<Integer> ids);

	public abstract int count();

	public abstract int randomNgram(double cumulativeTotal, double rndTarget, BiMap<Integer, Object> tokens, List<String> words);

}
