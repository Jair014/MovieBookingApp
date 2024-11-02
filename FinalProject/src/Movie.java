class Movie {
	   private String title;
	    private String genre;
	    private String time;
	    private String summary;

    public Movie(String title, String genre, String time, String summary) {
        this.title = title;
        this.genre = genre;
        this.time = time;
        this.summary = summary; 
     }
    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }
    public String getTime() {
        return time;
    } 
    public String getSummary() {
    	return summary; 
    }

}
