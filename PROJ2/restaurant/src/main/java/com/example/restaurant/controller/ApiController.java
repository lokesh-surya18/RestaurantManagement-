package com.example.restaurant.controller;

import com.example.restaurant.model.BookingRequest;
import com.example.restaurant.model.BookingResponse;
import com.example.restaurant.model.Branch;
import com.example.restaurant.model.MenuItem;
import com.example.restaurant.service.DataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ApiController {
    private final DataService dataService;

    public ApiController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/menu")
    public List<MenuItem> menu() {
        return dataService.getMenu();
    }

    @GetMapping("/suggestions")
    public List<MenuItem> suggestions(@RequestParam(defaultValue = "3") int limit) {
        return dataService.getTopSuggestions(limit);
    }

    @GetMapping("/branches")
    public List<Branch> branches() {
        return dataService.getBranches();
    }

    @PostMapping("/book")
    public BookingResponse book(@RequestBody BookingRequest req) {
        if (req.getBranchId() == null || req.getTimeslot() == null || req.getName()==null || req.getPhone()==null) {
            return new BookingResponse(false, "branchId, timeslot, name and phone are required");
        }
        boolean ok = dataService.bookSlot(req.getBranchId(), req.getTimeslot(), req.getName(), req.getPhone());
        if (ok) return new BookingResponse(true, "Your table has been successfully booked!");
        return new BookingResponse(false, "Selected timeslot is no longer available");
    }
    @PostMapping("/menu")
    public MenuItem addMenuItem(@RequestBody MenuItem menuItem) {
        return dataService.saveMenuItem(menuItem);
    }
    @DeleteMapping("/menu/{id}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long id) {
        if (dataService.deleteMenuItem(id)) {
            return ResponseEntity.ok().body("Menu item deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu item not found");
        }


    }
    @GetMapping("/bookedSlots")
    public Set<String> getBookedSlots(@RequestParam Long branchId) {
        return dataService.getBookedSlotsForBranch(branchId);
    }




}