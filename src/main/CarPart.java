package main;

public class CarPart {
	private int Id;
	private String name;
	private double weight;
	private boolean isDefective;
	
    
	/**
	 * Constructor for the CarPart class
	 *
	 * @param id The unique identifier for the CarPart
	 * @param name The name of the CarPart
	 * @param weight The weight of the CarPart
	 * @param isDefective A boolean indicating whether the CarPart is defective or not
	 */
	
    public CarPart(int id, String name, double weight, boolean isDefective) {
    	this.Id=id;
    	this.name=name;
    	this.weight=weight;
    	this.isDefective=isDefective;
    	
        
    }
    public int getId() {
        return this.Id;
    }
    public void setId(int id) {
    	this.Id=id;
    }
    public String getName() {
    	return this.name;
        
    }
    public void setName(String name) {
        this.name=name;
    }
    public double getWeight() {
      return this.weight;
    }
    public void setWeight(double weight) {
      this.weight=weight;
    }

    public boolean isDefective() {
    	return isDefective;
    }
    public void setDefective(boolean isDefective) {
        this.isDefective=isDefective;
    }
    /**
     * Returns the parts name as its string representation
     * @return (String) The part name
     */
    public String toString() {
        return this.getName();
    }
}