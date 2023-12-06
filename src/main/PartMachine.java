package main;

import interfaces.Queue;
import java.util.Random;

import data_structures.ListQueue;


public class PartMachine {
	private int Id;
	private CarPart p1;
	private int period;
	private double weightError;
	private int chanceOfDefective;
	private Queue<Integer> timer;
	private Queue<CarPart> conveyorBelt;
	private int totalPartsProduced=0;
	
		
	/**
	 * Constructor for the PartMachine class.
	 *
	 * @param id The unique identifier for the PartMachine.
	 * @param p1 The CarPart associated with the PartMachine.
	 * @param period The time period for the machine to process each CarPart.
	 * @param weightError The permissible weight error for the CarPart processing.
	 * @param chanceOfDefective The likelihood of producing a defective CarPart.
	 */
   
    public PartMachine(int id, CarPart p1, int period, double weightError, int chanceOfDefective) {
        this.Id=id;
        this.p1=p1;
        this.period=period;
        this.weightError=weightError;
        this.chanceOfDefective=chanceOfDefective;
        this.conveyorBelt=new ListQueue<CarPart>();
        this.timer= new ListQueue<Integer>();
       
        int time=this.period-1;
	while(time>=0) {
    	   this.timer.enqueue(time);
    	   time--;
        }
	for(int i=0;i<10;i++) {
		this.conveyorBelt.enqueue(null);
		}
        
    }
    
    
    public int getId() {
       return this.Id;
       
    }
    public void setId(int id) {
        this.Id=id;
    }
    public Queue<Integer> getTimer() {
       return this.timer;
    }
    public void setTimer(Queue<Integer> timer) {
        this.timer=timer;
    }
    public CarPart getPart() {
       return this.p1;
    }
    public void setPart(CarPart part1) {
        this.p1=part1;
    }
    public Queue<CarPart> getConveyorBelt() {
        return this.conveyorBelt;
    }
    public void setConveyorBelt(Queue<CarPart> conveyorBelt) {
    	this.conveyorBelt=conveyorBelt;
    }
    public int getTotalPartsProduced() {
         return this.totalPartsProduced;
    }
    public void setTotalPartsProduced(int count) {
    	this.totalPartsProduced=count;
    }
    public double getPartWeightError() {
        return this.weightError;
    }
    public void setPartWeightError(double partWeightError) {
        this.weightError=partWeightError;
    }
    public int getChanceOfDefective() {
        return this.chanceOfDefective;
    }
    public void setChanceOfDefective(int chanceOfDefective) {
        this.chanceOfDefective=chanceOfDefective;
    }
    public void resetConveyorBelt() {
    	int length=conveyorBelt.size();
        conveyorBelt.clear();
        int counter=0;
        while(counter<length) {
        	conveyorBelt.enqueue(null);
        	counter++;
        }
        
    }
    /**
     * Advances the timer by one tick
     * This method simulates the passage of time in the PartMachine's timer queue.
     *
     * @return The value at the front of the timer queue      */
    public int tickTimer() {
       int frontValue=this.timer.front();
       this.timer.enqueue(this.timer.dequeue());
       return frontValue;
    }
    
    /**
     * Produces a CarPart based on the PartMachine's timer and production
     * If the timer is not zero, null is added to the conveyor belt,
     * If the timer is zero, a new CarPart is produced,
     * 
     *
     * @return The CarPart that is produced, either a new part or null depending on the timer.
     */
    public CarPart produceCarPart() {
       if (tickTimer()!=0) {
    	   conveyorBelt.enqueue(null);
    	   
       }
       else {
    	   boolean defectiveCheck=false;
    	   int defectiveness=totalPartsProduced%chanceOfDefective;
    	   if(defectiveness==0) {
    		   defectiveCheck=true;
    	   }
    	   
    	   double lowWeightLimit=this.getPart().getWeight()-this.getPartWeightError();
    	   double upWeightLimit=this.getPart().getWeight()+this.getPartWeightError();
    	   
    	   Random randomizer=new Random();
    	   double randomizedValue=lowWeightLimit + (upWeightLimit - lowWeightLimit) * randomizer.nextDouble();
           
    	   
    	   
    	   CarPart newPart=new CarPart(this.getPart().getId(),this.getPart().getName(), randomizedValue, defectiveCheck);
    	   totalPartsProduced++;
    	   conveyorBelt.enqueue(newPart);
       
       }
       return conveyorBelt.dequeue();
       
    }
    
    /**
     * Returns string representation of a Part Machine in the following format:
     * Machine {id} Produced: {part name} {total parts produced}
     */
    @Override
    public String toString() {
        return "Machine " + this.getId() + " Produced: " + this.getPart().getName() + " " + this.getTotalPartsProduced();
    }
    /**
     * Prints the content of the conveyor belt. 
     * The machine is shown as |Machine {id}|.
     * If the is a part it is presented as |P| and an empty space as _.
     */
    public void printConveyorBelt() {
        // String we will print
        String str = "";
        // Iterate through the conveyor belt
        for(int i = 0; i < this.getConveyorBelt().size(); i++){
            // When the current position is empty
            if (this.getConveyorBelt().front() == null) {
                str = "_" + str;
            }
            // When there is a CarPart
            else {
                str = "|P|" + str;
            }
            // Rotate the values
            this.getConveyorBelt().enqueue(this.getConveyorBelt().dequeue());
        }
        System.out.println("|Machine " + this.getId() + "|" + str);
    }
}
