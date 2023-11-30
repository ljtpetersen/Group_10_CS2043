
package cs2043group10.test;

import java.util.ArrayList;

import cs2043group10.DatabaseException;
import cs2043group10.IDatabase;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.FinancialDocument;
import cs2043group10.data.FinancialQuery;
import cs2043group10.data.LoginClass;
import cs2043group10.data.MedicalDocument;
import cs2043group10.data.MedicalQuery;
import cs2043group10.data.PatientInformation;
import cs2043group10.data.PatientQuery;
import cs2043group10.data.IQuery;

public class TestDatabaseManager implements IDatabase {
	private int loggedInId;
	private String loggedInName;
	private LoginClass loginClass;
	private final IReversableManager manager;
	private final ArrayList<PatientInformation> patients = new ArrayList<PatientInformation>();
	private final ArrayList<ArrayList<MedicalDocument>> medicalDocumentsP = new ArrayList<ArrayList<MedicalDocument>>();
	private final ArrayList<ArrayList<FinancialDocument>> financialDocumentsP = new ArrayList<ArrayList<FinancialDocument>>();
	private final ArrayList<MedicalDocument> medicalDocuments = new ArrayList<MedicalDocument>();
	private final ArrayList<FinancialDocument> financialDocuments = new ArrayList<FinancialDocument>();
	
	public TestDatabaseManager(IReversableManager manager) {
		this.manager = manager;
		loggedInId = -1;
		loginClass = LoginClass.NOT_LOGGED_IN;
		loggedInName = null;
	}
	
	@Override
	public LoginClass tryLogin(int id, String password) throws DatabaseException {
		// Try to login using id and password.
		/**throw new RuntimeException("Not yet implemented.");*/
		if (id == 0) {
			this.loggedInId = id;
			this.loggedInName = "Newling, Ben";
			this.loginClass = LoginClass.DOCTOR;
		} else if (id <= patients.size()) {
			this.loggedInId = id;
			this.loggedInName = patients.get(id - 1).fullName;
			this.loginClass = LoginClass.PATIENT;
		}
		
		return loginClass;
	}
	
	@Override
	public LoginClass getLoginClass() {
		return loginClass;
	}
	
	@Override
	public int getId() {
		return loggedInId;
	}
	
	@Override
	public String getName() {
		return loggedInName;
	}
	
	@Override
	public void logout() {
		loggedInId = -1;
		loggedInName = null;
		loginClass = LoginClass.NOT_LOGGED_IN;
	}
	
	@Override
	public IReversable instantiateHomeView() throws DatabaseException {
		return loginClass.instantiateHomeView(manager, loggedInId);
	}
	
	@Override
	public PatientInformation queryPatientInformation(int patientId) throws DatabaseException {
		if (patientId <= 0 || patientId > patients.size()) {
			throw new DatabaseException("No patient with id " + patientId);
		}
		return patients.get(patientId - 1);
	}
	
	@Override
	public MedicalDocument queryMedicalDocument(int documentId) throws DatabaseException {
		if (documentId < 0 || documentId >= medicalDocuments.size()) {
			throw new DatabaseException("No medical document with id " + documentId);
		}
		return medicalDocuments.get(documentId);
	}
	
	@Override
	public FinancialDocument queryFinancialDocument(int documentId) throws DatabaseException {
		if (documentId < 0 || documentId >= financialDocuments.size()) {
			throw new DatabaseException("No financial document with id " + documentId);
		}
		return financialDocuments.get(documentId);
	}
	
	@Override
	public IQuery<PatientQuery.PatientEntry> queryPatientsUnderDoctor(int doctorId) throws DatabaseException {
		if (doctorId != 0) {
			throw new DatabaseException("No doctor with id " + doctorId);
		}
		return new PatientQuery(patients.stream().map((p) -> new PatientQuery.PatientEntry(p.fullName, p.patientId)).toArray(PatientQuery.PatientEntry[]::new));
	}
	
	@Override
	public IQuery<MedicalQuery.MedicalEntry> queryMedicalDocumentsUnderPatient(int patientId) throws DatabaseException {
		if (patientId <= 0 || patientId > patients.size()) {
			throw new DatabaseException("No patient with id " + patientId);
		}
		return new MedicalQuery(patientId, medicalDocumentsP.get(patientId - 1).stream().map((p) -> new MedicalQuery.MedicalEntry(p.createTimestamp, p.modifyTimestamp, p.title, p.type, p.documentId)).toArray(MedicalQuery.MedicalEntry[]::new));
	}
	
