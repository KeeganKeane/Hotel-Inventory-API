package jp.co.rakuten.inventoryapi.validator;

import jp.co.rakuten.inventoryapi.entity.Reservations;
import jp.co.rakuten.inventoryapi.exception.InvalidRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

 class ReservationValidatorTest {

    @Test
    void validateId_Valid() {
        Integer id = 20;
        Assertions.assertDoesNotThrow(() -> ReservationValidator.validateId(id));
    }

    @Test
    void validateId_Invalid() {
        Integer id = 0;
        Assertions.assertThrows(InvalidRequestException.class, () -> ReservationValidator.validateId(id));
    }

    @Test
    void validateDates_Valid() {
        String from = "2020-01-01";
        String to = "2021-01-01";
        Assertions.assertDoesNotThrow(() -> ReservationValidator.validateDates(from, to));
    }

    @Test
    void validateDates_Invalid1() {
        String from = " ";
        String to = "2021-01-01";
        Assertions.assertThrows(InvalidRequestException.class, () -> ReservationValidator.validateDates(from, to));
    }

    @Test
    void validateDates_Invalid2() {
        String from = "2022-01-01";
        String to = "2021-01-01";
        Assertions.assertThrows(InvalidRequestException.class, () -> ReservationValidator.validateDates(from, to));
    }

    @Test
    void validateDates_Invalid() {
        String from = "01-01-2020";
        String to = "09-09-2020";
        Assertions.assertThrows(InvalidRequestException.class, () -> ReservationValidator.validateDates(from, to));
    }

    @Test
    void validateGuest_Valid() {
        Integer guest = 5;
        Assertions.assertDoesNotThrow(() -> ReservationValidator.validateGuest(guest));
    }

    @Test
    void validateGuest_InValid1() {
        Integer guest = -1;
        Assertions.assertThrows(InvalidRequestException.class, () -> ReservationValidator.validateGuest(guest));
    }

    @Test
    void validateGuest_InValid2() {
        Integer guest = 0;
        Assertions.assertThrows(InvalidRequestException.class, () -> ReservationValidator.validateGuest(guest));
    }

    @Test
    void validateReservationsPOST_ValidCase1() {
        Reservations reservations = new Reservations();
        reservations.setCheckIn("2021-01-01");
        reservations.setCheckOut("2022-01-01");
        reservations.setGuests(5);
        Assertions.assertDoesNotThrow(() -> ReservationValidator.validateReservationPOST(reservations));
    }

    @Test
    void validateReservationsPOST_InValidCase1() {
        Reservations reservations = new Reservations();
        reservations.setCheckIn("2021-01-01");
        reservations.setCheckOut("2022-01-01");
        reservations.setGuests(-1);
        Assertions.assertThrows(InvalidRequestException.class, () -> ReservationValidator.validateReservationPOST(reservations));
    }

    @Test
    void validateReservationsPOST_InValidCase2() {
        Reservations reservations = new Reservations();
        reservations.setCheckIn("01-01-2020");
        reservations.setCheckOut("01-01-2021");
        reservations.setGuests(5);
        Assertions.assertThrows(InvalidRequestException.class, () -> ReservationValidator.validateReservationPOST(reservations));
    }

    @Test
    void validateReservationsPOST_InValidCase3() {
        Reservations reservations = new Reservations();
        reservations.setCheckIn("2020-01-01");
        reservations.setCheckOut(" ");
        reservations.setGuests(5);
        Assertions.assertThrows(InvalidRequestException.class, () -> ReservationValidator.validateReservationPOST(reservations));
    }

    @Test
    void validateReservationsPOST_InValidCase4() {
        Reservations reservations = new Reservations();
        reservations.setCheckIn("01-01-2024");
        reservations.setCheckOut("01-01-2021");
        reservations.setGuests(5);
        Assertions.assertThrows(InvalidRequestException.class, () -> ReservationValidator.validateReservationPOST(reservations));
    }

}
