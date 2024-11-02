import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class PaymentScreen extends JFrame {
    private String selectedMovie;
    private String selectedShowtime;
    private double totalCost;
    private JTextField[] textFields; // Declaration of textFields as an instance variable

    public PaymentScreen(String selectedMovie, String selectedShowtime, int numReservedSeats) {
        this.selectedMovie = selectedMovie;
        this.selectedShowtime = selectedShowtime;
        this.totalCost = numReservedSeats * 10.0; // $10 per seat

        setTitle("Payment Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        String[] labels = {"Card Number:", "CVC:", "Expiry Date:"};
        textFields = new JTextField[labels.length]; // Initialize textFields

        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));
            textFields[i] = new JTextField();
            panel.add(textFields[i]);
        }

        JButton payButton = new JButton("Pay");
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validatePayment()) {
                    processPayment();
                } else {
                    JOptionPane.showMessageDialog(
                            PaymentScreen.this,
                            "Invalid payment information. Please check your details and try again.",
                            "Invalid Payment",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        panel.add(new JLabel()); // Empty label for spacing
        panel.add(new JLabel()); // Empty label for spacing

        JLabel totalCostLabel = new JLabel("Total Cost: $" + String.format("%.2f", totalCost));
        totalCostLabel.setHorizontalAlignment(SwingConstants.RIGHT); // Align to the right
        panel.add(totalCostLabel); // Display the total cost

        panel.add(payButton);
        panel.add(cancelButton);

        add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    private boolean validatePayment() {
        for (JTextField textField : textFields) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }

        try {
            Long.parseLong(textFields[0].getText()); // Check if the card number is a valid number

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void processPayment() {
        JOptionPane.showMessageDialog(
                PaymentScreen.this,
                "Payment successful! Enjoy your movie.",
                "Payment Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	PaymentScreen paymentScreen = new PaymentScreen("Sample Movie", "Sample Showtime", 4);
            paymentScreen.setVisible(true);
        });
    }
}
