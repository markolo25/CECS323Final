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
	 * Due: 12/8 by 12PM
	 */
public class main {
		
		private final static Logger LOGGER = Logger.getLogger(main.class.getName());
		private final static String DB_DRIVER = "com.mysql.jdbc.Driver";
	    private final static String DB_URL = "jdbc:mysql://infoserver.cecs.csulb.edu:3306/";
	    
	    private Connection connection = null;
	    Scanner managerInput = new Scanner(System.in);
	    
	    
	    public main() {
	    	try
	        {
	            Class.forName(DB_DRIVER);
	        }//end try
	        catch(ClassNotFoundException e)
	        {
	            LOGGER.log(Level.SEVERE, "Loading JDBC driver failed. Reason: {0}", e);
	            System.exit(1);
	        }//end catch
	    }
	    
	    
	    public void connectToDatabase() {
	    	 try
	         {
	             System.out.println("\nPlease enter username and password for CECS 323 database.");
	             System.out.println("Username: ");
	             String dbManager = managerInput.nextLine();
	             System.out.print("Password: ");
	             String dbPassword = managerInput.nextLine();
	             
	             connection = DriverManager.getConnection(DB_URL, dbManager, dbPassword);
	             connection.setAutoCommit(false);
	         }//end try
	         catch(SQLException e)
	         {
	             LOGGER.log(Level.SEVERE, "Connection to database failed. Reason: ", e.getMessage());
	             connection = null;
	         }//end catch
	    }
	    
	    public void disconnectFromDatabase() {
	    	try
	        {
	            connection.close();
	        }//end try
	        catch(SQLException e)
	        {
	            LOGGER.log(Level.SEVERE, "Disconnection failed. Reason: ", e.getMessage());
	        }//end catch
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
					"1. " +
					"2. " +
					"3. " +
					"0. Return to Main Menu");
	    	
	    }
	    
	    public void mainMenu() throws SQLException {
	    	
	    	Scanner menuScanner = new Scanner(System.in);
	        int number;
	        do {
	        	displayMenu();
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
