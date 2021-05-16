package com.epam.jwd.rent.service;

import com.epam.jwd.rent.dao.impl.BicycleDao;
import com.epam.jwd.rent.dao.impl.OrderDao;
import com.epam.jwd.rent.dao.impl.UserDao;
import com.epam.jwd.rent.model.Bicycle;
import com.epam.jwd.rent.model.BicycleFactory;
import com.epam.jwd.rent.model.Order;
import com.epam.jwd.rent.model.OrderDto;
import com.epam.jwd.rent.model.OrderFactory;
import com.epam.jwd.rent.model.User;
import com.epam.jwd.rent.model.UserFactory;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderServiceTest {
    List<Order> orders;
    List<OrderDto> ordersDto;
    List<Bicycle> bicycles;
    OrderService orderService;
    User user;
    Order order;
    OrderDto orderDto;
    @Mock
    UserDao userDao;
    @Mock
    OrderDao orderDao;
    @Mock
    BicycleDao bicycleDao;
    @Mock
    OrderFactory orderFactory;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        orderService = new OrderService();
        orderService.setOrderDao(orderDao);
        orderService.setUserDao(userDao);
        orderService.setBicycleDao(bicycleDao);
        orders = new ArrayList<>(Arrays.asList(
                OrderFactory.getInstance().create(1, 1, 4, LocalDate.now()),
                OrderFactory.getInstance().create(1, 2, 1, LocalDate.now()),
                OrderFactory.getInstance().create(2, 1, 10, LocalDate.now())
        ));
        ordersDto = new ArrayList<>(Arrays.asList(
                new OrderDto("login1", "bicycle1", 4, "in processing", LocalDate.now()),
                new OrderDto("login1", "bicycle2", 1, "in processing", LocalDate.now()),
                new OrderDto("login2", "bicycle1", 10, "in processing", LocalDate.now())));
        bicycles = new ArrayList<>(Arrays.asList(
                BicycleFactory.getInstance().create("bicycle1", BigDecimal.TEN, "place1", 2),
                BicycleFactory.getInstance().create("bicycle2", BigDecimal.ZERO, "place2", 2),
                BicycleFactory.getInstance().create("bicycle3", BigDecimal.TEN, "place2", 3)
        ));
        user = UserFactory.getInstance().create("login1", "pass1");
        order = OrderFactory.getInstance().create(1, 2, 10, LocalDate.now());
        orderDto = new OrderDto("login1", "bicycle2", 10, "in processing", LocalDate.now());
    }

    @Test
    public void findAll_ShouldConvertToDtoAllOrders() throws SQLException {
        when(orderDao.findAll()).thenReturn(Optional.of(orders));
        when(orderDao.findBicycleModel(1)).thenReturn("bicycle1");
        when(orderDao.findBicycleModel(2)).thenReturn("bicycle2");
        when(orderDao.findUserName(1)).thenReturn("login1");
        when(orderDao.findUserName(2)).thenReturn("login2");
        Optional<List<OrderDto>> actual = orderService.findAll();
        assertTrue(actual.isPresent());
        for (int i = 0; i < ordersDto.size(); i++) {
            assertEquals((i + 1) + " order should be correct", ordersDto.get(i), actual.get().get(i));
        }
    }

    @Test
    public void findAll_ShouldBeEmpty_WhenSqlDidNotWorked() throws SQLException {
        when(orderDao.findUserName(anyInt())).thenThrow(new SQLException());
        when(orderDao.findAll()).thenReturn(Optional.of(orders));
        Optional<List<OrderDto>> actual = orderService.findAll();
        assertTrue(actual.isPresent());
        assertEquals(0, actual.get().size());
    }

    @Test
    public void findByPage_ShouldConverterToDtoAll() throws SQLException {
        when(orderDao.findByPageNumber(anyString(), anyInt())).thenReturn(Optional.of(orders));
        when(orderDao.findBicycleModel(1)).thenReturn("bicycle1");
        when(orderDao.findBicycleModel(2)).thenReturn("bicycle2");
        when(orderDao.findUserName(1)).thenReturn("login1");
        when(orderDao.findUserName(2)).thenReturn("login2");
        Optional<List<OrderDto>> actual = orderService.findByPage("id", 1);
        assertTrue(actual.isPresent());
        for (int i = 0; i < ordersDto.size(); i++) {
            assertEquals((i + 1) + " order should be correct", ordersDto.get(i), actual.get().get(i));
        }
    }

    @Test
    public void createOrder_ShouldBeEmpty_WhenUserOrBicycleDoesNotExist() {
        when(userDao.findByLogin(anyString())).thenReturn(Optional.empty());
        when(bicycleDao.findBicyclesBuModelAndPlace(anyString(), anyString())).thenReturn(Optional.empty());
        assertFalse(orderService.createOrder(
                "login1", "bicycle1", 10, LocalDate.now(), "place1").isPresent());
        verify(orderDao, times(0)).create(any());
    }

    @Test
    public void createOrder_ShouldBeEmpty_WhenBicycleIsNotAvailable() {
        when(userDao.findByLogin("login1")).thenReturn(Optional.of(user));
        when(bicycleDao.findBicyclesBuModelAndPlace("bicycle1", "place1")).thenReturn(Optional.of(bicycles.get(0)));
        when(orderDao.findAll()).thenReturn(Optional.of(orders));
        assertFalse(orderService.createOrder(
                "login1", "bicycle1", 10, LocalDate.now(), "place1").isPresent());
        verify(orderDao, times(0)).create(any());
    }

    @Test
    public void createOrder_ShouldBeEmpty_WhenNotEnoughMoney() {
        when(userDao.findByLogin("login1")).thenReturn(Optional.of(user));
        when(bicycleDao.findBicyclesBuModelAndPlace("bicycle3", "place2")).thenReturn(Optional.of(bicycles.get(2)));
        when(orderDao.findAll()).thenReturn(Optional.of(orders));
        when(orderFactory.create(1, 3, 10, LocalDate.now())).thenReturn(order);
        assertFalse(orderService.createOrder(
                "login1", "bicycle3", 10, LocalDate.now(), "place2").isPresent());
        verify(orderDao, times(0)).create(any());
    }

    @Test
    public void createOrder_ShouldBeEmpty_WhenSqlDidNotWorked() {
        when(userDao.findByLogin("login1")).thenReturn(Optional.of(user));
        when(bicycleDao.findBicyclesBuModelAndPlace("bicycle2", "place2")).thenReturn(Optional.of(bicycles.get(1)));
        when(orderDao.findAll()).thenReturn(Optional.of(orders));
        when(orderDao.create(any())).thenReturn(Optional.empty());
        when(orderFactory.create(1, 2, 10, LocalDate.now())).thenReturn(order);
        assertFalse(orderService.createOrder(
                "login1", "bicycle2", 10, LocalDate.now(), "place2").isPresent());
        verify(orderDao, times(1)).create(any());
    }

    @Test
    public void createOrder_ShouldReturnOrder_WhenAllIsCorrect() throws SQLException {
        when(userDao.findByLogin("login1")).thenReturn(Optional.of(user));
        when(bicycleDao.findBicyclesBuModelAndPlace("bicycle2", "place2")).thenReturn(Optional.of(bicycles.get(1)));
        when(orderDao.findAll()).thenReturn(Optional.of(orders));
        when(orderDao.create(any())).thenReturn(Optional.of(order));
        when(orderDao.findBicycleModel(2)).thenReturn("bicycle2");
        when(orderDao.findUserName(1)).thenReturn("login1");
        when(orderFactory.create(1, 2, 10, LocalDate.now())).thenReturn(order);
        Optional<OrderDto> actual = orderService.createOrder(
                "login1", "bicycle2", 10, LocalDate.now(), "place2");
        assertTrue(actual.isPresent());
        assertEquals(orderDto, actual.get());
    }

    @Test
    public void findAllOrdersByUserName_ShouldReturnOrdersDto() throws SQLException {
        when(orderDao.findAllOrdersByUserName("login1")).thenReturn(Optional.of(new ArrayList<>(
                Arrays.asList(orders.get(0), orders.get(1)))));
        when(orderDao.findBicycleModel(1)).thenReturn("bicycle1");
        when(orderDao.findBicycleModel(2)).thenReturn("bicycle2");
        when(orderDao.findUserName(1)).thenReturn("login1");
        Optional<List<OrderDto>> actual = orderService.findAllOrdersByUserName("login1");
        assertTrue(actual.isPresent());
        assertEquals(2, actual.get().size());
        assertEquals(ordersDto.get(0), actual.get().get(0));
        assertEquals(ordersDto.get(1), actual.get().get(1));
    }

    @Test
    public void changeStatus_ShouldNotAcceptStatus_WhenNotEnoughMoney() {
        when(orderDao.findById(1)).thenReturn(Optional.of(orders.get(0)));
        when(userDao.findById(1)).thenReturn(Optional.of(user));
        when(bicycleDao.findById(1)).thenReturn(Optional.of(bicycles.get(0)));
        orderService.changeStatus(1, "accepted");
        verify(orderDao, times(0)).changeStatus(1, "accepted");
    }

    @Test
    public void changeStatus_ShouldNotAcceptStatus_WhenAllIsCorrect() {
        when(orderDao.findById(2)).thenReturn(Optional.of(orders.get(1)));
        when(userDao.findById(1)).thenReturn(Optional.of(user));
        when(bicycleDao.findById(2)).thenReturn(Optional.of(bicycles.get(1)));
        orderService.changeStatus(2, "accepted");
        verify(orderDao, times(0)).changeStatus(2, "cancelled");
    }
}