/**
 * Project: Assignment 4 - Database Connection
 * Author: Jason Rossetti
 * Created: 2021-09-14
 */
/*

 */
import javax.swing.*;


public class AppGui {

    public static void main(String[] args) {
        StudentFrame app = new StudentFrame("Student Records");
        app.setSize(600, 200);
        app.setResizable(false);
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }
}


