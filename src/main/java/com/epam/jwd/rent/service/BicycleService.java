package com.epam.jwd.rent.service;

import com.epam.jwd.rent.dao.impl.BicycleDao;
import com.epam.jwd.rent.model.Bicycle;
import com.epam.jwd.rent.model.BicycleDto;
import com.epam.jwd.rent.model.BicycleFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class BicycleService implements CommonService<BicycleDto> {

    private final BicycleDao bicycleDao;

    public BicycleService() {
        this.bicycleDao = new BicycleDao();
    }

    @Override
    public Optional<List<BicycleDto>> findAll() {
        return bicycleDao.findAll()
                .map(bicycles -> bicycles.stream()
                        .map(this::convertToDto)
                        .collect(toList()));
    }

    public Optional<List<String>> findModels() {
        Optional<List<BicycleDto>> bicycles = findAll();
        return bicycles.map(bicycleDto -> bicycleDto.stream()
                .map(BicycleDto::getModel)
                .distinct()
                .collect(Collectors.toList()));
    }

    public void changeBicyclesCount(String model, String place, BigDecimal price, int count) {
        Optional<Bicycle> bicycles = bicycleDao.findAll().get().stream()
                .filter(bicycle -> bicycle.getModel().equals(model))
                .filter(bicycle -> bicycle.getPlace().equals(place))
                .findFirst();
        if (count < 0) {
            if (bicycles.isPresent()) {
                if (bicycles.get().getPrice().compareTo(price) == 0) {
                    bicycleDao.changeCountOfBicycles(bicycles.get().getId(), Math.max(bicycles.get().getCount() + count, 0));
                }
            }
        } else {
            if (bicycles.isPresent()) {
                if (bicycles.get().getPrice().compareTo(price) == 0) {
                    bicycleDao.changeCountOfBicycles(bicycles.get().getId(), bicycles.get().getCount() + count);
                } else {
                    Bicycle bicycle = BicycleFactory.getInstance().create(model, price, place, count);
                    bicycleDao.create(bicycle);
                }
            }
        }

    }

    private BicycleDto convertToDto(Bicycle bicycle) {
        return new BicycleDto(bicycle.getModel(),
                bicycle.getPrice(),
                bicycle.getPlace(),
                bicycle.getCount());
    }
}
