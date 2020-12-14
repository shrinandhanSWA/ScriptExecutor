import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.ExecutionException;

public class ScriptRunner {

  /* Defining the default width and height of the frame. */
  private static final int DEFAULT_WIDTH = 1000;
  private static final int DEFAULT_HEIGHT = 1000;

  /* Fields used for output, execution status, and exit status handling. */
  private JTextArea scriptOutputField;
  private JTextArea lastRunExitStatus;
  private JTextArea executingStatus;

  private void display() {

    /* Creating and setting size of frame. */
    JFrame frame = new JFrame("Script Executor");
    frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

    /* Initializing input and output panels. */
    Container container = frame.getContentPane();
    JTextArea scriptInputField = CreateInputPanel(container);
    scriptOutputField = createOutputPanel(container);

    /* Initializing the executable info panel. */
    JPanel scriptExecutionInfo = new JPanel();

    /* Creating the executing status field and initializing it. */
    executingStatus = new JTextArea();
    setExecutingStatus(false, executingStatus);
    executingStatus.setEditable(false);

    /* Creating the last run status field and initializing it. */
    lastRunExitStatus = new JTextArea();
    setLastExitStatus(true, lastRunExitStatus);
    lastRunExitStatus.setEditable(false);

    /* Creating button that starts the execution of the script when clicked. */
    JButton executor = new JButton("Execute Script");
    executor.addActionListener(
        e -> {
          /* Set script executing bool to be true and update the text area. */
          execute();
        });

    /* Add components to panel. */
    scriptExecutionInfo.add(executingStatus);
    scriptExecutionInfo.add(lastRunExitStatus);
    scriptExecutionInfo.add(executor);

    /* Add the execution info panel to the container. */
    container.add(scriptExecutionInfo, BorderLayout.CENTER);

    /* Final steps of initializing the frame. */
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  /* Handling the execution of the script. */
  private void execute() {
    SwingWorker<Boolean, Integer> worker =
        new SwingWorker<>() {
          @Override
          protected Boolean doInBackground() throws Exception {

            setExecutingStatus(true, executingStatus);
            scriptOutputField.setText("");
            boolean success = true;

            for (int i = 0; i < 10; i++) {
              Thread.sleep(100);

              publish(i);
            }

            return success;
          }

          @Override
          protected void process(java.util.List<Integer> chunks) {
            Integer last = chunks.get(chunks.size() - 1);
            scriptOutputField.append("Last: " + last + "\n");
          }

          @Override
          protected void done() {
            try {
              Boolean success = get();
              setLastExitStatus(success, lastRunExitStatus);
              setExecutingStatus(false, executingStatus);
            } catch (InterruptedException | ExecutionException e) {
              e.printStackTrace();
            }
            super.done();
          }
        };

    worker.execute();
  }

  /* Function that sets the last exit status based on given lastRunError boolean. */
  private void setLastExitStatus(boolean lastRunError, JTextArea lastRunExitStatus) {
    lastRunExitStatus.setText(
        "Did the last execution exit normally?\n "
                + (lastRunError ? "Yes" : "No"));
  }

  /* Function that handles executing the script. */
  private int executeScript(String text, JTextArea outputPanel) {
    outputPanel.append(text); // will be changed later.

    try {
      /* Create the file if it doesn't exits. */
      File myObj = new File("foo.kts");
      boolean created = myObj.createNewFile();

      /* Write script to the .kts file. */
      FileWriter myWriter = new FileWriter("foo.kts");
      myWriter.write(text);
      myWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return 0; // will be changed later.
  }

  /* Function that sets the executing status based on given executing bool. */
  private void setExecutingStatus(boolean executing, JTextArea textArea) {
    textArea.setText(
            "Is the script executing?\n " + (executing ? "Yes" : "No"));
  }

  /* Function that creates the output panel. */
  private JTextArea createOutputPanel(Container container) {

    /* Creating and setting panel. */
    JPanel outputPanel = new JPanel();
    outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));

    /* Create output text field. */
    JTextField outputText = new JTextField("Output: ");
    outputText.setEditable(false);

    /* Create script output text field. */
    JTextArea scriptOutputArea = new JTextArea("");
    scriptOutputArea.setPreferredSize(new Dimension(300, 500));
    scriptOutputArea.setEditable(false);

    /* Add components to panel, and add panel to the container. */
    outputPanel.add(outputText);
    outputPanel.add(scriptOutputArea);
    container.add(outputPanel, BorderLayout.LINE_END);

    /* Return the output field text area to be used later. */
    return scriptOutputArea;
  }

  /* Function that creates the input panel.  */
  private JTextArea CreateInputPanel(Container container) {

    /* Creating and setting panel. */
    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

    /* Create input text field. */
    JTextField inputText = new JTextField("Input: ");
    inputText.setEditable(false);

    /* Create script output text area. */
    JTextArea scriptArea = new JTextArea("");
    scriptArea.setPreferredSize(new Dimension(400, 500));

    /* Add components to panel, and add panel to the container. */
    inputPanel.add(inputText);
    inputPanel.add(scriptArea);
    container.add(inputPanel, BorderLayout.LINE_START);

    /* Return the input field text area to be used later. */
    return scriptArea;
  }

  public static void main(String[] args) {
    /* Actually making the frame. */
    new ScriptRunner().display();
  }
}
