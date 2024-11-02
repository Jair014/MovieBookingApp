import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class BookingSeats extends JFrame {
    private String selectedMovie;
    private String selectedShowtime;
    private static final int ROWS = 5;
    private static final int COLUMNS = 10;
    private static char[][] seats = new char[ROWS][COLUMNS];

    public BookingSeats(String selectedMovie, String selectedShowtime) {
        this.selectedMovie = selectedMovie;
        this.selectedShowtime = selectedShowtime;
        initializeSeats();
        setTitle("Movie Ticket Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();
        JLabel screenLabel = new JLabel("Screen");
        topPanel.add(screenLabel);

        JPanel gridPanel = new JPanel(new GridLayout(ROWS, COLUMNS, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                JButton seatButton = new JButton(row + "-" + col);
                seatButton.setPreferredSize(new Dimension(80, 80)); // Set the preferred size
                seatButton.addActionListener(new SeatButtonListener(row, col, seatButton));
                gridPanel.add(seatButton);
            }
        }

        JButton reserveButton = new JButton("Reserve Seats");
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPaymentScreen();
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(reserveButton, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeSeats() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                seats[row][col] = 'A'; // 'A' represents an available seat
            }
        }
    }

    private class SeatButtonListener implements ActionListener {
        private int row;
        private int col;
        private JButton seatButton;

        public SeatButtonListener(int row, int col, JButton seatButton) {
            this.row = row;
            this.col = col;
            this.seatButton = seatButton;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            reserveSeat(row, col, seatButton);
        }
    }

    private void reserveSeat(int row, int col, JButton seatButton) {
        if (seats[row][col] == 'A') {
            seats[row][col] = 'R'; // 'R' represents a reserved seat
            seatButton.setBackground(Color.RED);
            showAlert("Seat Reserved", "Seat " + row + "-" + col + " has been reserved.");
        } else {
            showAlert("Seat Unavailable", "Seat " + row + "-" + col + " is already reserved.");
        }
    }
    private int calculateNumReservedSeats() {
        int count = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (seats[row][col] == 'R') {
                    count++;
                }
            }
        }
        return count;
    }

    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void openPaymentScreen() {
        SwingUtilities.invokeLater(() -> {
            // Calculate the number of reserved seats
            int numReservedSeats = calculateNumReservedSeats();

            PaymentScreen paymentScreen = new PaymentScreen(selectedMovie, selectedShowtime, numReservedSeats);
            paymentScreen.setVisible(true);
            dispose(); // Close the booking seats screen
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BookingSeats bookingSeats = new BookingSeats("Sample Movie", "Sample Showtime");
            bookingSeats.setVisible(true);
        });
    }
}

