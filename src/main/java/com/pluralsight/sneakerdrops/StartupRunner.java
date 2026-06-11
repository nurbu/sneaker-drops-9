package com.pluralsight.sneakerdrops;

import com.pluralsight.sneakerdrops.data.BrandRepository;
import com.pluralsight.sneakerdrops.data.SneakerRepository;
import com.pluralsight.sneakerdrops.models.Brand;
import com.pluralsight.sneakerdrops.models.Sneaker;
import com.pluralsight.sneakerdrops.service.DropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component

public class StartupRunner implements CommandLineRunner {

    private final DropService dropService;
    private final BrandRepository brandRepository;
    private final SneakerRepository sneakerRepository;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public StartupRunner(DropService dropService, BrandRepository brandRepository, SneakerRepository sneakerRepository) {
        this.dropService = dropService;
        this.brandRepository = brandRepository;
        this.sneakerRepository = sneakerRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println(dropService.getStatus());
        seedData();

        boolean istrue = true;
        while (istrue) {
            System.out.println("1: List all sneakers");
            System.out.println("2: Find by model");
            System.out.println("3: Find under a certain price");
            System.out.println("4: Find by release year");
            System.out.println("5: Find by max price and min release year");
            System.out.println("6: Add Sneaker");
            System.out.println("7: Update Sneaker");
            System.out.println("8: delete Sneaker");
            System.out.println("9: Find by brand");
            System.out.println("0: Quit");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> listSneakers();
                case 2 -> findByModel();
                case 3 -> findByPrice();
                case 4 -> findByYear();
                case 5 -> findByPriceAndYear();
                case 6 -> addSneaker();
                case 7 -> updateSneaker();
                case 8 -> deleteSneaker();
                case 9 -> searchByBrand();
                case 0 -> istrue = false;
            }
        }
    }

    private void searchByBrand() {
        System.out.println("Enter brand");
        String brandName = scanner.nextLine();
        List<Sneaker> sneakers = sneakerRepository.findByBrandName(brandName);
        if (sneakers.isEmpty()) {
            System.out.println("No sneakers found for " + brandName);
        } else {
            sneakers.forEach(sneaker -> System.out.println(sneaker));
        }
    }

    private void addSneaker() {
        System.out.println("Enter sneaker model: ");
        String model = scanner.nextLine();
        System.out.println("Enter sneaker price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter sneaker release year: ");
        int releaseYear = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Brands Available");
        brandRepository.findAll().forEach(b -> System.out.println(b.getId() + ": " + b.getName()));
        System.out.println("Brands Id: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        brandRepository.findById(id).ifPresentOrElse(b -> {
            Sneaker sneaker = new Sneaker(model, price, releaseYear, b);
            sneakerRepository.save(sneaker);
            System.out.println("Sneaker added successfully!");
        }, () -> {
            System.out.println("Brand not found!");
        });


    }

    private void updateSneaker() {
        System.out.println("Enter sneaker id:");
        Long id = scanner.nextLong();
        scanner.nextLine();

        sneakerRepository.findById(id).ifPresent(sneaker -> {
            System.out.println("Enter new sneaker price: ");
            double newPrice = scanner.nextDouble();
            scanner.nextLine();
            sneaker.setPrice(newPrice);
            sneakerRepository.save(sneaker);
            System.out.println("Sneaker price updated successfully!");
        });
    }

    private void deleteSneaker() {
        System.out.println("Enter the sneaker id");
        Long id = scanner.nextLong();
        scanner.nextLine();

        if (sneakerRepository.existsById(id)) {
            sneakerRepository.deleteById(id);
            System.out.println("Deleted sneaker: " + id);
        } else {
            System.out.println("No sneaker found with that id " + id);
        }
    }

    private void findByPriceAndYear() {
        System.out.println("Enter max price for your sneaker");
        int maxPrice = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter min year for your sneaker");
        int minYear = scanner.nextInt();
        List<Sneaker> sneakers = sneakerRepository.search(maxPrice, minYear);
        for (Sneaker sneaker : sneakers) {
            System.out.println(sneaker);
        }
    }

    private void findByModel() {
        System.out.println("Please enter the model name");
        String modelName = scanner.nextLine();
        List<Sneaker> sneakers = sneakerRepository.findByModelContaining(modelName);
        for (Sneaker sneaker : sneakers) {
            System.out.println(sneaker);
        }
    }

    private void findByPrice() {
        System.out.println("Please enter the max price for a sneaker");
        double price = scanner.nextDouble();
        scanner.nextLine();
        List<Sneaker> sneakers = sneakerRepository.findByPriceLessThan(price);
        for (Sneaker sneaker : sneakers) {
            System.out.println(sneaker);
        }
    }

    private void findByYear() {
        System.out.println("Please enter the year");
        int year = scanner.nextInt();
        scanner.nextLine();
        List<Sneaker> sneakers = sneakerRepository.findByReleaseYear(year);
        for (Sneaker sneaker : sneakers) {
            System.out.println(sneaker);
        }
    }

    private void listSneakers() {
        System.out.println("Sneakers in stock: " + sneakerRepository.count());
        sneakerRepository.findAll().forEach(System.out::println);
    }

    private void seedData() {
        if (brandRepository.count() == 0) {
            Brand nike = brandRepository.save(new Brand("Nike"));
            Brand adidas = brandRepository.save(new Brand("Adidas"));
            Brand newBalance = brandRepository.save(new Brand("New Balance"));

            if (sneakerRepository.count() == 0) {
                sneakerRepository.save(new Sneaker("Jordans_1", 200, 1998, nike));
                sneakerRepository.save(new Sneaker("samba", 300, 2004, adidas));
                sneakerRepository.save(new Sneaker("new balanace 465", 150, 2007, newBalance));
            }
        }


    }
}
