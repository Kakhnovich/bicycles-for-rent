package com.epam.jwd.rent.service;

import com.epam.jwd.rent.dao.impl.BicycleDao;
import com.epam.jwd.rent.dao.impl.OrderDao;
import com.epam.jwd.rent.model.Bicycle;
import com.epam.jwd.rent.model.BicycleDto;
import com.epam.jwd.rent.model.BicycleFactory;
import com.epam.jwd.rent.model.Order;
import com.epam.jwd.rent.model.OrderFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


public class BicycleServiceTest {
    List<BicycleDto> bicyclesDto;
    List<Bicycle> bicycles;
    List<Order> orders;
    Map<Integer, BicycleDto> rezMap = new HashMap<>();
    BicycleService bicycleService;
    @Mock
    BicycleDao bicycleDao;
    @Mock
    OrderDao orderDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        bicycleService = new BicycleService();
        bicycleService.setBicycleDao(bicycleDao);
        bicycleService.setOrderDao(orderDao);
        bicycles = new ArrayList<>(Arrays.asList(
                BicycleFactory.getInstance().create("model1", BigDecimal.TEN, "place1", 2),
                BicycleFactory.getInstance().create("model2", BigDecimal.ONE, "place1", 1),
                BicycleFactory.getInstance().create("model3", BigDecimal.TEN, "place1", 3)));
        bicyclesDto = new ArrayList<>(Arrays.asList(
                new BicycleDto("model1", BigDecimal.TEN, "place1", 2),
                new BicycleDto("model2", BigDecimal.ONE, "place1", 1),
                new BicycleDto("model3", BigDecimal.TEN, "place1", 3)));
        orders = new ArrayList<>(Arrays.asList(
                OrderFactory.getInstance().create(1, 1, 5, LocalDate.now()),
                OrderFactory.getInstance().create(1, 2, 3, LocalDate.now())));
        rezMap.put(1, new BicycleDto("model1", BigDecimal.TEN, "place1", 2));
        rezMap.put(3, new BicycleDto("model3", BigDecimal.TEN, "place1", 3));
    }

    @Test
    public void testFindAll_ShouldConvertToDtoAllBicycles() {
        when(bicycleDao.findAll()).thenReturn(Optional.of(bicycles));
        Optional<List<BicycleDto>> actual = bicycleService.findAll();
        assertTrue("result shouldn't be empty", actual.isPresent());
        for (int i = 0; i < bicyclesDto.size(); i++) {
            assertEquals((i + 1) + " element expected should be correct", bicyclesDto.get(i), actual.get().get(i));
        }
    }

    @Test
    public void testFindByPage_ShouldConvertToDtoAllBicycles() {
        when(bicycleDao.findByPageNumber(anyString(), anyInt())).thenReturn(Optional.of(bicycles));
        Optional<List<BicycleDto>> actual = bicycleService.findByPage("id", 1);
        assertTrue("result shouldn't be empty", actual.isPresent());
        for (int i = 0; i < bicyclesDto.size(); i++) {
            assertEquals((i + 1) + " element expected should be correct", bicyclesDto.get(i), actual.get().get(i));
        }
    }

    @Test
    public void changeBicyclesCount_ShouldReturnFalse_WhenBicycleDoesNotExist() {
        when(bicycleDao.findAll()).thenReturn(Optional.of(bicycles));
        when(bicycleDao.changeCountOfBicycles(anyInt(), anyInt())).thenReturn(true);
        assertFalse(bicycleService.changeBicyclesCount("model1", "place2", 1));
    }

    @Test
    public void changeBicyclesCount_ShouldReturnTrue_WhenCountIsPositive() {
        when(bicycleDao.findAll()).thenReturn(Optional.of(bicycles));
        when(bicycleDao.changeCountOfBicycles(anyInt(), anyInt())).thenReturn(true);
        ;
        assertTrue(bicycleService.changeBicyclesCount("model1", "place1", 2));
    }

    @Test
    public void changeBicyclesCount_ShouldReturnTrue_WhenCountIsNegative() {
        when(bicycleDao.findAll()).thenReturn(Optional.of(bicycles));
        when(bicycleDao.changeCountOfBicycles(anyInt(), anyInt())).thenReturn(true);
        ;
        assertTrue(bicycleService.changeBicyclesCount("model1", "place1", -2));
    }

    @Test
    public void findBicyclesToOder_ShouldReturn() {
        when(bicycleDao.findAll()).thenReturn(Optional.of(bicycles));
        when(orderDao.findAll()).thenReturn(Optional.of(orders));
        when(bicycleDao.findModelId("model1")).thenReturn(1);
        when(bicycleDao.findModelId("model3")).thenReturn(3);
        Map<Integer, BicycleDto> actual = bicycleService.findBicyclesToOder("place1", LocalDate.now());
        assertEquals(rezMap, actual);
    }
}