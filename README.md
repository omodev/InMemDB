## Overview
In-memory database built using java.
The src folder contains the following:
- driver class: containing the main class from which the project is launched
- Transaction class: representing the transaction
- DB class: representing the in-memory DB
- TransactionState class: representing the various states of a transaction (e.g. ACTIVE, FAILED, etc)
- ExceptionMessages class: representing custom exception messages
## Getting Started
To get started, clone this repository locally:

```
https://github.com/omodev/logMeInDB.git
```
## Run
To run the project from the command line:
```
cd target/classes && java driver
```

You can also run the project by running the driver class from your preferred IDE.

## Valid Commands
The valid commands to insert when the program is running are:
- put key value
- put key value transactionID
- get key
- get key TransactionID
- delete key
- delete key TransactionID
- createTransaction TransactionID
- commitTransaction TransactionID
- rollbackTransaction TransactionID
