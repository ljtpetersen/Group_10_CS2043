package cs2043group10.misc;

import java.util.Arrays;
import java.util.function.Predicate;

public class WordFilter {
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

	public boolean test(String t) {
		String mod = t.toLowerCase().replaceAll("[^a-z]", "");
		for (int i = 0; i < words.length; ++i) {
			if (!mod.contains(words[i])) {
				return false;
			}
		}
		return true;
	}
	
	public boolean test(String[] t) {
		String[] mod = new String[t.length];
		for (int i = 0; i < t.length; ++i) {
			mod[i] = t[i].toLowerCase().replaceAll("[^a-z]", "");
		}
		for (int i = 0; i < words.length; ++i) {
			boolean found = false;
			for (int j = 0; j < mod.length; ++j) {
				if (mod[j].contains(words[i])) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}
}
