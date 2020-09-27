import java.util.concurrent.ConcurrentMap;


/*
 * Class representing the inMemory DB
 * */
public class DB {

  // Global DB memory
  private ConcurrentMap<String , String> inMemoryDB ;


  public DB(ConcurrentMap<String, String> inMemoryDB){
    this.inMemoryDB = inMemoryDB;
  }


  /*
   * Begin
   * Getters and Setters
   * */
  public ConcurrentMap<String, String> getInMemoryDB() {
    return inMemoryDB;
  }

  public void setInMemoryDB(ConcurrentMap<String, String> inMemoryDB) {
    this.inMemoryDB = inMemoryDB;
  }

  /*
   * End
   * Getters and Setters
   * */

}
