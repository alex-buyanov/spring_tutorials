package data_access_w_jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@SpringBootApplication
public class DataAccessWJpaApplication {
    private static final Logger LOG = LoggerFactory.getLogger(DataAccessWJpaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DataAccessWJpaApplication.class, args);
    }

    @Bean
    @Order(value = 1) // Database MUST be filled before any queries
    public CommandLineRunner fillDatabase(CustomerRepository repository) {
        return args -> {
            repository.save(new Customer("Jack", "Bauer"));
            repository.save(new Customer("Chloe", "O'Brian"));
            repository.save(new Customer("Kim", "Bauer"));
            repository.save(new Customer("David", "Palmer"));
            repository.save(new Customer("Michelle", "Dessler"));
        };
    }

    @Bean
    public CommandLineRunner fetchAll(CustomerRepository repository) {
        return args -> {
            LOG.info("Customers found with findAll():");
            LOG.info("-------------------------------");
            repository.findAll().forEach(customer -> LOG.info(customer.toString()));
            LOG.info("");
        };
    }

    @Bean
    public CommandLineRunner fetchById(CustomerRepository repository) {
        return args -> {
            LOG.info("Customer found with findById(1L):");
            LOG.info("-------------------------------");
            Customer customer = repository.findById(1);
            LOG.info("{}\n", customer);
        };
    }

    @Bean
    public CommandLineRunner fetchByLastName(CustomerRepository repository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                // fetch customers by last name
                LOG.info("Customer found with findByLastName('Bauer'):");
                LOG.info("-------------------------------");
                repository.findByLastName("Bauer").forEach(bauer -> LOG.info(bauer.toString()));
                LOG.info("");
            }
        };
    }
}
