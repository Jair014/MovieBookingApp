import java.util.List;

public class Booking {
    private String username;  
    private Showtime showtime;
    private List<String> selectedSeats;
    private double totalCost;

    public Booking(String username, Showtime showtime, List<String> selectedSeats) {
        this.username = username;
        this.showtime = showtime;
        this.selectedSeats = selectedSeats;
    
    }

  

    public String getUsername() {
        return username;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public List<String> getSelectedSeats() {
        return selectedSeats;
    }

    public double getTotalCost() {
        return totalCost;
    }

}
