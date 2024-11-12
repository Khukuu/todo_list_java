import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                // THIS PROMPTS USER TO ENTER A NEW LIST
                String listName = JOptionPane.showInputDialog(null, "Enter the name of the new list:", "New List", JOptionPane.PLAIN_MESSAGE);
                if (listName != null && !listName.trim().isEmpty()) {
                    // NEW TASK PANEL FOR NEW LIST
                    JPanel newTaskPanel = new JPanel();
                    newTaskPanel.setLayout(new BoxLayout(newTaskPanel, BoxLayout.Y_AXIS));
                    newTaskPanel.setBackground(panelColor);
                    JScrollPane newTaskScrollPane = new JScrollPane(newTaskPanel);
                    newTaskScrollPane.setPreferredSize(new Dimension(300, 500));
                    newTaskScrollPane.getViewport().setBackground(panelColor);

                    // HISTORY PANEL
                    JPanel newHistoryPanel = new JPanel();
                    newHistoryPanel.setLayout(new BoxLayout(newHistoryPanel, BoxLayout.Y_AXIS));
                    newHistoryPanel.setPreferredSize(new Dimension(300, 100));
                    newHistoryPanel.setBackground(panelColor);

                    // FULL PANEL
                    JPanel fullPanel = new JPanel();
                    fullPanel.setLayout(new BoxLayout(fullPanel, BoxLayout.Y_AXIS));
                    fullPanel.setBackground(panelColor);
                    fullPanel.add(newTaskScrollPane);
                    fullPanel.add(newHistoryPanel);

                    // ADD NEW LIST TO TABBED PANE
                    tabbedPane.addTab(listName, fullPanel);
                    tabbedPane.setBackground(panelColor);
                    tabbedPane.setForeground(foregroundColor);

                    // STORE TASK AND HISTORY PANELS
                    taskPanels.add(newTaskPanel);
                    historyPanels.add(newHistoryPanel);

                    // SELECT THE NEWLY CREATED TAB
                    tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });

        // ACTION LISTENER: Add Task
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex != -1 && selectedIndex < taskPanels.size()) {
                    JPanel currentTaskPanel = taskPanels.get(selectedIndex);
                    JPanel currentHistoryPanel = historyPanels.get(selectedIndex);

                    try {
                        // PROMPT FOR A NEW TASK
                        String task = JOptionPane.showInputDialog(null, "Enter a new task:", "Add Task", JOptionPane.PLAIN_MESSAGE);

                        if (task == null || task.trim().isEmpty()) {
                            throw new Exception("Invalid task entry!");
                        }

                        // CHECKBOX FOR TASK
                        JCheckBox taskCheckBox = new JCheckBox(task);
                        taskCheckBox.setBackground(panelColor);
                        taskCheckBox.setForeground(foregroundColor);

                        // ADD ACTION LISTENER TO HANDLE CHECK/UNCHECK
                        taskCheckBox.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (taskCheckBox.isSelected()) {
                                    // TASK CHECKED, STRIKE THROUGH AND MOVE TO HISTORY
                                    taskCheckBox.setText("<html><strike>" + task + "</strike></html>");
                                    
                                    if (currentHistoryPanel.getComponentCount() >= HISTORY_LIMIT) {
                                        currentHistoryPanel.remove(currentHistoryPanel.getComponentCount() - 1); // REMOVE OLDEST ITEM
                                    }
                                    currentHistoryPanel.add(taskCheckBox, 0); // ADD TO TOP OF HISTORY

                                    // REMOVE FROM TASK PANEL
                                    currentTaskPanel.remove(taskCheckBox);
                                } else {
                                    // TASK UNCHECKED, REMOVE STRIKE THROUGH AND MOVE BACK TO TASKS
                                    taskCheckBox.setText(task);
                                    currentHistoryPanel.remove(taskCheckBox);
                                    currentTaskPanel.add(taskCheckBox);
                                }

                                // REPAINT PANELS AFTER UPDATE
                                currentTaskPanel.revalidate();
                                currentTaskPanel.repaint();
                                currentHistoryPanel.revalidate();
                                currentHistoryPanel.repaint();
                            }
                        });

                        // ADD NEW TASK TO TASK PANEL
                        currentTaskPanel.add(taskCheckBox);
                        currentTaskPanel.revalidate();
                        currentTaskPanel.repaint();
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

        // FRAME SIZE AND DISPLAY
        frame.setSize(400, 600);
        frame.setVisible(true);

        // INITIALIZE WITH A DEFAULT LIST
        createNewListButton.doClick();
    }
}

// haha tite wala na naman akong tulog
// all goods