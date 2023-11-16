package cs2043group10;

import cs2043group10.data.FinancialDocument;
import cs2043group10.data.FinancialQuery;
import cs2043group10.data.IQuery;
import cs2043group10.data.LoginClass;
import cs2043group10.data.MedicalDocument;
import cs2043group10.data.MedicalQuery;
import cs2043group10.data.PatientInformation;
import cs2043group10.data.PatientQuery;

public interface IDatabase {
	public LoginClass tryLogin(int id, String password) throws DatabaseException;
	public LoginClass getLoginClass();
	public int getId();
	public String getName();
	public void logout();
	public IReversable instantiateHomeView() throws DatabaseException;
	public PatientInformation queryPatientInformation(int patientId) throws DatabaseException;
	public MedicalDocument queryMedicalDocument(int documentId) throws DatabaseException;
	public FinancialDocument queryFinancialDocument(int documentId) throws DatabaseException;
	public IQuery<PatientQuery.PatientEntry> queryPatientsUnderDoctor(int doctorId) throws DatabaseException;
	public IQuery<MedicalQuery.MedicalEntry> queryMedicalDocumentsUnderPatient(int patientId) throws DatabaseException;
	public IQuery<FinancialQuery.FinancialEntry> queryFinancialDocumentsUnderPatient(int patientId) throws DatabaseException;
	public int createPatient(PatientInformation information) throws DatabaseException;
	public int createFinancialDocument(FinancialDocument document) throws DatabaseException;	
	public int createMedicalDocument(MedicalDocument document) throws DatabaseException;
	public void updatePatient(PatientInformation information) throws DatabaseException;
	public void updateMedicalDocument(MedicalDocument document) throws DatabaseException;
	public void updateFinancialDocument(FinancialDocument document) throws DatabaseException;
}
