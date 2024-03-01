package com.proriberaapp.ribera.Api.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;

@RestController
public class BookingController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/bookings")
    public List<Map<String, Object>> getAllBookings() {
        String query = "SELECT * FROM public.booking";
        return jdbcTemplate.queryForList(query);
    }
}
