package main;

import interfaces.Map;

public class Order {
	private int Id;
	private String customerName; 
	private Map<Integer, Integer> requestedParts;
	private boolean fulfilled;
	
	/**
	 * Constructor for the Order class.
	 *
	 * @param id The unique identifier for the Order
	 * @param customerName The name of the customer placing the Order
	 * @param requestedParts A map representing the parts and their quantities requested 
	 * @param fulfilled A boolean indicating whether the Order has been fulfilled or not
	 */
   
    public Order(int id, String customerName, Map<Integer, Integer> requestedParts, boolean fulfilled) {
        this.Id=id;
        this.customerName=customerName;
        this.requestedParts=requestedParts;
        this.fulfilled=fulfilled;
        
    }
    public int getId() {
    	return this.Id;
         
    }
    public void setId(int id) {
        this.Id=id;
    }
    public boolean isFulfilled() {
      return fulfilled;
    }
    public void setFulfilled(boolean fulfilled) {
        this.fulfilled=fulfilled;
    }
    public Map<Integer, Integer> getRequestedParts() {
       return this.requestedParts;
    }
    public void setRequestedParts(Map<Integer, Integer> requestedParts) {
       this.requestedParts=requestedParts;
    }
    public String getCustomerName() {
      return this.customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName=customerName;
    }
    /**
     * Returns the order's information in the following format: {id} {customer name} {number of parts requested} {isFulfilled}
     */
    @Override
    public String toString() {
        return String.format("%d %s %d %s", this.getId(), this.getCustomerName(), this.getRequestedParts().size(), (this.isFulfilled())? "FULFILLED": "PENDING");
    }

    
    
}
