package spring.jdbctransactions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class BookingService {
    private static final Logger LOG = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public BookingService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void book(String... persons) {
        for (String person : persons) {
            LOG.info("Booking {} in a seat...", person);
            jdbcTemplate.update("INSERT INTO BOOKINGS(FIRST_NAME) VALUES (?)", person);
        }
    }

    public List<String> findAllBookings() {
        return jdbcTemplate.query("SELECT FIRST_NAME FROM BOOKINGS",
                (rs, rowNum) -> rs.getString("FIRST_NAME"));
    }
}
