package com.slava.learn.currency_exchanger.DAO;

import com.slava.learn.currency_exchanger.ConnectionDB;
import com.slava.learn.currency_exchanger.DTO.CurrencyDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JdbcCurrencyDAO implements CurrencyDAO{

    @Override
    public Optional<CurrencyDTO> findById(Long id) {
        final String query = "SELECT * FROM Currencies WHERE id = ?";

        try(Connection connection = ConnectionDB.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return Optional.of(getCurrencyDTO(resultSet));
            }

        } catch (SQLException e) {

        }
        return Optional.empty();
    }

    @Override
    public List<CurrencyDTO> findAll() {
        final String query = "SELECT * FROM Currencies";

        try(Connection connection = ConnectionDB.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<CurrencyDTO> currencies = new ArrayList<>();

            while(resultSet.next()) {
                currencies.add(getCurrencyDTO(resultSet));
            }

            return  currencies;

        } catch (SQLException e) {

        }
        return Collections.emptyList();
    }

    @Override
    public CurrencyDTO save(CurrencyDTO currency) {
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

            return getCurrencyDTO(resultSet);

        } catch (SQLException e) {

        }
        return null;
    }

    @Override
    public Optional<CurrencyDTO> update(CurrencyDTO currency) {
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
                return Optional.of(getCurrencyDTO(resultSet));
            }

        } catch (SQLException e) {

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

        }
    }

    @Override
    public Optional<CurrencyDTO> findByCode(String code) {
        final String query = "SELECT * FROM Currencies WHERE code = ?";

        try(Connection connection = ConnectionDB.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return Optional.of(getCurrencyDTO(resultSet));
            }

        } catch (SQLException e) {

        }
        return Optional.empty();
    }

    private CurrencyDTO getCurrencyDTO(ResultSet resultSet) {
        try {
            return new CurrencyDTO(
                    resultSet.getLong("id"),
                    resultSet.getString("code"),
                    resultSet.getString("full_name"),
                    resultSet.getString("sign");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
                
        );
    }
}
