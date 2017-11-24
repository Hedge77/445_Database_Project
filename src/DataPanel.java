import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DataPanel extends JPanel{
	
	private DatabaseConnection myDB;
	
	private String myCurrentUser;
	
	private String myCurrentID;
	
	private JButton myNRButton;
	
	private JButton myERButton;
	
	private JTextArea myLoginText;
	
	public DataPanel(JButton NRButton, JButton ERButton, JTextArea loginText) {
		super();
		myDB = new DatabaseConnection();
		myCurrentUser = "";
		myNRButton = NRButton;
		myERButton = ERButton;
		myLoginText = loginText;
//		setPreferredSize(new Dimension(800, 800));
		this.displayFlights(); //display flights by default
	}
	
	public void displayFlights() {
		this.removeAll();
		JScrollPane scroll = new JScrollPane(myDB.showFlights());
		this.add(scroll);
		this.validate();
		this.repaint();
	}
	
	public void displayLoginScreen() {
		this.removeAll();
		JTextField t = new JTextField(20);
		this.add(new JTextArea("Enter an ID to log in, or a first and last name to create a "
				+ "user."));
		this.add(t);
		JTextArea status = new JTextArea("");
		JButton login = new JButton("Log In");
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String result = myDB.tryLogin(t.getText());
				if (!"".equals(result)) {
					myCurrentUser = result;
					myCurrentID = t.getText();
//					DataPanel.this.add(new JTextArea("Logged in as " + myCurrentUser));
					DataPanel.this.enableButtons();
					status.setText("Logged in as " + myCurrentUser);
				} else {
//					DataPanel.this.add(new JTextArea("User not found."));
					status.setText("User not found.");
				}
				status.repaint();
			}
		});
		this.add(login);
		JButton newUser = new JButton("New User");
		newUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String id = myDB.createPassenger(t.getText());
				if (!"".equals(id)) {
					myCurrentUser = t.getText();
					myCurrentID = id;
					DataPanel.this.enableButtons();
					status.setText("New user " + myCurrentUser + " created.");
				} else {
					status.setText("Syntax error, please enter a first and last name.");
				}
				status.repaint();
			}
		});
		this.add(newUser);
		
		this.add(status);
		this.validate();
		this.repaint();
		
	}
	
	public void displayNewReservation() {
		this.removeAll();
		this.add(new JTextArea("Choose a flight to create a reservation.\nFind detailed "
				+ "information about flights in the 'Show Flights' section.\nEdit specific "
				+ "reservation details in the 'Edit Reservation' section."));
		String[] flightStrings = myDB.getFlightStrings();
		JComboBox<String> dropDown = new JComboBox<String>(flightStrings);
		dropDown.setSelectedIndex(-1);
		this.add(dropDown);
		JButton createButton = new JButton("Create Reservation");
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String choice = flightStrings[dropDown.getSelectedIndex()];
				choice = new StringTokenizer(choice).nextToken();
//				System.out.println(choice);
				myDB.createReservation(myCurrentID, choice);
			}
		});
		this.add(createButton);
		
		
		this.validate();
		this.repaint();
	}
	
	public void displayEditReservation() {
		this.removeAll();
		JTextArea text = new JTextArea("Choose a reservation to edit:");
		this.add(text);
		String[] resStrings = myDB.getReservations(myCurrentID);
		if (resStrings == null) {
			text.setText("You have no reservations currently. You must create one first.");
			this.validate();
			this.repaint();
			return;
		}
		JComboBox<String> dropDown = new JComboBox<String>(resStrings);
		dropDown.setSelectedIndex(-1);
		this.add(dropDown);
		JPanel lowerPanel = new JPanel();
		JButton selectButton = new JButton("Edit");
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				DataPanel.this.displayLowerPanel(lowerPanel, resStrings, 
						dropDown.getSelectedIndex());
				
				DataPanel.this.validate();
				DataPanel.this.repaint();
			}
		});
		this.add(selectButton);
		this.add(lowerPanel);
		
		this.validate();
		this.repaint();
	}
	
	private void displayLowerPanel(JPanel panel, String[] flightStrings, int fSelect) {
		panel.removeAll();
		String[] fields = {"Meal Option", "Seating Requirements", "Number of Seats(int)"};
		JComboBox<String> dropDown = new JComboBox<String>(fields);
		dropDown.setSelectedIndex(-1);
		panel.add(dropDown);
		JTextField entry = new JTextField(10);
		panel.add(entry);
		JButton enterButton = new JButton("Enter");
		JTextArea status = new JTextArea("");
		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int index = dropDown.getSelectedIndex();
				String flight = new StringTokenizer(flightStrings[fSelect]).nextToken();
				boolean valid = myDB.updateReservation(myCurrentID, flight, entry.getText(), 
						index);
				if (valid) {
					status.setText("Reservation updated.");
				} else {
					status.setText("Error. No update performed.");
				}
				status.repaint();
			}
		});
		panel.add(enterButton);
		panel.add(status);
		
		panel.validate();
		panel.repaint();
	}
	
	public void enableButtons() {
		myLoginText.setText("Logged In As:\n" + myCurrentUser);
		myLoginText.repaint();
		myNRButton.setEnabled(true);
		myERButton.setEnabled(true);
	}
}
