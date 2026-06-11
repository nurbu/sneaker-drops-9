package com.pluralsight.sneakerdrops.data;

import com.pluralsight.sneakerdrops.models.Sneaker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SneakerRepository extends CrudRepository<Sneaker, Long> {
    public List<Sneaker> findByModelContaining(String text);

    public List<Sneaker> findByPriceLessThan(double price);

    public List<Sneaker> findByReleaseYear(int year);

    public List<Sneaker> findByBrandName(String brandName);

    @Query("SELECT s FROM Sneaker s WHERE s.price <= :maxPrice AND s.releaseYear>= :minYear")
    public List<Sneaker> search(double maxPrice, int minYear);
}


