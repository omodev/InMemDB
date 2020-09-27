import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/*
* Driver class for calling the DB requests
* Valid commands:
* -> put key value
* -> put key value TransactionID
* -> get key
* -> get key TransactionID
* -> delete key
* -> delete key TransactionID
* -> createTransaction TransactionID
* -> commitTransaction TransactionID
* -> rollbackTransaction TransactionID
* */
public class driver {

  public static void main(String[] args) throws Exception {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      try {
        String[] arr;
        arr = br.readLine().split(" ");

        if (arr[0].equalsIgnoreCase("get")) {
          if(arr.length == 2){
            System.out.println(DBManager.get(arr[1]));
          } else if(arr.length == 3){
            System.out.println(DBManager.get(arr[1], arr[2]));
          } else{
            System.out.println("Invalid arguments");
          }
        }
        else if (arr[0].equalsIgnoreCase("put")) {
          if(arr.length == 3){
            DBManager.put(arr[1], arr[2]) ;
          } else if(arr.length == 4){
            DBManager.put(arr[1], arr[2], arr[3]) ;
          } else{
            System.out.println("Invalid arguments");
          }
        }
        else if (arr[0].equalsIgnoreCase("delete")) {
          if(arr.length == 2){
           DBManager.delete(arr[1]);
          } else if(arr.length == 3){
            DBManager.delete(arr[1], arr[2]);
          } else{
            System.out.println("Invalid arguments");
          }
        }
        else if (arr[0].equalsIgnoreCase("createTransaction")) {
          if(arr.length !=2){
            System.out.println("Invalid arguments");
          } else {
            DBManager.createTransaction(arr[1]);
          }
        }
        else if (arr[0].equalsIgnoreCase("rollbackTransaction")) {
          if(arr.length !=2){
            System.out.println("Invalid arguments");
          } else {
            DBManager.rollBackTransaction(arr[1]);
          }
        }
        else if (arr[0].equalsIgnoreCase("CommitTransaction")) {
          if(arr.length !=2){
            System.out.println("Invalid arguments");
          } else {
            DBManager.commitTransaction(arr[1]);
          }
        }
        else if (arr[0].equalsIgnoreCase("END")) {
          break;
        }
        else {
          System.out.println("Invalid command");
        }

      }catch (IOException e) {
        e.printStackTrace();
      }
    }
    }
  }

