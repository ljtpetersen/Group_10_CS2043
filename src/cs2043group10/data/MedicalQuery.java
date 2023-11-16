package cs2043group10.data;

import java.util.Collection;
import java.util.HashMap;

public class MedicalQuery implements IQuery<MedicalQuery.MedicalEntry> {
	public final int patientId;
	private final HashMap<Integer, MedicalEntry> entries;
	
	public MedicalQuery(int patientId, MedicalEntry[] entries) {
		this.patientId = patientId;
		this.entries = new HashMap<Integer, MedicalEntry>(entries.length);
		for (MedicalEntry entry : entries) {
			this.entries.put(entry.documentId, entry);
		}
	}
		
	@Override
	public int getEntryCount() {
		return entries.size();
	}
		
	@Override
	public MedicalEntry get(int id) {
		return entries.get(id);
	}
	
	@Override
	public Collection<MedicalEntry> getEntries() {
		return entries.values();
	}
	
	public static class MedicalEntry {
		public final long createTimestamp;
		public final long modifyTimestamp;
		public final String title;
		public final String type;
		public final int documentId;
		
		public MedicalEntry(long createTimestamp, long modifyTimestamp, String title, String type, int documentId) {
			this.title = title;
			this.type = type;
			this.documentId = documentId;
			this.createTimestamp = createTimestamp;
			this.modifyTimestamp = modifyTimestamp;
		}
	}
}
