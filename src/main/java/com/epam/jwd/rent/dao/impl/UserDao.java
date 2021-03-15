package com.epam.jwd.rent.dao.impl;

import com.epam.jwd.rent.model.User;
import com.epam.jwd.rent.model.UserFactory;
import com.epam.jwd.rent.pool.ConnectionPool;
import com.epam.jwd.rent.dao.CommonDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class UserDao implements CommonDao<User> {
    private static final String FIND_ALL_PERSONS_SQL = "select * from persons";
    private static final String FIND_PERSON_BY_LOGIN_SQL = "select * from persons where login=";
    private static final String FIND_PERSON_BY_ID_SQL = "select * from persons where id=";
    private static final String FIND_PERSON_STATUS_SQL = "select * from person_status where id=";
    private static final String CREATE_NEW_PERSON_SQL = "insert into persons value(?,?,?,?,?,?)";
    private static final String SET_BALANCE_SQL = "update persons set balance=? where login=?";
    private static final String BANE_USER_SQL = "update persons set banned=true where login=?";
    private static final String PROMOTE_USER_SQL = "update persons set status_id=1 where login=?";
    private static final String PERSON_STATUS_COLUMN_NAME = "type";
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(UserDao.class);
    private final ReentrantLock lock = new ReentrantLock();

    public void initCountOfUsers() {
        Optional<List<User>> users = findAll();
        if (users.isPresent()) {
            if (users.get().size() > 0) {
                User.count = users.map(userList -> userList.get(userList.size() - 1).getId()).orElse(0);
            }
        }
    }

    @Override
    public Optional<List<User>> findAll() {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_ALL_PERSONS_SQL)) {
            List<User> persons = new ArrayList<>();
            while (resultSet.next()) {
                persons.add(readPerson(resultSet));
            }
            return Optional.of(persons);
        } catch (SQLException e) {
            LOGGER.error("SQLException at method findAll: " + e.getSQLState());
            return Optional.empty();
        }
    }

    private User readPerson(ResultSet resultSet) throws SQLException {
        return UserFactory.getInstance().create(resultSet);
    }

    @Override
    public Optional<User> create(User user) {
        lock.lock();
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(CREATE_NEW_PERSON_SQL)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, user.getRoleId());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setBigDecimal(5, user.getBalance());
            preparedStatement.setBoolean(6, user.isBanned());
            preparedStatement.execute();
            return Optional.of(user);
        } catch (SQLException e) {
            LOGGER.error("SQLException at method create: " + e.getSQLState());
            return Optional.empty();
        } finally {
            lock.unlock();
        }
    }

    public Optional<User> findByLogin(String login) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_PERSON_BY_LOGIN_SQL + "'" + login + "'");
            if (resultSet.next()) {
                User user = readPerson(resultSet);
                if (!user.isBanned()) {
                    return Optional.of(user);
                }
                return Optional.empty();
            }
            return Optional.empty();
        } catch (SQLException e) {
            LOGGER.error("SQLException at method findByLogin: " + e.getSQLState());
            return Optional.empty();
        }
    }

    public String findPersonStatus(int id) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_PERSON_STATUS_SQL + id)) {
            if (resultSet.next()) {
                return resultSet.getString(PERSON_STATUS_COLUMN_NAME);
            }
            return "user";
            //todo remove hard code
        } catch (SQLException e) {
            LOGGER.error("SQLException at method findPersonStatus: " + e.getSQLState());
            return "user";
        }
    }

    public void changeBalance(String login, BigDecimal money) {
        Optional<User> user = findByLogin(login);
        if (user.isPresent()) {
            BigDecimal balance = user.get().getBalance();
            balance = balance.add(money);
            try (final Connection conn = POOL.retrieveConnection();
                 final PreparedStatement preparedStatement = conn.prepareStatement(SET_BALANCE_SQL)) {
                preparedStatement.setBigDecimal(1, balance);
                preparedStatement.setString(2, login);
                preparedStatement.execute();
            } catch (SQLException e) {
                LOGGER.error("SQLException at method changeBalance: " + e.getSQLState());
            }
        }
    }

    @Override
    public Optional<User> findById(int id) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_PERSON_BY_ID_SQL + id)) {
            if (resultSet.next()) {
                return Optional.of(readPerson(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException at method findById: " + e.getSQLState());
        }
        return Optional.empty();
    }

    public void banUser(String login) {
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(BANE_USER_SQL)) {
            preparedStatement.setString(1, login);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("SQLException at method banUser: " + e.getSQLState());
        }
    }

    public void promoteUser(String login) {
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(PROMOTE_USER_SQL)) {
            preparedStatement.setString(1, login);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("SQLException at method findById: " + e.getSQLState());
        }
    }
}
