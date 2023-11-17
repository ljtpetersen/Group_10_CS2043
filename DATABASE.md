# Database Structure

# Tables
- `accounts`
- `patients`
- `doctors`
- `report`
- `bill`

## `accounts` Table
- Integer `id` field. `INT UNIQUE NOT NULL AUTO_INCREMENT`
- Enumeration `class` field. `ENUM('patient', 'doctor') NOT NULL`
- String `password` field. `BINARY(144)`
- Date `createTimestamp` field. `DATETIME(0) NOT NULL DEFAULT CURRENT_TIMESTAMP`
- Date `modifyTimestamp` field. `DATETIME(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP`

## `patients` Table
- String `name` field. `VARCHAR(50) NOT NULL`
- String `address` field. `VARCHAR(75) NOT NULL`
- Integer `id` field. `INT UNIQUE NOT NULL`
- Date `createTimestamp` field. `DATETIME(0) NOT NULL DEFAULT CURRENT_TIMESTAMP`
- Date `modifyTimestamp` field. `DATETIME(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP`
- Date `dateOfBirth` field. `DATE NOT NULL`
- Integer `doctorId` field. `INT NOT NULL`.
- Integer `totalAmountDue` field. `INT NOT NULL DEFAULT 0`
- Integer `insuranceDeductible` field. `INT NOT NULL`
- Float `insuranceCostSharePercentage` field. `DECIMAL NOT NULL`
- Integer `insuranceOutOfPocketMaximum` field. `INT NOT NULL`

## `doctors` Table
- String `name` field. `VARCHAR(50) NOT NULL`
- Integer `id` field. `INT UNIQUE NOT NULL`
- Date `createTimestamp` field. `DATETIME(0) NOT NULL DEFAULT CURRENT_TIMESTAMP`

## `report` Table
- String `title` field. `VARCHAR(50) NOT NULL`
- String `type` field. `VARCHAR(20) NOT NULL`
- String `body` field. `TEXT NOT NULL`
- String `auxiliary` field. `TEXT`
- Integer `patientId` field. `INT NOT NULL`
- Integer `id` field. `INT NOT NULL UNIQUE AUTO_INCREMENT`
- Date `createTimestamp` field. `DATETIME(0) NOT NULL DEFAULT CURRENT_TIMESTAMP`
- Date `modifyTimestamp` field. `DATETIME(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP`

## `transactions` Table
- Integer `patientId` field. `INT NOT NULL`
- String `description` field. `TEXT NOT NULL`
- String `title` field. `VARCHAR(50) NOT NULL`
- Date `createTimestamp` field. `DATETIME(0) NOT NULL DEFAULT CURRENT_TIMESTAMP`
- Integer `id` field. `INT NOT NULL UNIQUE AUTO_INCREMENT`
- Integer `amount` field. `INT NOT NULL`
- Integer `amountPaid` field. `INT`
