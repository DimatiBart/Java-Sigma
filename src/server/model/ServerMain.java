package com.DimatiBart.sigma.server.model;

import java.net.*;
import java.io.*;
import java.util.Properties;

import com.DimatiBart.sigma.server.model.DAO.Database;

/**
 * Created by Dimati_Bart on 29.11.15.
 */

public class ServerMain implements Runnable{
    protected int serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected static Database database;
    protected static int clientAmount;

    public ServerMain() {
        try {
            clientAmount = 0;
            InputStream inputStream = new FileInputStream("./src/config.properties");
            Properties property = new Properties();
            property.load(inputStream);
            this.serverPort = Integer.parseInt(property.getProperty("server.port"));
            System.out.println("Порт сервера: "+this.serverPort);
            inputStream.close();
            this.database = Database.getInstance();
        } catch (IOException except) {
            System.err.println("Ошибка открытия файла конфигурации сервера");
        }
    }

    @Override
    public void run() {
        openServerSocket();
        System.out.println("Количество клиентов: " + ServerMain.clientAmount);
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException except) {
                if(isStopped()) {
                    System.out.println("Главный поток остановлен") ;
                    return;
                }
                throw new RuntimeException("Ошибка приёма запроса от клиента", except);
            }
            new Thread(new ServerThread(clientSocket)).start();
        }
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
            System.out.println("Серверный сокет открыт");
        } catch (IOException except) {
            throw new RuntimeException("Невозможно использовать порт номер "+ this.serverPort, except);
        }
    }

    public void stop () {
        try {
            this.isStopped = true;
            this.serverSocket.close();
        } catch (IOException except) {
            throw new RuntimeException("Невозможно использовать порт номер "+ this.serverPort, except);
        }
    }
    public static synchronized void showClientAmount() {
        System.out.println("Количество клиентов: "+ ServerMain.clientAmount);
    }
}
