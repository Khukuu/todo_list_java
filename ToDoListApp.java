import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class ToDoListApp {
    // PANELSSSS
    private static ArrayList<JPanel> taskPanels = new ArrayList<>();
    private static ArrayList<JPanel> historyPanels = new ArrayList<>();
    private static JTabbedPane tabbedPane = new JTabbedPane();
    private static final int HISTORY_LIMIT = 10;
    private static Color panelColor = Color.WHITE; // Assuming some default color
    private static Color foregroundColor = Color.BLACK; // Assuming some default color

    public static void main(String[] args) {
        // FRAME
        JFrame frame = new JFrame("ToDoIt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Load tasks from file at startup
        loadTasksFromFile();

        // BUTTONS
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton addTaskButton = new JButton("Add Task");
        JButton createNewListButton = new JButton("Create New List");
        JButton deleteListButton = new JButton("Delete List"); // New delete button

        // ACTION LISTENER: Create New List
        createNewListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                while (true) {
                    try {
                        String listName = JOptionPane.showInputDialog(null, "Enter the name of the new list:", "New List", JOptionPane.PLAIN_MESSAGE);
                        
                        // Check if the input is null (Cancel button pressed)
                        if (listName == null) {
                            return; // Exit the method, closing the dialog without creating a list
                        }

                        // Check for empty or null input
                        if (listName.trim().isEmpty()) {
                            throw new Exception("List name cannot be empty.");
                        }

                        JPanel newTaskPanel = new JPanel();
                        newTaskPanel.setLayout(new BoxLayout(newTaskPanel, BoxLayout.Y_AXIS));
                        newTaskPanel.setBackground(panelColor);
                        JScrollPane newTaskScrollPane = new JScrollPane(newTaskPanel);
                        newTaskScrollPane.setPreferredSize(new Dimension(300, 500));
                        newTaskScrollPane.getViewport().setBackground(panelColor);

                        JPanel newHistoryPanel = new JPanel();
                        newHistoryPanel.setLayout(new BoxLayout(newHistoryPanel, BoxLayout.Y_AXIS));
                        newHistoryPanel.setPreferredSize(new Dimension(300, 100));
                        newHistoryPanel.setBackground(panelColor);

                        JPanel fullPanel = new JPanel();
                        fullPanel.setLayout(new BoxLayout(fullPanel, BoxLayout.Y_AXIS));
                        fullPanel.setBackground(panelColor);
                        fullPanel.add(newTaskScrollPane);
                        fullPanel.add(newHistoryPanel);

                        tabbedPane.addTab(listName, fullPanel);
                        taskPanels.add(newTaskPanel);
                        historyPanels.add(newHistoryPanel);
                        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

                        frame.revalidate();
                        frame.repaint();
                        break; // Exit retry loop
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // ACTION LISTENER: Add Task
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if a list is selected
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "There are currently no list.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit the method without doing anything
                }

                while (true) {
                    try {
                        // Proceed with adding the task
                        JPanel currentTaskPanel = taskPanels.get(selectedIndex);
                        JPanel currentHistoryPanel = historyPanels.get(selectedIndex);

                        String task = JOptionPane.showInputDialog(null, "Enter a new task:", "Add Task", JOptionPane.PLAIN_MESSAGE);
                        
                        // Check if the input is null (Cancel button pressed)
                        if (task == null) {
                            return; // Exit the method, closing the dialog without adding a task
                        }

                        // Check for empty or null input
                        if (task.trim().isEmpty()) {
                            throw new Exception("Task cannot be empty.");
                        }

                        JCheckBox taskCheckBox = new JCheckBox(task);
                        taskCheckBox.setBackground(panelColor);
                        taskCheckBox.setForeground(foregroundColor);

                        taskCheckBox.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (taskCheckBox.isSelected()) {
                                    taskCheckBox.setText("<html><strike>" + task + "</strike></html>");
                                    if (currentHistoryPanel.getComponentCount() >= HISTORY_LIMIT) {
                                        currentHistoryPanel.remove(currentHistoryPanel.getComponentCount() - 1);
                                    }
                                    currentHistoryPanel.add(taskCheckBox, 0);
                                    currentTaskPanel.remove(taskCheckBox);
                                } else {
                                    taskCheckBox.setText(task);
                                    currentHistoryPanel.remove(taskCheckBox);
                                    currentTaskPanel.add(taskCheckBox);
                                }
                                currentTaskPanel.revalidate();
                                currentTaskPanel.repaint();
                                currentHistoryPanel.revalidate();
                                currentHistoryPanel.repaint();
                            }
                        });

                        currentTaskPanel.add(taskCheckBox);
                        currentTaskPanel.revalidate();
                        currentTaskPanel.repaint();
                        break; // Exit retry loop
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        // ACTION LISTENER: Delete List
        deleteListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex != -1) {
                    // CONFIRMATION DIALOG BEFORE DELETING LIST
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this list?", "Delete List", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // REMOVE SELECTED TAB AND ASSOCIATED PANELS
                        tabbedPane.remove(selectedIndex);
                        taskPanels.remove(selectedIndex);
                        historyPanels.remove(selectedIndex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No list selected to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ADD BUTTONS TO PANEL
        buttonPanel.add(addTaskButton);
        buttonPanel.add(createNewListButton);
        buttonPanel.add(deleteListButton);

        // ADD TABBED PANE AND BUTTON PANEL TO FRAME
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Add window listener to save tasks on close
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveTasksToFile();
            }
        });

        // FRAME SIZE AND DISPLAY
        frame.setSize(400, 600);
        frame.setVisible(true);

        // INITIALIZE WITH A DEFAULT LIST IF NONE EXIST
        if (tabbedPane.getTabCount() == 0) {
            createNewListButton.doClick();
        }
    }

    // Save tasks to a file, including list names
    private static void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
            for (int i = 0; i < taskPanels.size(); i++) {
                JPanel taskPanel = taskPanels.get(i);
                String listName = tabbedPane.getTitleAt(i);
                writer.write("LIST_START," + listName);
                writer.newLine();
                for (Component component : taskPanel.getComponents()) {
                    if (component instanceof JCheckBox) {
                        JCheckBox taskCheckBox = (JCheckBox) component;
                        writer.write(taskCheckBox.getText().replaceAll("<.*?>", "") + "," + taskCheckBox.isSelected());
                        writer.newLine();
                    }
                }
                writer.write("LIST_END");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load tasks from a file, including list names
    private static void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"))) {
            String line;
            JPanel currentTaskPanel = null;
            JPanel currentHistoryPanel = null;
            String listName = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("LIST_START")) {
                    String[] parts = line.split(",", 2);
                    listName = (parts.length > 1) ? parts[1] : "Unnamed List";

                    currentTaskPanel = new JPanel();
                    currentTaskPanel.setLayout(new BoxLayout(currentTaskPanel, BoxLayout.Y_AXIS));
                    currentTaskPanel.setBackground(panelColor);
                    taskPanels.add(currentTaskPanel);

                    currentHistoryPanel = new JPanel();
                    currentHistoryPanel.setLayout(new BoxLayout(currentHistoryPanel, BoxLayout.Y_AXIS));
                    currentHistoryPanel.setPreferredSize(new Dimension(300, 100));
                    currentHistoryPanel.setBackground(panelColor);
                    historyPanels.add(currentHistoryPanel);

                } else if (line.equals("LIST_END")) {
                    JPanel fullPanel = new JPanel();
                    fullPanel.setLayout(new BoxLayout(fullPanel, BoxLayout.Y_AXIS));
                    fullPanel.setBackground(panelColor);

                    JScrollPane newTaskScrollPane = new JScrollPane(currentTaskPanel);
                    newTaskScrollPane.setPreferredSize(new Dimension(300, 500));
                    newTaskScrollPane.getViewport().setBackground(panelColor);

                    fullPanel.add(newTaskScrollPane);
                    fullPanel.add(currentHistoryPanel);

                    tabbedPane.addTab(listName, fullPanel);
                    tabbedPane.setBackground(panelColor);
                    tabbedPane.setForeground(foregroundColor);
                } else {
                    String[] taskData = line.split(",");
                    if (taskData.length == 2 && currentTaskPanel != null && currentHistoryPanel != null) {
                        String taskName = taskData[0];
                        boolean isChecked = Boolean.parseBoolean(taskData[1]);

                        JCheckBox taskCheckBox = new JCheckBox(taskName);
                        taskCheckBox.setSelected(isChecked);
                        taskCheckBox.setBackground(panelColor);
                        taskCheckBox.setForeground(foregroundColor);

                        if (isChecked) {
                            taskCheckBox.setText("<html><strike>" + taskName + "</strike></html>");
                            currentHistoryPanel.add(taskCheckBox, 0);
                        } else {
                            currentTaskPanel.add(taskCheckBox);
                        }

                        final JPanel finalCurrentTaskPanel = currentTaskPanel;
                        final JPanel finalCurrentHistoryPanel = currentHistoryPanel;
                        final String finalTaskName = taskName;

                        taskCheckBox.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (taskCheckBox.isSelected()) {
                                    taskCheckBox.setText("<html><strike>" + finalTaskName + "</strike></html>");
                                    if (finalCurrentHistoryPanel.getComponentCount() >= HISTORY_LIMIT) {
                                        finalCurrentHistoryPanel.remove(finalCurrentHistoryPanel.getComponentCount() - 1);
                                    }
                                    finalCurrentHistoryPanel.add(taskCheckBox, 0);
                                    finalCurrentTaskPanel.remove(taskCheckBox);
                                } else {
                                    taskCheckBox.setText(finalTaskName);
                                    finalCurrentHistoryPanel.remove(taskCheckBox);
                                    finalCurrentTaskPanel.add(taskCheckBox);
                                }
                                finalCurrentTaskPanel.revalidate();
                                finalCurrentTaskPanel.repaint();
                                finalCurrentHistoryPanel.revalidate();
                                finalCurrentHistoryPanel.repaint();
                            }
                        });
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}