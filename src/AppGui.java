import javax.swing.*;

/**
 * Project: Assignment 1 - GUI
 * Author: Jason Rossetti
 * Created: 2021-09-14
 */
public class AppGui {
    public static void main(String[] args) {

        StudentFrame app = new StudentFrame("Student Records");

        app.setSize(600, 200);
        app.setResizable(false);
        double[] studentMarks = {90, 57, 76, 57, 45, 76};
        Student student1 = new Student("Jason", "Rossetti", "CPA", studentMarks);



        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
