import javax.swing.*;

/**
 * Project: Assignment 1 - GUI
 * Author: Jason Rossetti
 * Created: 2021-09-14
 */
public class AppGui {
    public static void main(String[] args) {

        StudentFrame app = new StudentFrame("Student Records");

        app.setSize(350, 250);
        app.setResizable(false);

        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }
}
