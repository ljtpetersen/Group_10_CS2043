package cs2043group10.data;

import java.time.LocalDate;

public class PatientInformation {
	public final int totalMoneyOwed;
	public final String fullName;
	public final String address;
	public final InsurancePlan insurance;
	public final int patientId;
	public final LocalDate dateOfBirth;
	public final long createTimestamp;
	public final long modifyTimestamp;
	
	public PatientInformation(int patientId, String fullName, String address, InsurancePlan insurance,
			int totalMoneyOwed, LocalDate dateOfBirth, long createTimestamp, long modifyTimestamp) {
		this.address = address;
		this.fullName = fullName;
		this.insurance = insurance;
		this.patientId = patientId;
		this.totalMoneyOwed = totalMoneyOwed;
		this.dateOfBirth = dateOfBirth;
		this.createTimestamp = createTimestamp;
		this.modifyTimestamp = modifyTimestamp;
	}
}