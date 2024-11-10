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

    public static void main(String[] args) {
        // FRAME
        JFrame frame = new JFrame("Todoit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // BUTTON
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton addTaskButton = new JButton("Add Task");
        JButton createNewListButton = new JButton("Create New List");

        // ACTION LISTENER
        createNewListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // THIS PROMPT USER TO ENTER A NEW LIST
                String listName = JOptionPane.showInputDialog(null, "Enter the name of the new list:", "New List", JOptionPane.PLAIN_MESSAGE);
                if (listName != null && !listName.trim().isEmpty()) {
                    // NEW TASK PANEL FOR NEW LIST
                    JPanel newTaskPanel = new JPanel();
                    newTaskPanel.setLayout(new BoxLayout(newTaskPanel, BoxLayout.Y_AXIS));
                    JScrollPane newTaskScrollPane = new JScrollPane(newTaskPanel);
                    newTaskScrollPane.setPreferredSize(new Dimension(300, 500));

                    // HISTORY PANEL
                    JPanel newHistoryPanel = new JPanel();
                    newHistoryPanel.setLayout(new BoxLayout(newHistoryPanel, BoxLayout.Y_AXIS));
                    newHistoryPanel.setPreferredSize(new Dimension(300, 100));
                    newHistoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);  // Left align na hindi pa gumagana pls fix

                    // THIS IS WHERE TASK AND HISTORY GO
                    JPanel fullPanel = new JPanel();
                    fullPanel.setLayout(new BoxLayout(fullPanel, BoxLayout.Y_AXIS));
                    fullPanel.add(newTaskScrollPane);
                    fullPanel.add(newHistoryPanel);

                    // NEW LIST TO TABBED PANE
                    tabbedPane.addTab(listName, fullPanel);

                    // STORE TASKS AND HISTORY 
                    taskPanels.add(newTaskPanel);
                    historyPanels.add(newHistoryPanel);

                    // NEW CREATED TAB
                    tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1); 

                    frame.revalidate();
                    frame.repaint();
                }
            }
        });

        // ACTION LISTENER FOR ADDING
        // WAG GAGALAWIN ITONG NASA BABA
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

                        // CHECKBOX FOR WHEN THE TASK IS DONE AND MAKE IT GO TO THE HISTORY PANEL BELOW
                        JCheckBox taskCheckBox = new JCheckBox(task);
                        taskCheckBox.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (taskCheckBox.isSelected()) {
                                    // MOVE TASK DONE BELOWw
                                    JLabel completedLabel = new JLabel("<html><strike>" + task + "</strike></html>");
                                    
                                    // MAX 10 ITEM FOR HISTORY PANEL PERO NAGSTORE LANG 7
                                    if (currentHistoryPanel.getComponentCount() >= HISTORY_LIMIT) {
                                        currentHistoryPanel.remove(currentHistoryPanel.getComponentCount() - 1); // REMOVE OLDEST POP
                                    }

                                    // ADD CHECKED TASK TO HISTORY
                                    currentHistoryPanel.add(completedLabel, 0); //ADDING TO TOP

                                    // DELETION FROM TASK PANEL
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

        // BUTTONS
        buttonPanel.add(addTaskButton);
        buttonPanel.add(createNewListButton);

        // TABS
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // GUI SIZE
        frame.setSize(400, 600);
        frame.setVisible(true);

        // YES
        createNewListButton.doClick();
    }
}

/*nya ichi ni san niya agirato*/
/*Ang sarap ko gar, glob glob glob */
//pre pagawa ng delete function para sa mga lists tyyyyy
