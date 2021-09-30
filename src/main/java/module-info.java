module com.app.dbgoo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.json;
    requires json.simple;
    requires java.sql;
    requires org.fxmisc.richtext;
    requires org.hibernate.orm.core;
    requires com.google.common;

    opens com.app.dbgoo to javafx.fxml;
    exports com.app.dbgoo;
}