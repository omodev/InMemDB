import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/*
* Class representing a transaction
* */
public class Transaction {

 // Transaction ID
  private String ID ;

  // Transaction state (e.g. ACTIVE, ABORTED ...)
  private TransactionState state ;

  // Local transaction cache
  private ConcurrentMap<String , String> transactionCache ;

  // Map storing the deleted entries (in case the transaction is committed,
  // the deleted info is stored and deleted from the DB)
  private ConcurrentMap<String , String> deletedEntries ;

  public Transaction(String ID){
    this.ID = ID;
    this.state = TransactionState.BEGIN;
  }

  /*
  * Begin
  * Getters and Setters*/
  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public TransactionState getState(){
    return state;
  }

  public void setState(TransactionState state) {
    this.state = state;
  }

  public ConcurrentMap<String, String> getTransactionCache() {
    return transactionCache;
  }

  public void setTransactionCache(ConcurrentMap<String, String> transactionCache) {
    this.transactionCache = transactionCache;
  }

  public ConcurrentMap<String, String> getDeletedEntries() {
    return deletedEntries;
  }

  public void setDeletedEntries(ConcurrentMap<String, String> deletedEntries) {
    this.deletedEntries = deletedEntries;
  }

  /*
   * End
   * Getters and Setters*/


   // put the key, value into the transaction cache
  public void put(String key, String value) throws Exception{
    try {
      if (key.isEmpty()) {
        throw new Exception(ExceptionMessages.EmptyKey);
      }
    } catch (Exception e){
      System.out.println(e.getLocalizedMessage());
      return ;
    }
    this.transactionCache.put(key, value);
  }

  // get the value of key from transaction cache
  public String get(String key) throws Exception {
    try {
      if (!this.transactionCache.containsKey(key)) {
        throw new Exception(ExceptionMessages.KeyNotFound);
      }
    } catch (Exception e){
      System.out.println(e.getLocalizedMessage());
      return null;
    }
    return this.transactionCache.get(key);
  }

  // delete the key,val from transaction cache
  public void delete(String key) throws Exception{
    try {
      if (!this.transactionCache.containsKey(key)) {
        throw new Exception(ExceptionMessages.KeyNotFound);
      }
    } catch (Exception e){
      System.out.println(e.getLocalizedMessage());
      return ;
    }
    deletedEntries.put(key ,this.transactionCache.remove(key));
    this.transactionCache.remove(key);
  }




  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Transaction that = (Transaction) o;
    return Objects.equals(ID, that.ID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ID);
  }
}
