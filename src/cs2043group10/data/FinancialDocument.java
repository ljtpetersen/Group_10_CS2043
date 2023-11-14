package cs2043group10.data;

public class FinancialDocument {
	public final int documentId;
	public final int patientId;
	public final long amount;
	public final String description;
	public final long createTimestamp;
	public final long modifyTimestamp;
	
	public FinancialDocument(int documentId, int patientId, long amount, String description, long createTimestamp, long modifyTimestamp) {
		this.documentId = documentId;
		this.patientId = patientId;
		this.amount = amount;
		this.description = description;
		this.createTimestamp = createTimestamp;
		this.modifyTimestamp = modifyTimestamp;
	}
}
