import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class ScriptRunner {

  private static final int DEFAULT_WIDTH = 1000;
  private static final int DEFAULT_HEIGHT = 1000;

  boolean running = false;


  private void display() {
    JFrame frame = new JFrame("Script Executor");
    frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

    boolean lastRunError = false;

    Container container = frame.getContentPane();

    CreateInputPanel(container);

    JTextArea scriptOutputField = createOutputPanel(container);

    JPanel scriptExecutionInfo = new JPanel();
    JTextArea executingStatus =
        new JTextArea("Is the script running?\n " + (running ? "Yes" : "No"));
    executingStatus.setEditable(false);
    JTextArea lastRunExitStatus =
        new JTextArea(
            "Did the last execution return a non-zero exit code?\n " + (lastRunError ? "Yes" : "No"));
    lastRunExitStatus.setEditable(false);
    JButton executor = new JButton("Execute Script");
    executor.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //TODO: Make this action write to a file and run the script, and get the outputs
        //This will be the model part of this system
        scriptOutputField.setText("YAYY THIS WORKS BABYYY");
        running = true;
        //TODO: Update the executing boolean to be true when the button is clicked.
      }
    });
    scriptExecutionInfo.add(executingStatus);
    scriptExecutionInfo.add(lastRunExitStatus);
    scriptExecutionInfo.add(executor);

    container.add(scriptExecutionInfo, BorderLayout.CENTER);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  private JTextArea createOutputPanel(Container container) {
    JPanel outputPanel = new JPanel();
    outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
    JTextField outputText = new JTextField("Output: ");
    outputText.setEditable(false);
    JTextArea scriptOutputField = new JTextArea("");
    scriptOutputField.setPreferredSize(new Dimension(300, 500));
    scriptOutputField.setEditable(false);
    outputPanel.add(outputText);
    outputPanel.add(scriptOutputField);
    container.add(outputPanel, BorderLayout.LINE_END);
    return scriptOutputField;
  }

  private void CreateInputPanel(Container container) {
    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
    JTextField inputText = new JTextField("Input: ");
    inputText.setEditable(false);
    inputPanel.add(inputText);
    JTextArea scriptField = new JTextArea("");
    scriptField.setPreferredSize(new Dimension(400, 500)); // watch the size mates
    inputPanel.add(scriptField);
    container.add(inputPanel, BorderLayout.LINE_START);
  }

  public static void main(String[] args) {
    new ScriptRunner().display();
  }
}
