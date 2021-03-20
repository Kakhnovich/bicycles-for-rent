package com.epam.jwd.rent.service;

import com.epam.jwd.rent.dao.impl.OrderDao;
import com.epam.jwd.rent.dao.impl.UserDao;
import com.epam.jwd.rent.model.Order;
import com.epam.jwd.rent.model.User;
import com.epam.jwd.rent.model.UserDto;
import com.epam.jwd.rent.model.UserFactory;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class UserService implements CommonService<UserDto> {
    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    @Override
    public Optional<List<UserDto>> findAll() {
        return userDao.findAll()
                .map(bicycles -> bicycles.stream()
                        .filter(user -> !user.isBanned())
                        .map(this::convertToDto)
                        .collect(toList()));
    }

    public Optional<UserDto> findByLogin(String login) {
        Optional<User> user = userDao.findByLogin(login);
        return user.map(this::convertToDto);
    }

    public Optional<UserDto> login(String login, String password) {
        if (checkInputData(login) || checkInputData(password)) {
            return Optional.empty();
        }
        final Optional<User> user = userDao.findByLogin(login);
        if (!user.isPresent()) {
            return Optional.empty();
        }
        final String realPassword = user.get().getPassword();
        if (BCrypt.checkpw(password, realPassword)) {
            return user.map(this::convertToDto);
        } else {
            return Optional.empty();
        }
    }

    public Optional<UserDto> signUp(String login, String password) {
        final Optional<User> allUsers = userDao.findByLogin(login);
        if (login.equals("null") || password.equals("null") || allUsers.isPresent()) {
            return Optional.empty();
        }
        if (checkInputData(login) || checkInputData(password)) {
            return Optional.empty();
        }
        User user = UserFactory.getInstance().create(login, BCrypt.hashpw(password, BCrypt.gensalt()));
        Optional<User> newUser = userDao.create(user);
        if (newUser.isPresent()) {
            return Optional.of(convertToDto(user));
        } else {
            return Optional.empty();
        }
    }

    private boolean checkInputData(String input) {
        final String INCORRECT_DATA_REGEX = "\\W+";
        Pattern p = Pattern.compile(INCORRECT_DATA_REGEX);
        Matcher loginMatcher = p.matcher(input);
        return loginMatcher.find();
    }

    private static HashMap<String, Integer> sortByValues(HashMap<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
        list.sort((Comparator) (o1, o2) -> ((Comparable) ((Map.Entry) (o2)).getValue())
                .compareTo(((Map.Entry) (o1)).getValue()));
        HashMap<String, Integer> sortedHashMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }


    private UserDto convertToDto(User user) {
        return new UserDto(userDao.findPersonStatus(user.getRoleId()),
                user.getLogin(),
                user.getBalance());
    }

    public Map<String, Integer> hoursRating() {
        Optional<List<UserDto>> users = findAll();
        HashMap<String, Integer> sortedMap = new HashMap<>();
        if (users.isPresent()) {
            HashMap<String, Integer> map = new HashMap<>();
            List<UserDto> usersDto = users.get();
            for (UserDto userDto : usersDto) {
                Optional<List<Order>> orders = new OrderDao().findAllOrdersByUserName(userDto.getLogin());
                int hours = 0;
                if (orders.isPresent()) {
                    for (Order order : orders.get().stream()
                            .filter(order -> order.getStatus().equals("accepted"))
                            .collect(Collectors.toList())) {
                        hours += order.getHours();
                    }
                }
                map.put(userDto.getLogin(), hours);
            }
            sortedMap = sortByValues(map);
        }
        return sortedMap;
    }
}
