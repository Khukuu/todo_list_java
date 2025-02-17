import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
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
        JButton addTaskButton = new JButton("Add to List"); // Changed the label here
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

                        // Add mouse listener for renaming the list tab
                        addRightClickRenameTab(tabbedPane, tabbedPane.getTabCount() - 1);

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

                        // Action to handle task completion (strike-through)
                        taskCheckBox.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // Strike-through effect without opening a new window
                                if (taskCheckBox.isSelected()) {
                                    taskCheckBox.setText("<html><strike>" + taskCheckBox.getText() + "</strike></html>");
                                    if (currentHistoryPanel.getComponentCount() >= HISTORY_LIMIT) {
                                        currentHistoryPanel.remove(currentHistoryPanel.getComponentCount() - 1);
                                    }
                                    currentHistoryPanel.add(taskCheckBox, 0);
                                    currentTaskPanel.remove(taskCheckBox);
                                } else {
                                    taskCheckBox.setText(taskCheckBox.getText().replaceAll("<.*?>", "")); // Remove HTML tags for editing
                                    currentHistoryPanel.remove(taskCheckBox);
                                    currentTaskPanel.add(taskCheckBox);
                                }
                                currentTaskPanel.revalidate();
                                currentTaskPanel.repaint();
                                currentHistoryPanel.revalidate();
                                currentHistoryPanel.repaint();
                            }
                        });

                        // Add right-click edit feature for tasks (Only Rename or Delete)
                        addRightClickEditFeature(taskCheckBox, currentTaskPanel, currentHistoryPanel);

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

    // Add right-click edit feature for tasks (Only Rename or Delete)
    private static void addRightClickEditFeature(JCheckBox checkBox, JPanel taskPanel, JPanel historyPanel) {
        checkBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Only allow editing if the task is not checked off
                if (!checkBox.isSelected() && SwingUtilities.isRightMouseButton(e)) {
                    String[] options = {"Rename", "Delete"};
                    int choice = JOptionPane.showOptionDialog(
                            null,
                            "Choose an action:",
                            "Right-click Task Options",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[0]  // Default to Rename
                    );

                    if (choice == 0) { // Rename
                        String newTaskName = JOptionPane.showInputDialog(null, "Rename task:", checkBox.getText().replaceAll("<.*?>", ""));
                        if (newTaskName != null && !newTaskName.trim().isEmpty()) {
                            checkBox.setText(newTaskName);
                        }
                    } else if (choice == 1) { // Delete
                        int confirmDelete = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this task?", "Delete Task", JOptionPane.YES_NO_OPTION);
                        if (confirmDelete == JOptionPane.YES_OPTION) {
                            taskPanel.remove(checkBox);
                            historyPanel.remove(checkBox);
                            taskPanel.revalidate();
                            taskPanel.repaint();
                            historyPanel.revalidate();
                            historyPanel.repaint();
                        }
                    }
                }
            }
        });
    }

    // Add right-click rename feature for JTabbedPane tabs
    private static void addRightClickRenameTab(JTabbedPane tabbedPane, int index) {
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && tabbedPane.getSelectedIndex() == index) {
                    int choice = JOptionPane.showConfirmDialog(null, "Do you want to rename this list?", "Rename List", JOptionPane.YES_NO_OPTION);

                    if (choice == JOptionPane.YES_OPTION) {
                        String newName = JOptionPane.showInputDialog(null, "Rename list:", tabbedPane.getTitleAt(index));
                        if (newName != null && !newName.trim().isEmpty()) {
                            tabbedPane.setTitleAt(index, newName);
                        }
                    }
                }
            }
        });
    }

    // Load tasks from file (Placeholder function)
    private static void loadTasksFromFile() {
        // Placeholder for loading tasks from a file
    }

    // Save tasks to file (Placeholder function)
    private static void saveTasksToFile() {
        // Placeholder for saving tasks to a file
    }
}