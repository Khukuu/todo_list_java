import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Todoit {
    // The main task and history lists for a single pane
    private static JPanel taskPanel = new JPanel();
    private static JScrollPane scrollPane;
    private static JPanel historyPanel = new JPanel();
    private static JScrollPane hScrollPane;
    
    // The tabbed pane that will hold the list of all task sections
    private static JTabbedPane tabbedPane = new JTabbedPane();

    public static void main(String[] args) {
        // Create a panel with a button for demonstration
        JFrame frame = new JFrame("Todoit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        // Create the panel for the buttons
        JPanel panel = new JPanel();
        JButton addTaskButton = new JButton("Add Task");
        JButton createNewListButton = new JButton("Create New List");

        // Create the "Tasks" panel with a vertical layout
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(taskPanel);
        scrollPane.setPreferredSize(new Dimension(300, 500));

        // Create the "History" panel with a vertical layout
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        hScrollPane = new JScrollPane(historyPanel);
        hScrollPane.setPreferredSize(new Dimension(300, 500));

        // Add initial "Tasks" tab and "History" tab
        tabbedPane.addTab("Tasks", scrollPane);
        tabbedPane.addTab("History", hScrollPane);

        // Action listener for adding a new task
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String task = JOptionPane.showInputDialog(null, "Enter a new task: ", "Add Task", JOptionPane.PLAIN_MESSAGE);

                    if (task == null || task.trim().isEmpty()) {
                        throw new Exception("Invalid task entry!");
                    }

                    // Create a checkbox for the new task and add it to the task panel
                    JCheckBox taskCheckBox = new JCheckBox(task);
                    taskCheckBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (taskCheckBox.isSelected()) {
                                // Move the task to the history panel
                                JCheckBox completedCheckBox = new JCheckBox(task);
                                completedCheckBox.setEnabled(false); // Disable it in history
                                completedCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
                                completedCheckBox.setText("<html><strike>" + task + "</strike></html>");
                                historyPanel.add(completedCheckBox);
                                
                                // Remove from task panel
                                taskPanel.remove(taskCheckBox);

                                taskPanel.revalidate();
                                taskPanel.repaint();
                                historyPanel.revalidate();
                                historyPanel.repaint();
                            }
                        }
                    });
                    taskPanel.add(taskCheckBox);

                    taskPanel.revalidate();
                    taskPanel.repaint();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action listener for creating a new list
        createNewListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String listName = JOptionPane.showInputDialog(null, "Enter the name of the new list:", "New List", JOptionPane.PLAIN_MESSAGE);
                if (listName != null && !listName.trim().isEmpty()) {
                    // Create a new task panel for the new list
                    JPanel newTaskPanel = new JPanel();
                    newTaskPanel.setLayout(new BoxLayout(newTaskPanel, BoxLayout.Y_AXIS));
                    JScrollPane newTaskScrollPane = new JScrollPane(newTaskPanel);
                    newTaskScrollPane.setPreferredSize(new Dimension(300, 500));

                    // Create a new history panel for the new list
                    JPanel newHistoryPanel = new JPanel();
                    newHistoryPanel.setLayout(new BoxLayout(newHistoryPanel, BoxLayout.Y_AXIS));
                    JScrollPane newHistoryScrollPane = new JScrollPane(newHistoryPanel);
                    newHistoryScrollPane.setPreferredSize(new Dimension(300, 500));

                    // Add the new list to the tabbed pane
                    tabbedPane.addTab(listName + " Tasks", newTaskScrollPane);
                    tabbedPane.addTab(listName + " History", newHistoryScrollPane);
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });

        // Add buttons to the panel
        panel.add(addTaskButton);
        panel.add(createNewListButton);

        // Add the tabbed pane to the frame
        frame.add(panel, BorderLayout.NORTH); // Buttons at the top
        frame.add(tabbedPane, BorderLayout.CENTER); // Tabbed pane in the center

        // Set the frame size and visibility
        frame.setSize(400, 600);
        frame.setVisible(true);
    }
}


/*nya ichi ni san niya agirato*/
/*Ang sarap ko gar, glob glob glob */
