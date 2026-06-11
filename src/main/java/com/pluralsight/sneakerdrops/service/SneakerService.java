package com.pluralsight.sneakerdrops.service;

import com.pluralsight.sneakerdrops.data.BrandRepository;
import com.pluralsight.sneakerdrops.data.SneakerRepository;
import com.pluralsight.sneakerdrops.models.Brand;
import com.pluralsight.sneakerdrops.models.Sneaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SneakerService {

    private final SneakerRepository sneakerRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public SneakerService(SneakerRepository sneakerRepository, BrandRepository brandRepository) {
        this.sneakerRepository = sneakerRepository;
        this.brandRepository = brandRepository;
    }

    public long count() {
        return sneakerRepository.count();
    }

    public List<Sneaker> allSneakers() {
        return sneakerRepository.findAll();
    }

    public Sneaker findById(Long id) {
        return sneakerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public List<Sneaker> findByYear(int year) {
        return sneakerRepository.findByReleaseYear(year);
    }

    public List<Sneaker> findByPrice(double price) {
        return sneakerRepository.findByPriceLessThan(price);
    }

    public List<Sneaker> findByModel(String model) {
        return sneakerRepository.findByModelContaining(model);
    }

    public Sneaker add(String model, double price, int releaseYear, Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("No brand found with id " + brandId));
        return sneakerRepository.save(new Sneaker(model, price, releaseYear, brand));
    }

    public Sneaker updatePrice(Long id, double newPrice) {
        Sneaker sneaker = findById(id);
        sneaker.setPrice(newPrice);
        return sneakerRepository.save(sneaker);
    }

    public void delete(Long id) {
        if (!sneakerRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        sneakerRepository.deleteById(id);
    }

    public List<Sneaker> findByBrand(String brandName) {
        return sneakerRepository.findByBrandName(brandName);
    }

    public List<Brand> listBrands() {
        return brandRepository.findAll();
    }

    public List<Sneaker> search(double maxPrice, int minReleaseYear) {
        return sneakerRepository.search(maxPrice, minReleaseYear);
    }

    public void seedData() {
        if (brandRepository.count() == 0) {
            Brand nike = brandRepository.save(new Brand("Nike"));
            Brand adidas = brandRepository.save(new Brand("Adidas"));
            Brand newBalance = brandRepository.save(new Brand("New Balance"));

            if (sneakerRepository.count() == 0) {
                sneakerRepository.save(new Sneaker("Jordans_1", 200, 1998, nike));
                sneakerRepository.save(new Sneaker("Jordans_4", 300, 2004, nike));
                sneakerRepository.save(new Sneaker("Jordans_11", 150, 2007, nike));
            }
        }
    }
}