package com.app.dbgoo.entity;

import javafx.beans.property.SimpleStringProperty;

public class ConnectionObj {
    private SimpleStringProperty connectionName;
    private SimpleStringProperty driver;
    private SimpleStringProperty testQuery;
    private SimpleStringProperty database;
    private SimpleStringProperty password;
    private SimpleStringProperty user;
    private SimpleStringProperty databaseServer;

    public ConnectionObj(String connectionName, String driver, String database, String user, String password, String databaseServer, String testQuery){
        this.connectionName = new SimpleStringProperty(connectionName);
        this.driver = new SimpleStringProperty(driver);
        this.database = new SimpleStringProperty(database);
        this.password = new SimpleStringProperty(password);
        this.testQuery = new SimpleStringProperty(testQuery);
        this.user = new SimpleStringProperty(user);
        this.databaseServer = new SimpleStringProperty(databaseServer);
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

    public String getTestQuery() {
        return testQuery.get();
    }

    public SimpleStringProperty testQueryProperty() {
        return testQuery;
    }

    public void setTestQuery(String testQuery) {
        this.testQuery.set(testQuery);
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
