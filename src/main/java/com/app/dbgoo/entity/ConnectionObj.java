package com.app.dbgoo.entity;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class ConnectionObj {
    private SimpleStringProperty connectionName;
    private SimpleStringProperty driver;
    private SimpleStringProperty database;
    private SimpleStringProperty password;
    private SimpleStringProperty user;
    private SimpleStringProperty databaseServer;
    private SimpleStringProperty databasePort;
    private SimpleStringProperty ssl;

    public ConnectionObj(String connectionName, String driver, String database, String user, String password, String databaseServer, String databasePort, String ssl){
        this.connectionName = new SimpleStringProperty(connectionName);
        this.driver = new SimpleStringProperty(driver);
        this.database = new SimpleStringProperty(database);
        this.password = new SimpleStringProperty(password);
        this.user = new SimpleStringProperty(user);
        this.databaseServer = new SimpleStringProperty(databaseServer);
        this.databasePort = new SimpleStringProperty(databasePort);
        this.ssl = new SimpleStringProperty(ssl);
    }
    public String getSsl() {
        return ssl.get();
    }

    public SimpleStringProperty sslProperty() {
        return ssl;
    }

    public void setSsl(String ssl) {
        this.ssl.set(ssl);
    }

    public String getDatabasePort() {
        return databasePort.get();
    }

    public SimpleStringProperty databasePortProperty() {
        return databasePort;
    }

    public void setDatabasePort(String databasePort) {
        this.databasePort.set(databasePort);
    }

    public String getUser() {
        return user.get();
    }

    public SimpleStringProperty userProperty() {
        return user;
    }

    public void setUser(String user) {
        this.user.set(user);
    }

    public String getDatabaseServer() {
        return databaseServer.get();
    }

    public SimpleStringProperty databaseServerProperty() {
        return databaseServer;
    }

    public void setDatabaseServer(String databaseServer) {
        this.databaseServer.set(databaseServer);
    }

    public String getConnectionName() {
        return connectionName.get();
    }

    public SimpleStringProperty connectionNameProperty() {
        return connectionName;
    }

    public String getDriver() {
        return driver.get();
    }

    public SimpleStringProperty driverProperty() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver.set(driver);
    }

    public void setConnectionName(String connectionName) {
        this.connectionName.set(connectionName);
    }

    public String getDatabase() {
        return database.get();
    }

    public SimpleStringProperty databaseProperty() {
        return database;
    }

    public void setDatabase(String database) {
        this.database.set(database);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }
}
