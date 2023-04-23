package com.example.ecommerce;
import java.sql.*;
public class DbConnection {
    private static final String dburl="jdbc:mysql://localhost:3306/ecommerce";
    private static final String userName="root";
    private static final String password="Sharukh@1234";
    private Statement getStatement(){
        try{
            Connection connection=DriverManager.getConnection(dburl,userName,password);
            return connection.createStatement();

        }catch (Exception e){
            e.printStackTrace();
        }return null;

    }
    public ResultSet getQueryTable(String query){
        try{
            Statement statement=getStatement();
            return statement.executeQuery(query);

        }catch (Exception e){
            e.printStackTrace();
        }return null;
    }

    public int updateDatabase(String query){
        try{
            Statement statement=getStatement();
            return statement.executeUpdate(query);

        }catch (Exception e){
            e.printStackTrace();
        }return 0;

    }

    public static void main(String args[])
    {
        DbConnection conn=new DbConnection();
        ResultSet rs=conn.getQueryTable("select * from customer");
        if(rs!=null){
            System.out.println("connection successful");

        }else{
            System.out.println("connection failed");
        }
    }
}
