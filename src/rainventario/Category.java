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
public class Category {
    
     private int Category_id;
    private String Category_title;

    
     Dbconnection connet = new Dbconnection(); // to get connection from the database
    public Category(int Category_id, String Category_title) {
        this.Category_id = Category_id;
        this.Category_title = Category_title;
    }

    public Category(String Category_title) {
        this.Category_title = Category_title;
    }

    public Category() {
    }

    
    
    public int getCategory_id() {
        return Category_id;
    }

    public void setCategory_id(int Category_id) {
        this.Category_id = Category_id;
    }

    public String getCategory_title() {
        return Category_title;
    }

    public void setCategory_title(String Category_title) {
        this.Category_title = Category_title;
    }
    
    
     public void Add_Category() {

        try {
            Statement Statement1 = connet.getConnection().createStatement();
            String sql = "INSERT INTO category VALUES ('" + getCategory_title()+ "')";
            Statement1.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "category Informatin added successfully");

        } catch (SQLException e) {

            System.out.println("unable to record to the  database");
            System.out.print(e);

        }

    }

    public void edit_Category() {

        try {

            PreparedStatement pst = connet.getConnection().prepareStatement("update category set category_title=? where product_id=?");
            pst.setString(1, getCategory_title());           
            pst.setInt(2, getCategory_id());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "category Informatin Updated successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "category Informatin Error" + e);
        }
    }

    public void delete_Category() {

        try {

            PreparedStatement pst = connet.getConnection().prepareStatement("delete from category where product_id = ?");
            pst.setInt(1, getCategory_id());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "category Informatin deleted successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "category Informatin Error" + e);

        }
    }

    
    

}
