package com.okstate.HIPL.rip;

import java.io.File;

public class DatabaseRipper implements Ripper {
    private String hostname;
    private int port;
    private String username;
    private String password;
    private String database;
    private String dbms;
    
    public DatabaseRipper(String hostname, int port, String username, String password, String database, String dbms){
        this.hostname=hostname;
        this.port=port;
        this.username=username;
        this.password=password;
        this.database=database;
        this.dbms=dbms;
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
        
        return new String[0];
    }

    @Override
    public void writeURLFile(File file) {
        // TODO Implement this method
    }
}
