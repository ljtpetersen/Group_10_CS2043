package cs2043group10.data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

public class PatientQuery implements IQuery<PatientQuery.PatientEntry> {
	private final PatientEntry[] patients;
	
	public PatientQuery(PatientEntry[] patients) {
		this.patients = patients;
	}
	
	@Override
	public int getEntryCount() {
		return patients.length;
	}
	
	@Override
	public PatientEntry get(int index) {
		return patients[index];
	}
	
	@Override
	public void sortBy(Comparator<PatientEntry> cmp) {
		Arrays.sort(patients, cmp);
	}
	
	public IQuery<PatientEntry> filter(Predicate<PatientEntry> pred) {
		return new PatientQuery(Arrays.stream(patients).filter(pred).toArray(PatientEntry[]::new));
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
