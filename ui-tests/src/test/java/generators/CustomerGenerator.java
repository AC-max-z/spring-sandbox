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
        String name = faker.name().firstName();
        String email = name + "@" + faker.internet().domainName();
        int age = faker.number().numberBetween(16, 99);
        List<String> availableGenders = List.of("MALE", "FEMALE", "DIFFERENT", "NONE_OF_YOUR_BUSINESS");
        String gender = availableGenders.get(new Random().nextInt(availableGenders.size()));
        return new Customer(name, email, age, gender);
    }
}
