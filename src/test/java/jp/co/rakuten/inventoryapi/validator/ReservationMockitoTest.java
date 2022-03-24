package jp.co.rakuten.inventoryapi.validator;

import jp.co.rakuten.inventoryapi.entity.Reservations;
import jp.co.rakuten.inventoryapi.repository.ReservationRepository;
import jp.co.rakuten.inventoryapi.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


/**
 * Inventory Tester that uses Mockito to perform integration testing.
 *
 * @author keegan.keane
 */


//ReservationControllerIT
public class ReservationMockitoTest {
    @InjectMocks
    ReservationService reservationService;

    @Mock
    ReservationRepository reservationRepository;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void getAllReservationTest() {
        List<Reservations> list = new ArrayList<>();
        Reservations reservation = new Reservations();
        reservation.setInventoryId(1);
        reservation.setGuests(4);
        reservation.setCheckIn("2020-01-01");
        reservation.setCheckOut("2021-01-01");

        Reservations reservation1 = new Reservations();
        reservation1.setInventoryId(2);
        reservation1.setGuests(4);
        reservation1.setCheckIn("2020-01-01");
        reservation1.setCheckOut("2021-01-01");

        list.add(reservation1);
        list.add(reservation);

        when(reservationRepository.findAll()).thenReturn(list);

        //test
        List<Reservations> reservationsList = reservationService.getAllReservations();

        assertEquals(2, reservationsList.size());
        verify(reservationRepository, times(1)).findAll();
    }

}
