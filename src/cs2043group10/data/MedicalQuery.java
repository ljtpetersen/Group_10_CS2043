package cs2043group10.data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

public class MedicalQuery implements IQuery<MedicalQuery.MedicalEntry> {
	public final int patientId;
	private final MedicalEntry[] entries;
	
	public MedicalQuery(int patientId, MedicalEntry[] entries) {
		this.patientId = patientId;
		this.entries = entries;
	}
	
	@Override
	public void sortBy(Comparator<MedicalEntry> comparator) {
		Arrays.sort(entries, comparator);
	}
	
	@Override
	public int getEntryCount() {
		return entries.length;
	}
	
	@Override
	public IQuery<MedicalEntry> filter(Predicate<MedicalEntry> pred) {
		return new MedicalQuery(patientId, Arrays.stream(entries).filter(pred).toArray(MedicalEntry[]::new));
	}
	
	@Override
	public MedicalEntry get(int index) {
		return entries[index];
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
