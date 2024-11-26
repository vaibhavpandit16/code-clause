import java.util.*;

public class MovieBookingSystem {
    static Map<Integer, Movie> movies = new HashMap<>();
    static List<String> bookingSummary = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static String userName;

    public static void main(String[] args) {
        System.out.print("Welcome! Please enter your name: ");
        userName = scanner.nextLine();

        initializeMovies();

        while (true) {
            System.out.println("\nHello, " + userName + "! Welcome to the Movie Booking System!");
            System.out.println("1. Browse Movies");
            System.out.println("2. Book Tickets");
            System.out.println("3. View Booking Summary");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1 -> displayMovies();
                case 2 -> bookTickets();
                case 3 -> displayBookingSummary();
                case 4 -> {
                    System.out.println("Thank you for using the Movie Booking System, " + userName + "!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    static void initializeMovies() {
        movies.put(1, new Movie("Singham Again", "7:00 PM"));
        movies.put(2, new Movie("Pushpa 2", "9:00 PM"));
        movies.put(3, new Movie("Bhool Bhulaiyaa 3", "10:30 PM"));
    }

    static void displayMovies() {
        System.out.println("\nAvailable Movies:");
        for (Map.Entry<Integer, Movie> entry : movies.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue().name + " (" + entry.getValue().time + ")");
        }
        System.out.println("0. Return to Main Menu");
    }

    static void bookTickets() {
        displayMovies();
        System.out.print("\nEnter the movie number to book or 0 to return: ");
        int movieId = scanner.nextInt();
        scanner.nextLine(); 

        if (movieId == 0) return;

        if (!movies.containsKey(movieId)) {
            System.out.println("Invalid movie choice. Try again.");
            return;
        }

        Movie selectedMovie = movies.get(movieId);

        while (true) {
            System.out.println("\nSelected Movie: " + selectedMovie.name + " (" + selectedMovie.time + ")");
            selectedMovie.displaySeats();

            System.out.print("\nEnter seat numbers to book (e.g., A1,A2) or 0 to stop booking: ");
            String seatInput = scanner.nextLine();

            if (seatInput.equals("0")) {
                System.out.println("Returning to main menu...");
                break;
            }

            String[] seatsToBook = seatInput.split(",");
            boolean bookingSuccess = selectedMovie.bookSeats(seatsToBook);

            if (bookingSuccess) {
                String bookingDetails = "User: " + userName + ", Movie: " + selectedMovie.name + ", Showtime: " + selectedMovie.time + ", Seats: " + String.join(", ", seatsToBook);
                bookingSummary.add(bookingDetails);
                System.out.println("Booking successful!");
            } else {
                System.out.println("Some seats were unavailable. Please try again.");
            }
        }
    }

    static void displayBookingSummary() {
        if (bookingSummary.isEmpty()) {
            System.out.println("\nNo bookings made yet.");
        } else {
            System.out.println("\nBooking Summary:");
            for (int i = 0; i < bookingSummary.size(); i++) {
                System.out.println((i + 1) + ". " + bookingSummary.get(i));
            }
        }
    }
}

class Movie {
    String name;
    String time;
    String[][] seats;

    public Movie(String name, String time) {
        this.name = name;
        this.time = time;
        this.seats = new String[3][5];
        for (int i = 0; i < seats.length; i++) {
            Arrays.fill(seats[i], "O"); 
        }
    }

    void displaySeats() {
        System.out.println("Screen");
        for (int i = 0; i < seats.length; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < seats[i].length; j++) {
                System.out.print("[" + seats[i][j] + "] ");
            }
            System.out.println();
        }
        System.out.println("[O] Available, [X] Booked");
    }

    boolean bookSeats(String[] seatsToBook) {
        boolean success = true;

        for (String seat : seatsToBook) {
            int row = seat.charAt(0) - 'A'; 
            int col = Integer.parseInt(seat.substring(1)) - 1; 
            if (row < 0 || row >= seats.length || col < 0 || col >= seats[row].length) {
                System.out.println("Invalid seat: " + seat);
                success = false;
                continue;
            }

            if (seats[row][col].equals("X")) {
                System.out.println("Seat already booked: " + seat);
                success = false;
            } else {
                seats[row][col] = "X"; 
            }
        }

        return success;
    }
}
