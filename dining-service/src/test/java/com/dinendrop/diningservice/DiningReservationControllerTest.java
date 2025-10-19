package com.dinendrop.diningservice;

import com.dinendrop.diningservice.controller.DiningReservationController;
import com.dinendrop.diningservice.dto.DiningReservationDTO;
import com.dinendrop.diningservice.exceptions.ResourceNotFoundException;
import com.dinendrop.diningservice.model.DiningReservation;
import com.dinendrop.diningservice.service.DiningReservationService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DiningReservationControllerTest {

    @Mock
    private DiningReservationService reservationService;

    @InjectMocks
    private DiningReservationController controller;

    private AutoCloseable closeable;

    // 🔹 Common reusable test data
    private DiningReservation reservation123;
    private DiningReservation reservation999;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        // Initialize test reservations
        reservation123 = new DiningReservation();
        reservation123.setId("123");
        reservation123.setUserId("user1");
        reservation123.setRestaurantId("rest1");

        reservation999 = new DiningReservation();
        reservation999.setId("999");
        reservation999.setUserId("user2");
        reservation999.setRestaurantId("rest2");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testBookDining_Success() {
        DiningReservationDTO dto = new DiningReservationDTO();

        when(reservationService.bookReservation(anyString(), any(DiningReservationDTO.class)))
                .thenReturn(reservation123);

        ResponseEntity<DiningReservation> response = controller.bookDining("Bearer token", dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("123", response.getBody().getId());

        verify(reservationService, times(1)).bookReservation(anyString(), any());
    }

    @Test
    void testGetById_NotFound() {
        when(reservationService.getById("999"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> controller.getById("999")
        );

        assertEquals("Reservation not found with id: 999", exception.getMessage());
        verify(reservationService, times(1)).getById("999");
    }

    @Test
    void testGetById_Found() {
        when(reservationService.getById("999"))
                .thenReturn(Optional.of(reservation999));

        ResponseEntity<DiningReservation> response = controller.getById("999");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("999", response.getBody().getId());
        verify(reservationService, times(1)).getById("999");
    }

    @Test
    void testCancelReservation_Success() {
        when(reservationService.cancelReservation("123"))
                .thenReturn(true);

        ResponseEntity<String> response = controller.cancel("123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Reservation cancelled", response.getBody());
        verify(reservationService, times(1)).cancelReservation("123");
    }

    @Test
    void testCancelReservation_NotFound() {
        when(reservationService.cancelReservation("999"))
                .thenReturn(false);

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> controller.cancel("999")
        );

        assertEquals("Reservation not found with id: 999", ex.getMessage());
        verify(reservationService, times(1)).cancelReservation("999");
    }

    @Test
    void testGetByUser() {
        when(reservationService.getByUserId("user1"))
                .thenReturn(List.of(reservation123, reservation999));

        ResponseEntity<List<DiningReservation>> response = controller.getByUser("user1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(reservationService, times(1)).getByUserId("user1");
    }
}
