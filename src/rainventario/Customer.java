/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rainventario;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

/**
 *
 * @author rdmn5
 */
public class Customer {

    private int customer_id;
    private String customer_name;
    private String cutomer_address;
    private String customer_contact;
    private String cutomer_email;
    private String customer_DOB;
    private double customer_points_earned;
    ////////////////
    private String date_added;
    private String user_added;

    Dbconnection db = new Dbconnection();

    public Customer(String customer_name, String cutomer_address, String customer_contact, String cutomer_email, String customer_DOB, double customer_points_earned, String date_added, String user_added) {

        setCustomer_name(customer_name);
        setCutomer_address(cutomer_address);
        setCustomer_contact(customer_contact);
        setCutomer_email(cutomer_email);
        setCustomer_DOB(customer_DOB);
        setCustomer_points_earned(customer_points_earned);
        ///////////////
        setDate_added(date_added);
        setUser_added(user_added);

    }

    public Customer(int customer_id, String customer_name, String cutomer_address, String customer_contact, String cutomer_email, String customer_DOB) {
        setCustomer_id(customer_id);
        setCustomer_name(customer_name);
        setCutomer_address(cutomer_address);
        setCustomer_contact(customer_contact);
        setCutomer_email(cutomer_email);
        setCustomer_DOB(customer_DOB);

    }

    public Customer(int customer_id) {
        setCustomer_id(customer_id);

    }

    public Customer() {

    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCutomer_address() {
        return cutomer_address;
    }

    public void setCutomer_address(String cutomer_address) {
        this.cutomer_address = cutomer_address;
    }

    public String getCustomer_contact() {
        return customer_contact;
    }

    public void setCustomer_contact(String customer_contact) {
        this.customer_contact = customer_contact;
    }

    public String getCutomer_email() {
        return cutomer_email;
    }

    public void setCutomer_email(String cutomer_email) {
        this.cutomer_email = cutomer_email;
    }

    public String getCustomer_DOB() {
        return customer_DOB;
    }

    public void setCustomer_DOB(String customer_DOB) {
        this.customer_DOB = customer_DOB;
    }

    public double getCustomer_points_earned() {
        return customer_points_earned;
    }

    public void setCustomer_points_earned(double customer_points_earned) {
        this.customer_points_earned = customer_points_earned;
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

            PreparedStatement pst = db.getConnection().prepareStatement("INSERT INTO customer VALUES(?,?,?,?,?,?,?,?)");
            pst.setString(1, getCustomer_name());
            pst.setString(2, getCutomer_address());
            pst.setString(3, getCustomer_contact());
            pst.setString(4, getCutomer_email());
            pst.setString(5, getCustomer_DOB());
            pst.setDouble(6, getCustomer_points_earned());
            pst.setString(7, getDate_added());
            pst.setString(8, getUser_added());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Customer Informatin saved successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Customer Informatin Error: " + e);
        }

    }

    public void UpdateToDB() {

        try {
            PreparedStatement pst = db.getConnection().prepareStatement("UPDATE customer SET customer_name=?,customer_address=?,customer_contact=?,customer_email=?,customer_DOB=? WHERE customer_id=?");
            pst.setString(1, getCustomer_name());
            pst.setString(2, getCutomer_address());
            pst.setString(3, getCustomer_contact());
            pst.setString(4, getCutomer_email());
            pst.setString(5, getCustomer_DOB());
            pst.setInt(6, getCustomer_id());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Customer Informatin Updated successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Customer Informatin Error: " + e);
        }

    }

    public void DeleteFromDB() {

        try {
            PreparedStatement pst = db.getConnection().prepareStatement("DELETE FROM customer WHERE customer_id=?;");
            pst.setInt(1, getCustomer_id());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Customer Informatin Delete successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Customer Informatin Error: " + e);
        }

    }

    public int lastIDEnterd() {

        try {

            String sql = "SELECT customer_id FROM customer WHERE customer_id=(SELECT max(customer_id) FROM customer);";
            Statement st = db.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int id = rs.getInt("customer_id");
                return (id);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Customer ID Error: " + e);
        }
        return 0;

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public int countrows() {

        try {

            //String sql = "SELECT * FROM customer WHERE customer_id=(SELECT max(customer_id) FROM customer);";
            String sql = "select(SELECT COUNT(*) FROM customer) AS Number_of_customers;";
            Statement st = db.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int id = rs.getInt("Number_of_customers");
                return (id);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Customer ID Error: " + e);
        }
        return 0;

    }

}