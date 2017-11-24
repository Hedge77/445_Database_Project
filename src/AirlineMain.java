import java.awt.EventQueue;


public class AirlineMain {
	
	public static void main(final String[] theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AirlineGUI().start();
            }
        });
    }
}
