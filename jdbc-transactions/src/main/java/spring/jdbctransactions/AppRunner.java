package spring.jdbctransactions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
class AppRunner implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(AppRunner.class);

    private final BookingService bookingService;

    public AppRunner(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public void run(String... args) {
        bookingService.book("Alice", "Bob", "Carol");
        Assert.isTrue(bookingService.findAllBookings().size() == 3, "First booking should work with no problem");
        LOG.info("Alice, Bob and Carol have been booked");
        try {
            bookingService.book("Chris", "Samuel");
        } catch (RuntimeException e) {
            LOG.info("v--- The following exception is expected because 'Samuel' is too big for the DB ---v");
            LOG.error(e.getMessage());
        }

        for (String person : bookingService.findAllBookings()) {
            LOG.info("So far, {} is booked.", person);
        }
        LOG.info("You shouldn't see Chris or Samuel. Samuel violated DB constraints, and Chris was rolled back in the same transaction");
        Assert.isTrue(bookingService.findAllBookings().size() == 3, "'Samuel' should have triggered a rollback");

        try {
            bookingService.book("Buddy", null);
        } catch (RuntimeException e) {
            LOG.info("v--- The following exception is expect because null is not valid for the DB ---v");
            LOG.error(e.getMessage());
        }

        for (String person : bookingService.findAllBookings()) {
            LOG.info("So far, {} is booked.", person);
        }
        LOG.info("You shouldn't see Buddy or null. null violated DB constraints, and Buddy was rolled back in the same transaction");
        Assert.isTrue(bookingService.findAllBookings().size() == 3, "'null' should have triggered a rollback");
    }

}
