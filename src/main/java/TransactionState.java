public enum TransactionState {
  BEGIN("begin"),
  ACTIVE("Active"),
  COMMITTED("Committed"),
  ABORTED("Aborted"),
  FAILED("Failed");

  private String state;

  TransactionState(final String state){
    this.state = state;
  }

  public String getState(){
    return this.state;
  }
}
