package com.slava.learn.currency_exchanger.DAO;

import com.slava.learn.currency_exchanger.ConnectionDB;
import com.slava.learn.currency_exchanger.entity.Currency;
import com.slava.learn.currency_exchanger.entity.ExchangeCurrency;
import com.slava.learn.currency_exchanger.exeptions.DatabaseOperationException;
import com.slava.learn.currency_exchanger.exeptions.EntityExistsException;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcExchangeCurrencyDAO implements ExchangeCurrencyDAO{
    @Override
    public Optional<ExchangeCurrency> findById(Long id) {
        final String query =
                "  SELECT" +
                        "    er.id AS id," +
                        "    bc.id AS base_id," +
                        "    bc.code AS base_code," +
                        "    bc.full_name AS base_name," +
                        "    bc.sign AS base_sign," +
                        "    tc.id AS target_id," +
                        "    tc.code AS target_code," +
                        "    tc.full_name AS target_name," +
                        "    tc.sign AS target_sign," +
                        "    er.rate AS rate" +
                        "  FROM ExchangeRates er" +
                        "  JOIN Currencies bc ON er.base_currency_id = bc.id" +
                        "  JOIN Currencies tc ON er.target_currency_id = tc.id" +
                        "  WHERE er.id = ?";
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return Optional.of(getExchangeCurrency(resultSet));
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    "Failed to read exchange rate with id '" + id + "' from the database"
            );
        }


        return Optional.empty();
    }

    @Override
    public List<ExchangeCurrency> findAll() {
        final String query =
                "  SELECT" +
                        "    er.id AS id," +
                        "    bc.id AS base_id," +
                        "    bc.code AS base_code," +
                        "    bc.full_name AS base_name," +
                        "    bc.sign AS base_sign," +
                        "    tc.id AS target_id," +
                        "    tc.code AS target_code," +
                        "    tc.full_name AS target_name," +
                        "    tc.sign AS target_sign," +
                        "    er.rate AS rate" +
                        "  FROM ExchangeRates er" +
                        "  JOIN Currencies bc ON er.base_currency_id = bc.id" +
                        "  JOIN Currencies tc ON er.target_currency_id = tc.id" +
                        "  ORDER BY er.id";
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            List<ExchangeCurrency> exchangeCurrencies = new ArrayList<>();
            while(resultSet.next()) {
                exchangeCurrencies.add(getExchangeCurrency(resultSet));
            }

            return exchangeCurrencies;
        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    "Failed to read exchange rates from the database"
            );
        }
    }

    @Override
    public ExchangeCurrency save(ExchangeCurrency exchangeRate) {
        final String query =
                "INSERT INTO ExchangeRates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)";
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             preparedStatement.setLong(1, exchangeRate.getBaseCurrency().getId());
             preparedStatement.setLong(2, exchangeRate.getTargetCurrency().getId());
             preparedStatement.setBigDecimal(3, exchangeRate.getRate());

             ResultSet resultSet = preparedStatement.executeQuery();

             if(!resultSet.next()) {
                 throw new DatabaseOperationException(
                         String.format("Filed to seve exchange rate '%s' to '%s' to the database",
                                 exchangeRate.getBaseCurrency().getCode(),
                                 exchangeRate.getTargetCurrency().getCode())
                 );
             }
            exchangeRate.setId(resultSet.getLong("id"));
            return exchangeRate;

        } catch (SQLException e) {
            if (e instanceof SQLiteException) {
                SQLiteException exception = (SQLiteException) e;
                if(exception.getResultCode().code == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code) {
                    throw new EntityExistsException(
                            String.format("Exchange rate '%s' to '%s' already exists",
                                    exchangeRate.getBaseCurrency().getCode(),
                                    exchangeRate.getTargetCurrency().getCode())
                    );
                }
            }
            throw new DatabaseOperationException(
                    String.format("Failed to save exchange rate '%s' to '%s' to  the database",
                            exchangeRate.getBaseCurrency().getCode(),
                            exchangeRate.getTargetCurrency().getCode())
            );
        }
    }

    @Override
    public Optional<ExchangeCurrency> update(ExchangeCurrency exchangeRates) {
        final String query =
                "UPDATE ExchangeRates SET rate = ? " +
                        "WHERE base_currency_id = ? and  target_currency_id = ? RETURNING id";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBigDecimal(1, exchangeRates.getRate());
            preparedStatement.setLong(2, exchangeRates.getBaseCurrency().getId());
            preparedStatement.setLong(3, exchangeRates.getTargetCurrency().getId());


             ResultSet resultSet = preparedStatement.executeQuery();

             if(resultSet.next()) {
                 exchangeRates.setId(resultSet.getLong("id"));
                 return Optional.of(exchangeRates);
             }

        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    "Failed to update exchange rate with id '" + exchangeRates.getId() + "' in the database"
            );
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        final String query = "DELETE FROM ExchangeRates WHERE id = ?";
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             preparedStatement.setLong(1, id);
             preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    "Failed delete exchange rate with id '" + id + "' in the database"
            );
        }
    }

    @Override
    public Optional<ExchangeCurrency> findByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        final String query = "  SELECT" +
                "    er.id AS id," +
                "    bc.id AS base_id," +
                "    bc.code AS base_code," +
                "    bc.full_name AS base_name," +
                "    bc.sign AS base_sign," +
                "    tc.id AS target_id," +
                "    tc.code AS target_code," +
                "    tc.full_name AS target_name," +
                "    tc.sign AS target_sign," +
                "    er.rate AS rate" +
                "  FROM ExchangeRates er" +
                "  JOIN Currencies bc ON er.base_currency_id = bc.id" +
                "  JOIN Currencies tc ON er.target_currency_id = tc.id" +
                "  WHERE (" +
                "    er.base_currency_id = (SELECT c.id FROM Currencies c WHERE c.code = ?) AND" +
                "    er.target_currency_id = (SELECT c2.id FROM Currencies c2 WHERE c2.code = ?)" +
                "  )";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, baseCurrencyCode);
            preparedStatement.setString(2, targetCurrencyCode);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(getExchangeCurrency(resultSet));
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    String.format("Failed to delete exchange rate '%s' to '%s' from the database",
                            baseCurrencyCode, targetCurrencyCode)
            );
        }
        return Optional.empty();
    }

    private ExchangeCurrency getExchangeCurrency(ResultSet resultSet) {

        try {
            return new ExchangeCurrency(
                    resultSet.getLong("id"),
                    new Currency(
                            resultSet.getLong("base_id"),
                            resultSet.getString("base_code"),
                            resultSet.getString("base_name"),
                            resultSet.getString("base_sign")
                    ),
                    new Currency(
                            resultSet.getLong("target_id"),
                            resultSet.getString("target_code"),
                            resultSet.getString("target_name"),
                            resultSet.getString("target_sign")
                    ),
                    resultSet.getBigDecimal("rate")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
