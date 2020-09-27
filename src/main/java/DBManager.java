import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Class representing the DB manager responsible for all the command calls (put, get , commitTransaction ...)
 * */
public class DBManager {

  // DB instance
  public static DB db = new DB(new ConcurrentHashMap<String, String>());
  // Map of <TransactionID, transaction>
  public static ConcurrentMap<String , Transaction> transactions = new ConcurrentHashMap<>();


  // get from the global memory
  public static String get(String key) throws Exception{
    if(!db.getInMemoryDB().containsKey(key)){
      return null;
    }
    return db.getInMemoryDB().get(key);
  }

  // put in the global memory
  public static void put(String key, String value) throws Exception{
    try {
      // check for conflict. i.e. changing a value already stored in the DB
      if (db.getInMemoryDB().containsKey(key) && !db.getInMemoryDB().get(key).equals(value)) {
        throw new Exception(ExceptionMessages.Conflict);
      }
      // Check of the key is empty
      if(key.isEmpty()){
        throw new Exception(ExceptionMessages.EmptyKey);
      }
    } catch (Exception e){
      System.out.println(e.getLocalizedMessage());
      return ;
    }
    // put in the DB map
    db.getInMemoryDB().put(key, value);
  }

  // delete from the DB
  public static void delete(String key) throws Exception{
    try {
      // Check if key exists
      if (!db.getInMemoryDB().containsKey(key)) {
        throw new Exception(ExceptionMessages.KeyNotFound);
      }
    } catch (Exception e){
      System.out.println(e.getLocalizedMessage());
      return ;
    }
    // remove from DB map
     db.getInMemoryDB().remove(key);
  }

  // get from within the transaction
  public static String get(String key, String transactionID) throws Exception {
    try {
      // check whether the transaction is null or not active
      if (transactions.get(transactionID) == null ||
        transactions.get(transactionID).getState() != TransactionState.ACTIVE) {
        throw new Exception(ExceptionMessages.TransactionNotFound);
      }
    } catch (Exception e){
      System.out.println(e.getLocalizedMessage());
      return null ;
    }
    // get value if it is already stored in DB (global map)
    if(db.getInMemoryDB().containsKey(key)){
      return get(key);
    }
    // get value from local transaction map
    return transactions.get(transactionID).get(key);
  }

  // put key,value in transaction local cache
  public static void put(String key, String value, String transactionID) throws Exception{
    try {
      // check if transaction does not exist or not valid
      if (transactions.get(transactionID) == null ||
        transactions.get(transactionID).getState() != TransactionState.ACTIVE) {
        throw new Exception(ExceptionMessages.TransactionNotFound);
      }
    } catch(Exception e){
      System.out.println(e.getLocalizedMessage());
      return ;
    }
    // put key,val in local transaction cache
     transactions.get(transactionID).put(key, value);
  }

  // delete key,val from local transaction cache
  public static void delete(String key, String transactionID) throws Exception{
    try {
      // check if transaction does not exist or not valid
      if (transactions.get(transactionID) == null ||
        transactions.get(transactionID).getState() != TransactionState.ACTIVE) {
        throw new Exception(ExceptionMessages.TransactionNotFound);
      }
    } catch (Exception e){
      System.out.println(e.getLocalizedMessage());
      return ;
    }
    // delete key,value from local cache
    transactions.get(transactionID).delete(key);
  }

  // create a transaction with the given TransactionID
  public static void createTransaction(String transactionID) throws Exception{
    try {
      // check if TransactionID is empty
      if (transactionID.isEmpty()) {
        throw new Exception(ExceptionMessages.EmptyTransactionID);
      }
    } catch (Exception e){
      System.out.println(e.getLocalizedMessage());
      return ;
    }
    // check if transaction is not already created and instantiate its local maps
    if(!transactions.containsKey(transactionID)){
      transactions.put(transactionID, new Transaction(transactionID));
      transactions.get(transactionID).setTransactionCache(new ConcurrentHashMap<String, String>());
      transactions.get(transactionID).setDeletedEntries(new ConcurrentHashMap<String, String>());
    }
    try {
      // check if transaction already created (active)
      if (transactions.get(transactionID).getState() == TransactionState.ACTIVE) {
        throw new Exception(ExceptionMessages.TransactionAlreadyCreated);
      }
    } catch (Exception e){
      System.out.println(e.getLocalizedMessage());
      return ;
    }
    // set transaction state to active
    transactions.get(transactionID).setState(TransactionState.ACTIVE);
  }

  // rollback a transaction with the given TransactionID
  public static void rollBackTransaction(String transactionID) throws Exception{
    try {
      // check if transaction exists
      if (!transactions.containsKey(transactionID)) {
        throw new Exception(ExceptionMessages.TransactionNotFound);
      }
      // check if transaction is active (I assume that only an active transaction can be rolled back)
      if (transactions.get(transactionID).getState() != TransactionState.ACTIVE) {
        throw new Exception(ExceptionMessages.CannotRollBack);
      }
    } catch (Exception e){
      System.out.println(e.getLocalizedMessage());
      return ;
    }
    // set state to ABORTED
    transactions.get(transactionID).setState(TransactionState.ABORTED);
  }

  // commit transaction with the given transactionID
  public static void commitTransaction(String transactionID) throws Exception{
    try {
      // check if transaction exists
      if (!transactions.containsKey(transactionID)) {
        throw new Exception(ExceptionMessages.TransactionNotFound);
      }
      // check if transaction is not active (cannot commit a transaction that is not active)
      if (transactions.get(transactionID).getState() != TransactionState.ACTIVE) {
        throw new Exception(ExceptionMessages.CannotCommit);
      }
      // check for conflicts (go through all the keys of global map and compare values
      for (String key : transactions.get(transactionID).getTransactionCache().keySet()) {
        if (db.getInMemoryDB().containsKey(key)) {
          if (!db.getInMemoryDB().get(key).equals(transactions.get(transactionID).get(key))) {
            transactions.get(transactionID).setState(TransactionState.FAILED);
            throw new Exception(ExceptionMessages.Conflict);
          }
        }
      }
    } catch (Exception e){
      System.out.println(e.getLocalizedMessage());
      return ;
    }
    // If no conflict found, put all the entries of the transaction local cache into the global DB map
    db.getInMemoryDB().putAll(transactions.get(transactionID).getTransactionCache());

    // loop through the transaction deleted entries and delete all the matched keys in the global DB
    for (String key: transactions.get(transactionID).getDeletedEntries().keySet()) {
      delete(key);
    }
    // set transaction state to committed
    transactions.get(transactionID).setState(TransactionState.COMMITTED);
  }

// DB getter
  public static DB getDb() {
    return db;
  }

  // DB setter
  public static void setDb(DB db) {
    DBManager.db = db;
  }

  /*
  *  Singleton  --> We want only one instance of the DBManager to be created
  * */
  private static final DBManager SINGLE_INSTANCE = new DBManager(db);
  private DBManager(DB db) {this.db = db;}
  public static DBManager getInstance() {
    return SINGLE_INSTANCE;
  }

}
