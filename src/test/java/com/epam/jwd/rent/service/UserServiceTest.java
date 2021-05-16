package com.epam.jwd.rent.service;

import com.epam.jwd.rent.dao.impl.UserDao;
import com.epam.jwd.rent.model.User;
import com.epam.jwd.rent.model.UserDto;
import com.epam.jwd.rent.model.UserFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;

public class UserServiceTest {
    List<User> users;
    List<UserDto> usersDto;
    HashMap<String, Integer> rating = new HashMap<>();
    HashMap<String, Integer> sortedRating = new HashMap<>();
    UserService userService;
    @Mock
    UserDao userDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService();
        userService.setUserDao(userDao);
        users = new ArrayList<>(Arrays.asList(
                UserFactory.getInstance().create("login1", "$2a$10$ik.QK02rNSnMlsQKyVhjEeSKPy.GrIR04wANmteeBrvi5aRP1b88O"),
                UserFactory.getInstance().create("login3", "pass3"),
                UserFactory.getInstance().create("login2", "pass2")));
        usersDto = new ArrayList<>(Arrays.asList(
                new UserDto("user", "login1", BigDecimal.ZERO),
                new UserDto("user", "login3", BigDecimal.ZERO),
                new UserDto("user", "login2", BigDecimal.ZERO)
        ));
        rating.put("login2", 200);
        rating.put("login1", 100);
        rating.put("login3", 300);
        sortedRating.put("login3", 300);
        sortedRating.put("login2", 200);
        sortedRating.put("login1", 100);
    }

    @Test
    public void findAll_ShouldConvertToDtoAllUsers() {
        when(userDao.findAll()).thenReturn(Optional.of(users));
        when(userDao.findPersonStatus(2)).thenReturn("user");
        Optional<List<UserDto>> actual = userService.findAll();
        assertTrue(actual.isPresent());
        for (int i = 0; i < usersDto.size(); i++) {
            assertEquals((i + 1) + " element should be correct", usersDto.get(i), actual.get().get(i));
        }
    }

    @Test
    public void findByLogin_ShouldFindCorrectly_WhenUserExists() {
        when(userDao.findByLogin("login1")).thenReturn(Optional.of(users.get(0)));
        when(userDao.findPersonStatus(2)).thenReturn("user");
        Optional<UserDto> actual = userService.findByLogin("login1");
        assertTrue(actual.isPresent() && actual.get().equals(usersDto.get(0)));
    }

    @Test
    public void findByLogin_ShouldByEmpty_WhenDataIsInvalid() {
        when(userDao.findByLogin("login")).thenReturn(Optional.empty());
        Optional<UserDto> actual = userService.findByLogin("login");
        assertFalse(actual.isPresent());
    }

    @Test
    public void login_ShouldBeEmpty_WhenDataIsInvalid() {
        assertFalse(userService.login("ada d 'a", "`pass/").isPresent());
        verify(userDao, times(0)).findByLogin(anyString());
    }

    @Test
    public void login_ShouldBeEmpty_WhenUserDoesNotExist() {
        when(userDao.findByLogin("login")).thenReturn(Optional.empty());
        assertFalse(userService.login("login", "password").isPresent());
        verify(userDao, times(1)).findByLogin("login");
    }

    @Test
    public void login_ShouldBeEmpty_WhenPasswordIsIncorrect() {
        when(userDao.findByLogin("login1")).thenReturn(Optional.of(users.get(0)));
        assertFalse(userService.login("login1", "incorrect").isPresent());
    }

    @Test
    public void login_ShouldReturnDto_WhenAllIsCorrect() {
        when(userDao.findByLogin("login1")).thenReturn(Optional.of(users.get(0)));
        when(userDao.findPersonStatus(2)).thenReturn("user");
        Optional<UserDto> actual = userService.login("login1", "test");
        assertTrue(actual.isPresent());
        assertEquals(usersDto.get(0), actual.get());
    }

    @Test
    public void getRating_ShouldReturnCorrectRating() {
        when(userDao.getRating(anyString())).thenReturn(rating);
        HashMap<String, Integer> actual = userService.getRating("hours");
        assertEquals(sortedRating, actual);
    }

    @Test
    public void signUp_ShouldByEmpty_WhenUserAlreadyExists() {
        when(userDao.findByLogin("login1")).thenReturn(Optional.of(users.get(0)));
        assertFalse(userService.signUp("login1", "password").isPresent());
        verify(userDao, times(0)).create(any());
    }

    @Test
    public void signUp_ShouldBeEmpty_WhenDataIsInvalid() {
        when(userDao.findByLogin("ada d 'a")).thenReturn(Optional.empty());
        assertFalse(userService.signUp("ada d 'a", "`pass/").isPresent());
        verify(userDao, times(0)).create(any());
    }

    @Test
    public void signUp_ShouldBeEmpty_WhenSqlDidNotWorkCorrect() {
        when(userDao.findByLogin("login1")).thenReturn(Optional.empty());
        when(userDao.create(any())).thenReturn(Optional.empty());
        assertFalse(userService.signUp("login1", "test").isPresent());
        verify(userDao, times(1)).create(any());
    }

    @Test
    public void signUp_ShouldReturnDto_WhenAllIsCorrect() {
        when(userDao.findByLogin("login1")).thenReturn(Optional.empty());
        when(userDao.create(any())).thenReturn(Optional.of(users.get(0)));
        when(userDao.findPersonStatus(2)).thenReturn("user");
        Optional<UserDto> actual = userService.signUp("login1", "test");
        assertTrue(actual.isPresent());
        assertEquals(usersDto.get(0), actual.get());
        verify(userDao, times(1)).create(any());
    }
}