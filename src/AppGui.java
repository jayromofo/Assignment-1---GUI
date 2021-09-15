import javax.swing.*;

/**
 * Project: Assignment 1 - GUI
 * Author: Jason Rossetti
 * Created: 2021-09-14
 */
public class AppGui {
    public static void main(String[] args) {

        // Create the application
        StudentFrame app = new StudentFrame("Student Records");

        app.setSize(600, 200);
        app.setResizable(false);

        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
