import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ScriptRunner {

  private static final int DEFAULT_WIDTH = 1000;
  private static final int DEFAULT_HEIGHT = 1000;

  private boolean running = false;
  private boolean lastRunError = false;

  private void display() {
    JFrame frame = new JFrame("Script Executor");
    frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

    Container container = frame.getContentPane();

    JTextArea scriptInputField = CreateInputPanel(container);

    JTextArea scriptOutputField = createOutputPanel(container);

    JPanel scriptExecutionInfo = new JPanel();
    JTextArea executingStatus = new JTextArea();
    setExecutingStatus(running, executingStatus);
    executingStatus.setEditable(false);
    JTextArea lastRunExitStatus = new JTextArea();
    lastRunExitStatus.setEditable(false);
    setLastExitStatus(lastRunError, lastRunExitStatus);
    JButton executor = new JButton("Execute Script");
    executor.addActionListener(
        e -> {
          // TODO: Extract this into a model class
          scriptOutputField.setText("We should be executing the script\n");
          running = true;
          setExecutingStatus(running, executingStatus);
          int status =
              executeScript(scriptInputField.getText(), scriptOutputField, executingStatus);
          if (status == 0) {
            lastRunError = false;
          } else {
            lastRunError = true;
          }
          setLastExitStatus(lastRunError, lastRunExitStatus);
          running = false;
          setExecutingStatus(running, executingStatus);
        });
    scriptExecutionInfo.add(executingStatus);
    scriptExecutionInfo.add(lastRunExitStatus);
    scriptExecutionInfo.add(executor);

    container.add(scriptExecutionInfo, BorderLayout.CENTER);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  private void setLastExitStatus(boolean lastRunError, JTextArea lastRunExitStatus) {
    lastRunExitStatus.setText(
        "Did the last execution return a non-zero exit code?\n " + (lastRunError ? "Yes" : "No"));
  }

  private int executeScript(String text, JTextArea outputPanel, JTextArea executingStatus) {
    outputPanel.append(text);
    running = true;
    setExecutingStatus(running, executingStatus);
    try {
      File myObj = new File("foo.kts");
      boolean created = myObj.createNewFile(); //maybe take it out later
      FileWriter myWriter = new FileWriter("foo.kts");
      myWriter.write(text);
      myWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  private void setExecutingStatus(boolean running, JTextArea textArea) {
    textArea.setText("Is the script running?\n " + (running ? "Yes" : "No"));
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

  private JTextArea CreateInputPanel(Container container) {
    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
    JTextField inputText = new JTextField("Input: ");
    inputText.setEditable(false);
    inputPanel.add(inputText);
    JTextArea scriptField = new JTextArea("");
    scriptField.setPreferredSize(new Dimension(400, 500)); // watch the size mates
    inputPanel.add(scriptField);
    container.add(inputPanel, BorderLayout.LINE_START);
    return scriptField;
  }

  public static void main(String[] args) {
    new ScriptRunner().display();
  }
}
