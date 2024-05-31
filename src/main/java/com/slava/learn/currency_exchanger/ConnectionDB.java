package com.slava.learn.currency_exchanger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private Connection coonnection;

    public void ConnectDB() {
        String url = "jdbc:sqlite:mydb.db";
        try {
            coonnection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getCoonnection() {
        return coonnection;
    }

    public void closeConnection() {
        try {
            if(coonnection != null) {
                coonnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
