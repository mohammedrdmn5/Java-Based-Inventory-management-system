/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rainventario;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author rdmn5
 */
public class Sale {

    private int saleID;
    private int productID;
    private int customerID;
    private int quantity;
 Dbconnection connet = new Dbconnection(); // to get connection from the database
    public Sale(int saleID, int productID, int customerID, int quantity) {
        this.saleID = saleID;
        this.productID = productID;
        this.customerID = customerID;
        this.quantity = quantity;
    }

    public Sale(int productID, int customerID, int quantity) {
        this.productID = productID;
        this.customerID = customerID;
        this.quantity = quantity;
    }

    
    
    
    
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

  
    

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    
    
      public void add_sales() {

        try {
            Statement Statement1 = connet.getConnection().createStatement();
            String sql = "INSERT INTO brand VALUES ('" + getCustomerID()+ "')";
            Statement1.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "sale Informatin added successfully");

        } catch (SQLException e) {

            System.out.println("unable to record to the  database");
            System.out.print(e);

        }

    }

    public void edit_sales() {

        try {

            PreparedStatement pst = connet.getConnection().prepareStatement("update sale set brand_title=? where brand_id=?");
            pst.setInt(1, getCustomerID());           
            pst.setInt(2, getProductID());
            pst.setInt(3, getQuantity());
            pst.setInt(4, getSaleID());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "sale Informatin Updated successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "brand Informatin Error" + e);
        
    }}

   public void delete_sales() {

        try {

            PreparedStatement pst = connet.getConnection().prepareStatement("delete from sale where product_id = ?");
            pst.setInt(1, getCustomerID());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "sale Informatin deleted successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "sale Informatin Error" + e);

        }
    }

}

