/**
 * Project: Assignment 2 - Events and Listeners
 * Author: Jason Rossetti
 * Created: 2021-10-11
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StudentFrame extends JFrame implements ActionListener {

    // Parent Panel
    private final JPanel panStudent = new JPanel(new BorderLayout());
    private final JButton btnPrev = new JButton("Prev");
    private final JButton btnNext = new JButton("Next");
    // Center panel that holds the buttons and the information fields
    private final JPanel panCenter = new JPanel(new BorderLayout());
    private final JPanel panFields = new JPanel(new GridLayout(2, 4));
    private final JLabel lblID = new JLabel("ID: ");
    private final JTextField txtID = new JTextField(12);
    private final JLabel lblFirstName = new JLabel("First Name: ");
    private final JTextField txtFirstName = new JTextField(12);
    private final JLabel lblLastName = new JLabel("Last Name: ");
    private final JTextField txtLastName = new JTextField(12);
    private final JLabel lblProgram = new JLabel("Program: ");
    private final JTextField txtProgram = new JTextField(12);
    // Panel that holds all the buttons
    private final JPanel panButtonRow = new JPanel(new GridLayout(1, 4));
    private final JButton btnLoad = new JButton("Load");
    private final JButton btnEdit = new JButton("Edit");
    private final JButton btnAdd = new JButton("Add");
    private final JButton btnSave = new JButton("Save");
    // Bottom portion of the frame
    private final JLabel lblMarks = new JLabel("Marks");
    // Parent panel of the mark area
    private final JPanel panMarkArea = new JPanel(new BorderLayout());
    // Panel that holds all the mark text boxes
    private final JPanel panMarks = new JPanel(new GridLayout(2, 3));
    private final JTextField[] txtMarks = new JTextField[6];
    // Student List
    private final static ArrayList<Student> studentList = new ArrayList<>();
    private static int currentIndex = 0;
    private final static boolean DEBUGMODE = true;  // Turns on/off debug mode

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FUNCTIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor for the Student Frame
    public StudentFrame(String name){
        super(name);
        // Generate fields
        generateFields();
        // Startup state for text boxes and buttons
        setInitialState();
        ///////////////////////////////////////////////////////////
        // ACTION LISTENERS
        ///////////////////////////////////////////////////////////

        // Add Button Event
        btnAdd.addActionListener(e -> {
                // Clear all information
                update();
                clearText();
                clearMarks();
                // Create new student and add to the list
                Student newStudent = new Student();
                studentList.add(newStudent);
                if (DEBUGMODE){
                    System.out.println("Student Added to List"); // DEBUG to make sure button is being called
                }
                currentIndex = newStudent.getNextNum() - 1;
                // Put the current student ID in text field
                String id = newStudent.getStudentID();
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

            // Using the event button inside the Add button
            btnEdit.addActionListener(ev -> {
                try
                {
                    createStudent();
                    enableTextBoxes(false);
                    if (DEBUGMODE){
                        System.out.println(currentIndex); // DEBUG
                    }
                    update();

                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                finally {
                    btnEdit.setText("Edit");
                    btnAdd.setEnabled(true);
                    btnLoad.setEnabled(true);
                    btnNext.setEnabled(false);
                }
            });
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        btnLoad.addActionListener(this);
        btnPrev.addActionListener(this);
        btnNext.addActionListener(this);
    } // End JFrame

    // Generate all the components of the frame
    private void generateFields() {
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
        // Create 6 text fields for marks array
        for (int i=0; i<6; i++) {
            txtMarks[i] = new JTextField();
            panMarks.add(txtMarks[i]);
        }
        // Add mark panel to the mark area panel
        panMarkArea.add(panMarks, BorderLayout.CENTER);
        // Add the areas into the parent panel and set positioning as a border layout
        panStudent.add(btnPrev, BorderLayout.WEST);
        panStudent.add(panCenter, BorderLayout.CENTER);
        panStudent.add(btnNext, BorderLayout.EAST);
        panStudent.add(panMarkArea, BorderLayout.SOUTH);
        // MAGIC
        getContentPane().add(panStudent);
        invalidate(); validate();

    }

    // Set the initial state for the program
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
    // Use this after each event to keep it up to date
    private void update() {
        int length = studentList.size();
        if (DEBUGMODE){
            System.out.println("From Update\nIndex: "+currentIndex +"\tList Length: "+ studentList.size()); // DEBUG
        }
        try
        {
            btnNext.setEnabled(true);
            btnPrev.setEnabled(true);
            if (DEBUGMODE){
                System.out.println("Index: "+currentIndex +"\tList - 1: "+ (length - 1)); // DEBUG
            }
            // If StudentList is empty buttons are disabled START STATE
            if (studentList.isEmpty()) {
                btnPrev.setEnabled(false);
                btnNext.setEnabled(false);
            }
            // If index is 0 prev disabled
            if (currentIndex == 0) {
                btnPrev.setEnabled(false);
            }  else if (currentIndex == (length - 1) || studentList.size() == 1){
                btnNext.setEnabled(false);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    // Clear the mark values
    private void clearMarks() {
        for (JTextField marks : txtMarks) {
            marks.setText("");
        }
    }
    // Clear all the text boxes on the top
    private void clearText() {
        txtID.setText("");
        txtFirstName.setText("");
        txtProgram.setText("");
        txtLastName.setText("");
    }
    // Enables or disables the text boxes
    private void enableTextBoxes(boolean result) {
        txtFirstName.setEnabled(result);
        txtID.setEnabled(result);
        txtProgram.setEnabled(result);
        txtLastName.setEnabled(result);
        for (JTextField txt : txtMarks){
            txt.setEnabled(result);
        }
    }
    // Gets the text from the text boxes and sets it in the created student.
    private void createStudent(){
        Student currentStudent = studentList.get(studentList.size() - 1);
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String program = txtProgram.getText();
        double[] studentMarks = currentStudent.getMarks();

        int markIndex = 0;
        for (JTextField mk : txtMarks) {
            // If no marks are in the text boxes, set them to 0
            if (mk.getText().isEmpty()){
                //studentMarks[markIndex] = 0;
                mk.setText(String.valueOf(0.0f));
            }
            // If there are values in the marks then add them to the array
            studentMarks[markIndex] = Double.parseDouble(mk.getText());
            markIndex++;
        }
        // Set the student information with the text box information
        currentStudent.setFname(firstName);
        currentStudent.setLname(lastName);
        currentStudent.setProgram(program);
        currentStudent.setMarks(studentMarks);
        // Repositions the current index
        currentIndex = studentList.size() - 1;
    }

    // Loads the current student into the text boxes
    private void loadStudent(Student student) {
        txtID.setText(student.getStudentID());
        txtProgram.setText(student.getProgram());
        txtFirstName.setText(student.getFname());
        txtLastName.setText(student.getLname());
        // Counter for the list of marks
        int i = 0;
        // Set each mark text box
        for (double mark : student.getMarks()){
            txtMarks[i].setText(String.valueOf(mark));
            ++i;
        }
        if (DEBUGMODE){
            double[] marks = student.getMarks(); // DEBUG
            System.out.printf("LOADING STUDENT: %s %s %s %s\n", student.getStudentID(), student.getFname(), student.getLname(), student.getProgram()); // DEBUG
            System.out.printf("GRADES: %2f %2f %2f %2f %2f %2f", marks[0], marks[1], marks[2], marks[3], marks[4], marks[5]); // DEBUG
        }
        update();
    }

    // Event Handlers for the buttons
    public void actionPerformed(ActionEvent e) {
        // When Next Button is clicked, it increases the index by 1 and loads that student.
        if (e.getSource() == btnPrev) {
            update();
            if (currentIndex > 0) {
                currentIndex -= 1;
                loadStudent(studentList.get(currentIndex));
                if (DEBUGMODE){
                    System.out.println("Current Index: "+ currentIndex); // DEBUG
                    System.out.println("Next index from Prev is: "+ currentIndex); // DEBUG
                }
            } else {
                System.out.println("Out of range");
            }
        }
        // When Next Button is clicked, it increases the index by 1 and loads that student.
        if (e.getSource() == btnNext) {
            update();
            if (currentIndex < studentList.size() - 1){
                currentIndex += 1;
                loadStudent(studentList.get(currentIndex));
                if (DEBUGMODE){
                    System.out.println("Current Index: "+ currentIndex); // DEBUG
                    System.out.println("Next index from Next is: "+ currentIndex); // DEBUG
                }
            } else {
                System.out.println("Out of range");
            }
        }
    }
} // END CLASS
