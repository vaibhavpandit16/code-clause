import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class HospitalManagementSystem extends JFrame implements ActionListener {
    JButton registerPatient, bookAppointment, viewPatients;

    public HospitalManagementSystem() {
        setTitle("Hospital Management System");
        setSize(400, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        registerPatient = new JButton("Register Patient");
        registerPatient.setBounds(100, 50, 200, 30);
        registerPatient.addActionListener(this);
        add(registerPatient);

        bookAppointment = new JButton("Book Appointment");
        bookAppointment.setBounds(100, 100, 200, 30);
        bookAppointment.addActionListener(this);
        add(bookAppointment);

        viewPatients = new JButton("View Patients");
        viewPatients.setBounds(100, 150, 200, 30);
        viewPatients.addActionListener(this);
        add(viewPatients);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerPatient) {
            new PatientRegistration();
        } else if (e.getSource() == bookAppointment) {
            new AppointmentBooking();
        } else if (e.getSource() == viewPatients) {
            new PatientViewer();
        }
    }

    class PatientRegistration extends JFrame implements ActionListener {
        JTextField firstNameField, lastNameField, dobField, contactField, addressField;
        JButton registerButton;

        public PatientRegistration() {
            setTitle("Patient Registration");
            setSize(400, 400);
            setLayout(null);

            JLabel firstNameLabel = new JLabel("First Name:");
            firstNameLabel.setBounds(50, 50, 100, 20);
            add(firstNameLabel);

            firstNameField = new JTextField();
            firstNameField.setBounds(150, 50, 200, 20);
            add(firstNameField);

            JLabel lastNameLabel = new JLabel("Last Name:");
            lastNameLabel.setBounds(50, 100, 100, 20);
            add(lastNameLabel);

            lastNameField = new JTextField();
            lastNameField.setBounds(150, 100, 200, 20);
            add(lastNameField);

            JLabel dobLabel = new JLabel("Date of Birth (yyyy-mm-dd):");
            dobLabel.setBounds(50, 150, 200, 20);
            add(dobLabel);

            dobField = new JTextField();
            dobField.setBounds(150, 150, 200, 20);
            add(dobField);

            JLabel contactLabel = new JLabel("Contact:");
            contactLabel.setBounds(50, 200, 100, 20);
            add(contactLabel);

            contactField = new JTextField();
            contactField.setBounds(150, 200, 200, 20);
            add(contactField);

            JLabel addressLabel = new JLabel("Address:");
            addressLabel.setBounds(50, 250, 100, 20);
            add(addressLabel);

            addressField = new JTextField();
            addressField.setBounds(150, 250, 200, 20);
            add(addressField);

            registerButton = new JButton("Register");
            registerButton.setBounds(150, 300, 100, 30);
            registerButton.addActionListener(this);
            add(registerButton);

            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String dob = dobField.getText();
            String contact = contactField.getText();
            String address = addressField.getText();

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hospital", "root", "V@!bh@v@16")) {
                String query = "INSERT INTO Patients (first_name, last_name, dob, contact, address) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, dob);
                stmt.setString(4, contact);
                stmt.setString(5, address);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Patient Registered Successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    class AppointmentBooking extends JFrame implements ActionListener {
        JTextField patientIdField, doctorNameField, dateField, timeField;
        JButton bookButton;

        public AppointmentBooking() {
            setTitle("Book Appointment");
            setSize(400, 300);
            setLayout(null);

            JLabel patientIdLabel = new JLabel("Patient ID:");
            patientIdLabel.setBounds(50, 50, 100, 20);
            add(patientIdLabel);

            patientIdField = new JTextField();
            patientIdField.setBounds(150, 50, 200, 20);
            add(patientIdField);

            JLabel doctorNameLabel = new JLabel("Doctor Name:");
            doctorNameLabel.setBounds(50, 100, 100, 20);
            add(doctorNameLabel);

            doctorNameField = new JTextField();
            doctorNameField.setBounds(150, 100, 200, 20);
            add(doctorNameField);

            JLabel dateLabel = new JLabel("Date (yyyy-mm-dd):");
            dateLabel.setBounds(50, 150, 150, 20);
            add(dateLabel);

            dateField = new JTextField();
            dateField.setBounds(150, 150, 200, 20);
            add(dateField);

            JLabel timeLabel = new JLabel("Time (HH:mm):");
            timeLabel.setBounds(50, 200, 100, 20);
            add(timeLabel);

            timeField = new JTextField();
            timeField.setBounds(150, 200, 200, 20);
            add(timeField);

            bookButton = new JButton("Book");
            bookButton.setBounds(150, 250, 100, 30);
            bookButton.addActionListener(this);
            add(bookButton);

            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int patientId = Integer.parseInt(patientIdField.getText());
            String doctorName = doctorNameField.getText();
            String date = dateField.getText();
            String time = timeField.getText();

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hospital", "root", "V@!bh@v@16")) {
                String query = "INSERT INTO Appointments (patient_id, doctor_name, appointment_date, appointment_time, status) VALUES (?, ?, ?, ?, 'Scheduled')";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, patientId);
                stmt.setString(2, doctorName);
                stmt.setString(3, date);
                stmt.setString(4, time);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Appointment Booked Successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    class PatientViewer extends JFrame {
        JTable table;

        public PatientViewer() {
            setTitle("Patient Records");
            setSize(600, 400);

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hospital", "root", "V@!bh@v@16")) {
                String query = "SELECT * FROM Patients";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                table = new JTable(buildTableModel(rs));
                JScrollPane scrollPane = new JScrollPane(table);
                add(scrollPane);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

            setVisible(true);
        }

        public DefaultTableModel buildTableModel(ResultSet rs) throws Exception {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Column names
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Data rows
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            return model;
        }
    }

    public static void main(String[] args) {
        new HospitalManagementSystem();
    }
}
