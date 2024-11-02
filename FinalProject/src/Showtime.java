public class Showtime {
    private String movieTitle;
    private String time;
    private String theater;

   

    public Showtime(String movieTitle, String time, String theater) {
	
    	 this.movieTitle = movieTitle;
         this.time = time;
         this.theater = theater;
	}

	
    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

 
    public String getTheater() {
        return theater;
    }

    public void setTheater(String theater) {
        this.theater = theater;
    }

    @Override
    public String toString() {
        return movieTitle + " - " + time + " at " + theater;
    }

    public double getTicketPrice() {
        return 10.0;  
    }


}
