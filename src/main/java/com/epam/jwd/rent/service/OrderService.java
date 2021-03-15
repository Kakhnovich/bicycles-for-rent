package com.epam.jwd.rent.service;

import com.epam.jwd.rent.dao.impl.BicycleDao;
import com.epam.jwd.rent.dao.impl.OrderDao;
import com.epam.jwd.rent.dao.impl.UserDao;
import com.epam.jwd.rent.model.Bicycle;
import com.epam.jwd.rent.model.Order;
import com.epam.jwd.rent.model.OrderDto;
import com.epam.jwd.rent.model.User;
import com.epam.jwd.rent.model.OrderFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;

public class OrderService implements CommonService<OrderDto> {
    private static final Logger LOGGER = LogManager.getLogger(OrderService.class);
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final BicycleDao bicycleDao;

    public OrderService() {
        this.orderDao = new OrderDao();
        this.userDao = new UserDao();
        this.bicycleDao = new BicycleDao();
    }

    @Override
    public Optional<List<OrderDto>> findAll() {
        return orderDao.findAll()
                .map(bicycles -> bicycles.stream()
                        .map(this::convertToDto)
                        .filter(Objects::nonNull)
                        .collect(toList()));
    }

    public Optional<OrderDto> createOrder(String login, String model, int hours, LocalDate date, String place) {
        final Optional<User> user = userDao.findByLogin(login);
        final Optional<Bicycle> bicycle = bicycleDao.findBicyclesBuModelAndPlace(model, place);
        if (!user.isPresent() || !bicycle.isPresent()) {
            return Optional.empty();
        }
        int orders = (int) orderDao.findAll().get().stream()
                .filter(order -> order.getBicycle_id() == bicycle.get().getId())
                .filter(order -> order.getDate().equals(date))
                .count();
        if (orders == bicycle.get().getCount() || user.get().getBalance().compareTo(bicycle.get().getPrice()) < 0) {
            return Optional.empty();
        }
        final int userId = user.get().getId();
        final int bicycleId = bicycle.get().getId();
        Order order = OrderFactory.getInstance().create(userId, bicycleId, hours, date);
        Optional<Order> newOrder = orderDao.create(order);
        if (newOrder.isPresent()) {
            return Optional.ofNullable(convertToDto(order));
        }
        return Optional.empty();
    }

    private OrderDto convertToDto(Order order) {
        try {
            return new OrderDto(orderDao.findUserName(order.getUser_id()),
                    orderDao.findBicycleModel(order.getBicycle_id()),
                    order.getHours(),
                    order.getStatus(),
                    order.getDate());
        } catch (SQLException e) {
            LOGGER.error("SQLException at converting to dto: " + e.getSQLState());
            return null;
        }
    }

    public Optional<List<OrderDto>> findAllOrdersByUserName(String login) {
        Optional<List<Order>> orders = orderDao.findAllOrdersByUserName(login);
        return orders.map(orderList -> orderList.stream()
                .map(this::convertToDto)
                .collect(toList()));
    }

    public void changeStatus(int id, String status) {
        Optional<Order> order = orderDao.findById(id);
        if (order.isPresent()) {
            if (status.equals("accepted")) {
                Optional<User> user = userDao.findById(order.get().getUser_id());
                Optional<Bicycle> bicycle = bicycleDao.findById(order.get().getBicycle_id());
                if (user.isPresent() && bicycle.isPresent()) {
                    BigDecimal price = bicycle.get().getPrice().multiply(BigDecimal.valueOf(order.get().getHours()));
                    if (user.get().getBalance().compareTo(price) >= 0) {
                        userDao.changeBalance(user.get().getLogin(), price.negate());
                    }
                }
            }
            orderDao.changeStatus(id, status);
        }
    }

    public Optional<List<String>> findPlaces() {
        return bicycleDao.findAllPlaces();
    }
}
