package com.pluralsight.sneakerdrops;

import com.pluralsight.sneakerdrops.models.Sneaker;
import com.pluralsight.sneakerdrops.service.NotFoundException;
import com.pluralsight.sneakerdrops.service.SneakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class StartupRunner implements CommandLineRunner {

    private final SneakerService sneakerService;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public StartupRunner(SneakerService sneakerService) {
        this.sneakerService = sneakerService;
    }

    @Override
    public void run(String... args) {
        sneakerService.seedData();

        boolean istrue = true;
        while (istrue) {
            System.out.println("1: List all sneakers");
            System.out.println("2: Find by model");
            System.out.println("3: Find under a certain price");
            System.out.println("4: Find by release year");
            System.out.println("5: Find by max price and min release year");
            System.out.println("6: Add Sneaker");
            System.out.println("7: Update Sneaker");
            System.out.println("8: Delete Sneaker");
            System.out.println("9: Find by brand");
            System.out.println("0: Quit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
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
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void searchByBrand() {
        System.out.println("Enter brand");
        String brandName = scanner.nextLine();
        List<Sneaker> sneakers = sneakerService.findByBrand(brandName);
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
        sneakerService.listBrands().forEach(b -> System.out.println(b.getId() + ": " + b.getName()));
        System.out.println("Brands Id: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        sneakerService.add(model, price, releaseYear, id);
        System.out.println("Sneaker added successfully!");
    }

    private void updateSneaker() {
        System.out.println("Enter sneaker id:");
        long id = scanner.nextLong();
        scanner.nextLine();
        System.out.println("Enter new sneaker price: ");
        double newPrice = scanner.nextDouble();
        scanner.nextLine();

        sneakerService.updatePrice(id, newPrice);
        System.out.println("Sneaker price updated successfully!");
    }

    private void deleteSneaker() {
        System.out.println("Enter the sneaker id");
        long id = scanner.nextLong();
        scanner.nextLine();

        sneakerService.delete(id);
        System.out.println("Deleted sneaker: " + id);
    }

    private void findByPriceAndYear() {
        System.out.println("Enter max price for your sneaker");
        double maxPrice = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter min year for your sneaker");
        int minYear = scanner.nextInt();
        scanner.nextLine();
        List<Sneaker> sneakers = sneakerService.search(maxPrice, minYear);
        for (Sneaker sneaker : sneakers) {
            System.out.println(sneaker);
        }
    }

    private void findByModel() {
        System.out.println("Please enter the model name");
        String modelName = scanner.nextLine();
        List<Sneaker> sneakers = sneakerService.findByModel(modelName);
        for (Sneaker sneaker : sneakers) {
            System.out.println(sneaker);
        }
    }

    private void findByPrice() {
        System.out.println("Please enter the max price for a sneaker");
        double price = scanner.nextDouble();
        scanner.nextLine();
        List<Sneaker> sneakers = sneakerService.findByPrice(price);
        for (Sneaker sneaker : sneakers) {
            System.out.println(sneaker);
        }
    }

    private void findByYear() {
        System.out.println("Please enter the year");
        int year = scanner.nextInt();
        scanner.nextLine();
        List<Sneaker> sneakers = sneakerService.findByYear(year);
        for (Sneaker sneaker : sneakers) {
            System.out.println(sneaker);
        }
    }

    private void listSneakers() {
        System.out.println("Sneakers in stock: " + sneakerService.count());
        sneakerService.allSneakers().forEach(System.out::println);
    }
}