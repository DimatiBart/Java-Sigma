package com.DimatiBart.sigma.server.model.DAO;

import com.DimatiBart.sigma.resources.dataPackages.UserData;

import java.io.*;
import java.util.Properties;
import java.sql.*;


/**
 * Created by Dimati_Bart on 28.11.15.
 */
public class Database implements Dao{
    private static Database ourInstance = new Database();
    private Connection connection;
    public static Statement DB_CONNECTION;
    public static Database getInstance() {
        return ourInstance;
    }


    private Database() {
        try {
            InputStream inputStream = new FileInputStream("./src/config.properties");
            Properties property = new Properties();
            property.load(inputStream);
            System.out.println("Расположение БД: " + property.getProperty("db.url"));
            connection = DriverManager.getConnection(property.getProperty("db.url"), property.getProperty("db.login"), property.getProperty("db.password"));
            inputStream.close();
            DB_CONNECTION = connection.createStatement();
            System.out.println("БД подключена успешна");
        } catch (SQLException except) {
            System.err.println("Ошибка соединения с БД");
        } catch (IOException except) {
            System.err.println("Ошибка открытия файла конфигурации БД");
        }
    }

    public void closeConnection () {
        try {
            connection.close();
        } catch (SQLException except) {
            except.printStackTrace();
        }
    }

    @Override
    public synchronized void Insert (String table, String fields, String values) {
        String query = "INSERT INTO " + table + "(" + fields +") VALUES (" + values + ")";
        try {
            DB_CONNECTION.executeUpdate(query);
        }
        catch (SQLException except) {
            System.err.println("Ошибка вставки данных");
            except.printStackTrace();
        }
    }

    @Override
    public synchronized void Delete(String table, String where) {
        String query = "DELETE FROM " + table + " WHERE " + where;
        try {
            DB_CONNECTION.executeUpdate(query);
        }
        catch (SQLException except) {
            System.err.println("Ошибка удаления данных" );
            except.printStackTrace();
        }
    }
    @Override
    public synchronized void Update(String table, String field, String value, String where) {
        String query = "UPDATE " + table + " SET " + field + " = " + value +
                " WHERE " + where;
        try {
            DB_CONNECTION.executeUpdate(query);
        }
        catch (SQLException except) {
            System.err.println("Ошибка редактирования данных" );
            except.printStackTrace();
        }
    }

    @Override
    public synchronized ResultSet Select (String table, String fields) {
        String query = "SELECT " + fields + " FROM " + table;
        try {
            return DB_CONNECTION.executeQuery(query);
        } catch ( SQLException except ) {
            except.printStackTrace();
        }
        return null;
    }

    public synchronized ResultSet ExecuteQuery(String query) {
        try {
            return DB_CONNECTION.executeQuery(query);
        } catch (SQLException except ) {
            except.printStackTrace();
        }
        return null;
    }

    public synchronized int ExecuteUpdate(String query) {
        try {
            return DB_CONNECTION.executeUpdate(query);
        } catch (SQLException except) {
            except.printStackTrace();
        }
        return -1;
    }

    public synchronized boolean authentication(UserData userData) {
        String query = "SELECT * FROM users" +
                " WHERE email = '" + userData.getEmail() +
                "' AND password = '" + userData.getPassword() + "'";
        try {
            ResultSet result = ExecuteQuery(query);
            if (result.next()) {
                userData.setId(result.getInt("user_id"));
                userData.setType(result.getBoolean("type"));
                userData.setCommand("SA");
                return true;
            }
            else {
                userData.setCommand("AF");
                return false;
            }
        } catch (SQLException except) {
            System.err.println("Ошибка SQL-запроса");
        }
        return false;
    }

    public synchronized boolean registration (UserData userData) {
        String query = "SELECT * FROM users" +
                " WHERE email = '" + userData.getEmail() +
                "' AND password = '" + userData.getPassword() + "'";
        try {
            ResultSet result = ExecuteQuery(query);
            if (result.next()) {
                userData.setCommand("RE");
                return false;
            }
            else {
                query =  "INSERT INTO users (email, password, type ) VALUES ('" + userData.getEmail() +
                        "', '" + userData.getPassword() +
                        "', '0')";
                ExecuteUpdate(query);
                query = "SELECT * FROM users" +
                        " WHERE email = '" + userData.getEmail() +
                        "' AND password = '" + userData.getPassword() + "'";
                result = ExecuteQuery(query);
                result.next();
                userData.setId(result.getInt("user_id"));
                userData.setType(result.getBoolean("type"));
                userData.setCommand("SR");
                return true;
            }
        }catch (SQLException except) {
            except.printStackTrace();
        }
        return false;
    }

    public synchronized ResultSet getUsers () {
        String query = "SELECT * FROM users";
        return ExecuteQuery(query);
    }
}
