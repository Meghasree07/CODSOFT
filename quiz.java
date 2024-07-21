import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class QuizApplication extends JFrame {

    private static final List<Question> questions = Arrays.asList(
        new Question("What is the default value of an int in Java?", new String[]{"0", "1", "null", "undefined"}, 0),
        new Question("Which keyword is used to create an object in Java?", new String[]{"new", "create", "object", "make"}, 0),
        new Question("What is the correct syntax to declare a variable in Java?", new String[]{"int x = 10;", "int x : 10;", "x = int 10;", "int : x = 10;"}, 0),
        new Question("What does JVM stand for?", new String[]{"Java Virtual Machine", "Java Variable Machine", "Java Verified Machine", "Java Virtual Method"}, 0),
        new Question("Which method is used to start a thread in Java?", new String[]{"start()", "run()", "begin()", "execute()"}, 0),
        new Question("Which package contains the Java collections framework?", new String[]{"java.util", "java.lang", "java.io", "java.net"}, 0),
        new Question("What is the base class for all exceptions in Java?", new String[]{"Exception", "Throwable", "Error", "RuntimeException"}, 1),
        new Question("What keyword is used to inherit a class in Java?", new String[]{"extends", "inherits", "implements", "super"}, 0),
        new Question("What is the size of an int in Java?", new String[]{"16 bits", "32 bits", "64 bits", "8 bits"}, 1),
        new Question("Which method is used to get the length of an array in Java?", new String[]{"length()", "getLength()", "size()", "length"}, 3),
        new Question("What is the default value of a boolean in Java?", new String[]{"true", "false", "null", "0"}, 1),
        new Question("Which class is the root of the Java class hierarchy?", new String[]{"Object", "Class", "Base", "Root"}, 0),
        new Question("What does the 'static' keyword do in Java?", new String[]{"Defines a class", "Creates a new instance", "Belongs to the class, not instance", "Makes a method private"}, 2),
        new Question("Which operator is used for logical AND in Java?", new String[]{"&&", "&", "||", "!"}, 0),
        new Question("How do you create a single-line comment in Java?", new String[]{"// comment", "# comment", "/* comment */", "<!-- comment -->"}, 0),
        new Question("Which method is used to convert a string to an integer in Java?", new String[]{"parseInt()", "toInt()", "convertInt()", "integerValue()"}, 0),
        new Question("Which keyword is used to prevent a method from being overridden in Java?", new String[]{"final", "static", "private", "abstract"}, 0),
        new Question("What is the size of a byte in Java?", new String[]{"8 bits", "16 bits", "32 bits", "64 bits"}, 0),
        new Question("Which of the following is a valid variable name in Java?", new String[]{"myVariable", "2variable", "_myVariable", "my-Variable"}, 2),
        new Question("Which collection class implements a resizable array in Java?", new String[]{"ArrayList", "LinkedList", "HashSet", "TreeSet"}, 0)
    );

    private int score = 0;
    private int currentQuestionIndex = 0;
    private final JLabel questionLabel = new JLabel();
    private final JButton[] optionButtons = new JButton[4];
    private final JLabel scoreLabel = new JLabel("Score: 0");
    private final JLabel timerLabel = new JLabel("Time: 30");
    private static final int TIME_LIMIT = 30; 

    private ExecutorService timerExecutor;
    private Future<?> timerFuture;
    private int remainingTime = TIME_LIMIT;

    public QuizApplication() {
        setTitle("Quiz Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new GridLayout(6, 1));
        add(questionPanel, BorderLayout.CENTER);

        questionLabel.setText(questions.get(currentQuestionIndex).getQuestion());
        questionPanel.add(questionLabel);

        Border buttonBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
        for (int i = 0; i < optionButtons.length; i++) {
            final int index = i;
            optionButtons[i] = new JButton();
            optionButtons[i].setBorder(buttonBorder); 
            optionButtons[i].setFocusPainted(false);
            optionButtons[i].setBackground(Color.WHITE);
            optionButtons[i].setForeground(Color.BLACK);
            optionButtons[i].setOpaque(true);
            optionButtons[i].addActionListener(e -> checkAnswer(index));
            questionPanel.add(optionButtons[i]);
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 2));
        add(bottomPanel, BorderLayout.SOUTH);

        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        bottomPanel.add(scoreLabel);

        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        bottomPanel.add(timerLabel);

        showQuestion();
        startTimer();
    }

    private void showQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            showResults();
            return;
        }

        Question question = questions.get(currentQuestionIndex);
        questionLabel.setText(question.getQuestion());
        String[] options = question.getOptions();
        for (int i = 0; i < options.length; i++) {
            optionButtons[i].setText(options[i]);
        }
    }

    private void checkAnswer(int selectedIndex) {
        Question question = questions.get(currentQuestionIndex);
        if (selectedIndex == question.getCorrectAnswer()) {
            score++;
        }
        currentQuestionIndex++;
        stopTimer();
        showQuestion();
        scoreLabel.setText("Score: " + score);
        startTimer();
    }

    private void startTimer() {
        remainingTime = TIME_LIMIT;
        timerLabel.setText("Time: " + remainingTime);

        timerExecutor = Executors.newSingleThreadExecutor();
        timerFuture = timerExecutor.submit(() -> {
            while (remainingTime > 0) {
                try {
                    Thread.sleep(1000);
                    remainingTime--;
                    SwingUtilities.invokeLater(() -> timerLabel.setText("Time: " + remainingTime));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Time's up!");
                currentQuestionIndex++;
                stopTimer();
                showQuestion();
            });
        });
    }

    private void stopTimer() {
        if (timerFuture != null && !timerFuture.isDone()) {
            timerFuture.cancel(true);
        }
        if (timerExecutor != null) {
            timerExecutor.shutdownNow();
        }
    }

    private void showResults() {
        JOptionPane.showMessageDialog(this, "Quiz completed!\nYour score: " + score + "/" + questions.size(), "Results", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuizApplication app = new QuizApplication();
            app.setVisible(true);
        });
    }
}

class Question {
    private final String question;
    private final String[] options;
    private final int correctAnswer;

    public Question(String question, String[] options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}
