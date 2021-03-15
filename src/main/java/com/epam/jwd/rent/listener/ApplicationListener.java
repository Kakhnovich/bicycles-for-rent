package com.epam.jwd.rent.listener;

import com.epam.jwd.rent.dao.impl.BicycleDao;
import com.epam.jwd.rent.dao.impl.OrderDao;
import com.epam.jwd.rent.dao.impl.UserDao;
import com.epam.jwd.rent.pool.ConnectionPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConnectionPool.getInstance().init();
        initCountOfEntities();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.getInstance().destroy();
    }

    private void initCountOfEntities() {
        new UserDao().initCountOfUsers();
        new OrderDao().initCountOfOrders();
        new BicycleDao().initCountOfBicycles();
    }
}
