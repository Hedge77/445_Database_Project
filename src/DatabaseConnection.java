import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JTable;

public class DatabaseConnection {
	
	private Connection myConnection;
	
	public DatabaseConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			myConnection = DriverManager.getConnection(
					"jdbc:mysql://vergil.u.washington.edu:13117/AirlineReservations",
					"GUIAccess", "tcss445");
			
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public JTable showFlights() {
		JTable table = null;
		try {
			Statement s = myConnection.createStatement();
			ResultSet result = s.executeQuery("SELECT FlightID, Origin, Destination,"
					+ "StartTime, EndTime, BasePrice FROM Flights");
			result.last();
			int size = result.getRow();
			result.beforeFirst();
			Object[][] data = new Object[6][size + 1];
			int counter = 0;
			while (result.next()) {
				data[counter][0] = result.getInt(1);
				data[counter][1] = result.getString(2);
				data[counter][2] = result.getString(3);
				data[counter][3] = result.getString(4);
				data[counter][4] = result.getString(5);
				data[counter][5] = result.getString(6);
				counter++;
			}
			Object[] col = {"a", "b", "c", "d", "e", "f"};
			
			table = new JTable(data, col);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
		
		return table;
	}
	
	public String tryLogin(String input) {
		try {
			Statement s = myConnection.createStatement();
			//note: scrub input string
			ResultSet result = s.executeQuery("SELECT FName, LName FROM Passengers WHERE "
					+ "PassengerID = " + input + ";");
			if (result.next()) {//something was returned
				String name = result.getString(1) + " " + result.getString(2);
				return name;
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
		return "";
	}
	
	public String createPassenger(String input) {
		StringTokenizer st = new StringTokenizer(input, " ");
		if (st.countTokens() != 2) {//not 2 tokens
			return "";
		}
		String fname = st.nextToken();
		String lname = st.nextToken();
		String id = "";
		try {
			Statement s = myConnection.createStatement();
//			System.out.println("INSERT INTO Passengers (FName, LName) VALUES ("
//					+ fname + ", " + lname + ");");
			s.executeUpdate("INSERT INTO Passengers (FName, LName) VALUES ('"
					+ fname + "', '" + lname + "');");
			ResultSet result = s.executeQuery("SELECT PassengerID from Passengers WHERE "
					+ "FName = '" + fname + "' AND LName = '" + lname + "' ORDER BY "
							+ "PassengerID DESC;");
			result.next();
			id = result.getString(1);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return id;
	}
	
	public String[] getFlightStrings() {
		try {
			Statement s = myConnection.createStatement();
			ResultSet result = s.executeQuery("SELECT FlightID, Origin, Destination "
					+ "FROM Flights;");
			ArrayList<String> list = new ArrayList<String>();
			while(result.next()) {
				String next = result.getString(1) + " (" + result.getString(2) + " -> "
						+ result.getString(3) + ")";
				list.add(next);
			}
			String[] formattedStrings = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				formattedStrings[i] = list.get(i);
			}
			return formattedStrings;
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return null;
	}
	
	public void createReservation(String passenger, String flight) {
		try {
			Statement s = myConnection.createStatement();
			s.executeUpdate("INSERT INTO Reservations (PassengerID, FlightID) VALUES ("
					+ passenger + ", " + flight + ");");
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}
	
	public String[] getReservations(String passenger) {
		try {
			Statement s = myConnection.createStatement();
			ResultSet result = s.executeQuery("SELECT Reservations.FlightID, Origin, "
					+ "Destination FROM "
					+ "Reservations JOIN Flights ON Reservations.FlightID = Flights.FlightID "
					+ "WHERE Reservations.PassengerID = '" + passenger + "';");
			ArrayList<String> list = new ArrayList<String>();
			while(result.next()) {
				String next = result.getString(1) + " (" + result.getString(2) + " -> "
						+ result.getString(3) + ")";
				list.add(next);
			}
			if (list.size() == 0) {
				return null;
			}
			String[] formattedStrings = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				formattedStrings[i] = list.get(i);
			}
			return formattedStrings;
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public boolean updateReservation(String passenger, String flight, String data, int index) {
		String[] fieldNames = {"MealOption", "SeatingRequirements", "SeatsReserved"};
		if (index != 2) {
			data = "'" + data + "'"; // add single quotes if not an int
		}
		try {
			Statement s = myConnection.createStatement();
//			System.out.println("UPDATE Reservations SET " + fieldNames[index] + " = " + data
//					+ " WHERE PassengerID = '" + passenger + "' AND FlightID = '"
//					+ flight + "';");
			s.executeUpdate("UPDATE Reservations SET " + fieldNames[index] + " = " + data
					+ " WHERE PassengerID = '" + passenger + "' AND FlightID = '"
					+ flight + "';");
		} catch (Exception e){
			return false;
		}
		
		
		return true;
	}
}
