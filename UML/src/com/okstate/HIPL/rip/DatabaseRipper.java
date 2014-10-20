package com.okstate.HIPL.rip;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;

import java.io.IOException;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import java.util.ArrayList;
import java.util.Properties;

public class DatabaseRipper implements Ripper {
    private String hostname;
    private int port;
    private String username;
    private String password;
    private String database;
    private String dbms;
    private String tablename;
    private String columnname;
    private String query;
    public DatabaseRipper(String hostname, int port, String username, String password, String database, String dbms, String tablename, String columnname){
        this.hostname=hostname;
        this.port=port;
        this.username=username;
        this.password=password;
        this.database=database;
        this.dbms=dbms;
        this.tablename=tablename;
        this.columnname=columnname;
    }
    public void setTableName(String tablename){
        this.tablename=tablename;
    }
    
    public void setColumnName(String columnname){
        this.columnname=columnname;
    }
    
    public String getTableName(){
        return this.tablename;
    }
    
    public String getColumnName(){
        return this.columnname;
    }
    
    public void setHostname(String hostname){
        this.hostname=hostname;
    }
    
    public void setUsername(String username){
        this.username=username;
    }
    
    public void setPassword(String password){
        this.password=password;
    }
    
    public void setDatabase(String database){
        this.database=database;
    }
    
    public void setDbms(String dbms){
        this.dbms=dbms;
    }
    
    public void setPort(int port){
        this.port=port;
    }
    
    public String getHostname(){
        return this.hostname;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public int getPort(){
        return this.port;
    }
    
    public String getDatabase(){
        return this.database;
    }
    
    public String getDbms(){
        return this.dbms;
    }
    
    
    @Override
    public String[] getURLs() {
        // TODO Implement this method
        ArrayList urllist=new ArrayList<String>();
        Connection conn=getConnection();
        Statement stmt=null;
        String query;
        if(this.query.equals("") || this.query==null){
            query="SELECT "+this.columnname+" from "+this.database+"."+this.tablename;
        }
        else{
            query=this.query;
        }
        if(conn!=null){
            try {
                stmt = conn.createStatement();
                ResultSet rs=stmt.executeQuery(query);
                while(rs.next()){
                    String temp=rs.getString(this.columnname);
                    urllist.add(temp);
                }
            } catch (SQLException e) {
                System.out.println("INVALID QUERY");
            }

        }else{
            System.out.println("NULL CONNECTION");
        }
        String[] out = (String[]) urllist.toArray(new String[0]);
        return out;
    }

    @Override
    public void writeURLFile(File file) {
        // TODO Implement this method
        String path=file.getAbsolutePath();
        path.replaceAll("\\","/");
        path=path.substring(0, path.lastIndexOf("/"));
        File temp=new File(path);
        if(temp.isDirectory()){
            temp.mkdirs();
        }
        String[] urls=getURLs();
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(file, true));
            for(int i=0;i<urls.length;i++){
                    bw.write(urls[i]);
                    bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            System.out.println("INVALID WRITE EXCEPTION");
        }    
    }

    private Connection getConnection() {
        Connection conn=null;
        Properties connProp=new Properties();
        connProp.put("user",this.username);
        connProp.put("password",this.password);
        
        if(this.dbms.equals("mysql")){
            try {
                conn =
                    DriverManager.getConnection("jdbc:" + this.dbms + "://" + this.hostname + ":" + this.port + "/",
                                                connProp);
            } catch (SQLException e) {
                System.out.println("Invalid connection");
            }
        }
        else if(this.dbms.equals("derby")){
            try {
                conn =
                    DriverManager.getConnection("jdbc:" + this.dbms + ":" + this.database + ";create=true", connProp);
            } catch (SQLException e) {
                System.out.println("Invalid connection");
            }
        }
        return conn;
    }
}
