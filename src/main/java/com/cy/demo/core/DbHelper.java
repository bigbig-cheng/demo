package com.cy.demo.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by jiangyi on 2018/7/23.
 */
public class DbHelper implements AutoCloseable{

    private final static String DRIVER_NAME = "org.sqlite.JDBC";
    private final static String DRIVER_URL = "jdbc:sqlite:db/demo.db";

    private final static ThreadLocal<Connection> connectionHolder = new ThreadLocal(){
        @Override
        protected Connection initialValue() {
            try {
                Class.forName(DRIVER_NAME);
                return DriverManager.getConnection(DRIVER_URL);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    public static Connection getDbConnection() throws Exception {
        return connectionHolder.get();
    }

    @Override
    public void close() throws Exception {
        Connection conn = connectionHolder.get();
        if(conn != null){
            conn.close();
        }
    }

    public static void commit() throws SQLException {
        Connection conn = connectionHolder.get();
        if(conn != null){
            conn.commit();
        }
    }

    public static void rollback() throws SQLException {
        Connection conn = connectionHolder.get();
        if(conn != null) {
            conn.rollback();
        }
    }

}
