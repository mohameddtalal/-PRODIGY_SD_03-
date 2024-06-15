import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactManagementSystem extends JFrame {
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTable contactTable;
    private DefaultTableModel tableModel;

    private List<Contact> contacts = new ArrayList<>();
    private static final String FILE_NAME = "contacts.txt";

    public ContactManagementSystem() {
        setTitle("Contact Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 20, 100, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(120, 20, 150, 25);
        add(nameField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(20, 60, 100, 25);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(120, 60, 150, 25);
        add(phoneField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 100, 100, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(120, 100, 150, 25);
        add(emailField);

        JButton addButton = new JButton("Add Contact");
        addButton.setBounds(20, 140, 150, 25);
        add(addButton);

        JButton editButton = new JButton("Edit Contact");
        editButton.setBounds(180, 140, 150, 25);
        add(editButton);

        JButton deleteButton = new JButton("Delete Contact");
        deleteButton.setBounds(340, 140, 150, 25);
        add(deleteButton);

        tableModel = new DefaultTableModel(new String[]{"Name", "Phone", "Email"}, 0);
        contactTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(contactTable);
        scrollPane.setBounds(20, 180, 550, 150);
        add(scrollPane);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editContact();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContact();
            }
        });

        loadContacts();
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        Contact contact = new Contact(name, phone, email);
        contacts.add(contact);
        tableModel.addRow(new Object[]{name, phone, email});

        saveContacts();
    }

    private void editContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow != -1) {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();

            Contact contact = contacts.get(selectedRow);
            contact.setName(name);
            contact.setPhone(phone);
            contact.setEmail(email);

            tableModel.setValueAt(name, selectedRow, 0);
            tableModel.setValueAt(phone, selectedRow, 1);
            tableModel.setValueAt(email, selectedRow, 2);

            saveContacts();
        }
    }

    private void deleteContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow != -1) {
            contacts.remove(selectedRow);
            tableModel.removeRow(selectedRow);

            saveContacts();
        }
    }

    private void saveContacts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Contact contact : contacts) {
                writer.write(contact.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContacts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Contact contact = new Contact(parts[0], parts[1], parts[2]);
                    contacts.add(contact);
                    tableModel.addRow(new Object[]{contact.getName(), contact.getPhone(), contact.getEmail()});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ContactManagementSystem().setVisible(true);
            }
        });
    }
}

class Contact {
    private String name;
    private String phone;
    private String email;

    public Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return name + "," + phone + "," + email;
    }
}
