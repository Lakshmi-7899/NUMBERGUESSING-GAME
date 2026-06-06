import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class NumberGuessingGame extends JFrame {

    // Game Logic Variables
    private int targetNumber;
    private int attempts;
    private final Random random = new Random();

    // UI Colors (Modern Dark Material Palette)
    private final Color COLOR_BG = new Color(24, 28, 36);         // Deep dark blue/gray
    private final Color COLOR_CARD = new Color(36, 42, 56);       // Lighter container card
    private final Color COLOR_ACCENT = new Color(99, 102, 241);   // Indigo blue
    private final Color COLOR_TEXT_MAIN = new Color(255, 255, 255);
    private final Color COLOR_TEXT_MUTED = new Color(156, 163, 175);
    private final Color COLOR_SUCCESS = new Color(34, 197, 94);   // Vibrant green
    private final Color COLOR_ERROR = new Color(239, 68, 68);     // Vibrant red

    // UI Components
    private JTextField guessInput;
    private JButton guessButton;
    private JButton resetButton;
    private JLabel feedbackLabel;
    private JLabel attemptsLabel;

    public NumberGuessingGame() {
        initGame();
        initUI();
    }

    private void initGame() {
        targetNumber = random.nextInt(100) + 1; // 1 to 100
        attempts = 0;
    }

    private void initUI() {
        // Main window configuration
        setTitle("Number Guessing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 500);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);

        // Main background panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // 1. Title Label
        JLabel titleLabel = new JLabel("Guess The Number", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(COLOR_TEXT_MAIN);
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        // 2. Subtitle Label
        JLabel subtitleLabel = new JLabel("I'm thinking of a number between 1 and 100.", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(COLOR_TEXT_MUTED);
        gbc.gridy = 1;
        mainPanel.add(subtitleLabel, gbc);

        // 3. The Central Card Panel
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(COLOR_CARD);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 65, 81), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        // Input Field
        guessInput = new JTextField();
        guessInput.setMaximumSize(new Dimension(300, 50));
        guessInput.setFont(new Font("Segoe UI", Font.BOLD, 24));
        guessInput.setHorizontalAlignment(JTextField.CENTER);
        guessInput.setBackground(COLOR_BG);
        guessInput.setForeground(COLOR_TEXT_MAIN);
        guessInput.setCaretColor(COLOR_TEXT_MAIN);
        guessInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_ACCENT, 2),
                new EmptyBorder(5, 10, 5, 10)
        ));
        
        // Allow pressing 'Enter' to submit guess
        guessInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleGuess();
                }
            }
        });

        // Feedback Label
        feedbackLabel = new JLabel("Take your first shot!", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        feedbackLabel.setForeground(COLOR_ACCENT);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Attempts Tracker Label
        attemptsLabel = new JLabel("Attempts: 0", SwingConstants.CENTER);
        attemptsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        attemptsLabel.setForeground(COLOR_TEXT_MUTED);
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Guess Button
        guessButton = createModernButton("Submit Guess", COLOR_ACCENT);
        guessButton.addActionListener(e -> handleGuess());

        // Assembly inside the card
        guessInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        guessButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cardPanel.add(guessInput);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(guessButton);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(feedbackLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(attemptsLabel);

        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(cardPanel, gbc);

        // 4. Reset Button (Bottom Footer)
        resetButton = createModernButton("Play Again", new Color(75, 85, 99));
        resetButton.setVisible(false); // Only show when game ends
        resetButton.addActionListener(e -> resetGame());
        
        gbc.gridy = 3;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(resetButton, gbc);
    }

    // Helper method to generate unified flat buttons
    private JButton createModernButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bg);
        button.setForeground(COLOR_TEXT_MAIN);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(300, 45));
        button.setPreferredSize(new Dimension(300, 45));
        
        // Clean hover effects via UI state mapping
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bg);
            }
        });
        return button;
    }

    private void handleGuess() {
        String inputStr = guessInput.getText().trim();
        
        if (inputStr.isEmpty()) {
            feedbackLabel.setText("Please enter a number!");
            feedbackLabel.setForeground(COLOR_ERROR);
            return;
        }

        try {
            int userGuess = Integer.parseInt(inputStr);
            
            if (userGuess < 1 || userGuess > 100) {
                feedbackLabel.setText("Out of bounds! Guess 1 - 100.");
                feedbackLabel.setForeground(COLOR_ERROR);
                return;
            }

            attempts++;
            attemptsLabel.setText("Attempts: " + attempts);

            if (userGuess == targetNumber) {
                feedbackLabel.setText("🎉 Correct! You got it!");
                feedbackLabel.setForeground(COLOR_SUCCESS);
                guessInput.setEnabled(false);
                guessButton.setEnabled(false);
                resetButton.setVisible(true);
            } else if (userGuess < targetNumber) {
                feedbackLabel.setText("📉 Too Low! Try a higher number.");
                feedbackLabel.setForeground(COLOR_ERROR);
                guessInput.requestFocus();
            } else {
                feedbackLabel.setText("📈 Too High! Try a lower number.");
                feedbackLabel.setForeground(COLOR_ERROR);
                guessInput.requestFocus();
            }

            guessInput.setText(""); // clear field for next attempt

        } catch (NumberFormatException ex) {
            feedbackLabel.setText("Invalid input! Numbers only.");
            feedbackLabel.setForeground(COLOR_ERROR);
        }
    }

    private void resetGame() {
        initGame();
        feedbackLabel.setText("Take your first shot!");
        feedbackLabel.setForeground(COLOR_ACCENT);
        attemptsLabel.setText("Attempts: 0");
        guessInput.setText("");
        guessInput.setEnabled(true);
        guessButton.setEnabled(true);
        resetButton.setVisible(false);
        guessInput.requestFocus();
    }

    public static void main(String[] args) {
        // Run UI construction on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            new NumberGuessingGame().setVisible(true);
        });
    }
}