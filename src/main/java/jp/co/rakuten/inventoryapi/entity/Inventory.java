package jp.co.rakuten.inventoryapi.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Inventory Object constructor with setters and getters created through lombok with the @Data annotation
 *
 * @author keegan.keane
 */
@Data
@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private String type;
    @Column
    private String description;
    @Column(name = "dt_available_from")
    private String availableFrom;
    @Column(name = "dt_available_to")
    private String availableTo;
    @Column
    private boolean status;

}



