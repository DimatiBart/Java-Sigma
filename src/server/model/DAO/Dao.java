package com.DimatiBart.sigma.server.model.DAO;

import java.sql.ResultSet;

/**
 * Created by Dimati_Bart on 28.11.15.
 */
public interface Dao {
    public void Insert(String table, String fields, String values);
    public void Delete(String table, String where);
    public void Update(String table, String field, String value, String id);
    public ResultSet Select (String table, String fields);
   // public void CreateTable();
}
