package cs2043group10.data;

import java.util.Optional;

public class FinancialDocument {
	public final int documentId;
	public final int patientId;
	public final int amount;
	public final Optional<Integer> amountPaid;
	public final String description;
	public final String title;
	public final long createTimestamp;
	
	public FinancialDocument(int documentId, int patientId, int amount, String description, long createTimestamp, String title, Optional<Integer> amountPaid) {
		this.documentId = documentId;
		this.patientId = patientId;
		this.amount = amount;
		this.description = description;
		this.createTimestamp = createTimestamp;
		this.title = title;
		this.amountPaid = amountPaid;
	}
}
