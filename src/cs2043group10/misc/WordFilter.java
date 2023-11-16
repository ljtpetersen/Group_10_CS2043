package cs2043group10.misc;

import java.util.Arrays;
import java.util.function.Predicate;

public class WordFilter implements Predicate<String> {
	String currentFilter = "";
	String[] words = new String[0];
	
	public WordFilter() {}
	
	public String getCurrentFilter() {
		return currentFilter;
	}
	
	public void setCurrentFilter(String filter) {
		currentFilter = filter;
		String modified = filter.toLowerCase().replaceAll("[^a-z]", " ");
		words = Arrays.stream(modified.split(" ")).filter(Predicate.not(String::isEmpty)).toArray(String[]::new);
	}

	@Override
	public boolean test(String t) {
		String mod = t.toLowerCase().replaceAll("[^a-z]", "");
		for (int i = 0; i < words.length; ++i) {
			if (!mod.contains(words[i])) {
				return false;
			}
		}
		return true;
	}
}
