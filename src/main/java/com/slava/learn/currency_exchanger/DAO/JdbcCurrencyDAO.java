package com.slava.learn.currency_exchanger.DAO;

import com.slava.learn.currency_exchanger.ConnectionDB;
import com.slava.learn.currency_exchanger.exeptions.DatabaseOperationException;
import com.slava.learn.currency_exchanger.entity.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCurrencyDAO implements CurrencyDAO{

    @Override
    public Optional<Currency> findById(Long id) {
        final String query = "SELECT * FROM Currencies WHERE id = ?";

        try(Connection connection = ConnectionDB.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return Optional.of(getCurrency(resultSet));
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to read currency with id '" + id + "' from the database");
        }
        return Optional.empty();
    }

    @Override
    public List<Currency> findAll() {
        final String query = "SELECT * FROM Currencies";

        try(Connection connection = ConnectionDB.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Currency> currencies = new ArrayList<>();

            while(resultSet.next()) {
                currencies.add(getCurrency(resultSet));
            }

            return currencies;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to read currencies from database");
        }
    }

    @Override
    public Currency save(Currency currency) {
        final String query = "INSERT INTO Currencies (code, full_name, sign) VALUES (?, ?, ?)";

        try(Connection connection = ConnectionDB.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFull_name());
            preparedStatement.setString(3, currency.getSign());

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
               throw new SQLException();
            }

            return getCurrency(resultSet);

        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    "Failed to save currency with code '" + currency.getCode() + "' to the database");
        }
    }

    @Override
    public Optional<Currency> update(Currency currency) {
        final String query = "UPDATE  Currencies SET (code, full_name, sign) = (?, ?, ?)" +
                "WHERE id = ?";

        try(Connection connection = ConnectionDB.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFull_name());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.setLong(4, currency.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return Optional.of(getCurrency(resultSet));
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    "Failed to update currency with id '" + currency.getId() + "' in the database"
            );
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        final String query = "DELETE FROM Currencies WHERE id = ?";

        try(Connection connection = ConnectionDB.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    "Failed to delete currency with id '" + id + "' in the database"
            );
        }
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        final String query = "SELECT * FROM Currencies WHERE code = ?";

        try(Connection connection = ConnectionDB.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return Optional.of(getCurrency(resultSet));
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    "Failed to read currency with code '" + code + "' in the database"
            );
        }
        return Optional.empty();
    }

    private Currency getCurrency(ResultSet resultSet) {
        try {
            return new Currency(
                    resultSet.getLong("id"),
                    resultSet.getString("code"),
                    resultSet.getString("full_name"),
                    resultSet.getString("sign")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
