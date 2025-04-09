package nl.tudelft.sem.hoa.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class CreationDate {

    private transient int day;
    private transient Month month;
    private transient int year;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");

    /**
     * Create creationDate given day, month and year.
     *
     * @param day The day
     * @param month The month
     * @param year The year
     */
    public CreationDate(int day, Month month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * Create creationDate given LocalDate.
     *
     * @param date The LocalDate
     */
    public CreationDate(LocalDate date) {
        this.day = date.getDayOfMonth();
        this.month = date.getMonth();
        this.year = date.getYear();
    }

    @Override
    public String toString() {
        return day + "-" + month.toString() + "-" + year;
    }

    /**
     * Check if two weeks have passed.
     *
     * @return True if 2 weeks have passed
     */
    public boolean twoWeeksPassed() {
        LocalDate now = LocalDate.now();
        LocalDate twoWeeks = parseDate().plusWeeks(2);
        if (now.isAfter(twoWeeks)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if voting has closed (2 weeks and 3 days have passed).
     *
     * @return True if voting has closed
     */
    public boolean votingHasClosed() {
        LocalDate now = LocalDate.now();
        LocalDate twoWeeks = parseDate().plusWeeks(2).plusDays(3);
        if (now.isAfter(twoWeeks)) {
            return true;
        } else {
            return false;
        }
    }

    private LocalDate parseDate() {
        return LocalDate.of(year, month, day);
    }
}
