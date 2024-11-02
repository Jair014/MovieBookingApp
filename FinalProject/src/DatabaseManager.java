import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:C:/sqlite/db/theater.db";
    private static int authenticatedUserId = -1; 

    public DatabaseManager() {
        try {
            // Load the JDBC driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Returns true if a matching user is found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Authentication failed due to an exception
        }
    }
    public static int getAuthenticatedUserId() {
       
		return authenticatedUserId;
    }
    public boolean authenticateUserID(String username, String password) {
        String query = "SELECT user_id FROM user WHERE username = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    authenticatedUserId = resultSet.getInt("user_id");
                    return true; // Authentication successful
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Authentication failed
    }


    public static List<Movie> getMovies() {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM movie";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                String time = resultSet.getString("time");
                String summary = resultSet.getString("summary");

                Movie movie = new Movie(title, genre, time, summary);
                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean addUser(String username, String password, String email) {
        String query = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Returns true if a row was inserted
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Failed to add user due to an exception
        }
    }
    public List<Showtime> getShowtimes(String selectedMovie) {
        List<Showtime> showtimes = new ArrayList<>();
        String query = "SELECT s.movie_title, s.start_time, t.name AS theater_name " +
                       "FROM showtime s " +
                       "JOIN theater t ON s.theater_id = t.theater_id " +
                       "WHERE s.movie_title = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, selectedMovie);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String movieTitle = resultSet.getString("movie_title");
                    String time = resultSet.getString("start_time");
                    String theater = resultSet.getString("theater_name");

                    Showtime showtime = new Showtime(movieTitle, time, theater);
                    showtimes.add(showtime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return showtimes;
    }
    public List<Showtime> getShowtimes() {
        List<Showtime> showtimes = new ArrayList<>();
        String query = "SELECT s.movie_title, s.start_time, t.name AS theater_name " +
                       "FROM showtime s " +
                       "JOIN theater t ON s.theater_id = t.theater_id";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String movieTitle = resultSet.getString("movie_title");
                String time = resultSet.getString("start_time");
                String theater = resultSet.getString("theater_name");

                Showtime showtime = new Showtime(movieTitle, time, theater);
                showtimes.add(showtime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return showtimes;
    }



    public String getTheaterName(String theaterName) {
        String query = "SELECT name FROM theater WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, theaterName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return a default name or handle the case where the theater name is not found
        return "Unknown Theater";
    }

    public static List<String> getBookingHistory(int userId) {
        List<String> bookingHistory = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM booking WHERE user_id = ?");
        ) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int bookingId = resultSet.getInt("booking_id");
                    int showtimeId = resultSet.getInt("showtime_id");
                    int numTickets = resultSet.getInt("num_tickets");
                    Timestamp bookingTime = resultSet.getTimestamp("booking_time");
                    
                    String entry = String.format("Booking ID: %d, Showtime ID: %d, Tickets: %d, Time: %s",
                            bookingId, showtimeId, numTickets, bookingTime.toString());

                    bookingHistory.add(entry);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookingHistory;
    }              
}

    



