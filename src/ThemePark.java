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
	
	
	
	private final static String LIST_EMPLOYEE_EARLY_BIRTHDAY = "";
	
	private final static String LIST_CUSTOMER_HIGEST_REWARD = "";
	
	private final static String LIST_RESTURANTS_PRICE_OVER_TEN = "";
	
	

	private final static String ADD_NEW_CUSTOMER_QUERY = "INSERT INTO Containers  (c_id, c_fName, c_lName, c_membershipType, c_reward, c_per_id) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

	private final static String LIST_CUSTOMERS_QUERY = "SELECT * FROM Customers";

	private final static String REMOVE_CUSTOMER_QUERY = "DELETE FROM Customers " + "WHERE c_id = ?";
	
	

	private Connection connection = null;
	Scanner managerInput = new Scanner(System.in);

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		LOGGER.setLevel(Level.INFO);
		ThemePark themePark = new ThemePark();
		themePark.connectToDatabase();

		if (themePark.isConnected()) {
			themePark.mainMenu();
		} else {
			System.out.println("\nNo Connection!");
		}
	}

	public ThemePark() {
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
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
			String dbPassword = "";// managerInput.nextLine();

			connection = DriverManager.getConnection(DB_URL, dbManager, dbPassword);
			
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Connection to database failed. Reason: {0} ", e.getMessage());
			connection = null;
		}
	}

	public void disconnectFromDatabase() {
		try {
			connection.close();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Disconnection failed. Reason: ", e.getMessage());
		}
	}

	public boolean isConnected() {
		return connection != null;
	}

	public String displayMenu() {

		return ("\tMain Menu\n" + "1. Quereies\n" + "2. Add a new row\n" + "3. Remove a row\n" + "4. Commit Changes\n"
				+ "5. Rollback Changes\n" + "0. Exit\n" + "Please enter in a number (0 to exit): ");

	}

	public String displayQueryMenu() {
		return ("Queuery Menu\n" 
				+ "1. List information on Employees with the earliest birthday. "
				+ "2. List customers with the highest reward points."
				+ "3. List items of all the resturants having a price of more than 10." 
				+ "0. Return to Main Menu");

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
			case 0:
				break;
			default:
				System.out.println("\n Please enter in a number 1 - 5 or 0 to exit...\n");
				break;

			}

		} while (number <= 0);

	}

	public void queryMenu() {

		Scanner queueryScanner = new Scanner(System.in);
		int number;
		do {
			System.out.println(displayQueryMenu());
			while (!queueryScanner.hasNextInt()) {
				System.out.println("That's not a number!");
				queueryScanner.next(); // this is important!
			}
			number = queueryScanner.nextInt();

			switch (number) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 0:
				break;
			default:
				System.out.println("\n Please enter in a number 1 - 3 or 0 to return to Main Menu...\n");
				break;

			}

		} while (number <= 0);

	}
	
	//Querys
	
	public void listEmployeeEarlyBirthday() throws SQLException {
		
	}
	
	public void listCustomerHighestReward() throws SQLException {
		
	}
	
	public void listResturantFoodPriceTen() throws SQLException {
		
	}

	public void addNewCustomer() throws SQLException {

		PreparedStatement addNewCustomer = connection.prepareStatement(ADD_NEW_CUSTOMER_QUERY);

		PreparedStatement listContractsQuery = connection.prepareStatement(LIST_CUSTOMERS_QUERY);

		ResultSet customerResults = listContractsQuery.executeQuery();

		boolean valid = false;

		System.out.print("Enter Customer ID: ");
		int c_id = Integer.parseInt(managerInput.nextLine());

		if (customerResults.next()) {

			do {
				if (c_id == customerResults.getInt("c_id")) {
					System.out.println(
							"Customer already exists in database...\n" + "Please Enter in a new Customer ID: ");
					c_id = Integer.parseInt(managerInput.nextLine());
					valid = true;
				}

			} while (!valid);
		}

		System.out.print("\nEnter Customer's First Name: ");
		String c_fName = managerInput.nextLine();

		System.out.print("\nEnter Customer's Last Name: ");
		String c_lName = managerInput.nextLine();

		System.out.print("\nEnter Customer's Membership Type: ");
		String c_membershipType = managerInput.nextLine();

		System.out.print("\nEnter Customer's Reward Points: ");
		String c_reward = managerInput.nextLine();

		System.out.print("\nEnter Customer's ID Code: ");
		int c_per_id = Integer.parseInt(managerInput.nextLine());

		addNewCustomer.setInt(1, c_id);
		addNewCustomer.setString(2, c_fName);
		addNewCustomer.setString(3, c_lName);
		addNewCustomer.setString(4, c_membershipType);
		addNewCustomer.setString(5, c_reward);
		addNewCustomer.setInt(6, c_per_id);

		System.out.println("Successfully added a new Customer...\n" + " Would you like to add another Customer? (y/n)");
		if (managerInput.nextLine().equals("y")) {
			addNewCustomer();
		}

	}

	public void removeCustomer() throws SQLException {

		PreparedStatement listCustomersQuery = connection.prepareStatement(LIST_CUSTOMERS_QUERY);

		ResultSet customerResults = listCustomersQuery.executeQuery();

		PreparedStatement removeCustomerQuery = connection.prepareStatement(REMOVE_CUSTOMER_QUERY);
		
		
		//Checks if there are any Customers in the Database, if there are, print them out
		if (!customerResults.next()) {
			System.out.println("\nThere are no Customers in this query." + "Returning to the Main Menu.");
			mainMenu();
		} else {
			System.out.println("\nID\t\tFirst Name\tLast Name\tMembership\tRewards\tID Code\n");
			do {
				System.out.print(customerResults.getString("c_id") + "\t\t\t" + customerResults.getString("c_fName")
						+ "\t\t\t" + customerResults.getString("c_lName") + "\t"
						+ customerResults.getString("c_membershipType") + "\t\t\t"
						+ customerResults.getString("c_reward") + "\n");

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

		} while (removeCustomerQuery.executeUpdate() == 0);

		System.out.println("Successfully removed a Customer.\n" + "Would you like to remove another Customer? (y/n)");
		if (managerInput.nextLine().equals("y")) {
			removeCustomer();
		}

	}

	public void commit() throws SQLException {
		connection.commit();
	}

	public void rollback() throws SQLException {
		connection.rollback();

	}
}
