package com.epam.jwd.rent.service;

import com.epam.jwd.rent.dao.impl.BicycleDao;
import com.epam.jwd.rent.dao.impl.OrderDao;
import com.epam.jwd.rent.model.Bicycle;
import com.epam.jwd.rent.model.BicycleDto;
import com.epam.jwd.rent.model.BicycleFactory;

import com.epam.jwd.rent.model.User;
import com.epam.jwd.rent.model.UserDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Service class to work with Bicycles
 *
 * @author Elmax19
 * @version 1.0
 */
public class BicycleService implements CommonService<BicycleDto> {
    /**
     * {@link BicycleDao} class to call
     */
    private final BicycleDao bicycleDao;

    /**
     * default class constructor - init {@link BicycleService#bicycleDao}
     */
    public BicycleService() {
        this.bicycleDao = new BicycleDao();
    }

    /**
     * {@link CommonService} method realisation
     * @return list of all Bicycles dto
     * @see CommonService
     */
    @Override
    public Optional<List<BicycleDto>> findAll() {
        return bicycleDao.findAll()
                .map(bicycles -> bicycles.stream()
                        .map(this::convertToDto)
                        .collect(toList()));
    }

    /**
     * method to find Bicycles dto for one of pages
     * @param column sort column name in database
     * @param page page number
     * @return list of Bicycles dto by page
     */
    public Optional<List<BicycleDto>> findByPage(String column, int page) {
        return bicycleDao.findByPageNumber(column, page)
                .map(bicycles -> bicycles.stream()
                        .map(this::convertToDto)
                        .collect(toList()));
    }

    /**
     * method to increase or decrease count of bicycles at rent places
     * @param model bicycles model name
     * @param place rent place
     * @param count count of bicycles
     * @return has been method worked correct
     */
    public boolean changeBicyclesCount(String model, String place, int count) {
        Optional<Bicycle> bicycles = bicycleDao.findAll().get().stream()
                .filter(bicycle -> bicycle.getModel().equals(model))
                .filter(bicycle -> bicycle.getPlace().equals(place))
                .findFirst();
        if (bicycles.isPresent()){
            if (count < 0) {
                return bicycleDao.changeCountOfBicycles(bicycles.get().getId(), Math.max(bicycles.get().getCount() + count, 0));
            } else {
                return bicycleDao.changeCountOfBicycles(bicycles.get().getId(), bicycles.get().getCount() + count);
            }
        }
        return false;
    }

    /**
     * method that find available bicycles for ordering
     * @param place bicycles rent place
     * @param date order date
     * @return map of available bicycles
     */
    public Map<Integer, BicycleDto> findBicyclesToOder(String place, LocalDate date) {
        Map<Integer, BicycleDto> rezMap = new HashMap<>();
        List<Bicycle> bicycles = bicycleDao.findAll().get().stream()
                .filter(bicycle -> bicycle.getPlace().equals(place)).collect(toList());
        List<Bicycle> rezList = new ArrayList<>();
        for (Bicycle bicycle : bicycles) {
            int countOfUsed = (int) new OrderDao().findAll().get().stream()
                    .filter(order -> order.getDate().compareTo(date) == 0)
                    .filter(order -> order.getBicycle_id() == bicycle.getId())
                    .count();
            if (countOfUsed < bicycle.getCount()) {
                rezList.add(bicycle);
            }
        }
        rezList.forEach(bicycle -> rezMap.put(bicycleDao.findModelId(bicycle.getModel()), convertToDto(bicycle)));
        return rezMap;
    }

    /**
     * method to covert {@link Bicycle} model to {@link BicycleDto}
     * @param bicycle {@link Bicycle} object
     * @return converted {@link BicycleDto} object
     */
    private BicycleDto convertToDto(Bicycle bicycle) {
        return new BicycleDto(bicycle.getModel(),
                bicycle.getPrice(),
                bicycle.getPlace(),
                bicycle.getCount());
    }
}
