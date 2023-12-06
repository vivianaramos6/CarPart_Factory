package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import data_structures.HashTableSC;
import data_structures.LinkedStack;
import data_structures.SinglyLinkedList;
import data_structures.BasicHashFunction;
import interfaces.HashFunction;
import interfaces.List;
import interfaces.Map;
import interfaces.Stack;

public class CarPartFactory {
	
	private List<PartMachine>machines;
	private List<Order>orders;
	private Map<Integer, CarPart>partCatalog;
	private Map<Integer, List<CarPart>>inventory;
	private Map<Integer,Integer> defectives;
	private Stack<CarPart> productionBin;
	
	
	/**
	 * CarPartFactory constructor, the production bin is initiated as linkedStack
	 * and defectives as a map the holds <ids, amount of defectives> and it was first filled with zeros 
	 * @param OrderPath, 
	 * @param partsPath
	 * @throws IOException
	 * 
	 * 
	 */
        
    public CarPartFactory(String orderPath, String partsPath) throws IOException {
                setupMachines(partsPath);
                setupOrders(orderPath);
                setupInventory();
                productionBin=new LinkedStack<CarPart>();
                defectives= new HashTableSC<Integer,Integer>(partCatalog.size(), new BasicHashFunction());
                fillDefective(defectives);
    }
   
    
    
    public List<PartMachine> getMachines() {
       return this.machines;
    }
    public void setMachines(List<PartMachine> machines) {
        this.machines=machines;
    }
    public Stack<CarPart> getProductionBin() {
    	return this.productionBin;
      
    }
    public void setProductionBin(Stack<CarPart> production) {
       this.productionBin=production;
    }
    public Map<Integer, CarPart> getPartCatalog() {
        return this.partCatalog;
    }
    public void setPartCatalog(Map<Integer, CarPart> partCatalog) {
        this.partCatalog=partCatalog;
    }
    public Map<Integer, List<CarPart>> getInventory() {
       return this.inventory;
    }
    public void setInventory(Map<Integer, List<CarPart>> inventory) {
        this.inventory=inventory;
    }
    public List<Order> getOrders() {
        return this.orders;
    }
    public void setOrders(List<Order> orders) {
        this.orders=orders;
    }
    public Map<Integer, Integer> getDefectives() {
        return this.defectives;
    }
    public void setDefectives(Map<Integer, Integer> defectives) {
        this.defectives=defectives;
    }
    /**
     * Filled the defectives with 0
     * @param defectives - a map that will hold the defective parts and their ids
     */
    
    public void fillDefective(Map<Integer,Integer>defectives) {
    	for(Integer keys: partCatalog.getKeys()) {
    		defectives.put(keys, 0);
    	}
    }
    /**
     * 
     * @param path
     * @throws IOException
     * 
     * This method runs through the orders file,first  the requestedParts map is created and holds the ids as keys and amount of
     * parts value. The order is the constructed with the parts of the line_list made in the split
     * 
     */

    public void setupOrders(String path) throws IOException {
     BufferedReader orderReader= new BufferedReader(new FileReader(path));
     orders=new SinglyLinkedList<Order>();
    	String line=orderReader.readLine();
		line=orderReader.readLine();
		while(line!=null) {
			
			String[]line_list=line.split(",");
			String[]tuples=line_list[2].split("-");
			Map<Integer,Integer>requestedParts=new HashTableSC<Integer,Integer>(tuples.length, new BasicHashFunction());
			int counter=0;
			while(counter<tuples.length) {
				String noParen=tuples[counter].substring(1,tuples[counter].length()-1);
				String[] separated=noParen.split(" ");
				Integer ID=Integer.parseInt(separated[0]);
				Integer partsAmount=Integer.parseInt(separated[1]);
				requestedParts.put(ID, partsAmount);
				counter++;
				}
			Integer personID=Integer.parseInt(line_list[0]);
			String name=line_list[1];
			
			orders.add(new Order(personID, name, requestedParts, false));
			line=orderReader.readLine();
			}
			orderReader.close();
    }
    /**
     * 
     * @param path
     * @throws IOException
     * 
     * This method runs through the parts file and counts the amounts of line in the file
     * to use as the size of the catalog. It holds the ID and the carpart. Then the carparts
     * are constructed and added to the partCatalog. The machines are then added to the machines list
     */
    
