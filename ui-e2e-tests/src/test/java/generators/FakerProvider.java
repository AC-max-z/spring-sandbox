package generators;

import com.github.javafaker.Faker;

public class FakerProvider {
    private static Faker faker = new Faker();

    public static Faker getFaker() {
        return faker;
    }
}
