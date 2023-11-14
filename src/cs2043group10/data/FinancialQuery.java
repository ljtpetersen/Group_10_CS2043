package cs2043group10.data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

public class FinancialQuery implements IQuery<FinancialQuery.FinancialEntry> {
	public final int patientId;
	private final FinancialEntry[] entries;
	
	public FinancialQuery(int patientId, FinancialEntry[] entries) {
		this.patientId = patientId;
		this.entries = entries;
	}
	
	@Override
	public void sortBy(Comparator<FinancialEntry> comparator) {
		Arrays.sort(entries, comparator);
	}
	
	@Override
	public int getEntryCount() {
		return entries.length;
	}
	
	@Override
	public IQuery<FinancialEntry> filter(Predicate<FinancialEntry> pred) {
		return new FinancialQuery(patientId, Arrays.stream(entries).filter(pred).toArray(FinancialEntry[]::new));
	}
	
	@Override
	public FinancialEntry get(int index) {
		return entries[index];
	}
	
	public static class FinancialEntry {
		public final long createTimestamp;
		public final long modifyTimestamp;
		public final String description;
		public final int documentId;
		public final long amount;
		
		public FinancialEntry(long createTimestamp, int modifyTimestamp, String description, long amount, int documentId) {
			this.createTimestamp = createTimestamp;
			this.modifyTimestamp = modifyTimestamp;
			this.description = description;
			this.amount = amount;
			this.documentId = documentId;
		}
	}

}
