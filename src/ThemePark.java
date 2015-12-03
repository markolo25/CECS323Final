	import com.mysql.jdbc.Driver;
	import java.sql.*;
	import java.util.Scanner;
	import java.util.logging.*;

	/**
	 * 
	 * @Author Mark Mendoza
	 * 		   Zachary Berg
	 * 		   Daryl Blancaflor
	 * 		   Romeo Garalde, Jr.
	 * 
	 * @Project Theme Park
	 * 		 	Disney AF
	 * 		
	 * @Class CECS 323
	 * 		  Jenny Li Jiang
	 * 
	 * @username: cecs323j11
	 * @passsword: Ehohwi - only use at home 
	 * 
	 * Due: 12/8 by 12PM
	 */
public class ThemePark {
		
		private final static Logger LOGGER = Logger.getLogger(ThemePark.class.getName());
		private final static String DB_DRIVER = "com.mysql.jdbc.Driver";
	    private final static String DB_URL = "jdbc:mysql://cecs-db01.coe.csulb.edu:3306/cecs323j11";
	    
	    private Connection connection = null;
	    Scanner managerInput = new Scanner(System.in);
	    
	    public static void main(String[] args) throws SQLException, ClassNotFoundException {
	    	LOGGER.setLevel(Level.INFO);
	        ThemePark themePark = new ThemePark();
	        themePark.connectToDatabase();
	        
	        if(themePark.isConnected()) {
	            themePark.mainMenu();
	        }
	        else {
	           System.out.println("\nNo Connection!");
	        }
	    }
	    
	    public ThemePark() {
	    	try {	
	    		Class.forName(DB_DRIVER);
	    	}
	    	catch(ClassNotFoundException e) {
	    		LOGGER.log(Level.SEVERE, "Loading JDBC driver failed. Reason: {0}", e);
	        	System.exit(1);
	    	}
	    }
	    
	    
	    public void connectToDatabase() {
	    	 try {
	             System.out.println("\nPlease enter username and password for CECS 323 database.");
	             System.out.print("Username: ");
	             String dbManager = managerInput.nextLine();
	             System.out.print("Password: ");
	             String dbPassword = "";//managerInput.nextLine();
	             
	             connection = DriverManager.getConnection(DB_URL, dbManager, dbPassword);
	             System.out.println("eHAGBG");
	             //connection.setAutoCommit(false);
	         }
	         catch(SQLException e)
	         {
	             LOGGER.log(Level.SEVERE, "Connection to database failed. Reason: ", e.getMessage());
	             connection = null;
	         }
	    }
	    
	    public void disconnectFromDatabase() {
	    	try {
	            connection.close();
	        }
	        catch(SQLException e) {
	            LOGGER.log(Level.SEVERE, "Disconnection failed. Reason: ", e.getMessage());
	        }
	    }
	    
	    public boolean isConnected() {
	        return connection != null;
	    }
	    
	    public String displayMenu() {
	    	
	    	return( "\tMain Menu\n" +
	    			"1. Quereies\n" +
	    			"2. Add a new row\n" +
	    			"3. Remove a row\n" +
	    			"4. Commit Changes\n" +
	    			"5. Rollback Changes\n" + 
	    			"0. Exit\n" +
	    			"Please enter in a number (0 to exit): ");
	    				
	    	
	    }
	    
	    public String displayQueueryMenu() {
			return( "Queuery Menu\n" +
					"1. List information on Employees with the earliest birthday. " +
					"2. List customers with the highest reward points" +
					"3. " +
					"0. Return to Main Menu");
	    	
	    }
	    
	    public void mainMenu() throws SQLException {
	    	
	    	Scanner menuScanner = new Scanner(System.in);
	        int number;
	        do {
	        	System.out.print(displayMenu());
	            while (!menuScanner.hasNextInt()) {
	                System.out.println("That's not a number!");
	                menuScanner.next(); // this is important!
	            }
	            number = menuScanner.nextInt();
	            
	            switch(number) {
	            case 1:
	            	break;
	            case 2:
	            	break;
	            case 3:
	            	break;
	            case 4: commit();
	            	break;
	            case 5: rollback();
	            	break;
	            case 0:
	            	break;
	            default: System.out.println("\n Please enter in a number 1 - 5 or 0 to exit...\n");
	            	break;
	            
	            }
	            
	        } while (number <= 0);
	    	
	    }
	    
	    public void quereyMenu() {
	    	
	    	Scanner queueryScanner = new Scanner(System.in);
	        int number;
	        do {
	        	displayMenu();
	            while (!queueryScanner.hasNextInt()) {
	                System.out.println("That's not a number!");
	                queueryScanner.next(); // this is important!
	            }
	            number = queueryScanner.nextInt();
	            
	            switch(number) {
	            case 1:
	            	break;
	            case 2:
	            	break;
	            case 3:
	            	break;
	            case 0:
	            	break;
	            default: System.out.println("\n Please enter in a number 1 - 3 or 0 to return to Main Menu...\n");
	            	break;
	            
	            }
	            
	        } while (number <= 0);
	    	
	    }
	    
	    public void add() {
	    	
	    }
	    
	    public void remove() {
	    	
	    }
	    
	    public void commit() throws SQLException {
	    	connection.commit();
	    }
	    
	    public void rollback() throws SQLException {
	    	connection.rollback();
	    	
	    }
}
