package cs2043group10;
import java.security.NoSuchAlgorithmException;
import cs2043group10.data.FinancialDocument;
import cs2043group10.data.FinancialQuery;
import cs2043group10.data.FinancialQuery.FinancialEntry;
import cs2043group10.data.LoginClass;
import cs2043group10.data.MedicalDocument;
import cs2043group10.data.MedicalQuery;
import cs2043group10.data.MedicalQuery.MedicalEntry;
import cs2043group10.data.PatientInformation;
import cs2043group10.data.PatientQuery;
import cs2043group10.data.PatientQuery.PatientEntry;
import cs2043group10.misc.PasswordHasher;
import cs2043group10.data.IQuery;
import cs2043group10.data.InsurancePlan;
import java.util.Optional;

import java.sql.Date;
// Imports required for connecting to the database
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.CallableStatement;
import java.sql.ResultSet;
// Import for ArrayLists
import java.util.ArrayList;
import java.util.Arrays;
public class DatabaseManager implements IDatabase {
	private int loggedInId;
	private String loggedInName;
	private LoginClass loginClass;
	private final IReversableManager manager;
	private final PasswordHasher hasher;
	private final Connection connector;
	
	public DatabaseManager(IReversableManager manager) throws DatabaseException  {
		this.manager = manager;
		loggedInId = -1;
		loginClass = LoginClass.NOT_LOGGED_IN;
		loggedInName = null;
		try {
			hasher = new PasswordHasher(); 
		} catch (NoSuchAlgorithmException e) {
			throw new DatabaseException(e);
		}
		
		try
       {   // Connect to the database
           connector = DriverManager.getConnection
                  ("jdbc:mysql://cs1103.cs.unb.ca:3306/iyoung",  // Database URL
                   "iyoung",   // MySQL username
                   "2RMFsZG2");  // MySQL password
       }	
		catch(SQLException e)
       {   throw new DatabaseException(e);
       }
	}
	
