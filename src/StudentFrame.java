import javax.swing.*;
import java.awt.*;

/**
 * Project: Assignment 1 - GUI
 * Author: Jason Rossetti
 * Created: 2021-09-14
 */
public class StudentFrame extends JFrame {

    private JPanel panStudent = new JPanel(new BorderLayout());
    private JButton btnPrev = new JButton("Prev");
    private JButton btnNext = new JButton("Next");
    private JPanel panButtonRow = new JPanel();
    private JButton btnLoad = new JButton("Load");
    private JButton btnEdit = new JButton("Edit");
    private JButton btnAdd = new JButton("Add");
    private JButton btnSave = new JButton("Save");

    private JPanel panID = new JPanel();
    private JLabel lblID = new JLabel("ID: ");
    private JTextField txtID = new JTextField(12);

    private JPanel panFirstName = new JPanel();
    private JLabel lblFirstName = new JLabel("First Name: ");
    private JTextField txtFirstName = new JTextField(12);

    private JPanel panLastName = new JPanel();
    private JLabel lblLastName = new JLabel("Last Name: ");
    private JTextField txtLastName = new JTextField(12);

    private JPanel panProgram = new JPanel();
    private JLabel lblProgram = new JLabel("Program: ");
    private JTextField txtProgram = new JTextField(12);

    // Bottom portion of the frame
    private JLabel lblMarks = new JLabel("Marks");
    private JPanel panMarkRow1 = new JPanel();
    private JTextField txtMark1 = new JTextField();
    private JTextField txtMark2 = new JTextField();
    private JTextField txtMark3 = new JTextField();
    private JPanel panMarkRow2 = new JPanel();
    private JTextField txtMark4 = new JTextField();
    private JTextField txtMark5 = new JTextField();
    private JTextField txtMark6 = new JTextField();


    public StudentFrame(String name){
        super(name);

        panStudent.add(btnPrev, BorderLayout.WEST);
        panStudent.add(btnNext, BorderLayout.EAST);

        panID.add(lblID);
        panID.add(txtID);

        panFirstName.add(lblFirstName);
        panFirstName.add(txtLastName);

        panLastName.add(lblLastName);
        panLastName.add(txtLastName);

        panProgram.add(lblProgram);
        panProgram.add(txtProgram);

        panButtonRow.add(btnLoad);
        panButtonRow.add(btnEdit);
        panButtonRow.add(btnAdd);
        panButtonRow.add(btnSave);



        // Rough Layout -- Just adding stuff in
        panStudent.add(panID);
        panStudent.add(panProgram);
        panStudent.add(panFirstName);
        panStudent.add(panLastName);
        panStudent.add(panButtonRow);
        panStudent.add(panMarkRow1);
        panStudent.add(panMarkRow2);

        getContentPane().add(panStudent);


        invalidate(); validate();

    }



}
