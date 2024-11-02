import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

@SuppressWarnings("serial")
public class MovieSelectionDialog extends JFrame {
    private JList<String> movieList;
    private JList<String> showtimeList;
    private List<Movie> movies;
    private List<Showtime> showtimes;
    private String selectedMovie;
    private String selectedShowtime;
    private DatabaseManager database;

    public MovieSelectionDialog(List<Movie> movies, List<Showtime> showtimes, DatabaseManager database) {
        this.movies = movies;
        this.showtimes = showtimes;
        this.database = database;

        setTitle("Select Movie and Showtime");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeUI();

        pack();
        setLocationRelativeTo(null);
    }

    private void initializeUI() {
        DefaultListModel<String> movieListModel = new DefaultListModel<>();
        for (Movie movie : movies) {
            movieListModel.addElement(movie.getTitle());
        }

        movieList = new JList<>(movieListModel);
        showtimeList = new JList<>();

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSelection();
            }
        });

        JButton refreshButton = new JButton("Refresh Showtimes");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMovieList(movies);
                String selectedMovie = movieList.getSelectedValue();
                updateShowtimeList(selectedMovie);
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Movie:"));
        panel.add(new JScrollPane(movieList));
        panel.add(new JLabel("Select Showtime:"));
        panel.add(new JScrollPane(showtimeList));
        panel.add(selectButton);
        panel.add(refreshButton);

        add(panel);
    }

    private void handleSelection() {
        String selectedMovie = movieList.getSelectedValue();
        if (selectedMovie != null) {
            updateShowtimeList(selectedMovie);
            this.selectedMovie = selectedMovie;
        } else {
            JOptionPane.showMessageDialog(null, "Please select a movie.");
        }

        String selectedShowtime = showtimeList.getSelectedValue();
        if (selectedShowtime != null) {
            this.selectedShowtime = selectedShowtime;
        } else {
            JOptionPane.showMessageDialog(null, "Please select a showtime.");
        }
    }
    public void displayBookTickets() {
        if (getSelectedMovie() != null && getSelectedShowtime() != null) {
            BookingSeats bookingSeats = new BookingSeats(getSelectedMovie(), getSelectedShowtime());
            bookingSeats.setVisible(true);
        } else {
            // Handle where the user hasn't selected a movie or showtime
            JOptionPane.showMessageDialog(
                    this,
                    "Please select both a movie and a showtime before booking tickets.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    public String getSelectedMovie() {
        return selectedMovie;
    }

    public String getSelectedShowtime() {
        if (!showtimeList.isSelectionEmpty()) {
            return showtimeList.getSelectedValue();
        } else {
            return null;
        }
    }

    public void updateMovieList(List<Movie> updatedMovies) {
        this.movies = updatedMovies;

        DefaultListModel<String> movieListModel = new DefaultListModel<>();
        for (Movie movie : updatedMovies) {
            movieListModel.addElement(movie.getTitle());
        }

        movieList.setModel(movieListModel);
    }

    public void updateShowtimeList(String selectedMovie) {
        DefaultListModel<String> showtimeListModel = new DefaultListModel<>();

        if (showtimes != null) {
            for (Showtime showtime : showtimes) {
                if (showtime.getMovieTitle().equals(selectedMovie)) {
                    String theaterName = database.getTheaterName(showtime.getTheater());
                    showtimeListModel.addElement(showtime.getTime() + " at " + theaterName);
                }
            }
        }

        showtimeList.setModel(showtimeListModel);
    }

    public static void main(String[] args) {
        DatabaseManager database = new DatabaseManager();
        List<Movie> movies = DatabaseManager.getMovies();
        List<Showtime> showtime = database.getShowtimes();

        SwingUtilities.invokeLater(() -> {
            MovieSelectionDialog dialog = new MovieSelectionDialog(movies, showtime, database);
            dialog.setVisible(true);
        });
    }
}