	@Override
	public LoginClass tryLogin(int id, String password) throws DatabaseException {
		// Try to login using id and password.
		
		boolean isValidCredentials = verifyCredentials(id, password);
		if (isValidCredentials) {
			return loginClass;
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
		try {
			// Create the executable SQL statement
			String call = "{CALL queryPatientInformation(?)}";
			// Sets parameter(s) for stored procedure call
			CallableStatement procedureCall = connector.prepareCall(call);
			procedureCall.setInt(1, patientId);
			// Executes stored procedure
			ResultSet resultSet = procedureCall.executeQuery();
			// Declare patient info contents
			String name;
			String address;
			int id;
			Long createTimeStamp;
			Long modifyTimeStamp;
			LocalDate dateOfBirth;
			Date dateOfBirthNotLocal;
			int doctorId;
			int totalAmountDue;
			int insuranceDeductible;
			int insuranceCostSharePercentage;
			int insuranceOutOfPocketMaximum;
			// Process the result set
			if (resultSet.next()) {
				name = resultSet.getString("name");
				address = resultSet.getString("address");
				id = patientId;
				createTimeStamp = resultSet.getTimestamp("createTimestamp").getTime() / 1000;
				modifyTimeStamp = resultSet.getTimestamp("modifyTimestamp").getTime() / 1000;
				dateOfBirthNotLocal = resultSet.getDate("dateOfBirth");
				dateOfBirth = dateOfBirthNotLocal.toLocalDate();
				doctorId = resultSet.getInt("doctorId");
				totalAmountDue = resultSet.getInt("totalAmountDue");
				insuranceDeductible = resultSet.getInt("insuranceDeductible");
				insuranceCostSharePercentage = resultSet.getInt("insuranceCostSharePercentage");
				insuranceOutOfPocketMaximum = resultSet.getInt("insuranceOutOfPocketMaximum");
			} else {
				throw new DatabaseException("No patient with id " + patientId);
			}
			
			InsurancePlan insurance = new InsurancePlan(insuranceDeductible, insuranceOutOfPocketMaximum, insuranceCostSharePercentage);
			PatientInformation patientInformation = new PatientInformation(id, name, address, insurance, totalAmountDue, dateOfBirth, createTimeStamp, modifyTimeStamp, doctorId);
			return patientInformation;
		} catch (SQLException e)
		{	throw new DatabaseException(e);
		}
	}
	
	@Override
	public MedicalDocument queryMedicalDocument(int documentId) throws DatabaseException {
		try {
			// Create the executable SQL statement
			String call = "{CALL queryMedicalDocument(?)}";
			// Sets parameter(s) for stored procedure call
			CallableStatement procedureCall = connector.prepareCall(call);
			procedureCall.setInt(1, documentId);
			// Executes stored procedure
			ResultSet resultSet = procedureCall.executeQuery();
			// Declare medical document contents
			// IMPORTANT: CAN WE REMOVE AUTHORID ITS NOT IN DATABASE???
			String title;
			String type;
			String body;
			String auxiliary;
			int patientId;
			Long createTimeStamp;
			Long modifyTimeStamp;
			// Process the result set
			if (resultSet.next()) {
				// Retrieve columns from database
				title = resultSet.getString("title");
				type = resultSet.getString("type");
				body = resultSet.getString("body");
				auxiliary = resultSet.getString("auxiliary");
				patientId = resultSet.getInt("patientId");
				createTimeStamp = resultSet.getTimestamp("createTimestamp").getTime();
				modifyTimeStamp = resultSet.getTimestamp("modifyTimestamp").getTime();
			} else {
				throw new DatabaseException("No document with id " + documentId);
			}
			MedicalDocument medicalDocument = new MedicalDocument(documentId, title, type, body, auxiliary, patientId, modifyTimeStamp, createTimeStamp);
			
			return medicalDocument;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public FinancialDocument queryFinancialDocument(int documentId) throws DatabaseException {
		try {
			// Create the executable SQL statement
			String call = "{CALL queryFinancialDocument(?)}";
			// Sets parameter(s) for stored procedure call
			CallableStatement procedureCall = connector.prepareCall(call);
			procedureCall.setInt(1, documentId);
			// Executes stored procedure
			ResultSet resultSet = procedureCall.executeQuery();
			// Declare financial document contents
			int id;
			int patientId;
			int amount;
			Optional<Integer> amountPaid;
			String description;
			Long createTimeStamp;
			String title;
			
			// Process the result set
			if (resultSet.next()) {
				// Retrieve columns from database
				id = resultSet.getInt("id");
				patientId = resultSet.getInt("patientId");
				amount = resultSet.getInt("amount");
				int tmp = resultSet.getInt("amountPaid");
				if (resultSet.wasNull()) {
					amountPaid = Optional.empty();
				} else {
					amountPaid = Optional.of(tmp);
				}
				description = resultSet.getString("description");
				createTimeStamp = resultSet.getTimestamp("createTimestamp").getTime();
				title = resultSet.getString("title");
			} else {
				throw new DatabaseException("No document with id " + documentId);
			}
			FinancialDocument financialDocument = new FinancialDocument(id, patientId, amount, description, createTimeStamp, title, amountPaid);
			return financialDocument;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public IQuery<PatientQuery.PatientEntry> queryPatientsUnderDoctor(int doctorId) throws DatabaseException {
		try {
			// Create the executable SQL statement
			String call = "{CALL queryPatientsUnderDoctor(?)}";
			
			// Sets parameter(s) for stored procedure call
			CallableStatement procedureCall = connector.prepareCall(call);
			procedureCall.setInt(1, doctorId);
			
			// Executes stored procedure
			ResultSet resultSet = procedureCall.executeQuery();
			
			// Declare patient entry contents
			String name;
			int id;
			LocalDate dateOfBirth;
			String address;
			Date dateOfBirthNotLocal;
			
			// Create ArrayList to store patients under doctor
			ArrayList<PatientEntry> patientList = new ArrayList<>();
			
			// Process the result set
			while (resultSet.next()) {
				name = resultSet.getString("name");
				address = resultSet.getString("address");
				id = resultSet.getInt("id");
				dateOfBirthNotLocal = resultSet.getDate("dateOfBirth");
				dateOfBirth = dateOfBirthNotLocal.toLocalDate();
				PatientEntry patientEntry = new PatientEntry(name, id, dateOfBirth, address);
				patientList.add(patientEntry);
			}
			// Convert ArrayList to array
			PatientEntry[] patientsArray = patientList.toArray(new PatientEntry[0]);
			
			PatientQuery patientsUnderDoctor = new PatientQuery(patientsArray);
			return patientsUnderDoctor;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public IQuery<MedicalQuery.MedicalEntry> queryMedicalDocumentsUnderPatient(int patientId) throws DatabaseException {
		try {
			// Create the executable SQL statement
			String call = "{CALL queryMedicalDocumentsUnderPatient(?)}";
			
			// Sets parameter(s) for stored procedure call
			CallableStatement procedureCall = connector.prepareCall(call);
			procedureCall.setInt(1, patientId);
			
			// Executes stored procedure
			ResultSet resultSet = procedureCall.executeQuery();
			
			// Declare medical entry contents
			long createTimeStamp;
			long modifyTimeStamp;
			String title;
			String type;
			int documentId;
			
			// Create ArrayList to store reports under patient
			ArrayList<MedicalEntry> reportList = new ArrayList<>();
			
			// Process the result set
			while (resultSet.next()) {
				createTimeStamp = resultSet.getTimestamp("createTimestamp").getTime() / 1000;
				modifyTimeStamp = resultSet.getTimestamp("modifyTimestamp").getTime() / 1000;
				title = resultSet.getString("title");
				type = resultSet.getString("type");
				documentId = resultSet.getInt("id");
				MedicalEntry medicalEntry = new MedicalEntry(createTimeStamp, modifyTimeStamp, title, type, documentId);
				reportList.add(medicalEntry);
			}
			
			// Convert ArrayList to array
			MedicalEntry[] reportsArray = reportList.toArray(new MedicalEntry[0]);
			
			MedicalQuery reportsUnderPatient = new MedicalQuery(patientId, reportsArray);
			return reportsUnderPatient;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public IQuery<FinancialQuery.FinancialEntry> queryFinancialDocumentsUnderPatient(int patientId) throws DatabaseException {
		try {
			// Create the executable SQL statement
			String call = "{CALL queryFinancialDocumentsUnderPatient(?)}";
			
			// Sets parameter(s) for stored procedure call
			CallableStatement procedureCall = connector.prepareCall(call);
			procedureCall.setInt(1, patientId);
			
			// Executes stored procedure
			ResultSet resultSet = procedureCall.executeQuery();
			
			// Declare financial entry contents
			long createTimeStamp;
			int documentId;
			long amount;
			String title;
			
			// Create ArrayList to store reports under patient
			ArrayList<FinancialEntry> transactionList = new ArrayList<>();
			
			// Process the result set
			while (resultSet.next()) {
				createTimeStamp = resultSet.getTimestamp("createTimestamp").getTime();
				title = resultSet.getString("title");
				documentId = resultSet.getInt("id");
				amount = resultSet.getLong("amount");
				FinancialEntry financialEntry = new FinancialEntry(title, createTimeStamp, amount, documentId);
				transactionList.add(financialEntry);
			}
			
			// Convert ArrayList to array
			FinancialEntry[] transactionsArray = transactionList.toArray(new FinancialEntry[0]);
			
			FinancialQuery transactionsUnderPatient = new FinancialQuery(patientId, transactionsArray);
			return transactionsUnderPatient;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public int createPatient(PatientInformation information) throws DatabaseException {
		// TODO The timestamp and id fields should be automatically generated by the database.
		// return the id of the new patient.
		// Creating an entry in the accounts table will happen here
		// Initialize attributes of patient to enter
		try {
			String name = information.fullName;
			String address = information.address;
			LocalDate dateOfBirth = information.dateOfBirth;
			Date dateOfBirthNotLocal;
			dateOfBirthNotLocal = Date.valueOf(dateOfBirth);
			int doctorId = information.doctorId;
			int totalAmountDue = information.totalMoneyOwed;
			int insuranceDeductible = information.insurance.deductible;
			int insuranceCostSharePercentage = information.insurance.costSharePercentage;
			int insuranceOutOfPocketMaximum = information.insurance.outOfPocketMaximum;
			// Create patient in patient table with stored procedure
			String call = "{CALL createPatient(?,?,?,?,?,?,?,?)}";
			// Insert values with stored procedure
			CallableStatement procedureCall = connector.prepareCall(call);
			procedureCall.setString(1, name);
			procedureCall.setString(2, address);
			procedureCall.setDate(3, dateOfBirthNotLocal);
			procedureCall.setInt(4, doctorId);
			procedureCall.setInt(5, totalAmountDue);
			procedureCall.setInt(6, insuranceDeductible);
			procedureCall.setInt(7, insuranceCostSharePercentage);
			procedureCall.setInt(8, insuranceOutOfPocketMaximum);
			
			ResultSet res = procedureCall.executeQuery();
			if (!res.next()) {
				throw new DatabaseException("Failed to get created patient id.");
			}
			return res.getInt("id");
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public int createFinancialDocument(FinancialDocument document) throws DatabaseException {
		try {
			// Initialize attributes of transaction to enter
			int patientId = document.patientId;
			int amount = document.amount;
			String description = document.description;
			String title = document.title;
			Optional<Integer> amountPaid = document.amountPaid;
			// Create report in patient table with stored procedure
			String call = "{CALL createTransaction(?,?,?,?,?)}";
			// Insert values with stored procedure
			CallableStatement procedureCall = connector.prepareCall(call);
			procedureCall.setInt(2, patientId);
			procedureCall.setString(5, description);
			procedureCall.setString(1, title);
			procedureCall.setInt(3, amount);
			if (amountPaid.isPresent()) {
				procedureCall.setInt(4, amountPaid.get());
			}
			
			procedureCall.executeUpdate();
			ResultSet res = procedureCall.executeQuery();
			if (!res.next()) {
				throw new DatabaseException("Failed to get created document id.");
			}
			int id = res.getInt("id");
			PatientInformation info = queryPatientInformation(document.patientId);
			updatePatient(new PatientInformation(info.patientId, info.fullName, info.address, info.insurance, info.totalMoneyOwed + document.amount, info.dateOfBirth, -1, -1, info.doctorId));
			return id;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public int createMedicalDocument(MedicalDocument document) throws DatabaseException {
		try {
			// Initialize attributes of report to enter
			int patientId = document.patientId;
			String type = document.type;
			String title = document.title;
			String body = document.body;
			String auxiliary = document.auxiliary;
			// Create patient in patient table with stored procedure
			String call = "{CALL createReport(?,?,?,?,?)}";
			// Insert values with stored procedure
			CallableStatement procedureCall = connector.prepareCall(call);
			procedureCall.setString(1, title);
			procedureCall.setString(5, type);
			procedureCall.setString(3, body);
			procedureCall.setString(4, auxiliary);
			procedureCall.setInt(2, patientId);
			
			procedureCall.executeUpdate();
			ResultSet res = procedureCall.executeQuery();
			if (!res.next()) {
				throw new DatabaseException("Failed to get created patient id.");
			}
			return res.getInt("id");
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public void updatePatient(PatientInformation information) throws DatabaseException {
		try {
			String name = information.fullName;
			String address = information.address;
			int id = information.patientId;
			LocalDate dateOfBirth = information.dateOfBirth;
			Date dateOfBirthNotLocal;
			dateOfBirthNotLocal = Date.valueOf(dateOfBirth);
			int doctorId = information.doctorId;
			int totalAmountDue = information.totalMoneyOwed;
			int insuranceDeductible = information.insurance.deductible;
			int insuranceCostSharePercentage = information.insurance.costSharePercentage;
			int insuranceOutOfPocketMaximum = information.insurance.outOfPocketMaximum;
			// Create patient in patient table with stored procedure
			String call = "{CALL updatePatient(?,?,?,?,?,?,?,?,?)}";
			// Insert values with stored procedure
			CallableStatement procedureCall = connector.prepareCall(call);
			procedureCall.setString(1, name);
			procedureCall.setString(2, address);
			procedureCall.setInt(3, id);
			procedureCall.setDate(4, dateOfBirthNotLocal);
			procedureCall.setInt(5, doctorId);
			procedureCall.setInt(6, totalAmountDue);
			procedureCall.setInt(7, insuranceDeductible);
			procedureCall.setInt(8, insuranceCostSharePercentage);
			procedureCall.setInt(9, insuranceOutOfPocketMaximum);
			
			procedureCall.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public void updateMedicalDocument(MedicalDocument document) throws DatabaseException {
		try {
			// Initialize attributes of report to enter
			int id = document.documentId;
			int patientId = document.patientId;
			String type = document.type;
			String title = document.title;
			String body = document.body;
			String auxiliary = document.auxiliary;
			// Create patient in patient table with stored procedure
			String call = "{CALL updateReport(?,?,?,?,?,?)}";
			// Insert values with stored procedure
			CallableStatement procedureCall = connector.prepareCall(call);
			procedureCall.setString(1, title);
			procedureCall.setString(2, type);
			procedureCall.setString(3, body);
			procedureCall.setString(4, auxiliary);
			procedureCall.setInt(5, patientId);
			procedureCall.setInt(6, id);
			
			procedureCall.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public boolean verifyCredentials(int id, String password) throws DatabaseException {
		//Create the executable SQL statement
		String call = "SELECT password, class, name FROM accounts WHERE id=?";
		// Sets parameter(s) for stored procedure call
		PreparedStatement callableStatement;
		try {
			callableStatement = connector.prepareStatement(call);
			//callableStatement = connector.prepareCall(call);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		try {
			callableStatement.setInt(1, id);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		//procedureCall.setString(2, password); // MIGHT END UP PRODUCING AN ERROR (MIGHT NEED TO BE setChar())
		// Executes stored procedure
		ResultSet resultSet;
		try {
			resultSet = callableStatement.executeQuery();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		byte[] correctHash;
		LoginClass accountClass;
		String name;
		try {
			if (resultSet.next()) {
				correctHash = resultSet.getBytes("password");
				if (resultSet.wasNull()) {
					return false;
				}
				name = resultSet.getString("name");
				String accountClassStr = resultSet.getString("class");
				if (accountClassStr.equals("patient")) {
					accountClass = LoginClass.PATIENT;
				} else if (accountClassStr.equals("doctor")) {
					accountClass = LoginClass.DOCTOR;
				} else {
					throw new DatabaseException("Invalid account class: " + accountClassStr);
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		
		byte[] salt = PasswordHasher.getSaltFromHash(correctHash);
		byte[] hashedPassword = hasher.hashPassword(password, salt);
		if (Arrays.equals(hashedPassword, correctHash)) {
			// are the same
			loginClass = accountClass;
			loggedInId = id;
			loggedInName = name;
			return true;
		} else {
			return false;
		}
	}
}
