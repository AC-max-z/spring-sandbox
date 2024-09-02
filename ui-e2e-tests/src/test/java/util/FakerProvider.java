package util;

import com.github.javafaker.Faker;

public class FakerProvider {
    private static final Faker faker = new Faker();

    public static Faker getFaker() {
        return faker;
    }
}
