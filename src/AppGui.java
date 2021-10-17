/**
 * Project: Assignment 2 - Events and Listeners
 * Author: Jason Rossetti
 * Created: 2021-09-14
 */
/*
    TODO: Some validation and error exception. Pretty sure most of that is done. Might need some for value/type checking the +
        the check boxes
 */
import javax.swing.*;

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


