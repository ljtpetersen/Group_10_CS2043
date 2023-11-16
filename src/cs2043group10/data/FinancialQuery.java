package cs2043group10.data;

import java.util.Collection;
import java.util.HashMap;

public class FinancialQuery implements IQuery<FinancialQuery.FinancialEntry> {
	public final int patientId;
	private final HashMap<Integer, FinancialEntry> entries;
	
	public FinancialQuery(int patientId, FinancialEntry[] entries) {
		this.patientId = patientId;
		this.entries = new HashMap<Integer, FinancialEntry>(entries.length);
		for (FinancialEntry entry : entries) {
			this.entries.put(entry.documentId, entry);
		}
	}
	
	@Override
	public int getEntryCount() {
		return entries.size();
	}
	
	@Override
	public FinancialEntry get(int id) {
		return entries.get(id);
	}
	
	@Override
	public Collection<FinancialEntry> getEntries() {
		return entries.values();
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
