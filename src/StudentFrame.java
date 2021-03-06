/**
 * Project: Assignment 3 - Save and Load to file
 * Author: Jason Rossetti
 * Created: 2021-11-07
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;


public class StudentFrame extends JFrame implements ActionListener {

    // Keep track of the running state
    public enum State {
        RUNNING,
        CREATE,
        EDIT
    }
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

    // Helper Variables
    private static final JFileChooser fc = new JFileChooser();
    private static ArrayList<Student> studentList = new ArrayList<>();
    private static int currentIndex = 0;
    private final static boolean DEBUGMODE = true;  // Turns on/off debug mode
    private static State currentState;
    static boolean isEditing = false;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FUNCTIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor for the Student Frame
    public StudentFrame(String name){
        super(name);
        currentState = State.RUNNING;
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
                    if (currentState == State.RUNNING) {
                        currentState = State.CREATE;
                        update();
                        clearText();
                        clearMarks();
                        // Create new student and add to the list
                        Student newStudent = new Student();
                        studentList.add(newStudent);
                        if (DEBUGMODE) {
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
                    }
                });

        // Using the event button inside the Add button



        btnEdit.addActionListener(e -> {
            // If in default state
            if (currentState == State.RUNNING) {
                currentState = State.EDIT;
                // Turn editing mode on
                isEditing = true;
                enableTextBoxes(true);
                btnEdit.setText("Done");
                btnLoad.setEnabled(false);
                btnPrev.setEnabled(false);
                btnSave.setEnabled(false);
                btnNext.setEnabled(false);
                btnAdd.setEnabled(false);
            } else if (currentState == State.EDIT && isEditing == true){
                try {
                    // Make replacement student
                    Student currentStudent = studentList.get(currentIndex);
                    String firstName = txtFirstName.getText();
                    String lastName = txtLastName.getText();
                    String program = txtProgram.getText();
                    double[] studentMarks = new double[6];

                    int markIndex = 0;
                    for (JTextField mk : txtMarks) {
                        // If no marks are in the text boxes, set them to 0
                        if (mk.getText().isEmpty()) {
                            //studentMarks[markIndex] = 0;
                            mk.setText(String.valueOf(0.0f));
                        }
                        // If there are values in the marks then add them to the array
                        studentMarks[markIndex] = Double.parseDouble(mk.getText());
                        markIndex++;

                        // Set the values on the student
                        currentStudent.setFname(firstName);
                        currentStudent.setLname(lastName);
                        currentStudent.setProgram(program);
                        currentStudent.setMarks(studentMarks);
                        // Replace the student in the list with the new one
                        studentList.set(currentIndex, currentStudent);
                        update();

                        // Turn off editing mode
                        isEditing = false;

                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    // Enable the appropriate buttons
                    btnEdit.setText("Edit");
                    btnAdd.setEnabled(true);
                    btnLoad.setEnabled(true);
                    btnSave.setEnabled(true);
                    btnNext.setEnabled(true);
                    btnPrev.setEnabled(true);
                    currentState = State.RUNNING;
                    enableTextBoxes(false);
                    update();
                }
                // If current state is on CREATE when edit button is pressed
            } else if (currentState == State.CREATE){
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
                    btnSave.setEnabled(true);
                    currentState = State.RUNNING;
                }
            }
        });
        // Add the other action listeners
        btnPrev.addActionListener(this);
        btnNext.addActionListener(this);
        btnSave.addActionListener(this);
        btnLoad.addActionListener(this);
    } // End Constructor

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
        currentState = State.RUNNING;
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
            System.out.println("Current State: "+ currentState);
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

    // Saves the current list to file
    public void saveStudentsToFile(){
        // Initialize file name
        File fileName = null;
        System.out.println("Save students to File");
        fc.setDialogTitle("Save to file");
        int result = fc.showSaveDialog(null);
        // If file is valid then it sets the filename
        if (result == JFileChooser.APPROVE_OPTION){
            fileName = fc.getSelectedFile();
        } else {
            System.out.println("Sorry incorrect file");
        }
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(fileName))) {
            // Write the whole ArrayList as an object
            out.writeObject(studentList);
        } catch (SecurityException | IOException ex) {
            ex.getStackTrace();
        }
    }

    // Loads the file
    public void loadStudentFile(){
        if (DEBUGMODE){
            System.out.println("Load students from file");
        }
        // Initialize the file
        File fileName = null;
        // Open JFileChooser to load a specific file
        fc.setDialogTitle("Load file");
        int result = fc.showOpenDialog(null);
        // If filename is valid then sets the filename
        if (result == JFileChooser.APPROVE_OPTION){
            fileName = fc.getSelectedFile();
        } else {
            System.out.println("Sorry invalid file name");
        }
        // Open the file into an array
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(fileName))) {
            ArrayList<Student> student = (ArrayList<Student>) in.readObject();

            if (DEBUGMODE){
                for (int i = 0; i < student.size(); i++){
                    System.out.printf("%s %s %s %s%n",
                            student.get(i).getStudentID(),
                            student.get(i).getFname(),
                            student.get(i).getLname(),
                            student.get(i).getProgram());
                }
            System.out.println(studentList.size());
                for (int i = 0; i < studentList.size(); i++){
                    System.out.printf("%s %s %s %s%n",
                            studentList.get(i).getStudentID(),
                            studentList.get(i).getFname(),
                            studentList.get(i).getLname(),
                            studentList.get(i).getProgram());
                }

            }
            // Copy/Clone list into the student list
            studentList = (ArrayList<Student>) student.clone();
            // Load the first student
            loadStudent(studentList.get(0));
            // Set the next num for adding
            Student.setNextNum(studentList.size() - 1);
            // Enable buttons
            btnNext.setEnabled(true);
            btnAdd.setEnabled(true);
            btnEdit.setEnabled(true);
            update();

        }
        catch (IOException | SecurityException| ClassNotFoundException | NullPointerException ex){
            ex.getStackTrace();
        }
        // Set index to 0
        currentIndex = 0;
        update();
    }

    // Event Handlers for the buttons
    public void actionPerformed(ActionEvent e) {

        ///////////////////////////////////////////////////////
        // PREVIOUS BUTTON EVENT
        ///////////////////////////////////////////////////////
        // When Next Button is clicked, it increases the index by 1 and loads that student.
        if (e.getSource() == btnPrev) {
            update();
            if (currentIndex > 0) {
                currentIndex -= 1;
                loadStudent(studentList.get(currentIndex));
                if (DEBUGMODE){
                    System.out.println("Current Index: "+ currentIndex);
                    System.out.println("Next index from Prev is: "+ currentIndex);
                }
            } else {
                System.out.println("Out of range");
            }
        }
        ////////////////////////////////////////////////////////////////////
        // NEXT BUTTON EVENT
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
        } // END OF NEXT BUTTON

        ///////////////////////////////////////////////////////////////////////
        // SAVE BUTTON EVENT
        if (e.getSource() == btnSave){
            saveStudentsToFile();
        } // END OF SAVE BUTTON

        ///////////////////////////////////////////////////////////////////////
        // LOAD BUTTON EVENT
        if (e.getSource() == btnLoad){
            loadStudentFile();
        } // END OF LOAD BUTTON
    } // END OF EVENTS
} // END CLASS

