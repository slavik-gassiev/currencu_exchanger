package com.slava.learn.currency_exchanger;

import com.slava.learn.currency_exchanger.DAO.JdbcCurrencyDAO;
import com.slava.learn.currency_exchanger.entity.Currency;
import com.slava.learn.currency_exchanger.exeptions.DatabaseOperationException;

import java.sql.*;
import java.util.Optional;


public class Main {

    private static JdbcCurrencyDAO jdbcCurrencyDAO = new JdbcCurrencyDAO();
    public static void main(String[] args) {

        jdbcCurrencyDAO.findByCode("USD");
//        final String query = "SELECT * FROM Currencies WHERE code = ?";
//
//        try (Connection connection = ConnectionDB.getConnection();
//            PreparedStatement statement = connection.prepareStatement(query)) {
//
//
//            statement.setString(1, "USD");
//            ResultSet resultSet = statement.executeQuery();
//
//            if(resultSet.next()) {
//                System.out.println(
//                        resultSet.getLong("id") +", " +
//                                resultSet.getString("code") + ", " +
//                                resultSet.getString("full_name") + ", " +
//                                resultSet.getString("sign")
//                );
//            }
//        } catch (SQLException e) {
//
//        }


    }
}

