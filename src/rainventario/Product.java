/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rainventario;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

/**
 *
 * @author rdmn5
 */
public class Product {

    private int product_id;
    private String product_title;
    private String product_Brand;
    private String product_category;
    private String date_added;
    private Float product_price;
    private String user_added;
    private String product_barcode;
    private String product_description;
    Dbconnection connet = new Dbconnection(); // to get connection from the database

    public Product(String product_title, String product_Brand, String product_category,
            Float product_price, String date_added, String user_added, String product_barcode, String product_description) {

        setProduct_title(product_title);
        setProduct_Brand(product_Brand);
        setProduct_category(product_category);
        setProduct_price(product_price);
        setDate_added(date_added);
        setUser_added(user_added);
        setProduct_barcode(product_barcode);
        setProduct_description(product_description);

    }

    public Product(int product_id, String product_title, String product_Brand, String product_category, Float product_price, String product_barcode, String product_description) {
        setProduct_id(product_id);
        setProduct_title(product_title);
        setProduct_Brand(product_Brand);
        setProduct_category(product_category);
        setProduct_price(product_price);
        setProduct_barcode(product_barcode);
        setProduct_description(product_description);

    }

    public Product(int product_id) {
        setProduct_id(product_id);
    }

    Product() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getProduct_Brand() {
        return product_Brand;
    }

    public void setProduct_Brand(String product_Brand) {
        this.product_Brand = product_Brand;
    }

    public String getProduct_barcode() {
        return product_barcode;
    }

    public void setProduct_barcode(String product_barcode) {
        this.product_barcode = product_barcode;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public Float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Float product_price) {
        this.product_price = product_price;
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

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    //-------------------------------------------------------------------------
    public void Add_product() {

        try {
            Statement Statement1 = connet.getConnection().createStatement();
            String sql = "INSERT INTO product VALUES ('" + getProduct_title() + "' ,'" + getProduct_Brand() + "', '" + getProduct_category() + "', " + getProduct_price() + " ,'" + getDate_added() + "', '" + getUser_added() + "', '" + getProduct_barcode() + "' ,'" + getProduct_description() + "')";
            Statement1.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "product Informatin added successfully");

        } catch (SQLException e) {

            System.out.println("unable to record to the  database");
            System.out.print(e);

        }

    }

    public void edit_product() {

        try {

            PreparedStatement pst = connet.getConnection().prepareStatement("update product set product_title=?,product_brand=?,product_category=?,product_price=?,product_barcode=? ,product_description=? where product_id=?");
            pst.setString(1, getProduct_title());
            pst.setString(2, getProduct_Brand());
            pst.setString(3, getProduct_category());
            pst.setFloat(4, getProduct_price());
            pst.setString(5, getProduct_barcode());
            pst.setString(6, getProduct_description());
            pst.setInt(7, getProduct_id());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "product Informatin Updated successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "product Informatin Error" + e);
        }
    }

    public void delete_product() {

        try {

            PreparedStatement pst = connet.getConnection().prepareStatement("delete from product where product_id = ?");
            pst.setInt(1, getProduct_id());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "product Informatin deleted successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "product Informatin Error" + e);

        }
    }
    /*
    public int getlastIDFromDB() { // to set an ID for new product by checking the database for last id to avoid duplicating.

        String sql = "SELECT * FROM product WHERE product_id = ( SELECT MAX(product_id) FROM product )";
        int result = 0;
        try {
            Statement Statement2 = connet.getConnection().createStatement();
            ResultSet ResultSet2 = Statement2.executeQuery(sql);
            ResultSet2.next();

            result = ResultSet2.getInt("product_id") + 1; // after get the last id we will add 1 to get the next 

        } catch (SQLException ex) {

        }
        return result;
    }*/
}
