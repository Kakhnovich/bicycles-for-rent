package com.epam.jwd.rent.service;

import com.epam.jwd.rent.dao.impl.UserDao;
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

import static java.util.stream.Collectors.toList;

/**
 * Service class to work with Users
 *
 * @author Elmax19
 * @version 1.0
 */
public class UserService implements CommonService<UserDto> {
    /**
     * {@link UserDao} class to call
     */
    private final UserDao userDao;

    /**
     * default class constructor - init {@link UserService#userDao}
     */
    public UserService() {
        this.userDao = new UserDao();
    }

    /**
     * {@link CommonService} method realisation
     * @return list of all Users dto
     * @see CommonService
     */
    @Override
    public Optional<List<UserDto>> findAll() {
        return userDao.findAll()
                .map(bicycles -> bicycles.stream()
                        .filter(user -> !user.isBanned())
                        .map(this::convertToDto)
                        .collect(toList()));
    }

    /**
     * method to find User by his/her login
     * @param login User name
     * @return converted to dto {@link UserDao#findByLogin(String)} method result
     */
    public Optional<UserDto> findByLogin(String login) {
        Optional<User> user = userDao.findByLogin(login);
        return user.map(this::convertToDto);
    }

    /**
     * method that returns {@link UserDto} of User by login and password
     * @param login User name
     * @param password User account password
     * @return {@link UserDto} if User with input login and password exists
     */
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

    /**
     * method to sort result of {@link UserDao#getRating} method result
     * @param criteria rating criteria
     * @return sorted {@link HashMap}
     * @see UserService#sortByValues(HashMap)
     */
    public HashMap<String, Integer> getRating(String criteria){
        return sortByValues(userDao.getRating(criteria));
    }

    /**
     * method that return {@link UserDto} of User by login and password
     * @param login User name
     * @param password User account password
     * @return {@link UserDto} if User with input login and password has been created
     */
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

    /**
     * method to check invalid input data
     * @param input input value
     * @return is it incorrect
     */
    private boolean checkInputData(String input) {
        final String INCORRECT_DATA_REGEX = "\\W+";
        Pattern p = Pattern.compile(INCORRECT_DATA_REGEX);
        Matcher loginMatcher = p.matcher(input);
        return loginMatcher.find();
    }

    /**
     * method to sort {@link HashMap} by its values
     * @param map {@link HashMap} to sort
     * @return sorted {@link HashMap}
     */
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

    /**
     * method to covert {@link User} model to {@link UserDto}
     * @param user {@link User} object
     * @return converted {@link UserDto} object
     */
    private UserDto convertToDto(User user) {
        return new UserDto(userDao.findPersonStatus(user.getRoleId()),
                user.getLogin(),
                user.getBalance());
    }
}
