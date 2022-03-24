package jp.co.rakuten.inventoryapi.service;

import jp.co.rakuten.inventoryapi.dto.IdEntity;
import jp.co.rakuten.inventoryapi.dto.SuccessEntity;
import jp.co.rakuten.inventoryapi.entity.Reservations;
import jp.co.rakuten.inventoryapi.exception.InvalidRequestException;
import jp.co.rakuten.inventoryapi.repository.InventoryRepository;
import jp.co.rakuten.inventoryapi.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static jp.co.rakuten.inventoryapi.constant.ErrorMessages.*;

/**
 * Reservations Servicer that performs operations regarding Reservation API Calls
 *
 * @author keegan.keane
 */
@Slf4j
@Service
@Transactional
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * Returns all existing Reservation objects in the database
     *
     * @param void
     * @return List</ Reservations>
     */
    public List<Reservations> getAllReservations() {
        return reservationRepository.findAll();
    }

    /**
     * Finds a user specified Reservation in the database
     *
     * @param Integer
     * @return Reservations
     */
    public Reservations getReservation(Integer id) {
        validateReservationExistence(id);
        return reservationRepository.findById(id).get();
    }

    /**
     * Saves a user created Reservation object to the database
     *
     * @param Reservations
     * @return IdEntity
     */
    public IdEntity saveReservation(Reservations reservations) {

        Integer reservationsInventoryId = reservations.getInventoryId();

        //boolean to determine if the Reservation is valid through the existence of the inventory ID.
        //if the inventory ID exists, then continue
        if (validateInventoryExistence(reservationsInventoryId)) {
            //boolean to determine if there is already a pre-existing reservation that overlaps with the users
            if (reservationOverlaps(reservations)) {
                throw new InvalidRequestException(INVALID_DATE_OVERLAP);
            }
            //determine if the dates are out of the Inventory's bounds
            if (dateIsBefore(inventoryRepository.getById(reservations.getInventoryId()).getAvailableFrom(), reservations.getCheckIn()) && dateIsBefore(reservations.getCheckOut(), inventoryRepository.getById(reservations.getInventoryId()).getAvailableTo())) {
                reservations = reservationRepository.save(reservations);
                IdEntity idEntity = new IdEntity();
                idEntity.setId(reservations.getId());
                return idEntity;
            } else {
                throw new InvalidRequestException(INVALID_RESERVATION_DATES);
            }
        } else {
            //Throw error if the Inventory ID does not exist
            throw new InvalidRequestException(INVALID_INVENTORY_IN_RESERVATION);
        }
    }

    /**
     * Deletes a user specified Reservation object from the database
     *
     * @param Integer
     * @return SuccessEntity
     */
    public SuccessEntity deleteReservation(Integer id) {
        validateReservationExistence(id);
        reservationRepository.deleteById(id);
        SuccessEntity successEntity = new SuccessEntity();
        successEntity.setSuccess(!reservationRepository.existsById(id));
        return successEntity;
    }

    /**
     * Checks to existence of an Inventory object in the database
     *
     * @param Integer
     * @return boolean
     */
    public boolean validateInventoryExistence(Integer id) {
        if (!inventoryRepository.existsById(id)) {
            throw new InvalidRequestException(INVALID_ID_EXISTENCE);
        } else if (inventoryRepository.getById(id).getAvailableFrom() == null && inventoryRepository.getById(id).getAvailableTo() == null) {
            //Checks if the inventory has available to and available from dates, if not then throw an error as a reservation cannot be made.
            throw new InvalidRequestException(EMPTY_INVENTORY_DATES);
        } else {
            return true;
        }
    }

    /**
     * Checks the chronological order of user specified dates.
     *
     * @return boolean
     * @params String, String
     */
    public boolean dateIsBefore(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(date1).before(sdf.parse(date2));
        } catch (ParseException e) {
            throw new InvalidRequestException(PARSE_ERROR);
        }
    }

    /**
     * Checks to see if a user specified Reservation overlaps with a pre-existing Reservation in the database
     *
     * @param Reservations
     * @return boolean
     */
    public boolean reservationOverlaps(Reservations reservations) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return reservationRepository.findAll().stream().anyMatch(dataBaseRes -> {
            //If two reservations have the same inventory id, then compare their check in and checkout dates
            if (dataBaseRes.getInventoryId() == reservations.getInventoryId()) {
                try {
                    int checkInBeforeDbCheckOut = sdf.parse(reservations.getCheckIn()).compareTo(sdf.parse(dataBaseRes.getCheckOut()));
                    int checkOutBeforeDbCheckIn = sdf.parse(reservations.getCheckOut()).compareTo(sdf.parse(dataBaseRes.getCheckIn()));
                    log.debug("check in int " + checkInBeforeDbCheckOut);
                    log.debug("check out int " + checkOutBeforeDbCheckIn);
                    if (checkInBeforeDbCheckOut == 0 || checkOutBeforeDbCheckIn == 0) {
                        return true;
                    } else {
                        return checkInBeforeDbCheckOut != checkOutBeforeDbCheckIn;
                    }
                } catch (ParseException e) {
                    throw new InvalidRequestException(PARSE_ERROR);
                }
            } else {
                return false;
            }

        });
    }

    /**
     * Checks the existence of a user specified Reservation object in the database
     *
     * @param Integer
     * @return boolean
     */
    public boolean validateReservationExistence(Integer id) {
        if (!reservationRepository.existsById(id)) {
            throw new InvalidRequestException(INVALID_ID_EXISTENCE);
        } else {
            return true;
        }

    }


}
