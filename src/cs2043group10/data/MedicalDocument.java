package cs2043group10.data;

public class MedicalDocument {
	public final int authorId;
	public final String title;
	public final String type;
	public final String body;
	public final String auxiliary;
	public final int patientId;
	public final int documentId;
	public final long createTimestamp;
	public final long modifyTimestamp;
	
	public MedicalDocument(int documentId, int authorId, String title, String type, String body, String auxiliary, int patientId, long modifyTimestamp, long createTimestamp) {
		this.authorId = authorId;
		this.title = title;
		this.type = type;
		this.body = body;
		this.auxiliary = auxiliary;
		this.patientId = patientId;
		this.documentId = documentId;
		this.modifyTimestamp = modifyTimestamp;
		this.createTimestamp = createTimestamp;
	}
}
