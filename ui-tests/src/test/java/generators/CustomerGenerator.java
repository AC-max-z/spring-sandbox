package generators;

import com.github.javafaker.Faker;
import org.springsandbox.domain.Customer;
import util.FakerProvider;

import java.util.List;
import java.util.Random;

public class CustomerGenerator implements ObjectGenerator<Customer> {

    @Override
    public Customer generate() {
        Faker faker = FakerProvider.getInstance();
        return new Customer(
                faker.name().firstName(),
                faker.internet().emailAddress(),
                faker.number().numberBetween(16, 99),
                List.of("MALE", "FEMALE", "DIFFERENT", "NONE_OF_YOUR_BUSINESS")
                        .get(new Random().nextInt(4))
        );
    }
}
