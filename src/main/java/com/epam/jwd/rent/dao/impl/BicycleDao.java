package com.epam.jwd.rent.dao.impl;

import com.epam.jwd.rent.dao.CommonDao;
import com.epam.jwd.rent.model.Bicycle;
import com.epam.jwd.rent.model.BicycleFactory;
import com.epam.jwd.rent.model.Order;
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

public class BicycleDao implements CommonDao<Bicycle> {
    private static final String FIND_ALL_BICYCLES_SQL = "select bicycles.id, model, price, address, count " +
            "from bicycles join bicycle_models bm on bicycles.model_id = bm.id " +
            "join rent_places rp on bicycles.place_id = rp.id";
    private static final String FIND_BICYCLE_BY_MODEL_SQL = "select bicycles.id, model, price, address, count " +
            "from bicycles join bicycle_models bm on bicycles.model_id = bm.id " +
            "join rent_places rp on bicycles.place_id = rp.id where model =";
    private static final String FIND_BICYCLES_BY_MODEL_AND_PLACE = "select bicycles.id, model, price, address, count " +
            "from bicycles join bicycle_models bm on bicycles.model_id = bm.id " +
            "join rent_places rp on bicycles.place_id = rp.id where ";
    private static final String FIND_BICYCLE_PLACES_SQL = "select address from rent_places";
    private static final String FIND_BICYCLE_BY_ID_SQL = "select bicycles.id, model, price, address, count " +
            "from bicycles join bicycle_models bm on bicycles.model_id = bm.id " +
            "join rent_places rp on bicycles.place_id = rp.id where bicycles.id=";
    private static final String SET_COUNT_SQL = "update bicycles set count=? where id=?";
    private static final String FIND_BICYCLES_FOR_PAGE_SQL = "select bicycles.id, model, price, address, count " +
            "from bicycles join bicycle_models bm on bicycles.model_id = bm.id " +
            "join rent_places rp on bicycles.place_id = rp.id order by ";
    private static final String GET_COUNT_OF_BICYCLES_SQL = "select count(id) AS count from bicycles";
    private static final String COUNT_COLUMN_NAME = "count";
    private static final String MODEL_COLUMN_NAME = "model";
    private static final String ADDRESS_COLUMN_NAME = "address";
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(BicycleDao.class);

    public void initCountOfBicycles() {
        Optional<List<Bicycle>> bicycles = findAll();
        if (bicycles.isPresent()) {
            if (bicycles.get().size() > 0) {
                Bicycle.bicycleCount = bicycles.map(bicycleList -> bicycleList.get(bicycleList.size() - 1).getId()).orElse(0);
            }
        }
    }

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

    @Override
    public Optional<Bicycle> create(Bicycle entity) {
        return Optional.empty();
    }

    private Bicycle readBicycle(ResultSet resultSet) throws SQLException {
        return BicycleFactory.getInstance().create(resultSet);
    }

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

    @Override
    public Optional<List<Bicycle>> findByPageNumber(String column, int n) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BICYCLES_FOR_PAGE_SQL + column + " LIMIT " + 3 * (n - 1) + ',' + 3)) {
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

    public Optional<List<String>> findAllPlaces() {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BICYCLE_PLACES_SQL)) {
            List<String> places = new ArrayList<>();
            while (resultSet.next()) {
                places.add(resultSet.getString(ADDRESS_COLUMN_NAME));
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

    public void changeCountOfBicycles(int id, int count) {
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(SET_COUNT_SQL)) {
            preparedStatement.setInt(1, count);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("SQLException at method changeCountOfBicycles: " + e.getSQLState());
        }
    }

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
