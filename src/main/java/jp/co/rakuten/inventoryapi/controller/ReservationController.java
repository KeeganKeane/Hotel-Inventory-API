package jp.co.rakuten.inventoryapi.controller;

import jp.co.rakuten.inventoryapi.dto.IdEntity;
import jp.co.rakuten.inventoryapi.dto.SuccessEntity;
import jp.co.rakuten.inventoryapi.entity.Reservations;
import jp.co.rakuten.inventoryapi.service.ReservationService;
import jp.co.rakuten.inventoryapi.validator.ReservationValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Reservations Controller containing endpoints of Reservation related API Calls
 *
 * @author keegan.keane
 */
@RestController
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    /**
     * End point to get all reservations.
     *
     * @return list of Reservations
     */
    @GetMapping(value = "/reservations", produces = "application/json")
    public List<Reservations> getReservationList() {
        return reservationService.getAllReservations();
    }

    /**
     * End point to get user specified reservation.
     *
     * @param id Integer
     * @return Reservations object
     */
    @GetMapping(value = "/reservations/{id}", produces = "application/json")
    public Reservations getReservation(@PathVariable Integer id) {
        ReservationValidator.validateId(id);
        return reservationService.getReservation(id);
    }

    /**
     * End point to update user specified Reservation
     *
     * @param reservations Reservations
     * @return idEntity
     */
    @PostMapping(value = "/reservations", produces = "application/json")
    public IdEntity saveReservations(@RequestBody Reservations reservations) {
        ReservationValidator.validateReservationPOST(reservations);
        return reservationService.saveReservation(reservations);
    }

    /**
     * End point to delete user specified Reservation.
     *
     * @param id Integer
     * @return successEntity
     */
    @DeleteMapping(value = "/reservations/{id}", produces = "application/json")
    public SuccessEntity deleteReservations(@PathVariable Integer id) {
        ReservationValidator.validateId(id);
        return reservationService.deleteReservation(id);
    }

}
