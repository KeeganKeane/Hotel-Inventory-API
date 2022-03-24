package jp.co.rakuten.inventoryapi.controller;

import jp.co.rakuten.inventoryapi.dto.IdEntity;
import jp.co.rakuten.inventoryapi.entity.Inventory;
import jp.co.rakuten.inventoryapi.entity.Reservations;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ReservationControllerDeleteIT {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate = null;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "");
    }


    @Test
    public void deleteReservations() {

        Inventory inventoryPost = new Inventory();
        inventoryPost.setName("Hotel 1");
        inventoryPost.setType("LUXURY");
        inventoryPost.setAvailableFrom("2020-01-01");
        inventoryPost.setAvailableTo("2021-01-01");

        IdEntity idEntity = restTemplate.postForObject(baseUrl.concat("/inventories"), inventoryPost, IdEntity.class);
        Integer inventoryId = idEntity.getId();

        Reservations reservations1 = new Reservations();
        reservations1.setInventoryId(inventoryId);
        reservations1.setGuests(5);
        reservations1.setCheckIn("2020-05-01");
        reservations1.setCheckOut("2020-07-01");

        Reservations reservations2 = new Reservations();
        reservations2.setInventoryId(inventoryId);
        reservations2.setGuests(3);
        reservations2.setCheckIn("2020-08-01");
        reservations2.setCheckOut("2020-10-01");

        IdEntity idEntityReservation1 = restTemplate.postForObject(baseUrl.concat("/reservations"), reservations1, IdEntity.class);
        restTemplate.postForObject(baseUrl.concat("/reservations"), reservations2, IdEntity.class);
        Integer reservation1Id = idEntityReservation1.getId();

        //Check to see if reservations were added
        List<Reservations> reservationsGetAll = restTemplate.getForObject(baseUrl.concat("/reservations"), List.class);
        assertEquals(2, reservationsGetAll.size());

        //Remove reservation 1
        Map<String, Integer> params = new HashMap<>();
        params.put("id", reservation1Id);
        restTemplate.delete(baseUrl.concat("/reservations/{id}"), params);

        List<Inventory> finalReservationsGetAll = restTemplate.getForObject(baseUrl.concat("/inventories"), List.class);

        assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            restTemplate.getForObject(baseUrl.concat("/reservations/{id}"), Inventory.class, reservation1Id);
        });
        assertNotNull(reservation1Id);
        assertEquals(1, finalReservationsGetAll.size());
    }


}
