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
public class Brand {
    
     private int brand_id;
    private String brand_title;
   Dbconnection connet = new Dbconnection(); // to get connection from the database
    public Brand(int brand_id, String brand_title) {
        this.brand_id = brand_id;
        this.brand_title = brand_title;
    }

    public Brand() {
    }

    public Brand(int brand_id) {
        this.brand_id = brand_id;
    }
    
    

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_title() {
        return brand_title;
    }

    public void setBrand_title(String brand_title) {
        this.brand_title = brand_title;
    }
   
    
    
      public void Add_brand() {

        try {
            Statement Statement1 = connet.getConnection().createStatement();
            String sql = "INSERT INTO brand VALUES ('" + getBrand_title()+ "')";
            Statement1.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "brand Informatin added successfully");

        } catch (SQLException e) {

            System.out.println("unable to record to the  database");
            System.out.print(e);

        }

    }

    public void edit_brand() {

        try {

            PreparedStatement pst = connet.getConnection().prepareStatement("update brand set brand_title=? where brand_id=?");
            pst.setString(1, getBrand_title());           
            pst.setInt(2, getBrand_id());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "brand Informatin Updated successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "brand Informatin Error" + e);
        }
    }

    public void delete_brand() {

        try {

            PreparedStatement pst = connet.getConnection().prepareStatement("delete from brand where product_id = ?");
            pst.setInt(1, getBrand_id());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "brand Informatin deleted successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "brand Informatin Error" + e);

        }
    }


    
    
}
