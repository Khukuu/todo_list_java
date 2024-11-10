import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ToDoListApp {
    // Store references to the task panels for each list
    private static ArrayList<JPanel> taskPanels = new ArrayList<>();
    private static ArrayList<JPanel> historyPanels = new ArrayList<>();
    private static JTabbedPane tabbedPane = new JTabbedPane();
    private static final int HISTORY_LIMIT = 10;

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Todoit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create the bottom button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton addTaskButton = new JButton("Add Task");
        JButton createNewListButton = new JButton("Create New List");

        // Action listener for creating a new list
        createNewListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the user to enter a new list name
                String listName = JOptionPane.showInputDialog(null, "Enter the name of the new list:", "New List", JOptionPane.PLAIN_MESSAGE);
                if (listName != null && !listName.trim().isEmpty()) {
                    // Create a new task panel for the new list
                    JPanel newTaskPanel = new JPanel();
                    newTaskPanel.setLayout(new BoxLayout(newTaskPanel, BoxLayout.Y_AXIS));
                    JScrollPane newTaskScrollPane = new JScrollPane(newTaskPanel);
                    newTaskScrollPane.setPreferredSize(new Dimension(300, 500));

                    // Create a history panel for the new list (history will show below)
                    JPanel newHistoryPanel = new JPanel();
                    newHistoryPanel.setLayout(new BoxLayout(newHistoryPanel, BoxLayout.Y_AXIS)); // Keep vertical layout
                    newHistoryPanel.setPreferredSize(new Dimension(300, 100));
                    newHistoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);  // Align all history components to the left

                    // Panel to hold both tasks and history
                    JPanel fullPanel = new JPanel();
                    fullPanel.setLayout(new BoxLayout(fullPanel, BoxLayout.Y_AXIS));
                    fullPanel.add(newTaskScrollPane);
                    fullPanel.add(newHistoryPanel);

                    // Add the new list to the tabbed pane
                    tabbedPane.addTab(listName, fullPanel);

                    // Store references to the task and history panels
                    taskPanels.add(newTaskPanel);
                    historyPanels.add(newHistoryPanel);

                    // Select the newly created tab
                    tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1); // Select the new list tab

                    frame.revalidate();
                    frame.repaint();
                }
            }
        });

        // Action listener for adding a task to the current list
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the currently selected tab index
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex != -1 && selectedIndex < taskPanels.size()) {
                    // Get the task panel for the currently selected list
                    JPanel currentTaskPanel = taskPanels.get(selectedIndex);
                    JPanel currentHistoryPanel = historyPanels.get(selectedIndex);

                    try {
                        // Prompt for a new task
                        String task = JOptionPane.showInputDialog(null, "Enter a new task:", "Add Task", JOptionPane.PLAIN_MESSAGE);

                        if (task == null || task.trim().isEmpty()) {
                            throw new Exception("Invalid task entry!");
                        }

                        // Create a checkbox for the new task and add it to the task panel
                        JCheckBox taskCheckBox = new JCheckBox(task);
                        taskCheckBox.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (taskCheckBox.isSelected()) {
                                    // Move the task to the history panel below the task list as a JLabel
                                    JLabel completedLabel = new JLabel("<html><strike>" + task + "</strike></html>");
                                    
                                    // Limit the number of items in the history panel (max 10)
                                    if (currentHistoryPanel.getComponentCount() >= HISTORY_LIMIT) {
                                        currentHistoryPanel.remove(currentHistoryPanel.getComponentCount() - 1); // Remove the oldest task (last one)
                                    }

                                    // Add the new completed task to the top of the history panel
                                    currentHistoryPanel.add(completedLabel, 0); // 0 means adding to the top of the panel

                                    // Remove from the task panel
                                    currentTaskPanel.remove(taskCheckBox);

                                    currentTaskPanel.revalidate();
                                    currentTaskPanel.repaint();
                                    currentHistoryPanel.revalidate();
                                    currentHistoryPanel.repaint();
                                }
                            }
                        });
                        currentTaskPanel.add(taskCheckBox);

                        currentTaskPanel.revalidate();
                        currentTaskPanel.repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Add the buttons to the button panel
        buttonPanel.add(addTaskButton);
        buttonPanel.add(createNewListButton);

        // Add the tabbed pane and button panel to the frame
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set the frame size and visibility
        frame.setSize(400, 600);
        frame.setVisible(true);

        // Initially, create the first list
        createNewListButton.doClick();
    }
}

/*nya ichi ni san niya agirato*/
/*Ang sarap ko gar, glob glob glob */
//pre pagawa ng delete function para sa mga lists tyyyyy
