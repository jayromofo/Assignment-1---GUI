import javax.swing.*;

/**
 * Project: Assignment 1 - GUI
 * Author: Jason Rossetti
 * Created: 2021-09-14
 */
public class StudentFrame extends JFrame {
    private JButton btnPrev = new JButton();
    private JButton btnNext = new JButton();
    private JButton btnLoad = new JButton();
    private JButton btnEdit = new JButton();
    private JButton btnAdd = new JButton();
    private JButton btnSave = new JButton();

    private JPanel panID = new JPanel();
    private JLabel lblID = new JLabel("ID: ");
    private JTextField txtID = new JTextField();

    private JPanel panFirstName = new JPanel();
    private JLabel lblFirstName = new JLabel("First Name: ");
    private JTextField txtFirstName = new JTextField();

    private JPanel panLastName = new JPanel();
    private JLabel lblLastName = new JLabel("Last Name: ");
    private JTextField txtLastName = new JTextField();

    private JPanel panProgram = new JPanel();
    private JLabel lblProgram = new JLabel("Program: ");
    private JTextField txtProgram = new JTextField();

    public StudentFrame(){

    }



}
