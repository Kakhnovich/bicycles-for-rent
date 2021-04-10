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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User data access class
 *
 * @author Elmax19
 * @version 1.0
 */
public class UserDao implements CommonDao<User> {
    /**
     * variables of SQL code to work with database
     */
    private static final String FIND_ALL_PERSONS_SQL = "select * from persons";
    private static final String FIND_PERSON_BY_LOGIN_SQL = "select * from persons where login=";
    private static final String FIND_PERSON_BY_ID_SQL = "select * from persons where id=";
    private static final String FIND_PERSON_STATUS_SQL = "select * from person_status where id=";
    private static final String CREATE_NEW_PERSON_SQL = "insert into persons value(?,?,?,?,?,?)";
    private static final String SET_BALANCE_SQL = "update persons set balance=? where login=?";
    private static final String BANE_USER_SQL = "update persons set banned=true where login=?";
    private static final String PROMOTE_USER_SQL = "update persons set status_id=1 where login=?";
    private static final String GET_RATING_BY_ORDERS_SQL
            = "select person_id, count(id) as count from orders GROUP BY person_id order by count desc LIMIT 3";
    private static final String GET_RATING_BY_HOURS_SQL
            = "select person_id, sum(hours) as count from orders GROUP BY person_id order by count desc LIMIT 3";
    private static final String FIND_USERS_FOR_PAGE_SQL = "select * from persons order by ";
    private static final String GET_COUNT_OF_USERS_SQL = "select count(id) AS count from persons";
    /**
     * variables of database columns names
     */
    private static final String COUNT_COLUMN_NAME = "count";
    private static final String PERSON_STATUS_COLUMN_NAME = "type";
    private static final String PERSON_ID_COLUMN_NAME = "person_id";
    /**
     * link at {@link ConnectionPool} to get connections
     */
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    /**
     * {@link UserDao} class {@link Logger}
     */
    private static final Logger LOGGER = LogManager.getLogger(UserDao.class);
    /**
     * {@link ReentrantLock} to lock methods
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * method that sets {@link User#count} as count of users in database
     */
    public void initCountOfUsers() {
        Optional<List<User>> users = findAll();
        if (users.isPresent()) {
            if (users.get().size() > 0) {
                User.count = users.map(userList -> userList.get(userList.size() - 1).getId()).orElse(0);
            }
        }
    }

    /**
     * {@link CommonDao} method realisation
     * @return list of all users
     * @see CommonDao
     */
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

    /**
     * method to crate {@link User} object from ResultSet
     * @param resultSet result of database operation
     * @return {@link UserFactory} creation method result
     */
    private User readPerson(ResultSet resultSet) throws SQLException {
        return UserFactory.getInstance().create(resultSet);
    }

    /**
     * method to create new User
     * @param user new User
     * @return User if hi/she has been created, otherwise return empty optional
     */
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

    /**
     * {@link CommonDao} method realisation
     * @param n count of raws
     * @return count of pages
     * @see CommonDao
     */
    @Override
    public int getCountOfPages(int n) {
        try (final Connection con = POOL.retrieveConnection();
             final Statement statement = con.createStatement();
             final ResultSet resultSet = statement.executeQuery(GET_COUNT_OF_USERS_SQL)) {
            if(resultSet.next()) {
                return (resultSet.getInt(COUNT_COLUMN_NAME) + n - 1) / n;
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException at method getCount: " + e.getSQLState());
        }
        return 0;
    }

    /**
     * {@link CommonDao} method realisation
     * @param column column name of sorting
     * @param n page number
     * @return list of sought Users
     * @see CommonDao
     */
    @Override
    public Optional<List<User>> findByPageNumber(String column, int n) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_USERS_FOR_PAGE_SQL + column + " LIMIT " + 5 * (n - 1) + ',' + 5)) {
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(readPerson(resultSet));
            }
            return Optional.of(users);
        } catch (SQLException e) {
            LOGGER.error("SQLException at method findAll: " + e.getSQLState());
            return Optional.empty();
        }
    }

    /**
     * method that return User by login in database
     * @param login User name
     * @return sought User if he/she exists, otherwise return empty optional
     */
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

    /**
     * method to find person role from database
     * @param id status number
     * @return User | Admin
     */
    public String findPersonStatus(int id) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_PERSON_STATUS_SQL + id)) {
            if (resultSet.next()) {
                return resultSet.getString(PERSON_STATUS_COLUMN_NAME);
            }
            return "user";
        } catch (SQLException e) {
            LOGGER.error("SQLException at method findPersonStatus: " + e.getSQLState());
            return "user";
        }
    }

    /**
     * method to replenish or reduce Person balance
     * @param login Person name in database
     * @param money amount of money
     */
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

    /**
     * {@link CommonDao} method realisation
     * @param id place in database
     * @return User if he/she exists, otherwise return empty optional
     * @see CommonDao
     */
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

    /**
     * method to set banned status for User
     * @param login User name
     */
    public void banUser(String login) {
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(BANE_USER_SQL)) {
            preparedStatement.setString(1, login);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("SQLException at method banUser: " + e.getSQLState());
        }
    }

    /**
     * method to promote User to Admin
     * @param login User name
     */
    public void promoteUser(String login) {
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(PROMOTE_USER_SQL)) {
            preparedStatement.setString(1, login);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("SQLException at method findById: " + e.getSQLState());
        }
    }

    /**
     * method to return one of ratings of users
     * @param criteria criteria of rating
     * @return Map of top 3 Users
     */
    public HashMap<String, Integer> getRating(String criteria) {
        HashMap<String, Integer> rezMap = new HashMap<>();
        final String QUERY = "hours".equals(criteria) ? GET_RATING_BY_HOURS_SQL : GET_RATING_BY_ORDERS_SQL;
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(QUERY)) {
            while (resultSet.next()) {
                rezMap.put(findById(resultSet.getInt(PERSON_ID_COLUMN_NAME)).get().getLogin(), resultSet.getInt(COUNT_COLUMN_NAME));
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException at method getRatingByOrdersCount: " + e.getSQLState());
        }
        return rezMap;
    }
}
