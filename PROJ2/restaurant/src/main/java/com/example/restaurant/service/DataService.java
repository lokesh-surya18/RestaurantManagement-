package com.example.restaurant.service;

import com.example.restaurant.model.Branch;
import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.BookingRequest;
import com.example.restaurant.repo.BranchRepository;
import com.example.restaurant.repo.MenuItemRepository;
import com.example.restaurant.repo.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataService {
    private final MenuItemRepository menuRepo;
    private final BranchRepository branchRepo;
    private final BookingRepository bookingRepo;
    private final Map<Long, Set<String>> bookedSlots = new ConcurrentHashMap<>();

    public DataService(MenuItemRepository menuRepo, BranchRepository branchRepo, BookingRepository bookingRepo) {
        this.menuRepo = menuRepo;
        this.branchRepo = branchRepo;
        this.bookingRepo = bookingRepo;
        // initialize map from existing branches
        for (Branch b : branchRepo.findAll()) bookedSlots.put(b.getId(), Collections.synchronizedSet(new HashSet<>()));
        for (BookingRequest br : bookingRepo.findAll()) bookedSlots.computeIfAbsent(br.getBranchId(), k -> Collections.synchronizedSet(new HashSet<>())).add(br.getTimeslot());
    }

    public List<MenuItem> getMenu() { return menuRepo.findAll(); }

    public List<MenuItem> getTopSuggestions(int limit) {
        return menuRepo.findAll().stream().sorted(Comparator.comparingInt(MenuItem::getRating).reversed()).limit(limit).toList();
    }

    public List<Branch> getBranches() { return branchRepo.findAll(); }

    public boolean isSlotAvailable(Long branchId, String timeslot) {
        Set<String> set = bookedSlots.get(branchId);
        if (set == null) return true;
        return !set.contains(timeslot);
    }

    public boolean bookSlot(Long branchId, String timeslot, String name, String phone) {
        Set<String> set = bookedSlots.computeIfAbsent(branchId, k -> Collections.synchronizedSet(new HashSet<>()));
        synchronized (set) {
            if (set.contains(timeslot)) return false;
            BookingRequest b = new BookingRequest();
            b.setBranchId(branchId); b.setTimeslot(timeslot); b.setName(name); b.setPhone(phone);
            bookingRepo.save(b);
            set.add(timeslot);
            return true;
        }
    }

    public MenuItem saveMenuItem(MenuItem item) {
        return menuRepo.save(item);
    }

    public Branch saveBranch(Branch branch) {
        return branchRepo.save(branch);
    }
    public boolean deleteMenuItem(Long id) {
        if (menuRepo.existsById(id)) {
            menuRepo.deleteById(id);
            return true;
        }
        return false;
    }
    public Set<String> getBookedSlotsForBranch(Long branchId) {
        return bookedSlots.getOrDefault(branchId, Collections.emptySet());
    }



}