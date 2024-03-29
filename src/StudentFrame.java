/**
 * Project: Assignment 3 - Save and Load to file
 * Author: Jason Rossetti
 * Created: 2021-11-07
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

/**
 * Creates the main frame
 */
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

    private final JButton btnEdit = new JButton("Edit");
    private final JButton btnAdd = new JButton("Add");

    // Bottom portion of the frame
    private final JLabel lblMarks = new JLabel("Marks");
    // Parent panel of the mark area
    private final JPanel panMarkArea = new JPanel(new BorderLayout());
    // Panel that holds all the mark text boxes
    private final JPanel panMarks = new JPanel(new GridLayout(2, 3));
    private final JTextField[] txtMarks = new JTextField[6];

    // Helper Variables
    // List that holds all the initial students. Remains empty if the database is empty
    private static ArrayList<Student> beforeStudentList = new ArrayList<>();
    // List that copies the initial list. All the new students and updated students are in this list
    private static ArrayList<Student> afterStudentList = new ArrayList<>();
    private static int currentIndex = 0;
    private final static boolean DEBUGMODE = false;  // Turns on/off debug mode
    private static State currentState;
    static boolean isEditing = false;

    // Database Variables
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private static ResultSetMetaData meta;
    private static String connectionString = "jdbc:postgresql://localhost:5432/javaclass";
    private static String username = "postgres";
    private static String password = "admin";
    private static PreparedStatement preparedStatement;
    private static String selectQuery;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FUNCTIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructor for the main frame
     * @param name Pass in the name of the application
     */
    public StudentFrame(String name) {
        super(name);
        currentState = State.RUNNING;
        // Generate fields
        generateFields();
        // Startup state for text boxes and buttons
        setInitialState();

        try {
            int count = 0;
            addFromDatabase();
            for (var student : beforeStudentList){
                System.out.println(student.getFname());
                count++;
            }
            // If there's items in the list then database loaded
            if (count != 0){
                System.out.println(beforeStudentList.size());
                afterStudentList = (ArrayList<Student>) beforeStudentList.clone(); // Clone the initial list
                loadStudent(afterStudentList.get(0));
                btnEdit.setEnabled(true);
            }

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
                    afterStudentList.add(newStudent);
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

                    btnPrev.setEnabled(false);

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

                    btnPrev.setEnabled(false);

                    btnNext.setEnabled(false);
                    btnAdd.setEnabled(false);
                } else if (currentState == State.EDIT && isEditing == true) {
                    try {
                        // Make replacement student
                        Student currentStudent = beforeStudentList.get(currentIndex);
                        String originalID = currentStudent.getStudentID();
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
                            afterStudentList.set(currentIndex, currentStudent);
                            updateRowInDatabase(originalID, currentStudent);
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


                        btnNext.setEnabled(true);
                        btnPrev.setEnabled(true);
                        currentState = State.RUNNING;
                        enableTextBoxes(false);
                        update();
                    }
                    // If current state is on CREATE when edit button is pressed
                } else if (currentState == State.CREATE) {
                    try {
                        createStudent();
                        enableTextBoxes(false);
                        if (DEBUGMODE) {
                            System.out.println(currentIndex); // DEBUG
                        }
                        update();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        btnEdit.setText("Edit");
                        btnAdd.setEnabled(true);

                        btnNext.setEnabled(false);

                        currentState = State.RUNNING;
                    }
                }
            });
            // Add the other action listeners
            btnPrev.addActionListener(this);
            btnNext.addActionListener(this);


        } catch (SQLException ex) {
            // End Constructor
            ex.printStackTrace();
        }
    }

    /**
     * Generates all of the fields needed for the UI
     */
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

        panButtonRow.add(btnEdit);
        panButtonRow.add(btnAdd);

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

    /**
     * Set the initial state for the buttons in the UI
     */
    private void setInitialState() {
        // Set the buttons and text fields
        currentState = State.RUNNING;

        btnAdd.setEnabled(true);
        btnPrev.setEnabled(false);
        btnEdit.setEnabled(false);

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

    /**
     * Update all the information to get the button states. Use this after each event to keep it up to date
     */
    private void update() {
        int length = afterStudentList.size();
        if (DEBUGMODE){
            System.out.println("From Update\nIndex: "+currentIndex +"\tList Length: "+ beforeStudentList.size()); // DEBUG
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
            if (afterStudentList.isEmpty()) {
                btnPrev.setEnabled(false);
                btnNext.setEnabled(false);
            }
            // If index is 0 prev disabled
            if (currentIndex == 0) {
                btnPrev.setEnabled(false);
            }  else if (currentIndex == (length - 1) || afterStudentList.size() == 1){
                btnNext.setEnabled(false);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Clear all the mark text fields
     */
    private void clearMarks() {
        for (JTextField marks : txtMarks) {
            marks.setText("");
        }
    }

    /**
     * Clear all the text fields
     */
    private void clearText() {
        txtID.setText("");
        txtFirstName.setText("");
        txtProgram.setText("");
        txtLastName.setText("");
    }

    /**
     * Enable or disable all the text boxes
     * @param result returns true or false
     */
    private void enableTextBoxes(boolean result) {
        txtFirstName.setEnabled(result);
        txtID.setEnabled(result);
        txtProgram.setEnabled(result);
        txtLastName.setEnabled(result);
        for (JTextField txt : txtMarks){
            txt.setEnabled(result);
        }
    }

    /**
     * Gets the text from the text boxes and sets it in the created student.
     */
    private void createStudent(){
        Student currentStudent = afterStudentList.get(afterStudentList.size() - 1);
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
        insertStudentIntoDatabase(currentStudent);
        // Repositions the current index
        currentIndex = afterStudentList.size() - 1;

    }

    /**
     * Loads the current student into the text boxes
     * @param student Pass in a student object to load
     */
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

    /**
     * Adds the student table and marks table to a list and loads it into the program
      * @throws SQLException
     */
    public static void addFromDatabase() throws SQLException {

        selectQuery = "Select * from assignment.students INNER JOIN assignment.student_marks sm on students.student_id = sm.id " +
                "ORDER BY student_id ASC";
        resultSet = null;
        statement = null;
       try (Connection connection = DriverManager.getConnection(connectionString, username, password)){
           statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
           resultSet = statement.executeQuery(selectQuery);
//
           while (resultSet.next()){
               double[] marks = new double[6];
               String student_id = resultSet.getString("student_id");
               String first_name = resultSet.getString("first_name");
               String last_name = resultSet.getString("last_name");
               String program = resultSet.getString("program");
               marks[0] = resultSet.getDouble("mark_1");
               marks[1] = resultSet.getDouble("mark_2");
               marks[2] = resultSet.getDouble("mark_3");
               marks[3] = resultSet.getDouble("mark_4");
               marks[4] = resultSet.getDouble("mark_5");
               marks[5] = resultSet.getDouble("mark_6");
               if (DEBUGMODE){
                   System.out.printf("%S %S %S %S \n%f %f %f %f %f %f\n\n", student_id, first_name, last_name, program,
                           marks[0], marks[1], marks[2], marks[3], marks[4], marks[5]);

               }
               Student newStudent = new Student(first_name, last_name, program, marks);
               newStudent.setStudentID(student_id);
               beforeStudentList.add(newStudent);
           }
       }
    }

    /**
     * Updates a student in the database
     * @param originalID Students original studentID in case it gets updated
     * @param student Student to be updated
     */
    public static void updateRowInDatabase(String originalID, Student student) {
        String updateQuery = "UPDATE assignment.students " +
                "SET student_id = ?, " +
                "first_name = ?, " +
                "last_name = ?, " +
                "program = ? " +
                "WHERE student_id = ?";
        String updateMarkQuery = "UPDATE assignment.student_marks " +
                "SET mark_1 = ?, " +
                "mark_2 = ?, " +
                "mark_3 = ?, " +
                "mark_4 = ?, " +
                "mark_5 = ?, " +
                "mark_6 = ? " +
                "WHERE id = ?";
        try
        {
            connection = DriverManager.getConnection(connectionString, username, password);
            // Update the student table
            try
            {
                preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setString(1, student.getStudentID());
                preparedStatement.setString(2, student.getFname());
                preparedStatement.setString(3, student.getLname());
                preparedStatement.setString(4, student.getProgram());
                preparedStatement.setString(5, originalID);
                preparedStatement.executeUpdate();
            } catch (SQLException ex){
                ex.printStackTrace();
            }

            try
            {
                double[] theMarks = student.getMarks();
                preparedStatement = connection.prepareStatement(updateMarkQuery);
                preparedStatement.setDouble(1, theMarks[0]);
                preparedStatement.setDouble(2, theMarks[1]);
                preparedStatement.setDouble(3, theMarks[2]);
                preparedStatement.setDouble(4, theMarks[3]);
                preparedStatement.setDouble(5, theMarks[4]);
                preparedStatement.setDouble(6, theMarks[5]);
                preparedStatement.setString(7, originalID);
                preparedStatement.executeUpdate();
            } catch (SQLException ex){
                ex.printStackTrace();
                System.out.println("Update Failed");
            }

        preparedStatement.close();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Inserts a single student into the database
     * @param student Student to be inserted into database
     */
    public static void insertStudentIntoDatabase(Student student) {
        preparedStatement = null;
        String studentQuery = "INSERT INTO assignment.students (student_id, first_name, last_name, program) " +
                "VALUES (?, ?, ?, ?)";
        String marksQuery = "INSERT INTO assignment.student_marks (mark_1, mark_2, mark_3, mark_4, mark_5, mark_6, id) " +
                "VALUES (?,?,?,?,?,?,?)";
        try
        {
            connection = DriverManager.getConnection(connectionString, username, password);
            // Insert Student
            preparedStatement = connection.prepareStatement(studentQuery);
            preparedStatement.setString(1, student.getStudentID());
            preparedStatement.setString(2, student.getFname());
            preparedStatement.setString(3, student.getLname());
            preparedStatement.setString(4, student.getProgram());
            preparedStatement.executeUpdate();

            // Insert Marks
            double[] marks = student.getMarks();
            preparedStatement = connection.prepareStatement(marksQuery);
            preparedStatement.setDouble(1, marks[0]);
            preparedStatement.setDouble(2, marks[1]);
            preparedStatement.setDouble(3, marks[2]);
            preparedStatement.setDouble(4, marks[3]);
            preparedStatement.setDouble(5, marks[4]);
            preparedStatement.setDouble(6, marks[5]);
            preparedStatement.setString(7, student.getStudentID());
            preparedStatement.executeUpdate();

            // Close the connection and statement
            preparedStatement.close();
            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("InsertFailed");
        }


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
                loadStudent(afterStudentList.get(currentIndex));
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
            if (currentIndex < afterStudentList.size() - 1){
                currentIndex += 1;
                loadStudent(afterStudentList.get(currentIndex));
                if (DEBUGMODE){
                    System.out.println("Current Index: "+ currentIndex); // DEBUG
                    System.out.println("Next index from Next is: "+ currentIndex); // DEBUG
                }
            } else {
                System.out.println("Out of range");
            }
        } // END OF NEXT BUTTON
    } // END OF EVENTS
} // END CLASS

