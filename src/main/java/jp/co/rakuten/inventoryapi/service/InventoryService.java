package jp.co.rakuten.inventoryapi.service;

import jp.co.rakuten.inventoryapi.dto.IdEntity;
import jp.co.rakuten.inventoryapi.dto.SuccessEntity;
import jp.co.rakuten.inventoryapi.entity.Inventory;
import jp.co.rakuten.inventoryapi.entity.Reservations;
import jp.co.rakuten.inventoryapi.exception.InvalidRequestException;
import jp.co.rakuten.inventoryapi.repository.InventoryRepository;
import jp.co.rakuten.inventoryapi.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import static jp.co.rakuten.inventoryapi.constant.ErrorMessages.*;

/**
 * Inventory Service that performs operations regarding Inventory API Calls
 *
 * @author keegan.keane
 */
@Slf4j
@Service
@Transactional
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Returns all existing Inventory objects in the database
     *
     * @param void
     * @return List</ Inventory>
     */
    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    /**
     * Returns a user specified Inventory item through the Inventory id
     *
     * @param Integer
     * @return Inventory
     */
    public Inventory getInventory(Integer id) {
        validateExistence(id);
        return inventoryRepository.findById(id).get();
    }

    /**
     * Returns all Inventory objects in the database that are available in between user specified dates
     *
     * @return List</ Inventory>
     * @params String, String
     */
    public List<Inventory> getAvailable(String dateFrom, String dateTo) {
        return inventoryRepository.findAllBetweenDates(dateFrom, dateTo);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        return inventoryRepository.findAll().stream().filter(inventoryItem ->
//        {
//            //Checks to see if the inventory item has a date, if there is no date associated with the item then do not add it to the list
//            if (StringUtils.hasText(inventoryItem.getAvailableFrom()) && (StringUtils.hasText(inventoryItem.getAvailableTo()))) {
//                try {
//                    int checkInAfterAvailFrom = sdf.parse(dateFrom).compareTo(sdf.parse(inventoryItem.getAvailableFrom()));
//                    int checkOutBeforeAvailTo = sdf.parse(dateTo).compareTo(sdf.parse(inventoryItem.getAvailableTo()));
//                    return checkInAfterAvailFrom >= 0 && checkOutBeforeAvailTo <= 0;
//                } catch (ParseException e) {
//                    throw new InvalidRequestException(PARSE_ERROR);
//                }
//            } else {
//                return false;
//            }
//        }).collect(Collectors.toList());
    }

    /**
     * Saves a user specified Inventory object to the database
     *
     * @param Inventory
     * @return IdEntity
     */
    @Transactional
    public IdEntity saveInventory(Inventory inventory) {
        //If dates are empty strings make them null values so that they can be accepted by the database
        if ((!StringUtils.hasText(inventory.getAvailableFrom())) && (!(StringUtils.hasText(inventory.getAvailableTo())))) {
            inventory.setAvailableFrom(null);
            inventory.setAvailableTo(null);
        }
        inventory = inventoryRepository.save(inventory);
        IdEntity idEntity = new IdEntity();
        idEntity.setId(inventory.getId());
        return idEntity;
    }

    /**
     * Deletes a user specified Inventory object from the database
     *
     * @param Integer
     * @return SuccessEntity
     */
    public SuccessEntity deleteInventory(Integer id) {
        validateExistence(id);
        if (reservationRepository.findAll().stream().anyMatch(reservations -> reservations.getInventoryId() == id)) {
            throw new InvalidRequestException(INVALID_INVENTORY_DELETE);
        }
        SuccessEntity successEntity = new SuccessEntity();
        inventoryRepository.deleteById(id);
        successEntity.setSuccess(!inventoryRepository.existsById(id));
        return successEntity;
    }

    /**
     * Updates a pre-existing Inventory object in the database
     *
     * @param Inventory
     * @return SuccessEntity
     */
    public SuccessEntity patchInventory(Inventory inventory) {
        validateExistence(inventory.getId());
        doesReservationOverlap(inventory);
        SuccessEntity successEntity = new SuccessEntity();
        inventory = inventoryRepository.save(inventory);
        successEntity.setSuccess(inventoryRepository.existsById(inventory.getId()));
        return successEntity;
    }

    /**
     * Checks to see if a reservation date overlaps with the inventory date
     *
     * @param Inventory
     * @return void
     */
    public void doesReservationOverlap(Inventory inventory) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String availTo = inventory.getAvailableTo();
        String availFrom = inventory.getAvailableFrom();
        Integer inventoryId = inventory.getId();
        List<Reservations> matchingReservationList = reservationRepository.findAll().stream().filter(reservations -> {
            if (reservations.getInventoryId() == inventoryId) {
                try {
                    //Checks to see if the user dates are null, if so throw an error as it conflicts with a reservation
                    if (!StringUtils.hasText(availTo) && !StringUtils.hasText(availFrom)) {
                        throw new InvalidRequestException(INVALID_DATE_CHANGE_NULL);
                    }
                    //should return 1 or 0 if there is no overlap, should return -1 if there is an overlap
                    int checkInBeforeAvailFrom = sdf.parse(reservations.getCheckIn()).compareTo(sdf.parse(availFrom));
                    //should return -1 or 0 if there is no overlap, should return 1 if there is an overlap
                    int checkOutBeforeAvailTo = sdf.parse(reservations.getCheckOut()).compareTo(sdf.parse(availTo));
                    if ((checkInBeforeAvailFrom < 0) || (checkOutBeforeAvailTo > 0)) {
                        return true;
                    }
                    ;

                } catch (ParseException e) {
                    throw new InvalidRequestException(PARSE_ERROR);
                }
            }
            return false;
        }).collect(Collectors.toList());

        if (matchingReservationList.size() != 0) {
            throw new InvalidRequestException(INVALID_INVENTORY_UPDATE);
        }
    }

    /**
     * Checks the existence of an Inventory object in the database
     *
     * @param Integer
     * @return boolean
     */
    public boolean validateExistence(Integer id) {
        if (!inventoryRepository.existsById(id)) {
            throw new InvalidRequestException(INVALID_ID_EXISTENCE);
        } else {
            return true;
        }

    }

}
