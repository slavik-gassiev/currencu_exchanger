package com.slava.learn.currency_exchanger;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        final String query = "SELECT * FROM Currencies WHERE code = ?";

        try (Connection connection = ConnectionDB.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {


            statement.setString(1, "USD");
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                System.out.println(
                        resultSet.getLong("id") +", " +
                                resultSet.getString("code") + ", " +
                                resultSet.getString("full_name") + ", " +
                                resultSet.getString("sign")
                );
            }
        } catch (SQLException e) {

        }




    }
}
