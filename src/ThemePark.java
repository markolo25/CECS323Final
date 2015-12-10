import com.mysql.jdbc.Driver;
import java.sql.*;
import java.util.Scanner;
import java.util.logging.*;

/**
 * 
 * @Author Mark Mendoza Zachary Berg Daryl Blancaflor Romeo Garalde, Jr.
 * 
 * @Project Theme Park Disney AF
 * 
 * @Class CECS 323 Jenny Li Jiang
 * 
 * @username: cecs323j11
 * @passsword: Ehohwi - only use at home
 * 
 *             Due: 12/8 by 12PM
 */
public class ThemePark {

	private final static Logger LOGGER = Logger.getLogger(ThemePark.class.getName());
	private final static String DB_DRIVER = "com.mysql.jdbc.Driver";
	private final static String DB_URL = "jdbc:mysql://cecs-db01.coe.csulb.edu:3306/cecs323j11";
	
	
	//Lists the employees with the earliest birthday
	private final static String LIST_EMPLOYEE_EARLY_BIRTHDAY = 
			"SELECT p.per_id AS 'ID', p.per_fName AS 'First Name', per_lName AS 'Last Name', per_dateOfBirth AS 'Date of Birth',\n" +
			"a.ad_address AS 'Address', a.ad_city AS 'City', a.ad_state AS 'State', a.ad_zip AS 'Zip', a.ad_type AS 'Address Type',\n" +
			"ph.ph_number AS 'Phone Number', ph.ph_type AS 'Phone', e.emp_id AS 'Employee ID' , e.emp_jobTitle AS 'Job Title', e.emp_salary AS 'Salary'\n" +
			"FROM Person p\n" +
			"LEFT OUTER JOIN Address a ON p.per_id = a.ad_per_id\n" +
			"LEFT OUTER JOIN Phone ph on p.per_id = ph.ph_per_id\n" +
			"LEFT OUTER JOIN Employee e ON e.emp_per_id = p.per_id\n" +
			"WHERE per_dateOfBirth = (SELECT MIN(per_dateOfBirth) From Person )";
	
	//lists the cusatomer with the highest rewards points that are executive or veteran members
	private final static String LIST_CUSTOMER_HIGEST_REWARD = 
			"SELECT p.per_id AS 'ID Code', c.c_id AS 'Customer ID', c.c_userName AS 'User Name',\n"+	   
			"p.per_fName AS 'First Name', p.per_lName AS 'Last Name', p.per_dateOfBirth AS 'Date of Birth',\n" +
			"c.c_membershipType AS 'Membership Type', r.rp_points AS 'Reward points'\n" +
			"FROM Person p\n" +
			"INNER JOIN Customer c ON p.per_id = c.c_per_id\n" +
			"LEFT OUTER JOIN RewardPoints r on c.c_id = r.rp_c_id\n" +
			"WHERE c.c_id IN (SELECT c_id FROM Customer\n" +  				
			"WHERE c_memberShipType = 'Veteran' || c_membershipType = 'Executive') && r.rp_points > 10000";
	
	//lists the items descriptions of food items with the highest price
	private final static String LIST_RESTURANTS_FOOD_MAX_PRICE = 
			"SELECT r.rest_name AS 'Resturant Name', r.rest_description AS 'Resturant Description', i.item_type AS 'Item Type', " +
			"i.item_price AS 'Item Price', f.food_name AS 'Food Name', f.food_type AS 'Food Type'\n" +
			"FROM Item i\n" +
			"LEFT OUTER JOIN Resturant r ON r.rest_id = i.rest_id\n" +
			"LEFT OUTER JOIN Food f on f.food_id = i.item_id\n" +
			"HAVING i.item_price = (SELECT MAX(i.item_price) FROM Item i WHERE i.item_type = 'Food')";
	
	//Query to add a new person 
	private final static String ADD_NEW_PERSON_QUERY = "INSERT INTO Person (per_id, per_fName, per_lName, per_dateOfBirth)\n"
			+ "VALUES (?, ?, ?, ?)";
		
	//Query to add a new customer
	private final static String ADD_NEW_CUSTOMER_QUERY = "INSERT INTO Customer  (c_id, c_userName, c_membershipType, c_reward, c_per_id)\n"
			+ "VALUES (?, ?, ?, ?, ?)";

