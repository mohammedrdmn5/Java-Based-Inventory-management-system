/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rainventario;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author NitroQ
 */
public class Supplier {

    int supplier_id;
    String supplier_name;
    String supplier_address;
    String supplier_post_code;
    String supplier_city;
    String supplier_contact;
    String date_added;
    String user_added;

    Dbconnection db = new Dbconnection();

    public Supplier(int supplier_id, String supplier_name, String supplier_address, String supplier_post_code, String supplier_city, String supplier_contact) {
        setSupplier_id(supplier_id);
        setSupplier_name(supplier_name);
        setSupplier_address(supplier_address);
        setSupplier_post_code(supplier_post_code);
        setSupplier_city(supplier_city);
        setSupplier_contact(supplier_contact);
    }

    public Supplier(String supplier_name, String supplier_address, String supplier_post_code, String supplier_city, String supplier_contact, String date_added, String user_added) {
        setSupplier_name(supplier_name);
        setSupplier_address(supplier_address);
        setSupplier_post_code(supplier_post_code);
        setSupplier_city(supplier_city);
        setSupplier_contact(supplier_contact);
        setDate_added(date_added);
        setUser_added(user_added);
    }

    public Supplier(int supplier_id) {
        setSupplier_id(supplier_id);
    }

    public Supplier() {
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getSupplier_address() {
        return supplier_address;
    }

    public void setSupplier_address(String supplier_address) {
        this.supplier_address = supplier_address;
    }

    public String getSupplier_post_code() {
        return supplier_post_code;
    }

    public void setSupplier_post_code(String supplier_post_code) {
        this.supplier_post_code = supplier_post_code;
    }

    public String getSupplier_city() {
        return supplier_city;
    }

    public void setSupplier_city(String supplier_city) {
        this.supplier_city = supplier_city;
    }

    public String getSupplier_contact() {
        return supplier_contact;
    }

    public void setSupplier_contact(String supplier_contact) {
        this.supplier_contact = supplier_contact;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getUser_added() {
        return user_added;
    }

    public void setUser_added(String user_added) {
        this.user_added = user_added;
    }

    public void addToDB() {

        try {

            PreparedStatement pst = db.getConnection().prepareStatement("INSERT INTO supplier VALUES(?,?,?,?,?,?,?)");
            pst.setString(1, getSupplier_name());
            pst.setString(2, getSupplier_address());
            pst.setString(3, getSupplier_post_code());
            pst.setString(4, getSupplier_city());
            pst.setString(5, getSupplier_contact());
            pst.setString(6, getDate_added());
            pst.setString(7, getUser_added());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Supplier Informatin saved successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Supplier Informatin Error: " + e);
        }

    }

    public void UpdateToDB() {

        try {
            PreparedStatement pst = db.getConnection().prepareStatement("UPDATE supplier SET supplier_name=?,supplier_address=?,supplier_post_code=?,supplier_city=?,supplier_contact=? WHERE supplier_id=?;");
            pst.setString(1, getSupplier_name());
            pst.setString(2, getSupplier_address());
            pst.setString(3, getSupplier_post_code());
            pst.setString(4, getSupplier_city());
            pst.setString(5, getSupplier_contact());
            pst.setInt(6, getSupplier_id());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Supplier Informatin Updated successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Supplier Informatin Error: " + e);
        }

    }

    public void DeleteFromDB() {

        try {
            PreparedStatement pst = db.getConnection().prepareStatement("DELETE FROM supplier WHERE supplier_id=?;");
            pst.setInt(1, getSupplier_id());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Supplier Informatin Delete successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Supplier Informatin Error: " + e);
        }

    }

    public int getlastIDFromDB() { // to set an ID for new product by checking the database for last id to avoid duplicating.

        try {
            String sql = "SELECT supplier_id FROM supplier WHERE supplier_id=(SELECT max(supplier_id) FROM supplier);";
            Statement st = db.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int id = rs.getInt("supplier_id");
                return (id + 1); // after get the last id we will add 1 to get the next 
            }
        } catch (SQLException ex) {

        }
        return 0;
    }

}
