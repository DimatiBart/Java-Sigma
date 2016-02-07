package com.DimatiBart.sigma.resources.dataPackages;

import java.io.Serializable;

/**
 * Created by Dimati_Bart on 07.12.15.
 */
public class ProductData implements Serializable{
    private String id;

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    private String userId;
    private String operationId;
    private String name;
    private float price;
    private float amount;
    private float K;
    private boolean recurrent;

    public boolean isRecurrent() {
        return recurrent;
    }

    public void setRecurrent(boolean type) {
        this.recurrent = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getK() {
        return K;
    }

    public void setK(float k) {
        K = k;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
