package com.epam.jwd.rent.service;

import com.epam.jwd.rent.dao.impl.BicycleDao;
import com.epam.jwd.rent.dao.impl.OrderDao;
import com.epam.jwd.rent.dao.impl.UserDao;
import com.epam.jwd.rent.model.Bicycle;
import com.epam.jwd.rent.model.BicycleDto;
import com.epam.jwd.rent.model.Order;
import com.epam.jwd.rent.model.OrderDto;
import com.epam.jwd.rent.model.User;
import com.epam.jwd.rent.model.OrderFactory;
import com.epam.jwd.rent.model.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;

/**
 * Service class to work with Orders
 *
 * @author Elmax19
 * @version 1.0
 */
public class OrderService implements CommonService<OrderDto> {
    /**
     * {@link OrderService} class {@link Logger}
     */
    private static final Logger LOGGER = LogManager.getLogger(OrderService.class);
    /**
     * {@link OrderDao} class to call
     */
    private final OrderDao orderDao;
    /**
     * {@link UserDao} class to call
     */
    private final UserDao userDao;
    /**
     * {@link BicycleDao} class to call
     */
    private final BicycleDao bicycleDao;

    /**
     * default class constructor - init {@link OrderService#orderDao}, {@link OrderService#userDao} and {@link OrderService#bicycleDao}
     */
    public OrderService() {
        this.orderDao = new OrderDao();
        this.userDao = new UserDao();
        this.bicycleDao = new BicycleDao();
    }

    /**
     * {@link CommonService} method realisation
     * @return list of all Orders dto
     * @see CommonService
     */
    @Override
    public Optional<List<OrderDto>> findAll() {
        return orderDao.findAll()
                .map(bicycles -> bicycles.stream()
                        .map(this::convertToDto)
                        .filter(Objects::nonNull)
                        .collect(toList()));
    }

    /**
     * method to find Orders dto for one of pages
     * @param column sort column name in database
     * @param page page number
     * @return list of Orders dto by page
     */
    public Optional<List<OrderDto>> findByPage(String column, int page) {
        return orderDao.findByPageNumber(column,page)
                .map(bicycles -> bicycles.stream()
                        .map(this::convertToDto)
                        .filter(Objects::nonNull)
                        .collect(toList()));
    }

    /**
     * method to create new Order for specific User
     * @param login User name
     * @param model bicycle model name
     * @param hours count of order hours
     * @param date order date
     * @param place rent place
     * @return Order dto, if it has been created
     */
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

    /**
     * method to covert {@link Order} model to {@link OrderDto}
     * @param order {@link Order} object
     * @return converted {@link OrderDto} object
     */
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

    /**
     * method to find all Orders of specific User
     * @param login User name
     * @return previous User orders
     */
    public Optional<List<OrderDto>> findAllOrdersByUserName(String login) {
        Optional<List<Order>> orders = orderDao.findAllOrdersByUserName(login);
        return orders.map(orderList -> orderList.stream()
                .map(this::convertToDto)
                .collect(toList()));
    }

    /**
     * method to change order status value
     * @param id order number
     * @param status status value
     */
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
}
