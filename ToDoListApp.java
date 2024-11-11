import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class ToDoListApp {
    private static ArrayList<JPanel> taskPanels = new ArrayList<>();
    private static ArrayList<JPanel> historyPanels = new ArrayList<>();
    private static JTabbedPane tabbedPane = new JTabbedPane();
    private static final int HISTORY_LIMIT = 10;

    public static void main(String[] args) {
        Color backgroundColor = new Color(45, 45, 45);
        Color foregroundColor = new Color(210, 210, 210);
        Color panelColor = new Color(60, 63, 65);
        Color buttonColor = new Color(75, 75, 75);

        JFrame frame = new JFrame("ToDoIt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(backgroundColor);

        // iniba ko yung kulay ng button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(backgroundColor);
        JButton addTaskButton = new JButton("Add Task");
        JButton createNewListButton = new JButton("Create New List");
        JButton deleteListButton = new JButton("Delete List");

        // pati tong button 
        addTaskButton.setBackground(buttonColor);
        addTaskButton.setForeground(Color.WHITE);
        createNewListButton.setBackground(buttonColor);
        createNewListButton.setForeground(Color.WHITE);
        deleteListButton.setBackground(buttonColor);
        deleteListButton.setForeground(Color.WHITE);

        createNewListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String listName = JOptionPane.showInputDialog(null, "Enter the name of the new list:", "New List", JOptionPane.PLAIN_MESSAGE);
                if (listName != null && !listName.trim().isEmpty()) {
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
                    tabbedPane.setBackground(panelColor);
                    tabbedPane.setForeground(foregroundColor);

                    taskPanels.add(newTaskPanel);
                    historyPanels.add(newHistoryPanel);

                    tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });

        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex != -1 && selectedIndex < taskPanels.size()) {
                    JPanel currentTaskPanel = taskPanels.get(selectedIndex);
                    JPanel currentHistoryPanel = historyPanels.get(selectedIndex);

                    String task = JOptionPane.showInputDialog(null, "Enter a new task:", "Add Task", JOptionPane.PLAIN_MESSAGE);
                    if (task != null && !task.trim().isEmpty()) {
                        JCheckBox taskCheckBox = new JCheckBox(task);
                        taskCheckBox.setBackground(panelColor);
                        taskCheckBox.setForeground(foregroundColor);

                        // nagdagdag ng ActionListener para pwedeng mag palipat-lipat sa mga may check at walang check na tasks
                        taskCheckBox.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (taskCheckBox.isSelected()) {
                                    taskCheckBox.setText("<html><strike>" + task + "</strike></html>");
                                    currentTaskPanel.remove(taskCheckBox);

                                    if (currentHistoryPanel.getComponentCount() >= HISTORY_LIMIT) {
                                        currentHistoryPanel.remove(currentHistoryPanel.getComponentCount() - 1);
                                    }
                                    currentHistoryPanel.add(taskCheckBox, 0);
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
                    } else {
                        JOptionPane.showMessageDialog(null, "Please enter a valid task!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        deleteListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex != -1) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this list?", "Delete List", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        tabbedPane.remove(selectedIndex);
                        taskPanels.remove(selectedIndex);
                        historyPanels.remove(selectedIndex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No list selected to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(addTaskButton);
        buttonPanel.add(createNewListButton);
        buttonPanel.add(deleteListButton);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(400, 600);
        frame.setVisible(true);

        createNewListButton.doClick();
    }
}

//tite