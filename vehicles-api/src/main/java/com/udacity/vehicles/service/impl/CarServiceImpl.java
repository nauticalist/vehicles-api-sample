package com.udacity.vehicles.service.impl;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.udacity.vehicles.service.CarNotFoundException;
import com.udacity.vehicles.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarServiceImpl implements CarService {
    private final static Logger LOG = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository repository;
    private final MapsClient mapsClient;
    private final PriceClient priceClient;

    @Autowired
    public CarServiceImpl(CarRepository repository, MapsClient mapsClient, PriceClient priceClient) {
        this.repository = repository;
        this.mapsClient = mapsClient;
        this.priceClient = priceClient;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        List<Car> cars = repository.findAll().stream().map(car -> {car.setPrice(priceClient.getPrice(car.getId()));
            car.setLocation(mapsClient.getAddress(car.getLocation()));
            return car;}).collect(Collectors.toList());
        return cars;
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {

        Optional<Car> carOptional = repository.findById(id);
        if (!carOptional.isPresent()) {
            throw new CarNotFoundException();
        }

        Car car = carOptional.get();
        try {
            car.setPrice(priceClient.getPrice(id));
            LOG.info(priceClient.getPrice(id));
            car.setLocation(mapsClient.getAddress(car.getLocation()));

        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        carToBeUpdated.setCondition(car.getCondition());
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }

        return repository.save(car);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        repository.findById(id).map(car -> {repository.delete(car); return car;}).orElseThrow(CarNotFoundException::new);
    }
}
