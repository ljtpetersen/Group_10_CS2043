package cs2043group10.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Predicate;

public class PatientQuery implements IQuery<PatientQuery.PatientEntry> {
	private final HashMap<Integer, PatientEntry> patients;
	
	public PatientQuery(PatientEntry[] patients) {
		this.patients = new HashMap<Integer, PatientEntry>(patients.length);
		for (PatientEntry entry : patients) {
			this.patients.put(entry.id, entry);
		}
	}
	
	@Override
	public int getEntryCount() {
		return patients.size();
	}
	
	@Override
	public PatientEntry get(int id) {
		return patients.get(id);
	}
	
	@Override
	public IQuery<PatientEntry> filter(Predicate<PatientEntry> pred) {
		return new PatientQuery(patients.values().stream().filter(pred).toArray(PatientEntry[]::new));
	}
	
	@Override
	public Collection<PatientEntry> getEntries() {
		return patients.values();
	}
	
	public static class PatientEntry {
		public final String name;
		public final int id;
		
		public PatientEntry(String name, int id) {
			this.name = name;
			this.id = id;
		}
		
		public String getName() {
			return name;
		}
	}
}
