import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Project: Assignment 2 - Events and Listeners
 * Author: Jason Rossetti
 * Created: 2021-10-11
 */
public class StudentFrame extends JFrame implements ActionListener {

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
    private JTextField[] txtMarks = new JTextField[6];
    private JTextField txtMark1 = new JTextField();
    private JTextField txtMark2 = new JTextField();
    private JTextField txtMark3 = new JTextField();
    private JTextField txtMark4 = new JTextField();
    private JTextField txtMark5 = new JTextField();
    private JTextField txtMark6 = new JTextField();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ArrayList that holds all the students
    // TODO: Find a better place for this
    final static ArrayList<Student> studentList = new ArrayList<>();
    static int currentIndex = 0;

    public StudentFrame(String name){
        super(name);
        // Generate fields
        generateFields();

        // Startup state for text boxes and buttons
        setInitialState();


        ///////////////////////////////////////////////////////////
        // ACTION LISTENERS
        ///////////////////////////////////////////////////////////

        // Add Button
        btnAdd.addActionListener(e -> {
            if (e.getSource() == btnAdd) {
                // Clear all information
                clearText();
                clearMarks();
                // Create new student and add to the list
                Student newStudent = new Student();
                studentList.add(newStudent);
                // Put the current student ID in text field
                String id = newStudent.getStudentID();
                System.out.println("Student Added to List"); // DEBUG to make sure button is being called
                btnEdit.setEnabled(true);
                btnEdit.setText("Done");
                btnAdd.setEnabled(false);
                // Disable other buttons
                btnLoad.setEnabled(false);
                btnPrev.setEnabled(false);
                btnSave.setEnabled(false);
                btnNext.setEnabled(false);
                // Enable text boxes for input
                enableTextBoxes(true);

                // Setup next student number to be used
                txtID.setText(id);
            }

            // Using the event button inside the Add button
            btnEdit.addActionListener(ev -> {
                try
                {
                    createStudent();

                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                finally {
                    btnEdit.setText("Edit");
                    btnAdd.setEnabled(true);
                    btnLoad.setEnabled(true);
                    displayCurrentStudent();
                    update();
                }
            });
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        btnLoad.addActionListener(this);
        btnPrev.addActionListener(this);
        btnNext.addActionListener(this);


    } // End JFrame

    // Generate all the components of the frame
    public void generateFields() {
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
        // Try out array
        txtMarks[0] = txtMark1;
        txtMarks[1] = txtMark2;
        txtMarks[2] = txtMark3;
        txtMarks[3] = txtMark4;
        txtMarks[4] = txtMark5;
        txtMarks[5] = txtMark6;
        // Add all the marks to the mark panel
        for (JTextField mark : txtMarks) {
            panMarks.add(mark);
        }
        // Add mark panel to the mark area panel
        panMarkArea.add(panMarks, BorderLayout.CENTER);
        // Add the areas into the parent panel and set positioning as a border layout
        panStudent.add(btnPrev, BorderLayout.WEST);
        panStudent.add(panCenter, BorderLayout.CENTER);
        panStudent.add(btnNext, BorderLayout.EAST);
        panStudent.add(panMarkArea, BorderLayout.SOUTH);

        getContentPane().add(panStudent);
        invalidate(); validate();
    }

    // Set the inital state for the program
    private void setInitialState() {
        // Set the buttons and text fields
        btnLoad.setEnabled(true);
        btnAdd.setEnabled(true);
        btnPrev.setEnabled(false);
        btnEdit.setEnabled(false);
        btnSave.setEnabled(false);
        btnNext.setEnabled(false);
        txtID.setEnabled(false);
        txtFirstName.setEnabled(false);
        txtProgram.setEnabled(false);
        txtLastName.setEnabled(false);
        // Clear text and marks to empty
        clearText();
        clearMarks();
        for (JTextField txt : txtMarks){
            txt.setEnabled(false);
        }
        update();
    }

    // Update all the information to get the button states
    public void update() {
        int length = studentList.size();
        System.out.println("From Update\nIndex: "+currentIndex +"\tList Length: "+ studentList.size());
        try
        {
            if (studentList.isEmpty()) {
                btnPrev.setEnabled(false);
                btnNext.setEnabled(false);
            }

            if (length > 0 && currentIndex >= 0){
                btnNext.setEnabled(true);
            }
            if (currentIndex <= length && currentIndex != 0) {
                btnPrev.setEnabled(true);
            }
        }
        catch (IndexOutOfBoundsException e) {
            JOptionPane.showInputDialog(e.getMessage());
        }

    }

    private void clearMarks() {
        for (JTextField marks : txtMarks) {
            marks.setText("");
        }
    }

    private void clearText() {
        txtID.setText("");
        txtFirstName.setText("");
        txtProgram.setText("");
        txtLastName.setText("");
    }

    private void enableTextBoxes(boolean result) {
        txtFirstName.setEnabled(result);
        txtID.setEnabled(result);
        txtProgram.setEnabled(result);
        txtLastName.setEnabled(result);
        for (JTextField txt : txtMarks){
            txt.setEnabled(result);
        }
    }

    public void createStudent(){
        Student currentStudent = studentList.get(studentList.size() - 1);
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String program = txtProgram.getText();
        double[] studentMarks = new double[6];
        int markIndex = 0;

        // If there are values in the marks then add them to the array
        // If not set all marks to 0
        if (studentMarks[0] != 0) {
            for (JTextField mk : txtMarks) {
                studentMarks[markIndex] = Double.parseDouble(mk.getText());
                markIndex++;
            }

        } else {
            for (JTextField mk : txtMarks) {
                studentMarks[markIndex] = 0;
                markIndex++;
            }
        }


        // Edit the student information
        currentStudent.setFname(firstName);
        currentStudent.setLname(lastName);
        currentStudent.setProgram(program);
        currentStudent.setMarks(studentMarks);
        currentIndex = studentList.size() - 1;
    }

    private void displayCurrentStudent(Student student) {

        enableTextBoxes(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPrev) {
            currentIndex = Student.getNextNum() - 1;
            loadStudent(studentList.get(currentIndex));
            System.out.println("Current Index: "+ currentIndex);
            update();

        } else if (e.getSource() == btnNext) {
            currentIndex = Student.getNextNum();
            loadStudent(studentList.get(currentIndex));
            System.out.println("Current Index: "+ currentIndex);
            update();
        }

    }

    public void loadStudent(Student student) {
        txtID.setText(student.getStudentID());
        txtProgram.setText(student.getProgram());
        txtFirstName.setText(student.getFname());
        txtLastName.setText(student.getLname());
    }
} // END CLASS
