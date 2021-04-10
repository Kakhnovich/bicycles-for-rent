package com.epam.jwd.rent.dao.impl;

import com.epam.jwd.rent.dao.CommonDao;
import com.epam.jwd.rent.model.Bicycle;
import com.epam.jwd.rent.model.BicycleFactory;
import com.epam.jwd.rent.model.Place;
import com.epam.jwd.rent.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Bicycle data access class
 *
 * @author Elmax19
 * @version 1.0
 */
public class BicycleDao implements CommonDao<Bicycle> {
    /**
     * variables of SQL code to work with database
     */
    private static final String FIND_ALL_BICYCLES_SQL = "select bicycles.id, model, price, address, count " +
            "from bicycles join bicycle_models bm on bicycles.model_id = bm.id " +
            "join rent_places rp on bicycles.place_id = rp.id";
    private static final String FIND_BICYCLES_BY_MODEL_AND_PLACE = "select bicycles.id, model, price, address, count " +
            "from bicycles join bicycle_models bm on bicycles.model_id = bm.id " +
            "join rent_places rp on bicycles.place_id = rp.id where ";
    private static final String FIND_BICYCLE_PLACES_SQL = "select address, opening_time, closing_time from rent_places";
    private static final String FIND_MODEL_ID_SQL = "select id from bicycle_models where model=";
    private static final String FIND_BICYCLE_BY_ID_SQL = "select bicycles.id, model, price, address, count " +
            "from bicycles join bicycle_models bm on bicycles.model_id = bm.id " +
            "join rent_places rp on bicycles.place_id = rp.id where bicycles.id=";
    private static final String SET_COUNT_SQL = "update bicycles set count=? where id=?";
    private static final String FIND_BICYCLES_FOR_PAGE_SQL = "select bicycles.id, model, price, address, count " +
            "from bicycles join bicycle_models bm on bicycles.model_id = bm.id " +
            "join rent_places rp on bicycles.place_id = rp.id order by ";
    private static final String GET_COUNT_OF_BICYCLES_SQL = "select count(id) AS count from bicycles";
    /**
     * variables of database columns names
     */
    private static final String ID_COLUMN_NAME = "id";
    private static final String COUNT_COLUMN_NAME = "count";
    private static final String MODEL_COLUMN_NAME = "model";
    private static final String OPENING_TIME_COLUMN_NAME = "opening_time";
    private static final String CLOSING_TIME_COLUMN_NAME = "closing_time";
    private static final String ADDRESS_COLUMN_NAME = "address";
    /**
     * link at {@link ConnectionPool} to get connections
     */
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    /**
     * {@link BicycleDao} class {@link Logger}
     */
    private static final Logger LOGGER = LogManager.getLogger(BicycleDao.class);

    /**
     * method that sets {@link Bicycle#bicycleCount} as count of bicycles in database
     */
    public void initCountOfBicycles() {
        Optional<List<Bicycle>> bicycles = findAll();
        if (bicycles.isPresent()) {
            if (bicycles.get().size() > 0) {
                Bicycle.bicycleCount = bicycles.map(bicycleList -> bicycleList.get(bicycleList.size() - 1).getId()).orElse(0);
            }
        }
    }

    /**
     * {@link CommonDao} method realisation
     * @return list of all bicycles
     * @see CommonDao
     */
    @Override
    public Optional<List<Bicycle>> findAll() {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_ALL_BICYCLES_SQL)) {
            List<Bicycle> bicycles = new ArrayList<>();
            while (resultSet.next()) {
                bicycles.add(readBicycle(resultSet));
            }
            return Optional.of(bicycles);
        } catch (SQLException e) {
            LOGGER.error("SQLException at method findAll: " + e.getSQLState());
            return Optional.empty();
        }
    }

    /**
     * method to crate {@link Bicycle} object from ResultSet
     * @param resultSet result of database operation
     * @return {@link BicycleFactory} creation method result
     */
    private Bicycle readBicycle(ResultSet resultSet) throws SQLException {
        return BicycleFactory.getInstance().create(resultSet);
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
             final ResultSet resultSet = statement.executeQuery(GET_COUNT_OF_BICYCLES_SQL)) {
            if (resultSet.next()) {
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
     * @return list of sought Bicycles
     * @see CommonDao
     */
    @Override
    public Optional<List<Bicycle>> findByPageNumber(String column, int n) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BICYCLES_FOR_PAGE_SQL + column + " LIMIT " + 5 * (n - 1) + ',' + 5)) {
            List<Bicycle> bicycles = new ArrayList<>();
            while (resultSet.next()) {
                bicycles.add(readBicycle(resultSet));
            }
            return Optional.of(bicycles);
        } catch (SQLException e) {
            LOGGER.error("SQLException at method findAll: " + e.getSQLState());
            return Optional.empty();
        }
    }

    /**
     * {@link CommonDao} method realisation
     * @param id place in database
     * @return Bicycle if it exists, otherwise return empty optional
     * @see CommonDao
     */
    @Override
    public Optional<Bicycle> findById(int id) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BICYCLE_BY_ID_SQL + id)) {
            if (resultSet.next()) {
                return Optional.of(readBicycle(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException at method findById: " + e.getSQLState());
        }
        return Optional.empty();
    }

    /**
     * method to get all rent places in database
     * @return list of Places
     */
    public Optional<List<Place>> findAllPlaces() {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BICYCLE_PLACES_SQL)) {
            List<Place> places = new ArrayList<>();
            while (resultSet.next()) {
                places.add(new Place(
                        resultSet.getString(ADDRESS_COLUMN_NAME),
                        resultSet.getTime(OPENING_TIME_COLUMN_NAME).toLocalTime(),
                        resultSet.getTime(CLOSING_TIME_COLUMN_NAME).toLocalTime()));
            }
            if (places.size() == 0) {
                return Optional.empty();
            }
            return Optional.of(places);
        } catch (SQLException e) {
            LOGGER.error("SQLException at method findAllPlaces: " + e.getSQLState());
            return Optional.empty();
        }
    }

    /**
     * method to get model id in database
     * @param model model name
     * @return sought model id
     */
    public int findModelId(String model){
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_MODEL_ID_SQL + "'" + model + "'")) {
            if (resultSet.next()) {
                return resultSet.getInt(ID_COLUMN_NAME);
            }
            return 0;
        } catch (SQLException e) {
            LOGGER.error("SQLException at method findAllPlaces: " + e.getSQLState());
            return 0;
        }
    }

    /**
     * method to change count of bicycles at rent places
     * @param id number of some bicycle by model & place
     * @param count count of bicycles
     * @return has been method worked without eny exceptions
     */
    public boolean changeCountOfBicycles(int id, int count) {
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(SET_COUNT_SQL)) {
            preparedStatement.setInt(1, count);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("SQLException at method changeCountOfBicycles: " + e.getSQLState());
            return false;
        }
        return true;
    }

    /**
     * method to find bicycle by its model and place
     * @param model bicycle model name
     * @param place rent place of this bicycle
     * @return Bicycle if it exists, otherwise return empty optional
     */
    public Optional<Bicycle> findBicyclesBuModelAndPlace(String model, String place) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BICYCLES_BY_MODEL_AND_PLACE +
                     MODEL_COLUMN_NAME + "='" + model + "' and " + ADDRESS_COLUMN_NAME + "='" + place + "'")) {
            Bicycle bicycle;
            if (resultSet.next()) {
                bicycle = readBicycle(resultSet);
                return Optional.of(bicycle);
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException at method findBicyclesBuModelAndPlace: " + e.getSQLState());
        }
        return Optional.empty();
    }
}
