package com.example.restaurant.service;

import com.example.restaurant.model.Branch;
import com.example.restaurant.model.MenuItem;
import com.example.restaurant.repo.BranchRepository;
import com.example.restaurant.repo.MenuItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final MenuItemRepository menuRepo;
    private final BranchRepository branchRepo;

    public DataInitializer(MenuItemRepository menuRepo, BranchRepository branchRepo) {
        this.menuRepo = menuRepo;
        this.branchRepo = branchRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if(menuRepo.count()==0){
            menuRepo.save(new MenuItem("Truffle Pasta","Creamy truffle pasta with parmesan.",12.50,5,"/images/truffle_pasta.jpg"));
            menuRepo.save(new MenuItem("Herb Roasted Chicken","Succulent roasted chicken with herbs.",15.00,4,"/images/roasted_chicken.jpg"));
            menuRepo.save(new MenuItem("Seafood Platter","Selection of fresh seafood.",22.00,5,"/images/seafood_platter.jpg"));
        }
        if(branchRepo.count()==0){
            branchRepo.save(new Branch("Downtown Branch","1234 Gourmet Street, Downtown City","/images/downtown.jpg"));
            branchRepo.save(new Branch("Lakeside Branch","56 Lake Road, Lakeside","/images/lakeside.jpg"));
        }
    }
}