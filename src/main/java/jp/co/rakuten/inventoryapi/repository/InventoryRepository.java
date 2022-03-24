package jp.co.rakuten.inventoryapi.repository;

import jp.co.rakuten.inventoryapi.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Inventory Repository containing operations created from the JpaRepository
 *
 * @author keegan.keane
 */
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    @Query(value = "SELECT inv.* FROM INVENTORY AS inv " +
            "WHERE inv.DT_AVAILABLE_FROM <= :dateFrom AND inv.DT_AVAILABLE_TO >= :dateTo " +
            "AND inv.ID NOT IN " +
            "(SELECT INVENTORY_ID FROM RESERVATION WHERE (DT_CHECK_IN > :dateFrom OR DT_CHECK_OUT < :dateTo))", nativeQuery = true)
    List<Inventory> findAllBetweenDates(@Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo);
}

//Old Queries that did not work

//    @Query(value="SELECT DISTINCT i.* FROM inventory i LEFT JOIN reservation r1 ON i.id = r1.inventory_id" +
//            " WHERE NOT EXISTS " +
//            "(SELECT r2.inventory_id FROM reservation r2 WHERE r2.inventory_id = i.id " +
//            "AND (i.DT_AVAILABLE_FROM > :dateFrom OR i.DT_AVAILABLE_TO < :dateTo " +
//            "OR (:dateTo > r2.DT_CHECK_IN AND :dateFrom < r2.DT_CHECK_OUT)))", nativeQuery = true)

//    @Query(value = "SELECT * FROM inventory i INNER JOIN reservation r ON i.id=r.inventory_id WHERE NOT EXISTS " +
//            "(SELECT r2.inventory_id FROM reservation r2 WHERE r2.inventory_id = i.id AND ((i.DT_AVAILABLE_FROM > :dateFrom AND i.DT_AVAILABLE_TO < :dateTo) OR" +
//            "(r.DT_CHECK_IN < :dateFrom AND r.DT_CHECK_OUT > :dateTo)))",
//            nativeQuery = true)

//@Query(value = "SELECT * FROM inventory i INNER JOIN reservation r ON i.id=r.inventory_id WHERE NOT EXISTS " +
//        "(SELECT r2.inventory_id FROM reservation r2 WHERE r2.inventory_id = i.id AND (reservations (i.DT_AVAILABLE_FROM <= :dateFrom AND i.DT_AVAILABLE_TO >= :dateTo) AND" +
//        "((r.DT_CHECK_IN > :dateTo AND r.DT_CHECK_OUT > :dateFrom) OR (r.DT_CHECK_IN < :dateTo AND r.DT_CHECK_OUT < :dateFrom))))",
//        nativeQuery = true)