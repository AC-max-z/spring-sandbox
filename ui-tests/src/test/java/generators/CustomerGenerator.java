package generators;

import org.springsandbox.domain.Customer;
import utils.FakerProvider;

import java.util.Random;

public class CustomerGenerator implements ObjectGenerator<Customer> {

    @Override
    public Customer generate() {
        var faker = FakerProvider.getInstance();
        String[] genders = {"MALE", "FEMALE", "DIFFERENT", "NONE_OF_YOUR_BUSINESS"};
        return new Customer(
                faker.name().firstName(),
                faker.internet().emailAddress(),
                faker.number().numberBetween(16, 99),
                genders[new Random().nextInt(genders.length)]
        );
    }
}
