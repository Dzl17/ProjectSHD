package com.a02.game.windows;

import com.a02.game.User;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;

import static com.a02.game.components.Utils.*;

public class UsersWindow extends JFrame {
    boolean isEditable = false;

    JTable table;
    HashMap<String, User> map;

    public UsersWindow() {
        //1.- Ajustes de ventana
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());   //UI Look&Feel más moderno y limpio
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("User management");
        setSize(740,410);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(true);
        setIconImage(Toolkit.getDefaultToolkit().createImage("core/assets/boredlion.png"));
        this.setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //2.- Creación de componentes
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(10, 1));
        JPanel leftPanel1 = new JPanel();
        JPanel leftPanel2 = new JPanel();
        JPanel leftPanel3 = new JPanel();
        JPanel leftPanel4 = new JPanel();
        JPanel leftPanel5 = new JPanel();
        JPanel leftPanel6 = new JPanel();
        leftPanel1.setLayout(new FlowLayout());
        leftPanel2.setLayout(new FlowLayout());
        leftPanel3.setLayout(new FlowLayout());
        leftPanel4.setLayout(new FlowLayout());
        leftPanel5.setLayout(new FlowLayout());
        leftPanel6.setLayout(new FlowLayout());
        leftPanel1.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));

        final JCheckBox editJCB = new JCheckBox();
        JLabel editLabel = new JLabel("Make editable");
        JButton saveButton = new JButton("Save changes");
        JButton deleteButton = new JButton("Delete user");
        JButton newUserButton = new JButton("Create new user");
        final JButton refreshButton = new JButton("Refresh table");
        JButton exitButton = new JButton("Exit");
        //Tamaños
        saveButton.setPreferredSize(new Dimension(114, 21));
        deleteButton.setPreferredSize(new Dimension(114, 21));
        newUserButton.setPreferredSize(new Dimension(114, 21));
        refreshButton.setPreferredSize(new Dimension(114, 21));
        exitButton.setPreferredSize(new Dimension(114, 21));

        //Tabla
        table = null;
        try {
            map = readSer("users.ser");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (map != null) table = new JTable(new UserTableModel(map));

        table.setAutoCreateRowSorter(true);
        table.setShowGrid(true);

        //3.- Interacción
        editJCB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isEditable = editJCB.isSelected();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Save changed data?",
                        "Save changes", JOptionPane.YES_NO_OPTION) == 0) {
                    try {
                        writeSer("users.ser", getTableData());
                        refreshTable();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                String key = null;
                if (row >= 0) {
                    key = table.getModel().getValueAt(row, 0).toString();
                    if (JOptionPane.showConfirmDialog(null, "Delete user '" + key + "'?",
                            "Delete user", JOptionPane.YES_NO_OPTION) == 0) {
                        deleteUser("users.ser", key);
                        refreshTable();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Select a user.",
                            "Select user", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        newUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new RegisterWindow().addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                refreshTable();
                            }
                        });
                    }
                });
            }
        });

        refreshButton.addActionListener(new ActionListener() { //Es esto necesario?
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //4.- Introducción de componentes

        leftPanel1.add(editJCB);
        leftPanel1.add(editLabel);
        leftPanel2.add(saveButton);
        leftPanel3.add(deleteButton);
        leftPanel4.add(newUserButton);
        leftPanel5.add(refreshButton);
        leftPanel6.add(exitButton);

        leftPanel.add(leftPanel1);
        leftPanel.add(leftPanel2);
        leftPanel.add(leftPanel3);
        leftPanel.add(leftPanel4);
        leftPanel.add(leftPanel5);
        leftPanel.add(leftPanel6);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(leftPanel, BorderLayout.LINE_END);
    }

    /**
     * TableModel para la tabla de usuarios
     */
    public class UserTableModel extends AbstractTableModel {
        private static final long serialVersionUID = -3976167725010173391L; //No sé si será necesario, pero mejor tenerlo

        private final String[] columnNames = {"Username", "Password", "Name", "Age", "Email", "Admin"};
        private Object[][] data; //Datos de los usuarios

        private HashMap<String, User> map; //Mapa de datos de usuario

        {
            try {
                map = readSer("users.ser");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public UserTableModel(HashMap<String, User> map) { //Pasa los datos del mapa a el array 'data'
            data = new Object[map.size()][6];
            int index = 0;
            for (User user : map.values()) {
                data[index][0] = user.getUsername();
                data[index][1] = user.getPassword();
                data[index][2] = user.getName();
                data[index][3] = user.getAge();
                data[index][4] = user.getMail();
                data[index][5] = user.isAdmin();
                index++;
            }
        }

        @Override
        public int getRowCount() {
            return map.size();
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            data[rowIndex][columnIndex] = value;
            fireTableCellUpdated(rowIndex, columnIndex);
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return isEditable;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 3) return Integer.class;
            else if (columnIndex == 5) return Boolean.class;
            else return String.class;
        }

    }

    private HashMap<String, User> getMap() {
        HashMap<String, User> tempMap = null;
        try {
            tempMap = readSer("users.ser");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tempMap;
    }

    private void refreshTable() {
        table.setModel(new UserTableModel(getMap()));
    }

    private HashMap<String, User> getTableData(){
        HashMap<String, User> map = new HashMap<>();
        for (int row = 0; row < table.getModel().getRowCount(); row++){
            User user = new User();
            user.setUsername(table.getValueAt(row, 0).toString());
            user.setPassword(table.getValueAt(row, 1).toString());
            user.setName(table.getValueAt(row, 2).toString());
            user.setAge((Integer) table.getValueAt(row, 3));
            user.setMail(table.getValueAt(row, 4).toString());
            user.setAdmin((Boolean) table.getValueAt(row, 5));

            map.put(user.getUsername(), user);
        }
        return map;
    }

    @Deprecated
    private JTable createTable(HashMap<String, User> map) { //Para testing
        String[] columnNames = {"Username", "Password", "Name", "Age", "Email", "Admin"};
        String[][] data = new String[map.size()][6];
        int index = 0;
        for (User user : map.values()) {
            data[index][0] = user.getUsername();
            data[index][1] = user.getPassword();
            data[index][2] = user.getName();
            data[index][3] = String.valueOf(user.getAge());
            data[index][4] = user.getMail();
            data[index][5] = String.valueOf(user.isAdmin());
            index++;
        }
        return new JTable(data, columnNames);
    }
}
