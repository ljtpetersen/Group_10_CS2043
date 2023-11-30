package cs2043group10.misc;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * This class represents a filter which is applied to a string.
 * 
 * @author James Petersen
 */
public class WordFilter {
	/**
	 * This represents the current filter string.
	 */
	private String currentFilter = "";
	/**
	 * This represents the words obtained from the filter.
	 */
	private String[] words = new String[0];
	
	/**
	 * Construct a new filter with an empty filter.
	 */
	public WordFilter() {}
	
	/**
	 * Get the current filter.
	 * @return The current filter.
	 */
	public String getCurrentFilter() {
		return currentFilter;
	}
	
	/**
	 * Set the current filter.
	 * @param filter The new filter.
	 */
	public void setCurrentFilter(String filter) {
		currentFilter = filter;
		String modified = filter.toLowerCase().replaceAll("[^a-z0-9]", " ");
		words = Arrays.stream(modified.split(" ")).filter(Predicate.not(String::isEmpty)).toArray(String[]::new);
	}

	/**
	 * Test a string to see if it matches the filter.
	 * @param t The string to test.
	 * @return Whether the string matches the filter.
	 */
	public boolean test(String t) {
		String mod = t.toLowerCase().replaceAll("[^a-z0-9]", "");
		for (int i = 0; i < words.length; ++i) {
			if (!mod.contains(words[i])) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Test several strings to see if any of them match the filter.
	 * @param t The strings.
	 * @return Whether the array matches the filter.
	 */
	public boolean test(String... t) {
		String accum = "";
		for (int i = 0; i < t.length; ++i) {
			accum += t[i].toLowerCase().replaceAll("[^a-z0-9]", "");
		}
		return test(accum);
	}
}
