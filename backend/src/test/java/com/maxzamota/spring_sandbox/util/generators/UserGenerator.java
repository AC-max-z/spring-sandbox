package com.maxzamota.spring_sandbox.util.generators;

import com.maxzamota.spring_sandbox.enums.UserRole;
import com.maxzamota.spring_sandbox.model.UserEntity;
import com.maxzamota.spring_sandbox.util.FakerProvider;

public class UserGenerator implements ObjectGenerator<UserEntity> {
    @Override
    public UserEntity generate() {
        var faker = FakerProvider.getInstance();
        return new UserEntity(
                faker.internet().safeEmailAddress(),
                faker.internet().password(),
                UserRole.ADMIN,
                true
        );
    }
}
