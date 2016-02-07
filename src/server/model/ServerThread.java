package com.DimatiBart.sigma.server.model;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.DimatiBart.sigma.resources.dataPackages.ProductData;
import com.DimatiBart.sigma.resources.dataPackages.UserData;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimati_Bart on 30.11.15.
 */
public class ServerThread implements Runnable{

    protected Socket clientSocket;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private boolean isClosed = false;
    private UserData userData;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());
            String command;
            ServerMain.clientAmount++;
            ServerMain.showClientAmount();
            while (!isClosed)
            {
                command = (String)input.readObject();
                switch (command) {
                    case "AUTH":
                        authentication();
                        break;
                    case "REG":
                        registration();
                        break;
                    case "GETUSERS":
                        selectUsers();
                        break;
                    case "UPDATEUSERS":
                        updateUsers();
                        break;
                    case "EXIT":
                        close();
                        break;
                    case "GETPRODDATA":
                        selectProductData();
                        break;
                    case "UPDATECOSTS":
                        updateCosts();
                        break;
                    case "LATESTOPER":
                        getLatestOperation();
                        break;
                    case "ADDPROD":
                        addNewProduct();
                        break;
                    case "ADDCOST":
                        addNewCost();
                        break;
                    case "GETPRODPRICE":
                        getProdPrice();
                        break;
                    case "DELETECOST":
                        deleteCost ();
                        break;
                    case "UPDATEPROD":
                        updateProduct();
                        break;
                    case "DELETEPROD":
                        deleteProduct();
                        break;
                    case "GETMAINPRODDATA":
                        selectMainProductData();
                        break;
                    case "EXCHANGE":
                        exchangeId();
                        break;
                }
            }
        }
        catch (IOException except) {
            except.printStackTrace();
        }
        catch (ClassNotFoundException except) {
            except.printStackTrace();
        }
    }

    private void authentication() {
        try {
            this.userData = (UserData)input.readObject();
            ServerMain.database.authentication(userData);
            output.writeObject(userData);
        } catch (IOException except) {
            except.printStackTrace();
        } catch (ClassNotFoundException except) {
            except.printStackTrace();
        }
    }

    private void registration() {
        try {
            this.userData = (UserData)input.readObject();
            ServerMain.database.registration(userData);
            output.writeObject(userData);
        } catch (IOException except) {
            except.printStackTrace();
        } catch (ClassNotFoundException except) {
            except.printStackTrace();
        }
    }

    private void close() {
        try {
            this.output.close();
            this.input.close();
            this.clientSocket.close();
            this.isClosed = true;
        } catch (IOException except) {
            except.printStackTrace();
        }
        ServerMain.clientAmount--;
        ServerMain.showClientAmount();
    }

    private void selectUsers() {
        ResultSet usersSet = ServerMain.database.Select("users", "*");
        List <UserData> data = new ArrayList();
        try {
            while(usersSet.next()){
                UserData tempData = new UserData();
                tempData.setEmail(usersSet.getString("email"));
                tempData.setId(usersSet.getInt("user_id"));
                tempData.setType(usersSet.getBoolean("type"));
                data.add(tempData);
            }
            output.writeObject(data);
        } catch (SQLException except) {
            except.printStackTrace();
        } catch (IOException except) {
            except.printStackTrace();
        }
    }

    private void updateUsers() {
        try {
            String[] data = (String[])input.readObject();
            ServerMain.database.Update(data[0], data[1], data[2], "user_id = " + data[3]);
        } catch (IOException| ClassNotFoundException except) {
            except.printStackTrace();
        }
    }
    private void selectProductData() {
        try {
            ResultSet result = ServerMain.database.Select("costs WHERE product_index = '"+userData.getCommand()+"'", "*");
            List <ProductData> costsData = new ArrayList();
            while (result.next()) {
                ProductData tempData = new ProductData();
                tempData.setID(result.getString("index"));
                tempData.setName(result.getString("cost_name"));
                tempData.setPrice(result.getFloat("price"));
                tempData.setK(result.getFloat("K"));
                tempData.setAmount(result.getFloat("amount"));
                tempData.setRecurrent(result.getBoolean("type"));
                costsData.add(tempData);
            }
            output.writeObject(costsData);
        } catch (IOException| SQLException except) {
            except.printStackTrace();
        }
    }

    private void updateCosts() {
        try {
            String[] data = (String[])input.readObject();
            ServerMain.database.Update("costs", data[0], data[1], "`index` = '" + data[2]+"'");
            reCalculateCost();
        } catch (IOException| ClassNotFoundException except) {
            except.printStackTrace();
        }
    }

    private void getLatestOperation() {
        try {
            ResultSet result = ServerMain.database.Select("products WHERE user_id = '"+userData.getId()+"' ORDER BY product_index DESC LIMIT 1", "*");
            result.next();
            output.writeObject(result.getInt("product_index"));
            userData.setCommand(Integer.toString(result.getInt("product_index")));
        } catch (IOException| SQLException except) {
            except.printStackTrace();
        }
    }

    private void addNewProduct() {
        try {
            String data = (String)input.readObject();
            ServerMain.database.Insert("products", "user_id, product_name, sum_cost", "'"+userData.getId()+"', '"+data+"', '0'");
            getLatestOperation();
        } catch (IOException| ClassNotFoundException except) {
            System.err.println("Ошибка при добавлении нового товара");
            except.printStackTrace();
        }
    }

    private void reCalculateCost () {
        try {
            ResultSet result = ServerMain.database.Select("costs WHERE product_index = '"+userData.getCommand()+"'", "*");
            float sum = 0;
            while (result.next())
            {
                if (result.getBoolean("type"))
                    sum-= (result.getInt("amount")*result.getFloat("K")*result.getFloat("price"));
                else sum +=(result.getInt("amount")*result.getFloat("K")*result.getFloat("price"));
            }
            ServerMain.database.Update("products", "sum_cost", Float.toString(sum), "product_index = '" + userData.getCommand() + "'");
        } catch (SQLException except) {
            System.err.println("Ошибка при перерасчёте себестоимости");
            except.printStackTrace();
        }
    }

    private void addNewCost() {
        try {
            String[] data = (String[])input.readObject();
            ServerMain.database.Insert("costs", " product_index, cost_name, K, price, amount, type", "'"+ userData.getCommand() +"', '"+ data[0] +"', '"+data[1]+"', '"+data[2]+"', '"+data[3]+"', "+data[4]);
            reCalculateCost();
            ResultSet result = ServerMain.database.Select("costs WHERE product_index = '"+userData.getCommand()+"' AND cost_name = '"+data[0]+"' ORDER BY 'index' DESC LIMIT 1", "*");
            result.next();
            output.writeObject(result.getString("index"));
        } catch (IOException| SQLException| ClassNotFoundException except) {
            System.err.println("Ошибка при добавлении новой статьи затрат");
            except.printStackTrace();
        }
    }

    private void getProdPrice() {
        ResultSet result = ServerMain.database.Select("products WHERE product_index = '"+userData.getCommand()+"'", "sum_cost");
        try {
            if (result.next())
                output.writeObject(result.getString("sum_cost"));
        } catch (SQLException| IOException except) {
            System.err.println("Ошибка при получении стоимости товара");
            except.printStackTrace();
        }
    }
    private void deleteCost () {
        try {
            String id = (String)input.readObject();
            ServerMain.database.Delete("costs", "`index` = '"+id+"'");
            reCalculateCost();
        } catch (IOException| ClassNotFoundException except) {
            System.err.println("Ошибка при добавлении новой статьи затрат");
            except.printStackTrace();
        }
    }

    private void updateProduct() {
        try {
            String[] data = (String[])input.readObject();
            ServerMain.database.Update("products", "product_name", "'"+data[1]+"'", "product_index = '"+data[0]+"'");
        } catch (IOException| ClassNotFoundException except) {
            System.err.println("Ошибка при редактировании данных товара");
            except.printStackTrace();
        }
    }

    private void deleteProduct() {
        try {
            String id = (String)input.readObject();
            ServerMain.database.Delete("costs", "product_index = '" + id + "'");
            ServerMain.database.Delete("products", "product_index = '" + id + "'");
        } catch (IOException| ClassNotFoundException except) {
            System.err.println("Ошибка при удалении данных товара");
            except.printStackTrace();
        }
    }

    private void selectMainProductData() {
        try {
            ResultSet result = ServerMain.database.Select("products WHERE user_id = '"+userData.getId()+"'", "*");
            List <ProductData> costsData = new ArrayList();
            while (result.next()) {
                ProductData tempData = new ProductData();
                tempData.setUserId(Integer.toString(userData.getId()));
                tempData.setOperationId(result.getString("product_index"));
                tempData.setName(result.getString("product_name"));
                tempData.setPrice(result.getFloat("sum_cost"));
                costsData.add(tempData);
            }
            output.writeObject(costsData);
        } catch (IOException| SQLException except) {
            System.err.println("Ошибка при получении данных товаров");
            except.printStackTrace();
        }
    }

    private void exchangeId() {
        try {
            String operation_id = (String)input.readObject();
            userData.setCommand(operation_id);
        } catch (IOException| ClassNotFoundException except) {
            System.err.println("Ошибка при обмене ID");
            except.printStackTrace();
        }
    }
}
