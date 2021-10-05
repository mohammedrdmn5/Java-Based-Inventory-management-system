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
public class Stock {

    private int stockID;
    private int productID;
    private int quantity;
 Dbconnection connet = new Dbconnection(); // to get connection from the database
    public int getStockID() {
        return stockID;
    }

    public Stock(int stockID, int productID, int quantity) {
        this.stockID = stockID;
        this.productID = productID;
        this.quantity = quantity;
    }

    public void setStockID(int stockID) {
        this.stockID = stockID;
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

    
    
    
      public void add_Stock() {

        try {
            Statement Statement1 = connet.getConnection().createStatement();
            String sql = "INSERT INTO brand VALUES ('" + getStockID()+ "')";
            Statement1.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "sale Informatin added successfully");

        } catch (SQLException e) {

            System.out.println("unable to record to the  database");
            System.out.print(e);

        }

    }

    public void edit_Stock() {

        try {

            PreparedStatement pst = connet.getConnection().prepareStatement("update sale set brand_title=? where brand_id=?");
            pst.setInt(1, getStockID());           
            pst.setInt(2, getProductID());
            pst.setInt(3, getQuantity());
           

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "sale Informatin Updated successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "brand Informatin Error" + e);
        
    }}

   public void delete_Stock() {

        try {

            PreparedStatement pst = connet.getConnection().prepareStatement("delete from sale where product_id = ?");
            pst.setInt(1, getStockID());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "sale Informatin deleted successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "sale Informatin Error" + e);

        }
    }
}
