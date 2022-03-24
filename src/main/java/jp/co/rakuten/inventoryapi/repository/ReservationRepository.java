package jp.co.rakuten.inventoryapi.repository;

import jp.co.rakuten.inventoryapi.entity.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Reservation Repository containing operations created from the JpaRepository
 *
 * @author keegan.keane
 */
public interface ReservationRepository extends JpaRepository<Reservations, Integer> {
}