    public void setupMachines(String path) throws IOException {
    	
    	 BufferedReader machinesReader= new BufferedReader(new FileReader(path));
    	 long lineCount=machinesReader.lines().count();
    	 int catalogSize=(int)lineCount-1;
    	 partCatalog=new HashTableSC<Integer, CarPart>(catalogSize,new BasicHashFunction());
    	 machines=new SinglyLinkedList<PartMachine>();
    	 machinesReader.close();
    	 machinesReader= new BufferedReader(new FileReader(path));
    	 
    	 String line=machinesReader.readLine();
    	 line=machinesReader.readLine();
 		
 		while(line!=null) {
 			String[]line_list=line.split(",");
 			int sharedID=Integer.parseInt(line_list[0]);
 			String partName=line_list[1];
 			Double partWeight=Double.parseDouble(line_list[2]);
 			Double weightError=Double.parseDouble(line_list[3]);
 			int period=Integer.parseInt(line_list[4]);
 			int chanceOfDefective=Integer.parseInt(line_list[5]);
 			Boolean defective=false;
 			
 			CarPart createdPart=new CarPart(sharedID, partName, partWeight, defective);
 			partCatalog.put(sharedID, createdPart);
 			PartMachine createdMachine= new PartMachine(sharedID, createdPart, period, weightError, chanceOfDefective);
 			machines.add(createdMachine);
 			line=machinesReader.readLine();
 		}
 		machinesReader.close();
    }
    /*
     * The inventory is created it holds the part's id as a key and as a value a list that has the amount of parts
     * in the inventory.
     */
    
    public void setupInventory() {
        inventory=new HashTableSC<Integer, List<CarPart>>(partCatalog.size(),new BasicHashFunction());
        for(Integer key : partCatalog.getKeys()) {
        	List<CarPart> emptyList=new SinglyLinkedList<CarPart>();
        	inventory.put(key, emptyList);	
        }
    }
    /*
     * If part is not defective it is added to the inventory, if it is then it is added to defectives
     * and the counter is increased;
     */
    public void storeInInventory() {
    	 while(!productionBin.isEmpty()) {
      	   CarPart topPart=productionBin.top();
      	   if(!topPart.isDefective()) {
      		   inventory.get(topPart.getId()).add(topPart); 
      	   }
      	   else{
      		   defectives.put(topPart.getId(),defectives.get(topPart.getId())+1);
      	   }
      	   productionBin.pop();
      	   
      	  
      	   
         }
    }
    /*
     * For each day parts are produced until the method runs out of minutes for the day, it then 
     * proceeds to push all the remaining parts in the conveyor belt to the production bin
     * At the end of the day the conyeyor belt is reseted
     * The production bin is then moved to the inventory and the orders are processed to fulfill customer orders
     */
    public void runFactory(int days, int minutes) {
        for(int i=0;i<days;i++) {
        	for(int j=0;j<minutes;j++) {
        		for(PartMachine machine: machines) {
        			CarPart producedPart=machine.produceCarPart();
        			if(producedPart!=null) {
        				productionBin.push(producedPart);
        			}
        			
        		}
        	}	
        
        	for(PartMachine machine:machines) {
        		while(!machine.getConveyorBelt().isEmpty()) {
        			if(machine.getConveyorBelt().front()!=null){
        				productionBin.push(machine.getConveyorBelt().dequeue());
        			}
        			else {
        				machine.getConveyorBelt().dequeue();
        			}    			
        		}
        		
        		machine.resetConveyorBelt(); 		
        	}
        	
        	}
        	storeInInventory();
        	processOrders();
        	
		}

    
    
    /**
     * Processes orders to check and fulfill requested parts based on inventory availability.
     *
     * This method iterates over the orders, checks the availability of requested parts
     * in the inventory, and fulfills the order if there is sufficient quantity in stock.
     *
     * @param orders The list of orders to process.
     */
   
    public void processOrders() {
    
    	int orderIndex = 0;
    	while (orderIndex < this.getOrders().size()) {
        Order order = this.getOrders().get(orderIndex);
        boolean happyCustomer = true;

    	 for (int partId : order.getRequestedParts().getKeys()) {
    	    if (this.getInventory().containsKey(partId)) {
    	       int availableQuantity = this.getInventory().get(partId).size();
    	       int requestedQuantity = order.getRequestedParts().get(partId);

    	         if (requestedQuantity > availableQuantity) {
    	                happyCustomer = false;
    	                break;
    	            }
    	        } else {
    	            happyCustomer = false;
    	            break;
    	        }
    	    }

    	    if (happyCustomer) {
    	        order.setFulfilled(true);

    	       for (int partId : order.getRequestedParts().getKeys()) {
    	            List<CarPart> partList = this.getInventory().get(partId);
    	            int requested= order.getRequestedParts().get(partId);

    	            int counter = partList.size() - 1;
    	            while (counter >= 0 && requested > 0) {
    	                this.getInventory().get(partId).remove(counter);
    	                counter--;
    	                requested--;
    	            }
    	        }
    	    }

    	    orderIndex++;
    	}
    }

	  
    /**
     * Generates a report indicating how many parts were produced per machine,
     * how many of those were defective and are still in inventory. Additionally, 
     * it also shows how many orders were successfully fulfilled. 
     */
    public void generateReport() {
        String report = "\t\t\tREPORT\n\n";
        report += "Parts Produced per Machine\n";
        for (PartMachine machine : this.getMachines()) {
            report += machine + "\t(" + 
            this.getDefectives().get(machine.getPart().getId()) +" defective)\t(" + 
            this.getInventory().get(machine.getPart().getId()).size() + " in inventory)\n";
        }
       
        report += "\nORDERS\n\n";
        for (Order transaction : this.getOrders()) {
            report += transaction + "\n";
        }
       System.out.println(report);
    }

   



}
