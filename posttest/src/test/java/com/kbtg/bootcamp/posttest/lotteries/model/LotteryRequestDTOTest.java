package com.kbtg.bootcamp.posttest.lotteries.model;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LotteryRequestDTOTest {

  private static Validator validator;

  @BeforeEach
  void setup() {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @Test
  @DisplayName("ticket should equal 6 characters.")
  public void ticketShouldEqualSixCharacters() {
    String dummyTicket = "123456";
    TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(dummyTicket, 80, 1);

    var violations = validator.validate(ticketRequestDTO);

    assertThat(violations).isEmpty();
  }

  @Test
  @DisplayName("ticket has errors because less than 6 characters or more.")
  public void ticketMoreOrLessSixCharacters() {
    String dummyTicket = "123456898978797";
    TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(dummyTicket, 80, 1);

    var violations = validator.validate(ticketRequestDTO);

    Iterator<ConstraintViolation<TicketRequestDTO>> iterator = violations.iterator();
    assertThat(iterator.next().getMessage()).isEqualTo("must be 6 characters only");

    dummyTicket = "123";
    ticketRequestDTO = new TicketRequestDTO(dummyTicket, 80, 1);

    violations = validator.validate(ticketRequestDTO);

    iterator = violations.iterator();
    assertThat(iterator.next().getMessage()).isEqualTo("must be 6 characters only");
  }

  @Test
  @DisplayName("ticket does not have to be null or empty")
  public void ticketDoesNotNullOrEmpty() {
    TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(null, 80, 1);

    var violations = validator.validate(ticketRequestDTO);

    Iterator<ConstraintViolation<TicketRequestDTO>> iterator = violations.iterator();
    assertThat(iterator.next().getMessage()).isEqualTo("is required");
  }

  @Test
  @DisplayName("ticket, price, amount invalid should return 3 errors")
  public void thereAreThreeErrorMessage() {
    String dummyTicket = "aaaaaa";
    TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(dummyTicket, -100, -11);

    var violations = validator.validate(ticketRequestDTO);

    Iterator<ConstraintViolation<TicketRequestDTO>> iterator = violations.iterator();

    assertThat(violations).hasSize(3);
  }
}
