package com.kbtg.bootcamp.posttest.users.entity;

import com.kbtg.bootcamp.posttest.lotteries.model.TicketRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserTest {
    private static Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("should validate user there no error")
    public void testValidateUserNoError(){
        User user = new User("0123456789", "john");
        var violations = validator.validate(user);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("should have error in user id")
    public void testErrorUserId(){
        User user = new User("0123456AAA", "john");
        var violations = validator.validate(user);

        Iterator<ConstraintViolation<User>> iterator = violations.iterator();
        assertThat(iterator.next().getMessage()).isEqualTo("must be number only");


        user = new User("1", "john");
        violations = validator.validate(user);
        iterator = violations.iterator();
        assertThat(iterator.next().getMessage()).isEqualTo("must be at least 10 characters");
    }
}
