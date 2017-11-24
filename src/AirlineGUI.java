import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class AirlineGUI {
	private static final int BUTTONS = 4;
	
	private JFrame myFrame;
	
	private JButton myNewReservationButton;
	
	private JButton myEditReservationButton;
	
	private JButton myShowFlightsButton;
	
	private JButton myLoginButton;
	
	private JPanel myButtonsPanel;
	
	private DataPanel myDataPanel;
	
	private JTextArea myLoginText;
	
	public AirlineGUI() {
		myFrame = new JFrame("Airline Reservations");
		myNewReservationButton = new JButton("New Reservation");
		myEditReservationButton = new JButton("Edit Reservation");
		myShowFlightsButton = new JButton("Flight Search");
		myLoginButton = new JButton("Log In");
		myButtonsPanel = new JPanel(new GridLayout(BUTTONS, 1));
		myLoginText = new JTextArea("Not Logged In", 2, 15);
		myDataPanel = new DataPanel(myNewReservationButton, myEditReservationButton, 
				myLoginText);
	}
	
	public void start() {
		myFrame.setResizable(false);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel leftOuterGrid = new JPanel(new GridLayout(2, 1));
        leftOuterGrid.add(myButtonsPanel);
        
        leftOuterGrid.add(new JPanel().add(myLoginText));
        
        addButtons();
        startConnection();
        
        myFrame.add(leftOuterGrid, BorderLayout.WEST);
        myFrame.add(myDataPanel, BorderLayout.CENTER);
        myFrame.pack();
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);
	}
	
	private void addButtons() {
		myShowFlightsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				myDataPanel.displayFlights();
			}
		});
		myButtonsPanel.add(myShowFlightsButton);
				
		myLoginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				myDataPanel.displayLoginScreen();
			}
		});
		myButtonsPanel.add(myLoginButton);
		
		myNewReservationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				myDataPanel.displayNewReservation();
			}
		});
		myNewReservationButton.setEnabled(false);
		myButtonsPanel.add(myNewReservationButton);
		
		myEditReservationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				myDataPanel.displayEditReservation();
			}
		});
		myEditReservationButton.setEnabled(false);
		myButtonsPanel.add(myEditReservationButton);
	}
	
	private void startConnection() {
		//check sql connection on startup and show warning box if error.
	}
	
//	public void updateLogin(String name) {
//		myLoginText.setText("Logged In As:\n" + name);
//		myLoginText.repaint();
//	}
}
