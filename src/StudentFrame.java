import javax.swing.*;
import java.awt.*;

/**
 * Project: Assignment 1 - GUI
 * Author: Jason Rossetti
 * Created: 2021-09-14
 */
public class StudentFrame extends JFrame {

    // Parent Panel
    private JPanel panStudent = new JPanel(new BorderLayout());


    private JButton btnPrev = new JButton("Prev");
    private JButton btnNext = new JButton("Next");

    // Center panel that holds the buttons and the information fields
    private JPanel panCenter = new JPanel(new BorderLayout());
    private JPanel panFields = new JPanel(new GridLayout(2, 4));
    private JLabel lblID = new JLabel("ID: ");
    private JTextField txtID = new JTextField(12);
    private JLabel lblFirstName = new JLabel("First Name: ");
    private JTextField txtFirstName = new JTextField(12);
    private JLabel lblLastName = new JLabel("Last Name: ");
    private JTextField txtLastName = new JTextField(12);
    private JLabel lblProgram = new JLabel("Program: ");
    private JTextField txtProgram = new JTextField(12);

    // Panel that holds all the buttons
    private JPanel panButtonRow = new JPanel(new GridLayout(1, 4));
    private JButton btnLoad = new JButton("Load");
    private JButton btnEdit = new JButton("Edit");
    private JButton btnAdd = new JButton("Add");
    private JButton btnSave = new JButton("Save");


    // Bottom portion of the frame
    private JLabel lblMarks = new JLabel("Marks");
    // Parent panel for the mark area
    private JPanel panMarkArea = new JPanel(new BorderLayout());
    // Panel that holds all the mark text boxes
    private JPanel panMarks = new JPanel(new GridLayout(2, 3));
    private JTextField txtMark1 = new JTextField();
    private JTextField txtMark2 = new JTextField();
    private JTextField txtMark3 = new JTextField();
    private JTextField txtMark4 = new JTextField();
    private JTextField txtMark5 = new JTextField();
    private JTextField txtMark6 = new JTextField();


    public StudentFrame(String name){
        super(name);

        // Add all the student fields to center panel
        panFields.add(lblID);
        panFields.add(txtID);
        panFields.add(lblProgram);
        panFields.add(txtProgram);
        panFields.add(lblFirstName);
        panFields.add(txtFirstName);
        panFields.add(lblLastName);
        panFields.add(txtLastName);

        // Add all the buttons on the bottom to the button panel
        panButtonRow.add(btnLoad);
        panButtonRow.add(btnEdit);
        panButtonRow.add(btnAdd);
        panButtonRow.add(btnSave);

        // Add the fields and button panels into the center panel
        panCenter.add(panFields, BorderLayout.CENTER);
        panCenter.add(panButtonRow, BorderLayout.SOUTH);

        // Add the label and marks to the mark area panel
        panMarkArea.add(lblMarks, BorderLayout.NORTH);
        panMarks.add(txtMark1);
        panMarks.add(txtMark2);
        panMarks.add(txtMark3);
        panMarks.add(txtMark4);
        panMarks.add(txtMark5);
        panMarks.add(txtMark6);
        panMarkArea.add(panMarks, BorderLayout.CENTER);


        // Add the areas into the parent panel and set positioning as a border layout
        panStudent.add(btnPrev, BorderLayout.WEST);
        panStudent.add(panCenter, BorderLayout.CENTER);
        panStudent.add(btnNext, BorderLayout.EAST);
        panStudent.add(panMarkArea, BorderLayout.SOUTH);

        getContentPane().add(panStudent);
        invalidate(); validate();
    }

    public void initialize() {

    }
}
