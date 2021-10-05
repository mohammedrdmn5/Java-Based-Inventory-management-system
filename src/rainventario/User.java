/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rainventario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import jdk.nashorn.api.tree.CatchTree;

/**
 *
 * @author rdmn5
 */
public class User {

    Dbconnection connet = new Dbconnection(); // to get connection from the database

    private int uid;
    private String username;
    private String password;
    private String user_fname;
    private String user_lname;
    private String user_address;
    private String user_email;
    private String user_contact_no;
    private String position;
    private float Salary;
    private String date_added;

    public User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public User(int uid) {
        this.uid = uid;
    }

    public User(String username, String password, String user_fname, String user_lname, String user_address, String user_email, String user_contact_no, String position, float Salary, String date_added) {

        this.username = username;
        this.password = password;
        this.user_fname = user_fname;
        this.user_lname = user_lname;
        this.user_address = user_address;
        this.user_email = user_email;
        this.user_contact_no = user_contact_no;
        this.position = position;
        this.Salary = Salary;
        this.date_added = date_added;
    }

    public User(int uid, String username, String password, String user_fname, String user_lname, String user_address, String user_email, String user_contact_no, String position, float Salary) {

        this.uid = uid;
        this.username = username;
        this.password = password;
        this.user_fname = user_fname;
        this.user_lname = user_lname;
        this.user_address = user_address;
        this.user_email = user_email;
        this.user_contact_no = user_contact_no;
        this.position = position;
        this.Salary = Salary;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public User() {
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public float getSalary() {
        return Salary;
    }

    public void setSalary(float Salary) {
        this.Salary = Salary;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_fname() {
        return user_fname;
    }

    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_contact_no() {
        return user_contact_no;
    }

    public void setUser_contact_no(String user_contact_no) {
        this.user_contact_no = user_contact_no;
    }

    public String isIs_manger() {
        return position;
    }

    public void setIs_manger(String position) {
        this.position = position;
    }

    public boolean verifylogin() {
        String sql = "select Ra_user.username,Ra_user.password from Ra_user where Ra_user.username='" + getUsername() + "' and Ra_user.password='" + getPassword() + "'";
        try {
            Dbconnection dblogin = new Dbconnection();
            Statement Statement = dblogin.getConnection().createStatement();
            ResultSet ResultSet = null;
            ResultSet = Statement.executeQuery(sql);

            if (ResultSet.next()) {

                return true;

            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }

    }

    public void addLoginRec() {
        LocalDateTime now = LocalDateTime.now(); // getting date from the system
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); // change the format to be accepted in sql.
        Dbconnection dblogin = new Dbconnection();

        try {
            Statement Statement1 = dblogin.getConnection().createStatement();
            Statement1.executeUpdate("INSERT INTO login_record VALUES ('" + getUsername() + "', '" + dtf.format(now) + "')");
            System.out.println("has been record to the loginrecord in database");
        } catch (SQLException e) {

            System.out.println("unable to record the login in database");
            System.out.print(e);
        }

    }

    public void AddUser() {

        try {

            PreparedStatement pst = connet.getConnection().prepareStatement("INSERT INTO RA_user VALUES (?,?,?,?,?,?,?,?,?,?)");

            pst.setString(1, getUsername());
            pst.setString(2, getPassword());
            pst.setString(3, getUser_fname());
            pst.setString(4, getUser_lname());
            pst.setString(5, getUser_address());
            pst.setString(6, getUser_email());
            pst.setString(7, getUser_contact_no());
            pst.setString(8, getPosition());
            pst.setFloat(9, getSalary());
            pst.setString(10, getDate_added());
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "user Informatin Updated successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "user Informatin Error" + e);
        }
    }

    public void editUser() {

        try {

            PreparedStatement pst = connet.getConnection().prepareStatement("update RA_user set username=?, password=?, user_fname=?, user_lname=?, user_address=?, user_email=?, user_contact_no=?, position=?, Salary=?  WHERE uid=?");
            pst.setString(1, getUsername());
            pst.setString(2, getPassword());
            pst.setString(3, getUser_fname());
            pst.setString(4, getUser_lname());
            pst.setString(5, getUser_address());
            pst.setString(6, getUser_email());
            pst.setString(7, getUser_contact_no());
            pst.setString(8, getPosition());
            pst.setFloat(9, getSalary());
            pst.setFloat(10, getUid());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "User Informatin Updated successfully to Database table");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "User Informatin Error" + e);
        }
    }

    public void deleteUser() {

        try {

            PreparedStatement pst = connet.getConnection().prepareStatement("delete from Ra_user where uid = ?");
            pst.setInt(1, getUid());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "User Informatin deleted successfully to Database table");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "This user cannot be deleted because there are many data reference from this user");

        }
    }

    public int getlastIDFromDB() { // to set an ID for new product by checking the database for last id to avoid duplicating.

        try {
            String sql = "SELECT uid FROM Ra_user WHERE uid=(SELECT max(uid) FROM Ra_user);";
            Statement st = connet.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int id = rs.getInt("uid");
                return (id + 1); // after get the last id we will add 1 to get the next 
            }
        } catch (SQLException ex) {

        }
        return 0;
    }

}

class admin extends User {

    private float bonus;

    public admin(int uid, String username, String password, String user_fname, String user_lname, String user_address, String user_email, String user_contact_no, String position, float Salary) {
        super(uid, username, password, user_fname, user_lname, user_address, user_email, user_contact_no, position, Salary);
    }

}

class seller extends User {

    private int work_time;
    private float commission;

    public seller(int uid, String username, String password, String user_fname, String user_lname, String user_address, String user_email, String user_contact_no, String position, float Salary) {
        super(uid, username, password, user_fname, user_lname, user_address, user_email, user_contact_no, position, Salary);
    }

}

class InventoryOfficer extends User {

    private int TotalProductAdded;

    public InventoryOfficer(int uid, String username, String password, String user_fname, String user_lname, String user_address, String user_email, String user_contact_no, String position, float Salary) {
        super(uid, username, password, user_fname, user_lname, user_address, user_email, user_contact_no, position, Salary);
    }

}

class Reviewer extends User {

    private String ProductReviewed;

    public Reviewer(int uid, String username, String password, String user_fname, String user_lname, String user_address, String user_email, String user_contact_no, String position, float Salary) {
        super(uid, username, password, user_fname, user_lname, user_address, user_email, user_contact_no, position, Salary);
    }

}

class Analyst extends User {

    private int HourWorked;

    public Analyst(int uid, String username, String password, String user_fname, String user_lname, String user_address, String user_email, String user_contact_no, String position, float Salary) {
        super(uid, username, password, user_fname, user_lname, user_address, user_email, user_contact_no, position, Salary);
    }

}
