package cs2043group10.data;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

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
	public Collection<PatientEntry> getEntries() {
		return patients.values();
	}
	
	public static class PatientEntry {
		public final String name;
		public final int id;
		public final LocalDate dateOfBirth;
		public final String address;
		
		public PatientEntry(String name, int id, LocalDate dateOfBirth, String address) {
			this.name = name;
			this.id = id;
			this.address = address;
			this.dateOfBirth = dateOfBirth;
		}
		
		public String getName() {
			return name;
		}
	}
}
