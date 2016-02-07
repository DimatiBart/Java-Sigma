package com.DimatiBart.sigma.client.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.DimatiBart.sigma.resources.dataPackages.ProductData;
import com.DimatiBart.sigma.resources.dataPackages.UserData;

import java.security.*;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by Dimati_Bart on 01.12.15.
 */
public class ClientModel {
    private Socket clientSocket;
    private ObjectOutputStream output ;
    private ObjectInputStream input;
    private UserData userData;

    public ClientModel () {
        try {
            clientSocket = new Socket("127.0.0.1", 8071);
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());
            userData = new UserData();
        } catch (IOException except) {
            except.printStackTrace();
            System.exit(0);
        }


    }
    public boolean registration (String email, String password) {
        try {
            userData.setEmail(email);
            userData.setPassword(md5(password));
            output.writeObject("REG");
            output.writeObject(userData);
            userData = (UserData)input.readObject();
            if (userData.getCommand().equals("SR"))
                return true;
            else return false;
        } catch (IOException except) {
            except.printStackTrace();
        } catch (ClassNotFoundException except) {
            except.printStackTrace();
        }
        return false;
    }

    public boolean authentication (String email, String password) {
        try {
            output.writeObject("AUTH");
            userData.setEmail(email);
            userData.setPassword(md5(password));
            output.writeObject(userData);
            userData = (UserData)input.readObject();
            if (userData.getCommand().equals("SA"))
                return true;
            else return false;
        } catch (IOException except) {
            except.printStackTrace();
        } catch (ClassNotFoundException except) {
            except.printStackTrace();
        }
        return false;
    }
    public void closeConnection() {
        try {
            this.output.writeObject("EXIT");
            this.clientSocket.close();
        } catch (IOException except) {
            except.printStackTrace();
        }

    }

    public static String md5 (String st) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException except) {
            except.printStackTrace();
        }
        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }

        return md5Hex;
    }

    public boolean getType() {
        return userData.getType();
    }

    public List<UserData> getUsers () {
        try {
            output.writeObject("GETUSERS");
            List<UserData> users;
            users = (ArrayList<UserData>)input.readObject();
            return users;
        } catch (IOException except) {
            except.printStackTrace();
        } catch (ClassNotFoundException except) {
            except.printStackTrace();
        }
        return null;
    }

    public void adminUpdate (String id, String field, String newValue) {
        try {
            output.writeObject("UPDATEUSERS");
            String[] data = {"users", field, newValue, id};
            output.writeObject(data);

        } catch (IOException except) {
            except.printStackTrace();
        }
    }

//    public int makeLatestOperation () {
//        try {
//            output.writeObject("LATESTOPER");
//            output.writeObject(userData.getId());
//            int result = (int)input.readObject()+1;
//            userData.setCommand(Integer.toString(result));
//            return result;
//        } catch (IOException except) {
//            except.printStackTrace();
//        } catch (ClassNotFoundException except) {
//            except.printStackTrace();
//        }
//        return -2;
//    }

    public String getCommand () {
        return userData.getCommand();
    }

    public List<ProductData> getProductData () {
        try {
            output.writeObject("GETPRODDATA");
            List<ProductData> data = (ArrayList<ProductData>)input.readObject();
            return data;
        } catch (IOException| ClassNotFoundException except) {
            except.printStackTrace();
        }
        return null;
    }

    public void costsUpdate(String id, String field, String newValue) {
        try {
            output.writeObject("UPDATECOSTS");
            String[] data = {field, newValue, id};
            output.writeObject(data);

        } catch (IOException except) {
            except.printStackTrace();
        }
    }

    public void addProduct(String name) {
        try {
            output.writeObject("ADDPROD");
            output.writeObject(name);
            userData.setCommand(input.readObject().toString());
        } catch (IOException| ClassNotFoundException except) {
            except.printStackTrace();
        }
    }
    public String addCost (String name, String K, String price,  String amount, boolean isReccurent ) {
        try {
            output.writeObject("ADDCOST");
            String[] data = { name, K, price, amount, Boolean.toString(isReccurent)};
            output.writeObject(data);
            return input.readObject().toString();

        } catch (IOException| ClassNotFoundException except) {
            except.printStackTrace();
        }
        return null;
    }

    public String getProductPrice() {
        try {
            output.writeObject("GETPRODPRICE");
            return (String)input.readObject();

        } catch (IOException| ClassNotFoundException except) {
            except.printStackTrace();
        }
        return null;
    }

    public void deleteCost(String id) {
        try {
            output.writeObject("DELETECOST");
            output.writeObject(id);

        } catch (IOException except) {
            except.printStackTrace();
        }
    }

    public List<ProductData> getMainProductData () {
        try {
            output.writeObject("GETMAINPRODDATA");
            List<ProductData> data = (ArrayList<ProductData>)input.readObject();
            return data;
        } catch (IOException| ClassNotFoundException except) {
            except.printStackTrace();
        }
        return null;
    }

    public void productUpdate(String id, String newValue) {
        try {
            output.writeObject("UPDATEPROD");
            output.writeObject(new String[] {id, newValue});

        } catch (IOException except) {
            except.printStackTrace();
        }
    }

    public void productDelete (String id) {
        try {
            output.writeObject("DELETEPROD");
            output.writeObject(id);

        } catch (IOException except) {
            except.printStackTrace();
        }
    }

    public void productIdExchange (String productId) {
        try {
            output.writeObject("EXCHANGE");
            output.writeObject(productId);
            userData.setCommand(productId);

        } catch (IOException except) {
            except.printStackTrace();
        }

    }
}