	@Override
	public IQuery<FinancialQuery.FinancialEntry> queryFinancialDocumentsUnderPatient(int patientId) throws DatabaseException {
		if (patientId <= 0 || patientId > patients.size()) {
			throw new DatabaseException("No patient with id " + patientId);
		}
		return new FinancialQuery(patientId, financialDocumentsP.get(patientId - 1).stream().map((p) -> new FinancialQuery.FinancialEntry(p.title, p.createTimestamp, p.amount, p.documentId)).toArray(FinancialQuery.FinancialEntry[]::new));
	}
	
	@Override
	public int createPatient(PatientInformation information) throws DatabaseException {
		PatientInformation info = new PatientInformation(patients.size() + 1, information.fullName, information.address, information.insurance, 0, information.dateOfBirth, System.currentTimeMillis() / 1000, System.currentTimeMillis() / 1000, information.doctorId);
		patients.add(info);
		medicalDocumentsP.add(new ArrayList<MedicalDocument>());
		financialDocumentsP.add(new ArrayList<FinancialDocument>());
		return patients.size();
	}
	
	@Override
	public int createFinancialDocument(FinancialDocument document) throws DatabaseException {
		if (document.patientId <= 0 || document.patientId > patients.size()) {
			throw new DatabaseException("No patient with id " + document.patientId);
		}
		FinancialDocument doc = new FinancialDocument(financialDocuments.size(), document.patientId, document.amount, document.description, System.currentTimeMillis() / 1000, document.title, document.amountPaid);
		financialDocuments.add(doc);
		financialDocumentsP.get(document.patientId - 1).add(doc);
		return doc.documentId;
	}
	
	@Override
	public int createMedicalDocument(MedicalDocument document) throws DatabaseException {
		if (document.patientId <= 0 || document.patientId > patients.size()) {
			throw new DatabaseException("No patient with id " + document.patientId);
		}
		MedicalDocument doc = new MedicalDocument(medicalDocuments.size(), document.title, document.type, document.body, document.auxiliary, document.patientId, System.currentTimeMillis() / 1000, System.currentTimeMillis() / 1000);
		medicalDocuments.add(doc);
		medicalDocumentsP.get(doc.patientId - 1).add(doc);
		return doc.documentId;
	}
	
	@Override
	public void updatePatient(PatientInformation information) throws DatabaseException {
		if (information.patientId <= 0 || information.patientId > patients.size()) {
			throw new DatabaseException("No patient with id " + information.patientId);
		}
		long createTimestamp = patients.get(information.patientId).createTimestamp;
		PatientInformation info = new PatientInformation(information.patientId, information.fullName, information.address, information.insurance, 0, information.dateOfBirth, createTimestamp, System.currentTimeMillis() / 1000, information.doctorId);
		patients.set(information.patientId - 1, info);
	}
	
	@Override
	public void updateMedicalDocument(MedicalDocument document) throws DatabaseException {
		if (document.patientId <= 0 || document.patientId > patients.size()) {
			throw new DatabaseException("No patient with id " + document.patientId);
		}
		if (document.documentId < 0 || document.documentId >= medicalDocuments.size()) {
			throw new DatabaseException("No medical document with id " + document.documentId);
		}
		long createTimestamp = medicalDocuments.get(document.documentId).createTimestamp;
		MedicalDocument doc = new MedicalDocument(document.documentId, document.title, document.type, document.body, document.auxiliary, document.patientId, System.currentTimeMillis() / 1000, createTimestamp);
		medicalDocuments.set(doc.documentId, doc);
		int i = 0;
		for (MedicalDocument d : medicalDocumentsP.get(document.patientId - 1)) {
			if (d.documentId == doc.documentId) {
				break;
			}
			++i;
		}
		if (i >= medicalDocumentsP.get(document.patientId - 1).size()) {
			throw new DatabaseException("No medical document under patient " + document.patientId + " with id " + document.documentId);
		}
		medicalDocumentsP.get(document.patientId - 1).set(i, doc);
	}
	
	@Override
	public boolean verifyCredentials(int id, String password) throws DatabaseException {
		if (id >= 0 && id <= patients.size()) {
			return true;
		}
		return false;
	}
}