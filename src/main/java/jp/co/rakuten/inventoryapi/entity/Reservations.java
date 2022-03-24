package jp.co.rakuten.inventoryapi.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Reservation Object constructor with setters and getters created through lombok with the @Data annotation
 *
 * @author keegan.keane
 */
@Data
@Entity
@Table(name = "reservation")
public class Reservations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "inventory_id")
    private Integer inventoryId;
    @Column(name = "dt_check_in")
    private String checkIn;
    @Column(name = "dt_check_out")
    private String checkOut;
    @Column
    private Integer guests;
    @Column
    private boolean status;

}

