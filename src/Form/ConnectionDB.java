/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import static javax.swing.JOptionPane.showMessageDialog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Phong
 */
public class ConnectionDB {

    private static String dbURL = "jdbc:sqlite:history.db";
    private static String tableName = "LIST";
    // jdbc Connection
    private static Connection conn = null;
  
    public void createConnection() {
        try {
            //Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    public void insert(String word) {
        int a = 0;
        try {
            String query = "insert into " + tableName + "(word) values (?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,word);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlExcept) {
            a = 1;
            sqlExcept.printStackTrace();
            //showMessageDialog(null, "DUPLICATED !");
        }
        if (a == 1) {
            try {
              
               String query = "update " + tableName + " set count = count + 1 where word = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1,word);
                stmt.executeUpdate();
                System.out.println("Update complete");
                stmt.close();
            } catch (SQLException sqlExcept) {
                sqlExcept.printStackTrace();
            }
        }
    }

    public ObservableList<String> select() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from " + tableName);
            while (rs.next()) {
                String word = rs.getString("word");
                list.add(word);
            }
            stmt.close();

        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            //showMessageDialog(null, "DUPLICATED !");
        }
        return list;
    }
}