	//Query to list all the customers
	private final static String LIST_CUSTOMERS_QUERY = "SELECT * FROM Customer";

	//Query to delete a person/customer
	private final static String REMOVE_CUSTOMER_QUERY = "DELETE FROM Person\n" + "WHERE per_id = ?";
	
	//Initialize connection and input scanner
	private Connection connection = null;
	Scanner managerInput = new Scanner(System.in);

	//Main Method
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		LOGGER.setLevel(Level.INFO);
		ThemePark themePark = new ThemePark(); //Create Theme Park
		
		themePark.connectToDatabase(); //connect to the database

		if (themePark.isConnected()) {
			themePark.mainMenu();
		} else {
			System.out.println("\nNo Connection!");
		}
	}

	//Default Constructor
	public ThemePark() {
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, "Loading JDBC driver failed. Reason: {0}", e);
			System.exit(1);
		}
	}

	//Connect to database
	public void connectToDatabase() {
		try {
			System.out.println("\nPlease enter username and password for CECS 323 database.");
			System.out.print("Username: ");
			String dbManager = managerInput.nextLine();
			System.out.print("Password: ");
			String dbPassword = managerInput.nextLine();

			connection = DriverManager.getConnection(DB_URL, dbManager, dbPassword);
			connection.setAutoCommit(false);
			
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Connection to database failed. Reason: {0} ", e.getMessage());
			connection = null;
		}
	}

	//Disconnect from database
	public void disconnectFromDatabase() {
		try {
			connection.close();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Disconnection failed. Reason: ", e.getMessage());
		}
	}

	//Check if connected
	public boolean isConnected() {
		return connection != null;
	}

	//Display main menu
	public String displayMenu() {

		return ("\n\tMain Menu\n" + "1. Quereies\n" + "2. Add a new row\n" + "3. Remove a row\n" + "4. Commit Changes\n"
				+ "5. Rollback Changes\n" + "0. Exit\n" + "Please enter in a number (0 to exit): ");

	}

	//Main Menu
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

			switch (number) {
			case 1:
				queryMenu();
				break;
			case 2: 
				addNewCustomer();
				break;
			case 3: 
				removeCustomer();
				break;
			case 4:
				commit();
				break;
			case 5:
				rollback();
				break;
			case 0: exit();
				break;
			default:
				System.out.println("\n Please enter in a number 1 - 5 or 0 to exit...\n");
				break;

			}

		} while (number <= 0);

	}
	
	//Display sub menu
	public String displayQueryMenu() {
		return ("\nQueuery Menu\n" 
				+ "1. List information on Employees with the earliest birthday.\n"
				+ "2. List customers with the highest reward points.\n"
				+ "3. List the information of the Resturant, Item and Food with the maximum price.\n" 
				+ "0. Return to Main Menu\n" 
				+ "Please enter in a number (0 to return): ");

	}

	//Sub menu
	public void queryMenu() throws SQLException {

		Scanner queueryScanner = new Scanner(System.in);
		int number;
		do {
			System.out.print(displayQueryMenu());
			while (!queueryScanner.hasNextInt()) {
				System.out.println("That's not a number!");
				queueryScanner.next(); // this is important!
			}
			number = queueryScanner.nextInt();

			switch (number) {
			case 1:
				listEmployeeEarlyBirthday();
				break;
			case 2:
				listCustomerHighestReward();
				break;
			case 3:
				listResturantFoodPriceMax();
				break;
			case 0: mainMenu();
				break;
			default:
				System.out.println("\n Please enter in a number 1 - 3 or 0 to return to Main Menu...\n");
				break;

			}

		} while (number <= 0);

	}
	
	//Querys
	
	public void listEmployeeEarlyBirthday() throws SQLException {
		 PreparedStatement listEmployeeEarlyBirthdayQuery = 
         		connection.prepareStatement(LIST_EMPLOYEE_EARLY_BIRTHDAY);            
         ResultSet results = listEmployeeEarlyBirthdayQuery.executeQuery();
         System.out.print("\nID\t|First Name\t|Last Name\t|Date of Birth\t"
         		+ "|Address\t|City\t\t|State\t|Zip\t|Address Type\t|Phone Number\t"
         		+ "|Phone\t|Employee ID\t|Job Title\t|Salary\n");
         System.out.println("---------------------------------------------"
         		+ "-----------------------------------------------------"
         		+ "-----------------------------------------------------"
         		+ "----------------------------------------------");
         //Prints out query 
         if(results.next())
         {
             do
             {
                 System.out.print(results.getInt("ID") + "\t"
                 + "|" + results.getString("First Name") + "\t"
                 + "|" + results.getString("Last Name") + "\t\t"
                 + "|" + results.getDate("Date of Birth") + "\t"
                 + "|" + results.getString("Address") + "\t"
                 + "|" + results.getString("City") + "\t"
                 + "|" + results.getString("State") + "\t"
                 + "|" + results.getInt("Zip") + "\t"
                 + "|" + results.getString("Address Type") + "\t\t"
                 + "|" + results.getString("Phone Number") + "\t"
                 + "|" + results.getString("Phone") + "\t"
                 + "|" + results.getInt("Employee ID") + "\t\t"
                 + "|" + results.getString("Job Title") + "\t\t"
                 + "|" + results.getDouble("Salary") + "\t\t"
                 + "\n");
                 
             }while(results.next());
         }
         
         queryMenu();
	}
	
	public void listCustomerHighestReward() throws SQLException {
		 PreparedStatement listCustomerHighestRewardQuery = 
	         		connection.prepareStatement(LIST_CUSTOMER_HIGEST_REWARD);            
	         ResultSet results = listCustomerHighestRewardQuery.executeQuery();
	         System.out.print("\nID\t|Customer ID\t|User Name\t|First Name\t|LastName\t|Date of Birth\t|Membership Type\t|Reward Points\n");
	         System.out.println("-----------------------------------------"
	         		+ "---------------------------------------------------"
	         		+ "----------------------------------");
	         //Prints out query 
	         if(results.next())
	         {
	             do
	             {
	                 System.out.print(results.getString("ID Code") + "\t"
	                 + "|" + results.getInt("Customer ID") + "\t\t"
	                 + "|" + results.getString("User Name") + "\t"
	                 + "|" + results.getString("First Name") + "\t\t"
	                 + "|" + results.getString("Last Name") + "\t\t"
	                 + "|" + results.getDate("Date of Birth") + "\t"
	                 + "|" + results.getString("MemberShip Type") + "\t\t"
	                 + "|" + results.getInt("Reward Points") + "\t\t"
	                 + "\n");
	                 
	             }while(results.next());
	         }
	         
	         queryMenu();
	}
	
	public void listResturantFoodPriceMax() throws SQLException {
		PreparedStatement listResturantMaxFoodPriceQuery = 
         		connection.prepareStatement(LIST_RESTURANTS_FOOD_MAX_PRICE);            
         ResultSet results = listResturantMaxFoodPriceQuery.executeQuery();
         System.out.print("\nResturant Name\t\t|Resturant Description\t\t|Item Type\t|Item Price\t|Food Name\t|Food Type\n");
         System.out.println("-----------------------------------------"
         		+ "---------------------------------------------------"
         		+ "----------------------------------");
         //Prints out query 
         if(results.next())
         {
             do
             {
                 System.out.print(results.getString("Resturant Name") + "\t"
                 + "|" + results.getString("Resturant Description") + "\t"
                 + "|" + results.getString("Item Type") + "\t\t"
                 + "|" + results.getInt("Item Price") + "\t\t"
                 + "|" + results.getString("Food Name") + "\t\t"
                 + "|" + results.getString("Food Type") + "\t"
                 + "\n");
                 
             }while(results.next());
         }
         
         queryMenu();
	}

	//Adds a new customer
	public void addNewCustomer() throws SQLException {

		PreparedStatement addNewPerson = connection.prepareStatement(ADD_NEW_PERSON_QUERY);
		
		PreparedStatement addNewCustomer = connection.prepareStatement(ADD_NEW_CUSTOMER_QUERY);

		PreparedStatement listContractsQuery = connection.prepareStatement(LIST_CUSTOMERS_QUERY);

		ResultSet customerResults = listContractsQuery.executeQuery();

		boolean valid = false;
		
		System.out.print("Enter Customer ID: ");
		int c_id = Integer.parseInt(managerInput.nextLine());
		

		if (customerResults.next()) {

			do {
				if (c_id == customerResults.getInt("c_id")) {
					System.out.print(
							"Customer already exists in database...\n");
							addNewCustomer();
				}
		

			} while (customerResults.next());
		}
		
		System.out.print("\nEnter Customer's First Name: ");
		String per_fName = managerInput.nextLine();
		
		System.out.print("\nEnter Customer's Last Name: ");
		String per_lName = managerInput.nextLine();
		
		System.out.print("\nEnter Customer's Date of Birth(yyyy-mm-dd): ");
		String per_dateOfBirth = managerInput.nextLine();

		System.out.print("\nEnter Customer's User Name: ");
		String c_userName = managerInput.nextLine();

		System.out.print("\nEnter Customer's Membership Type: ");
		String c_membershipType = managerInput.nextLine();

		System.out.print("\nEnter Customer's Reward Points: ");
		String c_reward = managerInput.nextLine();

		System.out.print("\nEnter Customer's ID Code: ");
		int c_per_id = Integer.parseInt(managerInput.nextLine());
		
		System.out.println(c_per_id);
		
		addNewPerson.setInt(1, c_per_id);
		addNewPerson.setString(2, per_lName);
		addNewPerson.setString(3, per_fName);
		addNewPerson.setString(4, per_dateOfBirth);
		addNewPerson.executeUpdate();

		addNewCustomer.setInt(1, c_id);
		addNewCustomer.setString(2, c_userName);
		addNewCustomer.setString(3, c_membershipType);
		addNewCustomer.setString(4, c_reward);
		addNewCustomer.setInt(5, c_per_id);
		addNewCustomer.executeUpdate();

		System.out.println("Successfully added a new Customer...\n" + " Would you like to add another Customer? (y/n)");
		if (managerInput.nextLine().equals("y")) {
			addNewCustomer();
		}

	}

	//Removes a selected customer
	public void removeCustomer() throws SQLException {

		PreparedStatement listCustomersQuery = connection.prepareStatement(LIST_CUSTOMERS_QUERY);

		ResultSet customerResults = listCustomersQuery.executeQuery();

		PreparedStatement removeCustomerQuery = connection.prepareStatement(REMOVE_CUSTOMER_QUERY);
		
		
		//Checks if there are any Customers in the Database, if there are, print them out
		if (!customerResults.next()) {
			System.out.println("\nThere are no Customers in this query." + "Returning to the Main Menu.");
			mainMenu();
		} else {
			System.out.print("\nID\t|User Name\t|Membership\t|Rewards\n");
			System.out.println("-----------------------------------------"
	         		+ "-------------------------");
			do {
				System.out.print(customerResults.getString("c_per_id") + "\t"
						+ "|" + customerResults.getString("c_userName") + "\t"
						+ "|" + customerResults.getString("c_membershipType") + " \t"
						+ "|" + customerResults.getString("c_reward") + "\n");

			} while (customerResults.next());
		}

		// Prompts user to enter in Customer ID to remove, if not found, try
		// again
		do {

			System.out.print("Please enter in the Customer ID you would like to remove: ");
			int input = Integer.parseInt(managerInput.nextLine());

			removeCustomerQuery.setInt(1, input);

			if (removeCustomerQuery.executeUpdate() == 0) {
				System.out.println("Unable to find Customer, please enter in a new ID...");
			}

		} while (removeCustomerQuery.executeUpdate() != 0);

		System.out.println("Successfully removed a Customer.\n" + "Would you like to remove another Customer? (y/n)");
		if (managerInput.nextLine().equals("y")) {
			removeCustomer();
		}
		else {
			mainMenu();
		}

	}

	//Commit changes
	public void commit() throws SQLException {
		connection.commit();
	}

	//Rollback changes
	public void rollback() throws SQLException {
		connection.rollback();

	}
	
	public void exit() throws SQLException {

    	System.out.println("Are you sure you want to exit? (y/n)");

    	if(managerInput.nextLine().equals("yes")) {
    		System.out.println("Would you like your changes committed? (yes/no)");
    		if(managerInput.nextLine().equals("yes")) {
    			connection.commit();
    			System.out.println("Your changes has been committed");
        		System.exit(1);
        	}
    		else {
    			connection.rollback();
    			System.out.println("Your work has been rolled back!");
    			System.out.println("Thank you for using Group 5's JDBC API!");
        		System.exit(1);
    		}
    	}
    	else {
    		System.out.println("Returning to the Main Menu...");
    		mainMenu();
    	}
	}
}
