import java.time.LocalDate;

record StayPeriod(LocalDate checkIn, LocalDate checkOut) {

    public StayPeriod {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException(
                    "Check-out must be after check-in");
        }
    }

    public boolean overlapsWith(StayPeriod other) {
        return checkIn.isBefore(other.checkOut())
                && checkOut.isAfter(other.checkIn());
    }
}

enum ReservationStatus {
    REQUESTED,
    CONFIRMED,
    CHECKED_IN,
    COMPLETED,
    CANCELLED
}

class Reservation {

    private final int reservationId;
    private final String guestName;
    private final int roomId;
    private final StayPeriod stayPeriod;

    private ReservationStatus status;

    public Reservation(int reservationId,
                       String guestName,
                       int roomId,
                       StayPeriod stayPeriod) {

        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomId = roomId;
        this.stayPeriod = stayPeriod;
        this.status = ReservationStatus.REQUESTED;
    }

    public void confirm() {
        if (status != ReservationStatus.REQUESTED) {
            throw new IllegalStateException(
                    "Only requested reservations can be confirmed");
        }

        status = ReservationStatus.CONFIRMED;
    }

    public void checkIn() {
        if (status != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Reservation not confirmed");
        }

        status = ReservationStatus.CHECKED_IN;
    }

    public void checkOut() {
        if (status != ReservationStatus.CHECKED_IN) {
            throw new IllegalStateException(
                    "Guest not checked in");
        }

        status = ReservationStatus.COMPLETED;
    }

    public void cancel() {
        if (status == ReservationStatus.CHECKED_IN
                || status == ReservationStatus.COMPLETED) {

            throw new IllegalStateException(
                    "Cannot cancel reservation");
        }

        status = ReservationStatus.CANCELLED;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public StayPeriod getStayPeriod() {
        return stayPeriod;
    }
}

public class CaseStudy4 {

    public static void main(String[] args) {

        StayPeriod stay1 =
                new StayPeriod(
                        LocalDate.of(2026, 11, 10),
                        LocalDate.of(2026, 11, 15));

        StayPeriod stay2 =
                new StayPeriod(
                        LocalDate.of(2026, 11, 14),
                        LocalDate.of(2026, 11, 18));

        System.out.println(
                "Overlapping: "
                        + stay1.overlapsWith(stay2));

        Reservation reservation =
                new Reservation(
                        101,
                        "Sujith",
                        129,
                        stay1);

        reservation.confirm();
        System.out.println(
                "Status: "
                        + reservation.getStatus());

        reservation.checkIn();
        System.out.println(
                "Status: "
                        + reservation.getStatus());

        reservation.checkOut();
        System.out.println(
                "Status: "
                        + reservation.getStatus());
    }
}