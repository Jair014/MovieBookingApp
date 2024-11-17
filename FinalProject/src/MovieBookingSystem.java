import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MovieBookingSystem {
    private static List<Movie> movies;
    private static JFrame mainFrame;
    private static List<Showtime> showtimes;
    private static MovieSelectionDialog movieSelectionDialog;
    private static DatabaseManager database = new DatabaseManager();
    private static String selectedMovie;
    private static int loggedInUserId;
    private static String selectedShowtime;

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            movies = DatabaseManager.getMovies();
            showtimes = database.getShowtimes();

            JFrame loginFrame = new JFrame("Login");
            loginFrame.setSize(300, 150);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setLayout(new BorderLayout());

            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new GridLayout(4, 2));

            JLabel usernameLabel = new JLabel("Username:");
            JTextField usernameField = new JTextField();
            JLabel passwordLabel = new JLabel("Password:");
            JPasswordField passwordField = new JPasswordField();
            JButton loginButton = new JButton("Login");
            JButton createAccountButton = new JButton("Create an Account");

            createAccountButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createAccount();
                }
            });

            loginPanel.add(usernameLabel);
            loginPanel.add(usernameField);
            loginPanel.add(passwordLabel);
            loginPanel.add(passwordField);
            loginPanel.add(new JLabel()); // Empty label for spacing
            loginPanel.add(new JLabel()); // Empty label for spacing
            loginPanel.add(createAccountButton);
            loginPanel.add(loginButton);

            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (authenticateUser(usernameField.getText(), new String(passwordField.getPassword()))) {
                    	loggedInUserId = 1;

                        loginFrame.setVisible(false);
                        showMainMenu();
                    } else {
                        JOptionPane.showMessageDialog(loginFrame, "Invalid username or password. Try again.");
                    }
                }
            });

            loginFrame.add(loginPanel, BorderLayout.CENTER);
            loginFrame.setVisible(true);
        });
    }

    private static void createAccount() {
        JFrame registrationFrame = new JFrame("Create an Account");
        registrationFrame.setSize(300, 200);
        registrationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registrationFrame.setLayout(new BorderLayout());

        JPanel registrationPanel = new JPanel();
        registrationPanel.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Username:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                boolean success = database.addUser(nameField.getText(), new String(passwordField.getPassword()), emailField.getText());

                if (success) {
                    JOptionPane.showMessageDialog(registrationFrame, "Account created successfully!");
                    registrationFrame.dispose(); // Close the registration frame
                } else {
                    JOptionPane.showMessageDialog(registrationFrame, "Failed to create an account. Please try again.");
                }
            }
        });

        registrationPanel.add(nameLabel);
        registrationPanel.add(nameField);
        registrationPanel.add(emailLabel);
        registrationPanel.add(emailField);
        registrationPanel.add(passwordLabel);
        registrationPanel.add(passwordField);
        registrationPanel.add(new JLabel()); // Empty label for spacing
        registrationPanel.add(registerButton);

        registrationFrame.add(registrationPanel, BorderLayout.CENTER);
        registrationFrame.setVisible(true);
    }

    private static void showMainMenu() {
        mainFrame = new JFrame("Movie Booking System");
        mainFrame.setSize(400, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JButton nowPlayingButton = new JButton("Now Playing");
        JButton bookTicketsButton = new JButton("Book Tickets");
        JButton viewBookingHistoryButton = new JButton("View Booking History");
        JButton exitButton = new JButton("Exit");

        nowPlayingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	  displayNowPlaying();
            }
        });

        bookTicketsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayBookTickets();
            }
        });

        viewBookingHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewBookingHistory(loggedInUserId);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panel.add(nowPlayingButton);
        panel.add(bookTicketsButton);
        panel.add(viewBookingHistoryButton);
        panel.add(exitButton);

        mainFrame.add(panel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }

    private static boolean authenticateUser(String username, String password) {
        boolean isAuthenticated = database.authenticateUser(username, password);
        if (!isAuthenticated) {
            JOptionPane.showMessageDialog(null, "Incorrect username or password. Please try again.");
        }

        return isAuthenticated;
    }

    private static void listMovies() {
        // Get the list of movies from the database
        movies = DatabaseManager.getMovies();
        showtimes = database.getShowtimes();

        // Create an array of movie titles to display in the dialog
        String[] movieTitles = new String[movies.size()];
        for (int i = 0; i < movies.size(); i++) {
            movieTitles[i] = movies.get(i).getTitle();
        }

        // Show the movie selection dialog
        int selection = JOptionPane.showOptionDialog(
                null,
                "Which movie would you like to see?",
                "Select a Movie",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                movieTitles, // Use the array of movie titles
                movieTitles[0]); // The default selection

        if (selection >= 0) {
            selectedMovie = movies.get(selection).getTitle();

            // Update the showtimes list for the selected movie
            showtimes = database.getShowtimes(selectedMovie);

            JOptionPane.showMessageDialog(
                    null,
                    "Title: " + selectedMovie 
                    + "\n",
                    "Movie Info",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    private static void displayNowPlaying() {
    	
        movies = DatabaseManager.getMovies();

        // Create an array of movie titles to display in the dialog
        String[] movieTitles = new String[movies.size()];
        for (int i = 0; i < movies.size(); i++) {
            movieTitles[i] = movies.get(i).getTitle();
        }

        // Show the movie selection dialog
        int selection = JOptionPane.showOptionDialog(
                null,
                "Which movie would you like to see?",
                "Select a Movie",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                movieTitles, // Use the array of movie titles
                movieTitles[0]); // The default selection

        if (selection >= 0) {
            Movie selectedMovie = movies.get(selection);
            JOptionPane.showMessageDialog(
                    null,
                    "Title: " + selectedMovie.getTitle() + "\n" +
                            "Genre: " + selectedMovie.getGenre() + "\n" +
                            "Duration: " + selectedMovie.getTime() + " minutes\n" +
                            "Summary: " + selectedMovie.getSummary(),
                    "Movie Info",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private static void displayBookTickets() {
      
    	listMovies();
    	selectMovie();
    }
    

    private static void viewBookingHistory(int UserId) {
        // Fetch booking history entries for the current user from the database
        List<String> bookingHistoryEntries = DatabaseManager.getBookingHistory(UserId);

        if (!bookingHistoryEntries.isEmpty()) {
            // Concatenate booking history entries into a single string
            StringBuilder historyMessage = new StringBuilder("Your Booking History:\n");
            for (String entry : bookingHistoryEntries) {
                historyMessage.append(entry).append("\n");
            }

            // Display booking history
            JOptionPane.showMessageDialog(mainFrame, historyMessage.toString(), "Booking History", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainFrame, "No booking history available.", "Booking History", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void selectMovie() {
        if (selectedMovie != null) {
            // Show available showtimes for the selected movie
            StringBuilder showtimeMessage = new StringBuilder("Available Showtimes:\n");
            for (Showtime showtime : showtimes) {
                showtimeMessage.append(showtime.getTime()).append(" at ").append(showtime.getTheater()).append("\n");
            }

            // Display the showtimes
            JOptionPane.showMessageDialog(mainFrame, showtimeMessage.toString());

            // Prompt the user to select a showtime
            String[] showtimeStrings = showtimes.stream().map(Showtime::toString).toArray(String[]::new);

            String selectedShowtime = (String) JOptionPane.showInputDialog(
                    mainFrame,
                    "Select a Showtime:",
                    "Choose Showtime",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    showtimeStrings,
                    showtimeStrings[0]);

            if (selectedShowtime != null) {
           
                JOptionPane.showMessageDialog(mainFrame, "You selected: " + selectedShowtime);
                SwingUtilities.invokeLater(() -> {
                    BookingSeats bookingSeats = new BookingSeats(selectedMovie, selectedShowtime);
                    bookingSeats.setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(mainFrame, "No showtime selected.");
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, "No movie selected.");
        }
    }



}


