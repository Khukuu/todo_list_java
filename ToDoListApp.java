import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ToDoListApp {
    private JFrame frame;
    private JPanel taskPanel;
    private JPanel historyPanel;
    private JPanel newTaskPanel;
    private JLabel listNameLabel;  // Label to display the name of the current list
    private ArrayList<JCheckBox> taskCheckboxes;
    private ArrayList<String> tasks;
    private ArrayList<String> completedTasks;
    private ArrayList<String> newTasks;  // New tasks for the new To-Do list
    private ArrayList<String> newCompletedTasks; // Completed tasks for the new list
    private String currentListName;  // To store the name of the current list

    public ToDoListApp() {
        tasks = new ArrayList<>();
        completedTasks = new ArrayList<>();
        taskCheckboxes = new ArrayList<>();
        newTasks = new ArrayList<>();
        newCompletedTasks = new ArrayList<>();

        // Initially set the current list name to a default name
        currentListName = "Default List";

        frame = new JFrame("To-Do List");
        taskPanel = new JPanel();
        historyPanel = new JPanel();
        newTaskPanel = new JPanel();

        // List Name Label
        listNameLabel = new JLabel("Current List: " + currentListName);
        listNameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Task Panel Setup
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        JScrollPane taskScrollPane = new JScrollPane(taskPanel);
        taskScrollPane.setPreferredSize(new Dimension(400, 150));

        // History Panel Setup
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        JScrollPane historyScrollPane = new JScrollPane(historyPanel);
        historyScrollPane.setPreferredSize(new Dimension(400, 100));

        // New Task Panel Setup (for the new To-Do list)
        newTaskPanel.setLayout(new BoxLayout(newTaskPanel, BoxLayout.Y_AXIS));
        JScrollPane newTaskScrollPane = new JScrollPane(newTaskPanel);
        newTaskScrollPane.setPreferredSize(new Dimension(400, 150));

        // Create the "Add Task" button
        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String task = JOptionPane.showInputDialog(frame, "Enter a new task:");
                if (task != null && !task.trim().isEmpty()) {
                    addTask(task);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid task. Please try again.");
                }
            }
        });

        // Create the "Create New List" button
        JButton createNewListButton = new JButton("Create New List");
        createNewListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewToDoList();
            }
        });

        // Layout the components
        JPanel topPanel = new JPanel();  // To hold both buttons
        topPanel.setLayout(new FlowLayout());
        topPanel.add(listNameLabel);
        topPanel.add(addButton);
        topPanel.add(createNewListButton);

        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(taskScrollPane, BorderLayout.CENTER);
        frame.add(historyScrollPane, BorderLayout.SOUTH);
        frame.add(newTaskScrollPane, BorderLayout.EAST);  // Add new task list panel to the right

        // Frame settings
        frame.setSize(650, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void addTask(String task) {
        // Add tasks to the current list (main list or new list)
        tasks.add(task);
        JCheckBox taskCheckbox = new JCheckBox(task);
        taskCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (taskCheckbox.isSelected()) {
                    tasks.remove(task);
                    completedTasks.add(task);
                    taskCheckboxes.remove(taskCheckbox);
                    taskPanel.remove(taskCheckbox);  // Remove from task panel
                    updateTaskAndHistoryPanels();
                }
            }
        });
        taskCheckboxes.add(taskCheckbox);
        taskPanel.add(taskCheckbox);
        updateTaskAndHistoryPanels();
    }

    private void createNewToDoList() {
        // Prompt user for the name of the new list
        String newListName = JOptionPane.showInputDialog(frame, "Enter a name for the new To-Do list:");
        if (newListName != null && !newListName.trim().isEmpty()) {
            // Set the current list name to the new list name
            currentListName = newListName.trim();
            listNameLabel.setText("Current List: " + currentListName);

            // Clear the new list's tasks and completed tasks
            newTasks.clear();
            newCompletedTasks.clear();

            // Update the new task panel to reflect the new, empty list
            newTaskPanel.removeAll();
            newTaskPanel.revalidate();
            newTaskPanel.repaint();

            JOptionPane.showMessageDialog(frame, "New To-Do List Created: " + currentListName);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid list name. Please try again.");
        }
    }

    private void updateTaskAndHistoryPanels() {
        // Clear the panels before re-adding the updated content
        taskPanel.removeAll();
        historyPanel.removeAll();
        newTaskPanel.removeAll();

        // Re-add active tasks (unchecked checkboxes) for both the main list and the new list
        for (JCheckBox taskCheckbox : taskCheckboxes) {
            taskPanel.add(taskCheckbox);
        }

        // Add completed tasks to history panel (strikethrough, most recent on top)
        for (String completedTask : getRecentCompletedTasks()) {
            JLabel completedTaskLabel = new JLabel("<html><s>" + completedTask + "</s></html>");
            historyPanel.add(completedTaskLabel, 0);  // Add at the top (index 0)
        }

        // Re-add tasks for the new To-Do list
        for (String newTask : newTasks) {
            JCheckBox newTaskCheckbox = new JCheckBox(newTask);
            newTaskPanel.add(newTaskCheckbox);
        }

        taskPanel.revalidate();
        taskPanel.repaint();
        historyPanel.revalidate();
        historyPanel.repaint();
        newTaskPanel.revalidate();
        newTaskPanel.repaint();
    }

    private ArrayList<String> getRecentCompletedTasks() {
        // Limit the completed tasks to 10 most recent
        int startIndex = Math.max(completedTasks.size() - 10, 0);
        return new ArrayList<>(completedTasks.subList(startIndex, completedTasks.size()));
    }

    public static void main(String[] args) {
        new ToDoListApp();
    }
}
