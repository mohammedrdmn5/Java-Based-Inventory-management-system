/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rainventario;

import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowListener;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.CategoryTick;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author rdmn5
 */
public class JRAInventario extends javax.swing.JFrame implements ActionListener {

    /**
     * Creates new form login
     */
    int mouseX, mouseY;
    String PressedTime = ""; // to save time when the user do action
    Dbconnection connect = new Dbconnection(); // to call the database to get the connection

    public JRAInventario() {
        initComponents();
        setUnVisible();

        labelsunEnabled();

    }

    public void setUnVisible() {

        JPanel[] jPanels = {JP_product, JP_Customers, JP_Sales, JP_Stock, JP_Suppiler, JP_Dashboard, JP_Addproduct, JP_users, JP_AddSales, JP_AddSupplier, JP_Addusers,JP_CategoryBrand};
        for (JPanel Panel : jPanels) {
            Panel.setVisible(false);
        }

    }

    public void setColorToBlue(JLabel Label, JLabel Labe2, JLabel Labe3, JLabel Labe4, JLabel Labe5, JLabel Labe6, JLabel Labe7, JLabel Labe8, JLabel Labe9 ) {

        JLabel[] labels = {Label, Labe2, Labe3, Labe4, Labe5, Labe6, Labe7, Labe8, Labe9};

        for (JLabel label : labels) {
            label.setForeground(new java.awt.Color(240, 240, 240));
        }
        labels[0].setForeground(new java.awt.Color(102, 204, 255));

    }

    public void labelsunEnabled() {

        JLabel[] labels2 = {JLDashboard, JLProducts, JLCustomers, JLSales, JLStock, JLSupplier, JLCategory, JLloginRec, JLuser,JLloginRec};

        for (JLabel label : labels2) {
            label.setEnabled(false);
        }

    }

    public String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now(); // getting date from the system
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); // change the format to be accepted in sql.  
        String date_added = dtf.format(now);

        return date_added;
    }

    public void get_categoriesFromDB() {
        String sql = "select category.category_title from category";

        try {
            Statement Statement2 = connect.getConnection().createStatement();
            ResultSet ResultSet2 = Statement2.executeQuery(sql);
            jCAddproductcategory.removeAllItems(); // to delete all the categories which is there to avoid duplicating
            jCAddproductcategory.addItem("Select");
            while (ResultSet2.next()) {
                jCAddproductcategory.addItem(ResultSet2.getString("category_title"));
            }
        } catch (SQLException ex) {

        }

    }

    public void get_BrandsFromDB() {
        String sql = "select brand.brand_title from brand";

        try {
            Statement Statement2 = connect.getConnection().createStatement();
            ResultSet ResultSet2 = Statement2.executeQuery(sql);
            JCAddproductBrand.removeAllItems(); // to delete all the brand which is there to avoid duplicating
            JCAddproductBrand.addItem("Select");
            while (ResultSet2.next()) {
                JCAddproductBrand.addItem(ResultSet2.getString("brand_title"));
            }
        } catch (SQLException ex) {

        }

    }

    public void getBrandsFromDB_forP() {
        String sql = "select brand.brand_title from brand";

        try {
            Statement Statement2 = connect.getConnection().createStatement();
            ResultSet ResultSet2 = Statement2.executeQuery(sql);
            JCproductBrand.removeAllItems(); // to delete all the brand which is there to avoid duplicating
            JCproductBrand.addItem("Select");
            while (ResultSet2.next()) {
                JCproductBrand.addItem(ResultSet2.getString("brand_title"));
            }
        } catch (SQLException ex) {

        }

    }

    public void getcategoryFromDB_forP() {
        String sql = "select category.category_title from category";

        try {
            Statement Statement2 = connect.getConnection().createStatement();
            ResultSet ResultSet2 = Statement2.executeQuery(sql);
            jCproductcategory.removeAllItems(); // to delete all the brand which is there to avoid duplicating
            jCproductcategory.addItem("Select");
            while (ResultSet2.next()) {
                jCproductcategory.addItem(ResultSet2.getString("category_title"));
            }
        } catch (SQLException ex) {

        }

    }

    public void getlastIDFromDB() { // to set an ID for new product by checking the database for last id to avoid duplicating.

        String sql = "SELECT * FROM product WHERE product_id = ( SELECT MAX(product_id) FROM product )";

        try {
            Statement Statement2 = connect.getConnection().createStatement();
            ResultSet ResultSet2 = Statement2.executeQuery(sql);
            ResultSet2.next();

            int result = ResultSet2.getInt("product_id") + 1; // after get the last id we will add 1 to get the next 
            JTAddproductID.setText(Integer.toString(result)); // change the int to string to work with jtext
        } catch (SQLException ex) {

        }

    }

    public void searchForProduct() {
        String select;
        String selectinDB = "";
        select = jCselectSearch.getSelectedItem().toString();
        if ("ID".equals(select)) {
            selectinDB = "product_id";
        } else if ("Title".equals(select)) {
            selectinDB = "product_title";
        } else if ("Brand".equals(select)) {
            selectinDB = "product_brand";
        } else if ("Category".equals(select)) {
            selectinDB = "product_category";
        } else if ("Price".equals(select)) {
            selectinDB = "product_price";
        } else if ("Addedd user".equals(select)) {
            selectinDB = "user_added";
        } else if ("Barcode".equals(select)) {
            selectinDB = "product_barcode";
        } else if ("Description".equals(select)) {
            selectinDB = "product_description";
        }
        try {
            String sql = "select * from product where product." + selectinDB + "='" + TFSearchProduct.getText() + "'";
            Statement Statement2 = connect.getConnection().createStatement();
            ResultSet ResultSet2 = null;
            ResultSet2 = Statement2.executeQuery(sql);
            DefaultTableModel tmm = (DefaultTableModel) jTproduct.getModel();
            tmm.setRowCount(0);
            while (ResultSet2.next()) {

                Object o[] = {ResultSet2.getInt("product_id"), ResultSet2.getString("product_title"), ResultSet2.getString("product_brand"), ResultSet2.getString("product_category"), ResultSet2.getFloat("product_price"),
                    ResultSet2.getDate("date_added") + " " + ResultSet2.getTime("date_added"), ResultSet2.getString("user_added"), ResultSet2.getString("product_barcode"), ResultSet2.getString("product_description")};
                tmm.addRow(o);

            }

        } catch (Exception er) {
            JOptionPane.showMessageDialog(null, "unable to get your search try again ");
        }

    }

    public void searchForuser() {
        String select;
        String selectinDB = "";
        select = jCselectUserSearch.getSelectedItem().toString();
        if ("ID".equals(select)) {
            selectinDB = "uid";
        } else if ("First name".equals(select)) {
            selectinDB = "user_fname";
        } else if ("Last name".equals(select)) {
            selectinDB = "user_lname";
        } else if ("Salary".equals(select)) {
            selectinDB = "Salary";
        } else if ("Position".equals(select)) {
            selectinDB = "position";
        } else if ("User name".equals(select)) {
            selectinDB = "username";
        } else if ("Addedd user".equals(select)) {
            selectinDB = "user_added";
        } else if ("Address".equals(select)) {
            selectinDB = "user_address";
        } else if ("Contact No".equals(select)) {
            selectinDB = "user_contact_no";
        } else if ("Email".equals(select)) {
            selectinDB = "user_email";
        }
        try {
            String sql = "select * from Ra_user where Ra_user." + selectinDB + "='" + TFSearchUser.getText() + "'";
            Statement Statement2 = connect.getConnection().createStatement();
            ResultSet ResultSet2 = null;
            ResultSet2 = Statement2.executeQuery(sql);
            DefaultTableModel tmm = (DefaultTableModel) jTUser.getModel();
            tmm.setRowCount(0);
            while (ResultSet2.next()) {

                Object o[] = {ResultSet2.getInt("uid"), ResultSet2.getString("user_fname"), ResultSet2.getString("user_lname"), ResultSet2.getDouble("Salary"), ResultSet2.getString("position"),
                    ResultSet2.getString("username"), ResultSet2.getString("password"),
                    ResultSet2.getString("user_address"), ResultSet2.getString("user_contact_no"), ResultSet2.getString("user_email"), ResultSet2.getString("date_added")};
                tmm.addRow(o);

            }

        } catch (SQLException er) {
            JOptionPane.showMessageDialog(null, "unable to get your search try again " + er);
        }

    }

    public void searchForSupplier() {

        String select;
        String selectinDB = "";
        select = jCselectSearchSupplier.getSelectedItem().toString();
        if ("ID".equals(select)) {
            selectinDB = "supplier_id";
        } else if ("Name".equals(select)) {
            selectinDB = "supplier_name";
        } else if ("Address".equals(select)) {
            selectinDB = "supplier_address";
        } else if ("Post code".equals(select)) {
            selectinDB = "supplier_post_code";
        } else if ("City".equals(select)) {
            selectinDB = "supplier_city";
        } else if ("Contact".equals(select)) {
            selectinDB = "supplier_contact";

        } else if ("Addedd user".equals(select)) {
            selectinDB = "user_added";
        }
        try {
            String sql = "select * from supplier where supplier." + selectinDB + "='" + TFSearchSupplier.getText() + "'";
            Statement Statement2 = connect.getConnection().createStatement();
            ResultSet ResultSet2 = null;
            ResultSet2 = Statement2.executeQuery(sql);
            DefaultTableModel tmm = (DefaultTableModel) jTSupplier.getModel();
            tmm.setRowCount(0);
            while (ResultSet2.next()) {

                Object o[] = {ResultSet2.getInt("supplier_id"), ResultSet2.getString("supplier_name"), ResultSet2.getString("supplier_address"), ResultSet2.getString("supplier_post_code"),
                    ResultSet2.getString("supplier_city"), ResultSet2.getString("supplier_contact"),
                    ResultSet2.getDate("date_added") + " " + ResultSet2.getTime("date_added"), ResultSet2.getString("user_added")};
                tmm.addRow(o);

            }

        } catch (Exception er) {
            JOptionPane.showMessageDialog(null, "unable to get your search try again ");
        }

    }
    //////////////////////////////////////////////////////////////////////
    public void searchForCustomer() {

        String select;
        String selectinDB = "";
        select = jCselectSearchCustomer.getSelectedItem().toString();
        if ("ID".equals(select)) {
            selectinDB = "customer_id";
        } else if ("Name".equals(select)) {
            selectinDB = "customer_name";
        } else if ("Address".equals(select)) {
            selectinDB = "customer_address";
        } else if ("Email".equals(select)) {
            selectinDB = "customer_email";
        } else if ("DOB".equals(select)) {
            selectinDB = "customer_DOB";
        } else if ("Contact".equals(select)) {
            selectinDB = "customer_contact";

        } else if ("Addedd user".equals(select)) {
            selectinDB = "user_added";
        }
        else if ("Point earend".equals(select)) {
            selectinDB = "customer_points_earned";
        }
        try {
            String sql = "select * from customer where customer." + selectinDB + "='" + TFSearchCustomer.getText() + "'";
            Statement Statement2 = connect.getConnection().createStatement();
            ResultSet rs = null;
            rs = Statement2.executeQuery(sql);
            DefaultTableModel tmm = (DefaultTableModel) jTableCustomer.getModel();
            tmm.setRowCount(0);
            while (rs.next()) {

                 Object ob[] = {rs.getInt("customer_id"), rs.getString("customer_name"), rs.getString("customer_address"), rs.getString("customer_contact"),
                    rs.getString("customer_email"), rs.getDate("customer_DOB"), rs.getDouble("customer_points_earned"),
                    rs.getDate("date_added") + " " + rs.getTime("date_added"), rs.getString("user_added")};
                tmm.addRow(ob);

            }

        } catch (Exception er) {
            JOptionPane.showMessageDialog(null, "unable to get your search try again ");
        }

    }

    private void barchart() {
       
        String sql1 = "select top 10 customer.customer_name ,customer.customer_points_earned from customer ORDER BY customer.customer_points_earned DESC";

        try {
            Statement Statement2 = connect.getConnection().createStatement();
            ResultSet ResultSet2 = null;
            ResultSet2 = Statement2.executeQuery(sql1);
            DefaultCategoryDataset dcd = new DefaultCategoryDataset();

            while (ResultSet2.next()) {
                dcd.setValue(ResultSet2.getInt("customer_points_earned"),"", ResultSet2.getString("customer_name"));
            }
            JFreeChart jfc = ChartFactory.createBarChart3D("Best customers", "Customer name", "Number of points", dcd, PlotOrientation.VERTICAL, true, true, true);
            CategoryPlot plot = jfc.getCategoryPlot();
            plot.setRangeGridlinePaint(Color.white);
            ChartPanel cp = new ChartPanel(jfc);

            JP_Bar_chart.removeAll();
            JP_Bar_chart.add(cp);
            JP_Bar_chart.updateUI();

        } catch (SQLException e) {
            //errorlogin.setText("error");
        }

    }

    private void piechart() {
        DefaultPieDataset dpd = new DefaultPieDataset();
        dpd.setValue("product1", 10);
        dpd.setValue("product2", 10);
        dpd.setValue("product3", 30);
        dpd.setValue("product4", 20);
        dpd.setValue("product5", 10);
        dpd.setValue("product6", 30);

        JFreeChart jfc2 = ChartFactory.createPieChart("Sales record", dpd, true, true, false);

        jfc2.getPlot().setBackgroundPaint(Color.white);
        jfc2.getPlot().setOutlinePaint(Color.white);
        jfc2.getLegend().setBorder(BlockBorder.NONE);

        //ChartPanel cp2 = new ChartPanel(jfc2); 
        JPanel panelChart1 = new ChartPanel(jfc2);

        JP_Pie_chart.removeAll();
        JP_Pie_chart.add(panelChart1);
        JP_Pie_chart.updateUI();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        JLSupplier = new javax.swing.JLabel();
        JLTitle = new javax.swing.JLabel();
        JLDashboard = new javax.swing.JLabel();
        JLProducts = new javax.swing.JLabel();
        JLCustomers = new javax.swing.JLabel();
        JLStock = new javax.swing.JLabel();
        JLSales = new javax.swing.JLabel();
        JLloginRec = new javax.swing.JLabel();
        JLCategory = new javax.swing.JLabel();
        JLuser = new javax.swing.JLabel();
        JP_Header = new javax.swing.JPanel();
        JLUser = new javax.swing.JLabel();
        exit = new javax.swing.JLabel();
        jp_Login = new javax.swing.JPanel();
        JLHide = new javax.swing.JLabel();
        JLShow = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        JLLogin = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        JBLogin = new javax.swing.JButton();
        user_name = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        errorlogin = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        JP_Dashboard = new javax.swing.JPanel();
        JP_Bar_chart = new javax.swing.JPanel();
        JP_Pie_chart = new javax.swing.JPanel();
        JP_product = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTproduct = new javax.swing.JTable();
        JBadd_product = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        TFSearchProduct = new javax.swing.JTextField();
        jCselectSearch = new javax.swing.JComboBox<>();
        JBSearchProduct = new javax.swing.JButton();
        jTproductUser = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jDproductDate = new com.toedter.calendar.JDateChooser();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTproductDesc = new javax.swing.JTextArea();
        jLabel26 = new javax.swing.JLabel();
        jTproductprice = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jCproductcategory = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        JCproductBrand = new javax.swing.JComboBox<>();
        jTproductbarcode = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jTproducttitle = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        JTproductID = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        JBDelete_product = new javax.swing.JButton();
        JBedit_product = new javax.swing.JButton();
        JBproductlock = new javax.swing.JButton();
        JP_Addproduct = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTaddProduct = new javax.swing.JTable();
        JBAddProductto = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTAddproductprice = new javax.swing.JTextField();
        jDAddproductDate = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jCAddproductcategory = new javax.swing.JComboBox<>();
        JCAddproductBrand = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTAddproductUser = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTAddproductbarcode = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        JTAddproductID = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jTAddproducttitle = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTAddproductDesc = new javax.swing.JTextArea();
        jLabel22 = new javax.swing.JLabel();
        JP_Customers = new javax.swing.JPanel();
        jScrollPaneCutomer = new javax.swing.JScrollPane();
        jTableCustomer = new javax.swing.JTable();
        JDCC_DOB = new com.toedter.calendar.JDateChooser();
        JBSearchCustomer = new javax.swing.JButton();
        JBDeleteCustomer = new javax.swing.JButton();
        jScrollPane13 = new javax.swing.JScrollPane();
        JTACaddress = new javax.swing.JTextArea();
        jLabel71 = new javax.swing.JLabel();
        JTCemail = new javax.swing.JTextField();
        jCselectSearchCustomer = new javax.swing.JComboBox<>();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        JTCcontact = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        JDCC_dateadded = new com.toedter.calendar.JDateChooser();
        TFSearchCustomer = new javax.swing.JTextField();
        JBInsertNewCustomer = new javax.swing.JButton();
        JBSaveCustomer = new javax.swing.JButton();
        JTCuserAdded = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        JTCid = new javax.swing.JTextField();
        JTCname = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        JTCpointsEarned = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        JBClearCustomer = new javax.swing.JButton();
        JBUpdateCustomer = new javax.swing.JButton();
        JP_Stock = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTproduct1 = new javax.swing.JTable();
        JBadd_product1 = new javax.swing.JButton();
        jLabel98 = new javax.swing.JLabel();
        TFSearchProduct2 = new javax.swing.JTextField();
        jCselectSearch2 = new javax.swing.JComboBox<>();
        JBSearchProduct2 = new javax.swing.JButton();
        jTproductUser2 = new javax.swing.JTextField();
        jLabel99 = new javax.swing.JLabel();
        jDproductDate2 = new com.toedter.calendar.JDateChooser();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTproductDesc1 = new javax.swing.JTextArea();
        jLabel102 = new javax.swing.JLabel();
        jTproductprice3 = new javax.swing.JTextField();
        jLabel103 = new javax.swing.JLabel();
        jCproductcategory2 = new javax.swing.JComboBox<>();
        jLabel104 = new javax.swing.JLabel();
        JCproductBrand2 = new javax.swing.JComboBox<>();
        jTproductbarcode2 = new javax.swing.JTextField();
        jLabel105 = new javax.swing.JLabel();
        jTproducttitle2 = new javax.swing.JTextField();
        jLabel106 = new javax.swing.JLabel();
        JTproductID2 = new javax.swing.JTextField();
        jLabel107 = new javax.swing.JLabel();
        JBDelete_product1 = new javax.swing.JButton();
        JBedit_product1 = new javax.swing.JButton();
        JBproductlock1 = new javax.swing.JButton();
        JP_Sales = new javax.swing.JPanel();
        JBAdd_Sales = new javax.swing.JButton();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPaneSales = new javax.swing.JScrollPane();
        jTableSales = new javax.swing.JTable();
        JP_Suppiler = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTSupplier = new javax.swing.JTable();
        JBadd_supplier = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        TFSearchSupplier = new javax.swing.JTextField();
        jCselectSearchSupplier = new javax.swing.JComboBox<>();
        JBSearchSupplier = new javax.swing.JButton();
        jTSupplierUser = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jDSupplierDate = new com.toedter.calendar.JDateChooser();
        jLabel38 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTSupplieraddress = new javax.swing.JTextArea();
        jLabel47 = new javax.swing.JLabel();
        jTSupplierPostcode = new javax.swing.JTextField();
        jTSupplierContact = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jTSupplierName = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        JTSupplierID = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        JBDelete_Supplier = new javax.swing.JButton();
        JBedit_Supplier = new javax.swing.JButton();
        JBSupplierlock = new javax.swing.JButton();
        jTSupplierCity = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        JP_AddSales = new javax.swing.JPanel();
        jScrollPaneAddSales = new javax.swing.JScrollPane();
        jTableAddSales = new javax.swing.JTable();
        JBAddProduct = new javax.swing.JButton();
        jTproductUser1 = new javax.swing.JTextField();
        jDproductDate1 = new com.toedter.calendar.JDateChooser();
        jLabel77 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jTproductprice1 = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        jCproductcategory1 = new javax.swing.JComboBox<>();
        jLabel89 = new javax.swing.JLabel();
        JCproductBrand1 = new javax.swing.JComboBox<>();
        jTproductbarcode1 = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        jTproducttitle1 = new javax.swing.JTextField();
        TFSearchProduct1 = new javax.swing.JTextField();
        jCselectSearch1 = new javax.swing.JComboBox<>();
        JTproductID1 = new javax.swing.JTextField();
        jLabel95 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        JBSearchProduct1 = new javax.swing.JButton();
        jLabel97 = new javax.swing.JLabel();
        jTproductprice2 = new javax.swing.JTextField();
        JP_users = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTUser = new javax.swing.JTable();
        JBadd_user = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        TFSearchUser = new javax.swing.JTextField();
        jCselectUserSearch = new javax.swing.JComboBox<>();
        JBSearchUser = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTUserAddress = new javax.swing.JTextArea();
        jLabel36 = new javax.swing.JLabel();
        jTUserUsername = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jCUserPosition = new javax.swing.JComboBox<>();
        jTUserLname = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jTUserFname = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        JTUserID = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        JBDelete_User = new javax.swing.JButton();
        JBedit_User = new javax.swing.JButton();
        JBUserclock = new javax.swing.JButton();
        jTUserSalary = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jTUserPassword = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jTUserContactNo = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jTUserEmail = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        JP_AddSupplier = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTAddSupplier = new javax.swing.JTable();
        jTAddSupplierUser = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jDAddSupplierDate = new com.toedter.calendar.JDateChooser();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTAddSupplieraddress = new javax.swing.JTextArea();
        jLabel56 = new javax.swing.JLabel();
        jTAddSupplierPostcode = new javax.swing.JTextField();
        jTAddSupplierContact = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jTAddSupplierName = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        JTAddSupplierID = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        JBAddNewSupplier = new javax.swing.JButton();
        jTAddSupplierCity = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        JP_Addusers = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTAddUser = new javax.swing.JTable();
        jLabel61 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTAddUserAddress = new javax.swing.JTextArea();
        jLabel62 = new javax.swing.JLabel();
        jTAddUserUsername = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        jCAddUserPosition = new javax.swing.JComboBox<>();
        jTAddUserLname = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jTAddUserFname = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        JTAddUserID = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        JBAddNew_User = new javax.swing.JButton();
        jTAddUserSalary = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jTAddUserPassword = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        jTAddUserContactNo = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        jTAddUserEmail = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        jDAddUserDate = new com.toedter.calendar.JDateChooser();
        jLabel48 = new javax.swing.JLabel();
        JP_CategoryBrand = new javax.swing.JPanel();
        JP_CategoryAndBrand = new javax.swing.JScrollPane();
        jTableCategory = new javax.swing.JTable();
        JBSearchCustomer1 = new javax.swing.JButton();
        JBDeleteCustomer1 = new javax.swing.JButton();
        jCselectSearchCustomer1 = new javax.swing.JComboBox<>();
        jLabel79 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        TFSearchCustomer1 = new javax.swing.JTextField();
        JBInsertNewCustomer1 = new javax.swing.JButton();
        JBSaveCustomer1 = new javax.swing.JButton();
        jLabel87 = new javax.swing.JLabel();
        JTCBrandID = new javax.swing.JTextField();
        JTCBrandname = new javax.swing.JTextField();
        JBClearCustomer1 = new javax.swing.JButton();
        JBUpdateCustomer1 = new javax.swing.JButton();
        JP_CategoryAndBrand1 = new javax.swing.JScrollPane();
        jTableBrand = new javax.swing.JTable();
        JBDeleteCustomer2 = new javax.swing.JButton();
        JBUpdateCustomer2 = new javax.swing.JButton();
        JBClearCustomer2 = new javax.swing.JButton();
        JBSaveCustomer2 = new javax.swing.JButton();
        JBInsertNewCustomer2 = new javax.swing.JButton();
        JTCcategoryID = new javax.swing.JTextField();
        JTCCategoryName = new javax.swing.JTextField();
        jLabel91 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setUndecorated(true);
        setOpacity(0.0F);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setForeground(new java.awt.Color(37, 55, 70));
        jPanel1.setMaximumSize(new java.awt.Dimension(1100, 650));
        jPanel1.setPreferredSize(new java.awt.Dimension(1100, 650));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(37, 55, 70));
        jPanel3.setLayout(null);

        JLSupplier.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        JLSupplier.setForeground(new java.awt.Color(240, 240, 240));
        JLSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/manufacture_1.png"))); // NOI18N
        JLSupplier.setText("Supplier");
        JLSupplier.setToolTipText("Supplier");
        JLSupplier.setEnabled(false);
        JLSupplier.setPreferredSize(new java.awt.Dimension(113, 34));
        JLSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLSupplierMouseClicked(evt);
            }
        });
        jPanel3.add(JLSupplier);
        JLSupplier.setBounds(10, 330, 150, 32);

        JLTitle.setFont(new java.awt.Font("Bauhaus 93", 1, 26)); // NOI18N
        JLTitle.setForeground(new java.awt.Color(240, 240, 240));
        JLTitle.setText("RAInventario");
        jPanel3.add(JLTitle);
        JLTitle.setBounds(10, 21, 167, 39);

        JLDashboard.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        JLDashboard.setForeground(new java.awt.Color(240, 240, 240));
        JLDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon_dashboard.png"))); // NOI18N
        JLDashboard.setText("Dashboard");
        JLDashboard.setToolTipText("Dashboard");
        JLDashboard.setEnabled(false);
        JLDashboard.setFocusCycleRoot(true);
        JLDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLDashboardMouseClicked(evt);
            }
        });
        jPanel3.add(JLDashboard);
        JLDashboard.setBounds(10, 100, 113, 31);

        JLProducts.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        JLProducts.setForeground(new java.awt.Color(240, 240, 240));
        JLProducts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon_products.png"))); // NOI18N
        JLProducts.setText("Products");
        JLProducts.setToolTipText("Products");
        JLProducts.setEnabled(false);
        JLProducts.setPreferredSize(new java.awt.Dimension(113, 34));
        JLProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLProductsMouseClicked(evt);
            }
        });
        jPanel3.add(JLProducts);
        JLProducts.setBounds(10, 150, 113, 29);

        JLCustomers.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        JLCustomers.setForeground(new java.awt.Color(240, 240, 240));
        JLCustomers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon_customers.png"))); // NOI18N
        JLCustomers.setText("Customers");
        JLCustomers.setToolTipText("Customers");
        JLCustomers.setEnabled(false);
        JLCustomers.setPreferredSize(new java.awt.Dimension(113, 34));
        JLCustomers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLCustomersMouseClicked(evt);
            }
        });
        jPanel3.add(JLCustomers);
        JLCustomers.setBounds(10, 190, 167, 32);

        JLStock.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        JLStock.setForeground(new java.awt.Color(240, 240, 240));
        JLStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/store.png"))); // NOI18N
        JLStock.setText("Stock");
        JLStock.setToolTipText("Stock");
        JLStock.setEnabled(false);
        JLStock.setPreferredSize(new java.awt.Dimension(113, 34));
        JLStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLStockMouseClicked(evt);
            }
        });
        jPanel3.add(JLStock);
        JLStock.setBounds(10, 230, 113, 36);

        JLSales.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        JLSales.setForeground(new java.awt.Color(240, 240, 240));
        JLSales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sales.png"))); // NOI18N
        JLSales.setText("Sales");
        JLSales.setToolTipText("Sales");
        JLSales.setEnabled(false);
        JLSales.setPreferredSize(new java.awt.Dimension(113, 34));
        JLSales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLSalesMouseClicked(evt);
            }
        });
        jPanel3.add(JLSales);
        JLSales.setBounds(10, 280, 113, 34);

        JLloginRec.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        JLloginRec.setForeground(new java.awt.Color(240, 240, 240));
        JLloginRec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enter.png"))); // NOI18N
        JLloginRec.setText("Login record");
        JLloginRec.setToolTipText("Supplier");
        JLloginRec.setEnabled(false);
        JLloginRec.setPreferredSize(new java.awt.Dimension(113, 34));
        JLloginRec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLloginRecMouseClicked(evt);
            }
        });
        jPanel3.add(JLloginRec);
        JLloginRec.setBounds(10, 430, 140, 32);

        JLCategory.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        JLCategory.setForeground(new java.awt.Color(240, 240, 240));
        JLCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/maintenance (2).png"))); // NOI18N
        JLCategory.setText("Category & Brand");
        JLCategory.setToolTipText("Supplier");
        JLCategory.setEnabled(false);
        JLCategory.setPreferredSize(new java.awt.Dimension(113, 34));
        JLCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLCategoryMouseClicked(evt);
            }
        });
        jPanel3.add(JLCategory);
        JLCategory.setBounds(10, 380, 170, 32);

        JLuser.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        JLuser.setForeground(new java.awt.Color(240, 240, 240));
        JLuser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/group.png"))); // NOI18N
        JLuser.setText("users");
        JLuser.setToolTipText("Supplier");
        JLuser.setEnabled(false);
        JLuser.setPreferredSize(new java.awt.Dimension(113, 34));
        JLuser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLuserMouseClicked(evt);
            }
        });
        jPanel3.add(JLuser);
        JLuser.setBounds(10, 480, 140, 32);

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 650));

        JP_Header.setBackground(new java.awt.Color(37, 55, 70));
        JP_Header.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                JP_HeaderMouseDragged(evt);
            }
        });
        JP_Header.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                JP_HeaderMousePressed(evt);
            }
        });

        JLUser.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        JLUser.setForeground(new java.awt.Color(255, 255, 255));
        JLUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/user.png"))); // NOI18N
        JLUser.setToolTipText("User");
        JLUser.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        JLUser.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        exit.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        exit.setForeground(new java.awt.Color(240, 240, 240));
        exit.setText(" X");
        exit.setToolTipText("Exit");
        exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                exitMousePressed(evt);
            }
        });

        javax.swing.GroupLayout JP_HeaderLayout = new javax.swing.GroupLayout(JP_Header);
        JP_Header.setLayout(JP_HeaderLayout);
        JP_HeaderLayout.setHorizontalGroup(
            JP_HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JP_HeaderLayout.createSequentialGroup()
                .addContainerGap(1019, Short.MAX_VALUE)
                .addComponent(JLUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        JP_HeaderLayout.setVerticalGroup(
            JP_HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JP_HeaderLayout.createSequentialGroup()
                .addComponent(exit)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JP_HeaderLayout.createSequentialGroup()
                .addGap(0, 5, Short.MAX_VALUE)
                .addComponent(JLUser, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(JP_Header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 50));

        jp_Login.setLayout(null);

        JLHide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/vision hide.png"))); // NOI18N
        JLHide.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JLHide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLHideMouseClicked(evt);
            }
        });
        jp_Login.add(JLHide);
        JLHide.setBounds(530, 300, 35, 35);

        JLShow.setText("jLabel3");
        JLShow.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JLShow.setMaximumSize(new java.awt.Dimension(32, 23));
        JLShow.setMinimumSize(new java.awt.Dimension(32, 23));
        JLShow.setPreferredSize(new java.awt.Dimension(32, 23));
        JLShow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLShowMouseClicked(evt);
            }
        });
        jp_Login.add(JLShow);
        JLShow.setBounds(530, 300, 35, 35);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(240, 240, 240));
        jLabel2.setText("Password");
        jLabel2.setToolTipText("Password");
        jp_Login.add(jLabel2);
        jLabel2.setBounds(300, 270, 78, 20);

        JLLogin.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        JLLogin.setForeground(new java.awt.Color(255, 255, 255));
        JLLogin.setText("LOGIN");
        jp_Login.add(JLLogin);
        JLLogin.setBounds(300, 120, 81, 29);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Username");
        jLabel1.setToolTipText("Username");
        jp_Login.add(jLabel1);
        jLabel1.setBounds(300, 170, 82, 20);

        JBLogin.setBackground(new java.awt.Color(37, 55, 70));
        JBLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/button login.png"))); // NOI18N
        JBLogin.setActionCommand("Login");
        JBLogin.addActionListener(this);
        JBLogin.setBorder(null);
        JBLogin.setDoubleBuffered(true);
        JBLogin.setPreferredSize(new java.awt.Dimension(148, 44));
        jp_Login.add(JBLogin);
        JBLogin.setBounds(360, 370, 148, 44);

        user_name.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        user_name.setForeground(new java.awt.Color(255, 255, 255));
        user_name.setToolTipText("Username");
        user_name.setBorder(null);
        user_name.setCaretColor(new java.awt.Color(255, 255, 255));
        user_name.setOpaque(false);
        user_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                user_nameKeyTyped(evt);
            }
        });
        jp_Login.add(user_name);
        user_name.setBounds(300, 200, 230, 35);

        password.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        password.setForeground(new java.awt.Color(255, 255, 255));
        password.setToolTipText("Password");
        password.setBorder(null);
        password.setCaretColor(new java.awt.Color(255, 255, 255));
        password.setOpaque(false);
        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                passwordKeyTyped(evt);
            }
        });
        jp_Login.add(password);
        password.setBounds(300, 300, 226, 35);

        errorlogin.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        errorlogin.setForeground(new java.awt.Color(255, 0, 0));
        jp_Login.add(errorlogin);
        errorlogin.setBounds(300, 340, 270, 20);

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/login_1.png"))); // NOI18N
        jp_Login.add(jLabel15);
        jLabel15.setBounds(270, 100, 350, 336);

        jPanel1.add(jp_Login, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 910, 600));

        JP_Dashboard.setLayout(null);

        JP_Bar_chart.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bar Chart", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        JP_Bar_chart.setLayout(new javax.swing.BoxLayout(JP_Bar_chart, javax.swing.BoxLayout.LINE_AXIS));
        JP_Dashboard.add(JP_Bar_chart);
        JP_Bar_chart.setBounds(20, 10, 880, 260);

        JP_Pie_chart.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pie chart", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        JP_Pie_chart.setPreferredSize(new java.awt.Dimension(440, 320));
        JP_Pie_chart.setLayout(new javax.swing.BoxLayout(JP_Pie_chart, javax.swing.BoxLayout.LINE_AXIS));
        JP_Dashboard.add(JP_Pie_chart);
        JP_Pie_chart.setBounds(20, 270, 440, 320);

        jPanel1.add(JP_Dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 910, 600));

        JP_product.setLayout(null);

        jTproduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Title", "Brand", "Category", "Price", "Added date", "Added user", "Barcode", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTproduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTproductMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTproduct);

        JP_product.add(jScrollPane1);
        jScrollPane1.setBounds(10, 290, 890, 280);

        JBadd_product.setBackground(new java.awt.Color(255, 255, 255));
        JBadd_product.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBadd_product.setForeground(new java.awt.Color(255, 255, 255));
        JBadd_product.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBadd_product.setText("Add new product");
        JBadd_product.setToolTipText("");
        JBadd_product.setBorder(null);
        JBadd_product.addActionListener(this);
        JBadd_product.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBadd_product.setOpaque(false);
        JBadd_product.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_product.add(JBadd_product);
        JBadd_product.setBounds(10, 10, 148, 44);

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/line.png"))); // NOI18N
        JP_product.add(jLabel14);
        jLabel14.setBounds(450, 40, 410, 3);

        TFSearchProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        TFSearchProduct.setToolTipText("Search");
        TFSearchProduct.setBorder(null);
        TFSearchProduct.setCaretColor(new java.awt.Color(255, 255, 255));
        TFSearchProduct.setOpaque(false);
        TFSearchProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TFSearchProductKeyTyped(evt);
            }
        });
        JP_product.add(TFSearchProduct);
        TFSearchProduct.setBounds(450, 10, 410, 30);

        jCselectSearch.setBackground(new java.awt.Color(240, 240, 240));
        jCselectSearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Title", "Brand", "Category", "Price", "Addedd user", "Barcode", "Description", " " }));
        jCselectSearch.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(240, 240, 240), 4, true));
        JP_product.add(jCselectSearch);
        jCselectSearch.setBounds(250, 10, 190, 40);

        JBSearchProduct.setBackground(new java.awt.Color(255, 255, 255));
        JBSearchProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/magnifier.png"))); // NOI18N
        JBSearchProduct.setBorder(null);
        JBSearchProduct.setOpaque(false);
        JBSearchProduct.addActionListener(this);
        JBSearchProduct.setPreferredSize(new java.awt.Dimension(24, 24));
        JP_product.add(JBSearchProduct);
        JBSearchProduct.setBounds(870, 10, 24, 30);

        jTproductUser.setEditable(false);
        JP_product.add(jTproductUser);
        jTproductUser.setBounds(320, 240, 160, 30);

        jLabel23.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel23.setText("Added user");
        JP_product.add(jLabel23);
        jLabel23.setBounds(240, 240, 80, 30);

        jDproductDate.setForeground(new java.awt.Color(255, 45, 45));
        jDproductDate.setDateFormatString("yyyy/MM/dd HH:mm:ss");
        jDproductDate.setEnabled(false);
        JP_product.add(jDproductDate);
        jDproductDate.setBounds(90, 240, 140, 30);

        jLabel24.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel24.setText("Added Date");
        JP_product.add(jLabel24);
        jLabel24.setBounds(10, 240, 80, 30);

        jLabel25.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel25.setText("Description");
        JP_product.add(jLabel25);
        jLabel25.setBounds(10, 180, 80, 30);

        jTproductDesc.setColumns(20);
        jTproductDesc.setRows(5);
        jScrollPane4.setViewportView(jTproductDesc);

        JP_product.add(jScrollPane4);
        jScrollPane4.setBounds(90, 170, 610, 60);

        jLabel26.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel26.setText("Price");
        JP_product.add(jLabel26);
        jLabel26.setBounds(10, 120, 70, 30);

        jTproductprice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTproductpriceActionPerformed(evt);
            }
        });
        JP_product.add(jTproductprice);
        jTproductprice.setBounds(50, 120, 130, 30);

        jLabel27.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel27.setText("category");
        JP_product.add(jLabel27);
        jLabel27.setBounds(200, 120, 70, 30);

        JP_product.add(jCproductcategory);
        jCproductcategory.setBounds(270, 120, 220, 30);

        jLabel28.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel28.setText("brand");
        JP_product.add(jLabel28);
        jLabel28.setBounds(510, 120, 70, 30);

        JP_product.add(JCproductBrand);
        JCproductBrand.setBounds(560, 120, 220, 30);
        JP_product.add(jTproductbarcode);
        jTproductbarcode.setBounds(600, 60, 290, 30);

        jLabel29.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel29.setText("Barcode");
        JP_product.add(jLabel29);
        jLabel29.setBounds(540, 60, 70, 30);
        JP_product.add(jTproducttitle);
        jTproducttitle.setBounds(230, 60, 300, 30);

        jLabel30.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel30.setText("Title");
        JP_product.add(jLabel30);
        jLabel30.setBounds(190, 60, 70, 30);

        JTproductID.setEditable(false);
        JP_product.add(JTproductID);
        JTproductID.setBounds(50, 60, 130, 30);

        jLabel31.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel31.setText("ID");
        JP_product.add(jLabel31);
        jLabel31.setBounds(10, 60, 70, 30);

        JBDelete_product.setBackground(new java.awt.Color(255, 255, 255));
        JBDelete_product.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBDelete_product.setForeground(new java.awt.Color(255, 255, 255));
        JBDelete_product.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBDelete_product.setText("Delete");
        JBDelete_product.addActionListener(this);
        JBDelete_product.setToolTipText("");
        JBDelete_product.setBorder(null);
        JBDelete_product.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBDelete_product.setOpaque(false);
        JBDelete_product.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_product.add(JBDelete_product);
        JBDelete_product.setBounds(740, 240, 148, 44);

        JBedit_product.setBackground(new java.awt.Color(255, 255, 255));
        JBedit_product.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBedit_product.setForeground(new java.awt.Color(255, 255, 255));
        JBedit_product.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBedit_product.addActionListener(this);
        JBedit_product.setText("Edit");
        JBedit_product.setToolTipText("");
        JBedit_product.setActionCommand("");
        JBedit_product.setBorder(null);
        JBedit_product.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBedit_product.setOpaque(false);
        JBedit_product.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_product.add(JBedit_product);
        JBedit_product.setBounds(580, 240, 148, 44);

        JBproductlock.setBackground(new java.awt.Color(255, 255, 255));
        JBproductlock.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBproductlock.setForeground(new java.awt.Color(255, 255, 255));
        JBproductlock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lock.png"))); // NOI18N
        JBproductlock.setToolTipText("");
        JBproductlock.addActionListener(this);
        JBproductlock.setBorder(null);
        JBproductlock.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBproductlock.setOpaque(false);
        JBproductlock.setPreferredSize(new java.awt.Dimension(34, 34));
        JP_product.add(JBproductlock);
        JBproductlock.setBounds(530, 245, 34, 34);

        jPanel1.add(JP_product, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 910, 600));

        JP_Addproduct.setLayout(null);

        jTaddProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Title", "Brand", "Category", "Price", "Added date", "Added user", "Barcode", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTaddProduct);

        JP_Addproduct.add(jScrollPane2);
        jScrollPane2.setBounds(10, 240, 890, 330);

        JBAddProductto.setBackground(new java.awt.Color(255, 255, 255));
        JBAddProductto.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBAddProductto.setForeground(new java.awt.Color(255, 255, 255));
        JBAddProductto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBAddProductto.addActionListener(this);
        JBAddProductto.setText("Add");
        JBAddProductto.setBorder(null);
        JBAddProductto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBAddProductto.setOpaque(false);
        JBAddProductto.setPreferredSize(new java.awt.Dimension(148, 44));
        JBAddProductto.setVerifyInputWhenFocusTarget(false);
        JBAddProductto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBAddProducttoActionPerformed(evt);
            }
        });
        JP_Addproduct.add(JBAddProductto);
        JBAddProductto.setBounds(750, 190, 150, 44);

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel7.setText("Description");
        JP_Addproduct.add(jLabel7);
        jLabel7.setBounds(10, 130, 80, 30);

        jTAddproductprice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTAddproductpriceActionPerformed(evt);
            }
        });
        JP_Addproduct.add(jTAddproductprice);
        jTAddproductprice.setBounds(50, 70, 130, 30);

        jDAddproductDate.setForeground(new java.awt.Color(255, 45, 45));
        jDAddproductDate.setDateFormatString("yyyy/MM/dd HH:mm:ss");
        jDAddproductDate.setEnabled(false);
        JP_Addproduct.add(jDAddproductDate);
        jDAddproductDate.setBounds(90, 200, 140, 30);

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel8.setText("Title");
        JP_Addproduct.add(jLabel8);
        jLabel8.setBounds(190, 10, 70, 30);

        JP_Addproduct.add(jCAddproductcategory);
        jCAddproductcategory.setBounds(270, 70, 220, 30);

        JP_Addproduct.add(JCAddproductBrand);
        JCAddproductBrand.setBounds(560, 70, 220, 30);

        jLabel16.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel16.setText("Price");
        JP_Addproduct.add(jLabel16);
        jLabel16.setBounds(10, 70, 70, 30);

        jLabel17.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel17.setText("brand");
        JP_Addproduct.add(jLabel17);
        jLabel17.setBounds(510, 70, 70, 30);

        jTAddproductUser.setEditable(false);
        JP_Addproduct.add(jTAddproductUser);
        jTAddproductUser.setBounds(320, 200, 160, 30);

        jLabel18.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel18.setText("Barcode");
        JP_Addproduct.add(jLabel18);
        jLabel18.setBounds(540, 10, 70, 30);

        jLabel19.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel19.setText("category");
        JP_Addproduct.add(jLabel19);
        jLabel19.setBounds(200, 70, 70, 30);
        JP_Addproduct.add(jTAddproductbarcode);
        jTAddproductbarcode.setBounds(600, 10, 290, 30);

        jLabel20.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel20.setText("ID");
        JP_Addproduct.add(jLabel20);
        jLabel20.setBounds(10, 10, 70, 30);

        JTAddproductID.setEditable(false);
        JP_Addproduct.add(JTAddproductID);
        JTAddproductID.setBounds(50, 10, 130, 30);

        jLabel21.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel21.setText("Added user");
        JP_Addproduct.add(jLabel21);
        jLabel21.setBounds(240, 200, 80, 30);
        JP_Addproduct.add(jTAddproducttitle);
        jTAddproducttitle.setBounds(230, 10, 300, 30);

        jTAddproductDesc.setColumns(20);
        jTAddproductDesc.setRows(5);
        jScrollPane3.setViewportView(jTAddproductDesc);

        JP_Addproduct.add(jScrollPane3);
        jScrollPane3.setBounds(90, 120, 610, 60);

        jLabel22.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel22.setText("Added Date");
        JP_Addproduct.add(jLabel22);
        jLabel22.setBounds(10, 200, 80, 30);

        jPanel1.add(JP_Addproduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 910, 600));

        JP_Customers.setToolTipText("");
        JP_Customers.setLayout(null);

        jTableCustomer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Address", "Contact", "Email", "DOF", "Points earned", "Date added", "User added"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCustomerMouseClicked(evt);
            }
        });
        jScrollPaneCutomer.setViewportView(jTableCustomer);

        JP_Customers.add(jScrollPaneCutomer);
        jScrollPaneCutomer.setBounds(10, 260, 890, 320);

        JDCC_DOB.setForeground(new java.awt.Color(255, 45, 45));
        JDCC_DOB.setDateFormatString("y/MM/d");
        JP_Customers.add(JDCC_DOB);
        JDCC_DOB.setBounds(280, 170, 150, 30);

        JBSearchCustomer.setBackground(new java.awt.Color(255, 255, 255));
        JBSearchCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/magnifier.png"))); // NOI18N
        JBSearchCustomer.setBorder(null);
        JBSearchCustomer.addActionListener(this);
        JBSearchCustomer.setOpaque(false);
        JBSearchCustomer.setPreferredSize(new java.awt.Dimension(24, 24));
        JP_Customers.add(JBSearchCustomer);
        JBSearchCustomer.setBounds(870, 10, 24, 30);

        JBDeleteCustomer.setBackground(new java.awt.Color(255, 255, 255));
        JBDeleteCustomer.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBDeleteCustomer.setForeground(new java.awt.Color(255, 255, 255));
        JBDeleteCustomer.addActionListener(this);
        JBDeleteCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBDeleteCustomer.setText("Delete");
        JBDeleteCustomer.setToolTipText("");
        JBDeleteCustomer.setBorder(null);
        JBDeleteCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBDeleteCustomer.setOpaque(false);
        JBDeleteCustomer.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_Customers.add(JBDeleteCustomer);
        JBDeleteCustomer.setBounds(740, 210, 148, 44);

        JTACaddress.setColumns(20);
        JTACaddress.setRows(5);
        jScrollPane13.setViewportView(JTACaddress);

        JP_Customers.add(jScrollPane13);
        jScrollPane13.setBounds(90, 100, 560, 60);

        jLabel71.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel71.setText("Contact");
        JP_Customers.add(jLabel71);
        jLabel71.setBounds(540, 60, 70, 30);

        JTCemail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTCemailActionPerformed(evt);
            }
        });
        JP_Customers.add(JTCemail);
        JTCemail.setBounds(50, 170, 150, 30);

        jCselectSearchCustomer.setBackground(new java.awt.Color(240, 240, 240));
        jCselectSearchCustomer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Name", "Contact", "Address", "Email", "Addedd user", "DOB", "Point earend", " " }));
        jCselectSearchCustomer.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(240, 240, 240), 4, true));
        JP_Customers.add(jCselectSearchCustomer);
        jCselectSearchCustomer.setBounds(250, 10, 190, 40);

        jLabel72.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/line.png"))); // NOI18N
        JP_Customers.add(jLabel72);
        jLabel72.setBounds(450, 40, 410, 3);

        jLabel73.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel73.setText("Added Date");
        JP_Customers.add(jLabel73);
        jLabel73.setBounds(660, 100, 80, 30);
        JP_Customers.add(JTCcontact);
        JTCcontact.setBounds(600, 60, 290, 30);

        jLabel74.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel74.setText("Added user");
        JP_Customers.add(jLabel74);
        jLabel74.setBounds(660, 140, 80, 30);

        jLabel75.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel75.setText("Address");
        JP_Customers.add(jLabel75);
        jLabel75.setBounds(10, 110, 80, 30);

        jLabel76.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel76.setText("ID");
        JP_Customers.add(jLabel76);
        jLabel76.setBounds(10, 60, 70, 30);

        JDCC_dateadded.setForeground(new java.awt.Color(255, 45, 45));
        JDCC_dateadded.setDateFormatString("yyyy/MM/dd HH:mm:ss");
        JDCC_dateadded.setEnabled(false);
        JP_Customers.add(JDCC_dateadded);
        JDCC_dateadded.setBounds(740, 100, 160, 30);

        TFSearchCustomer.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        TFSearchCustomer.setToolTipText("Search");
        TFSearchCustomer.setBorder(null);
        TFSearchCustomer.setCaretColor(new java.awt.Color(255, 255, 255));
        TFSearchCustomer.setOpaque(false);
        TFSearchCustomer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TFSearchCustomerKeyTyped(evt);
            }
        });
        JP_Customers.add(TFSearchCustomer);
        TFSearchCustomer.setBounds(450, 10, 410, 30);

        JBInsertNewCustomer.setBackground(new java.awt.Color(255, 255, 255));
        JBInsertNewCustomer.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBInsertNewCustomer.setForeground(new java.awt.Color(255, 255, 255));
        JBInsertNewCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBInsertNewCustomer.addActionListener(this);
        JBInsertNewCustomer.setText("New customer");
        JBInsertNewCustomer.setToolTipText("");
        JBInsertNewCustomer.setBorder(null);
        JBInsertNewCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBInsertNewCustomer.setOpaque(false);
        JBInsertNewCustomer.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_Customers.add(JBInsertNewCustomer);
        JBInsertNewCustomer.setBounds(80, 10, 148, 44);

        JBSaveCustomer.setBackground(new java.awt.Color(255, 255, 255));
        JBSaveCustomer.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBSaveCustomer.setForeground(new java.awt.Color(255, 255, 255));
        JBSaveCustomer.addActionListener(this);
        JBSaveCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBSaveCustomer.setText("Save");
        JBSaveCustomer.setToolTipText("");
        JBSaveCustomer.setActionCommand("");
        JBSaveCustomer.setBorder(null);
        JBSaveCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBSaveCustomer.setOpaque(false);
        JBSaveCustomer.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_Customers.add(JBSaveCustomer);
        JBSaveCustomer.setBounds(260, 210, 148, 44);

        JTCuserAdded.setEditable(false);
        JP_Customers.add(JTCuserAdded);
        JTCuserAdded.setBounds(740, 140, 160, 30);

        jLabel78.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel78.setText("Name");
        JP_Customers.add(jLabel78);
        jLabel78.setBounds(190, 60, 40, 30);

        jLabel80.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel80.setText("DOB");
        JP_Customers.add(jLabel80);
        jLabel80.setBounds(230, 170, 50, 30);

        JTCid.setEditable(false);
        JP_Customers.add(JTCid);
        JTCid.setBounds(50, 60, 130, 30);
        JP_Customers.add(JTCname);
        JTCname.setBounds(230, 60, 300, 30);

        jLabel81.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel81.setText("Email");
        JP_Customers.add(jLabel81);
        jLabel81.setBounds(10, 170, 70, 30);

        JTCpointsEarned.setEditable(false);
        JTCpointsEarned.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTCpointsEarnedActionPerformed(evt);
            }
        });
        JP_Customers.add(JTCpointsEarned);
        JTCpointsEarned.setBounds(540, 170, 150, 30);

        jLabel82.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel82.setText("Point earend");
        JP_Customers.add(jLabel82);
        jLabel82.setBounds(450, 170, 100, 30);

        JBClearCustomer.setBackground(new java.awt.Color(255, 255, 255));
        JBClearCustomer.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBClearCustomer.setForeground(new java.awt.Color(255, 255, 255));
        JBClearCustomer.addActionListener(this);
        JBClearCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBClearCustomer.setText("Clear");
        JBClearCustomer.setToolTipText("");
        JBClearCustomer.setActionCommand("");
        JBClearCustomer.setBorder(null);
        JBClearCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBClearCustomer.setOpaque(false);
        JBClearCustomer.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_Customers.add(JBClearCustomer);
        JBClearCustomer.setBounds(420, 210, 148, 44);

        JBUpdateCustomer.setBackground(new java.awt.Color(255, 255, 255));
        JBUpdateCustomer.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBUpdateCustomer.setForeground(new java.awt.Color(255, 255, 255));
        JBUpdateCustomer.addActionListener(this);
        JBUpdateCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBUpdateCustomer.setText("Edit");
        JBUpdateCustomer.setToolTipText("");
        JBUpdateCustomer.setActionCommand("");
        JBUpdateCustomer.setBorder(null);
        JBUpdateCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBUpdateCustomer.setOpaque(false);
        JBUpdateCustomer.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_Customers.add(JBUpdateCustomer);
        JBUpdateCustomer.setBounds(580, 210, 148, 44);

        jPanel1.add(JP_Customers, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 910, 600));

        JP_Stock.setLayout(null);

        jTproduct1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Title", "Brand", "Category", "Price", "Added date", "Added user", "Barcode", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTproduct1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTproduct1MouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(jTproduct1);

        JP_Stock.add(jScrollPane14);
        jScrollPane14.setBounds(10, 290, 890, 280);

        JBadd_product1.setBackground(new java.awt.Color(255, 255, 255));
        JBadd_product1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBadd_product1.setForeground(new java.awt.Color(255, 255, 255));
        JBadd_product1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBadd_product1.setText(" Add new stock");
        JBadd_product1.setToolTipText("");
        JBadd_product1.setBorder(null);
        JBadd_product1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBadd_product1.setOpaque(false);
        JBadd_product1.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_Stock.add(JBadd_product1);
        JBadd_product1.setBounds(10, 10, 148, 44);

        jLabel98.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/line.png"))); // NOI18N
        JP_Stock.add(jLabel98);
        jLabel98.setBounds(450, 40, 410, 3);

        TFSearchProduct2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        TFSearchProduct2.setToolTipText("Search");
        TFSearchProduct2.setBorder(null);
        TFSearchProduct2.setCaretColor(new java.awt.Color(255, 255, 255));
        TFSearchProduct2.setOpaque(false);
        TFSearchProduct2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TFSearchProduct2KeyTyped(evt);
            }
        });
        JP_Stock.add(TFSearchProduct2);
        TFSearchProduct2.setBounds(450, 10, 410, 30);

        jCselectSearch2.setBackground(new java.awt.Color(240, 240, 240));
        jCselectSearch2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Title", "Brand", "Category", "Price", "Addedd user", "Barcode", "Description", " " }));
        jCselectSearch2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(240, 240, 240), 4, true));
        JP_Stock.add(jCselectSearch2);
        jCselectSearch2.setBounds(250, 10, 190, 40);

        JBSearchProduct2.setBackground(new java.awt.Color(255, 255, 255));
        JBSearchProduct2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/magnifier.png"))); // NOI18N
        JBSearchProduct2.setBorder(null);
        JBSearchProduct2.setOpaque(false);
        JBSearchProduct2.setPreferredSize(new java.awt.Dimension(24, 24));
        JP_Stock.add(JBSearchProduct2);
        JBSearchProduct2.setBounds(870, 10, 24, 30);

        jTproductUser2.setEditable(false);
        JP_Stock.add(jTproductUser2);
        jTproductUser2.setBounds(320, 240, 160, 30);

        jLabel99.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel99.setText("Added user");
        JP_Stock.add(jLabel99);
        jLabel99.setBounds(240, 240, 80, 30);

        jDproductDate2.setForeground(new java.awt.Color(255, 45, 45));
        jDproductDate2.setDateFormatString("yyyy/MM/dd HH:mm:ss");
        jDproductDate2.setEnabled(false);
        JP_Stock.add(jDproductDate2);
        jDproductDate2.setBounds(90, 240, 140, 30);

        jLabel100.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel100.setText("Added Date");
        JP_Stock.add(jLabel100);
        jLabel100.setBounds(10, 240, 80, 30);

        jLabel101.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel101.setText("Description");
        JP_Stock.add(jLabel101);
        jLabel101.setBounds(10, 180, 80, 30);

        jTproductDesc1.setColumns(20);
        jTproductDesc1.setRows(5);
        jScrollPane15.setViewportView(jTproductDesc1);

        JP_Stock.add(jScrollPane15);
        jScrollPane15.setBounds(90, 170, 610, 60);

        jLabel102.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel102.setText("Price");
        JP_Stock.add(jLabel102);
        jLabel102.setBounds(10, 120, 70, 30);

        jTproductprice3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTproductprice3ActionPerformed(evt);
            }
        });
        JP_Stock.add(jTproductprice3);
        jTproductprice3.setBounds(50, 120, 130, 30);

        jLabel103.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel103.setText("category");
        JP_Stock.add(jLabel103);
        jLabel103.setBounds(200, 120, 70, 30);

        JP_Stock.add(jCproductcategory2);
        jCproductcategory2.setBounds(270, 120, 220, 30);

        jLabel104.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel104.setText("brand");
        JP_Stock.add(jLabel104);
        jLabel104.setBounds(510, 120, 70, 30);

        JP_Stock.add(JCproductBrand2);
        JCproductBrand2.setBounds(560, 120, 220, 30);
        JP_Stock.add(jTproductbarcode2);
        jTproductbarcode2.setBounds(600, 60, 290, 30);

        jLabel105.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel105.setText("Barcode");
        JP_Stock.add(jLabel105);
        jLabel105.setBounds(540, 60, 70, 30);
        JP_Stock.add(jTproducttitle2);
        jTproducttitle2.setBounds(230, 60, 300, 30);

        jLabel106.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel106.setText("Title");
        JP_Stock.add(jLabel106);
        jLabel106.setBounds(190, 60, 70, 30);

        JTproductID2.setEditable(false);
        JP_Stock.add(JTproductID2);
        JTproductID2.setBounds(50, 60, 130, 30);

        jLabel107.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel107.setText("ID");
        JP_Stock.add(jLabel107);
        jLabel107.setBounds(10, 60, 70, 30);

        JBDelete_product1.setBackground(new java.awt.Color(255, 255, 255));
        JBDelete_product1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBDelete_product1.setForeground(new java.awt.Color(255, 255, 255));
        JBDelete_product1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBDelete_product1.setText("Delete");
        JBDelete_product1.setToolTipText("");
        JBDelete_product1.setBorder(null);
        JBDelete_product1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBDelete_product1.setOpaque(false);
        JBDelete_product1.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_Stock.add(JBDelete_product1);
        JBDelete_product1.setBounds(740, 240, 148, 44);

        JBedit_product1.setBackground(new java.awt.Color(255, 255, 255));
        JBedit_product1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBedit_product1.setForeground(new java.awt.Color(255, 255, 255));
        JBedit_product1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBedit_product1.setText("Edit");
        JBedit_product1.setToolTipText("");
        JBedit_product1.setActionCommand("");
        JBedit_product1.setBorder(null);
        JBedit_product1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBedit_product1.setOpaque(false);
        JBedit_product1.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_Stock.add(JBedit_product1);
        JBedit_product1.setBounds(580, 240, 148, 44);

        JBproductlock1.setBackground(new java.awt.Color(255, 255, 255));
        JBproductlock1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBproductlock1.setForeground(new java.awt.Color(255, 255, 255));
        JBproductlock1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lock.png"))); // NOI18N
        JBproductlock1.setToolTipText("");
        JBproductlock1.setBorder(null);
        JBproductlock1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBproductlock1.setOpaque(false);
        JBproductlock1.setPreferredSize(new java.awt.Dimension(34, 34));
        JP_Stock.add(JBproductlock1);
        JBproductlock1.setBounds(530, 245, 34, 34);

        jPanel1.add(JP_Stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 910, 600));

        JP_Sales.setLayout(null);

        JBAdd_Sales.setBackground(new java.awt.Color(255, 255, 255));
        JBAdd_Sales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Add sales button.png"))); // NOI18N
        JBAdd_Sales.addActionListener(this);
        JBAdd_Sales.setBorder(null);
        JBAdd_Sales.setOpaque(false);
        JBAdd_Sales.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_Sales.add(JBAdd_Sales);
        JBAdd_Sales.setBounds(70, 30, 148, 44);
        JP_Sales.add(jDateChooser2);
        jDateChooser2.setBounds(350, 150, 130, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/magnifier.png"))); // NOI18N
        JP_Sales.add(jLabel3);
        jLabel3.setBounds(480, 150, 24, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Search by date");
        JP_Sales.add(jLabel4);
        jLabel4.setBounds(90, 150, 110, 30);
        JP_Sales.add(jDateChooser3);
        jDateChooser3.setBounds(200, 150, 130, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("From");
        JP_Sales.add(jLabel5);
        jLabel5.setBounds(240, 120, 40, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("To");
        JP_Sales.add(jLabel6);
        jLabel6.setBounds(390, 120, 20, 30);

        jTableSales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "product_id", "product_title", "product_category", "product_price", "date_added", "user_added"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneSales.setViewportView(jTableSales);

        JP_Sales.add(jScrollPaneSales);
        jScrollPaneSales.setBounds(10, 210, 890, 330);

        jPanel1.add(JP_Sales, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 910, 600));

        JP_Suppiler.setLayout(null);

        jTSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Address", "Post code", "City", "Contact", "Added date", "Added user"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTSupplierMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(jTSupplier);

        JP_Suppiler.add(jScrollPane7);
        jScrollPane7.setBounds(10, 290, 890, 280);

        JBadd_supplier.setBackground(new java.awt.Color(255, 255, 255));
        JBadd_supplier.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBadd_supplier.setForeground(new java.awt.Color(255, 255, 255));
        JBadd_supplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBadd_supplier.addActionListener(this);
        JBadd_supplier.setText("Add new supplier");
        JBadd_supplier.setToolTipText("");
        JBadd_supplier.setBorder(null);
        JBadd_supplier.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBadd_supplier.setOpaque(false);
        JBadd_supplier.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_Suppiler.add(JBadd_supplier);
        JBadd_supplier.setBounds(10, 10, 148, 44);

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/line.png"))); // NOI18N
        JP_Suppiler.add(jLabel33);
        jLabel33.setBounds(450, 40, 410, 3);

        TFSearchSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        TFSearchSupplier.setToolTipText("Search");
        TFSearchSupplier.setBorder(null);
        TFSearchSupplier.setCaretColor(new java.awt.Color(255, 255, 255));
        TFSearchSupplier.setOpaque(false);
        TFSearchSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TFSearchSupplierKeyTyped(evt);
            }
        });
        JP_Suppiler.add(TFSearchSupplier);
        TFSearchSupplier.setBounds(450, 10, 410, 30);

        jCselectSearchSupplier.setBackground(new java.awt.Color(240, 240, 240));
        jCselectSearchSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Name", "Address", "Post code", "City", "Contact", "Addedd user", " ", " " }));
        jCselectSearchSupplier.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(240, 240, 240), 4, true));
        JP_Suppiler.add(jCselectSearchSupplier);
        jCselectSearchSupplier.setBounds(250, 10, 190, 40);

        JBSearchSupplier.setBackground(new java.awt.Color(255, 255, 255));
        JBSearchSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/magnifier.png"))); // NOI18N
        JBSearchSupplier.setBorder(null);
        JBSearchSupplier.addActionListener(this);
        JBSearchSupplier.setOpaque(false);
        JBSearchSupplier.setPreferredSize(new java.awt.Dimension(24, 24));
        JP_Suppiler.add(JBSearchSupplier);
        JBSearchSupplier.setBounds(870, 10, 24, 30);

        jTSupplierUser.setEditable(false);
        JP_Suppiler.add(jTSupplierUser);
        jTSupplierUser.setBounds(320, 240, 160, 30);

        jLabel34.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel34.setText("Added user");
        JP_Suppiler.add(jLabel34);
        jLabel34.setBounds(240, 240, 80, 30);

        jDSupplierDate.setForeground(new java.awt.Color(255, 45, 45));
        jDSupplierDate.setDateFormatString("yyyy/MM/dd HH:mm:ss");
        jDSupplierDate.setEnabled(false);
        JP_Suppiler.add(jDSupplierDate);
        jDSupplierDate.setBounds(90, 240, 140, 30);

        jLabel38.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel38.setText("Added Date");
        JP_Suppiler.add(jLabel38);
        jLabel38.setBounds(10, 240, 80, 30);

        jLabel46.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel46.setText("Address");
        JP_Suppiler.add(jLabel46);
        jLabel46.setBounds(10, 110, 70, 30);

        jTSupplieraddress.setColumns(20);
        jTSupplieraddress.setRows(5);
        jScrollPane8.setViewportView(jTSupplieraddress);

        JP_Suppiler.add(jScrollPane8);
        jScrollPane8.setBounds(90, 100, 610, 60);

        jLabel47.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel47.setText("Post code");
        JP_Suppiler.add(jLabel47);
        jLabel47.setBounds(10, 170, 70, 30);

        jTSupplierPostcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTSupplierPostcodeActionPerformed(evt);
            }
        });
        JP_Suppiler.add(jTSupplierPostcode);
        jTSupplierPostcode.setBounds(90, 170, 130, 30);
        JP_Suppiler.add(jTSupplierContact);
        jTSupplierContact.setBounds(600, 60, 290, 30);

        jLabel50.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel50.setText("Contact");
        JP_Suppiler.add(jLabel50);
        jLabel50.setBounds(540, 60, 60, 30);
        JP_Suppiler.add(jTSupplierName);
        jTSupplierName.setBounds(230, 60, 300, 30);

        jLabel51.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel51.setText("Name");
        JP_Suppiler.add(jLabel51);
        jLabel51.setBounds(190, 60, 40, 30);

        JTSupplierID.setEditable(false);
        JP_Suppiler.add(JTSupplierID);
        JTSupplierID.setBounds(50, 60, 130, 30);

        jLabel52.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel52.setText("ID");
        JP_Suppiler.add(jLabel52);
        jLabel52.setBounds(10, 60, 20, 30);

        JBDelete_Supplier.setBackground(new java.awt.Color(255, 255, 255));
        JBDelete_Supplier.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBDelete_Supplier.setForeground(new java.awt.Color(255, 255, 255));
        JBDelete_Supplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBDelete_Supplier.addActionListener(this);
        JBDelete_Supplier.setText("Delete");
        JBDelete_Supplier.setToolTipText("");
        JBDelete_Supplier.setBorder(null);
        JBDelete_Supplier.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBDelete_Supplier.setOpaque(false);
        JBDelete_Supplier.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_Suppiler.add(JBDelete_Supplier);
        JBDelete_Supplier.setBounds(740, 240, 148, 44);

        JBedit_Supplier.setBackground(new java.awt.Color(255, 255, 255));
        JBedit_Supplier.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBedit_Supplier.setForeground(new java.awt.Color(255, 255, 255));
        JBedit_Supplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBedit_Supplier.addActionListener(this);
        JBedit_Supplier.setText("Edit");
        JBedit_Supplier.setToolTipText("");
        JBedit_Supplier.setActionCommand("");
        JBedit_Supplier.setBorder(null);
        JBedit_Supplier.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBedit_Supplier.setOpaque(false);
        JBedit_Supplier.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_Suppiler.add(JBedit_Supplier);
        JBedit_Supplier.setBounds(580, 240, 148, 44);

        JBSupplierlock.setBackground(new java.awt.Color(255, 255, 255));
        JBSupplierlock.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBSupplierlock.setForeground(new java.awt.Color(255, 255, 255));
        JBSupplierlock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lock.png"))); // NOI18N
        JBSupplierlock.addActionListener(this);
        JBSupplierlock.setToolTipText("");
        JBSupplierlock.setBorder(null);
        JBSupplierlock.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBSupplierlock.setOpaque(false);
        JBSupplierlock.setPreferredSize(new java.awt.Dimension(34, 34));
        JP_Suppiler.add(JBSupplierlock);
        JBSupplierlock.setBounds(530, 245, 34, 34);

        jTSupplierCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTSupplierCityActionPerformed(evt);
            }
        });
        JP_Suppiler.add(jTSupplierCity);
        jTSupplierCity.setBounds(330, 170, 130, 30);

        jLabel53.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel53.setText("City");
        JP_Suppiler.add(jLabel53);
        jLabel53.setBounds(270, 170, 60, 30);

        jPanel1.add(JP_Suppiler, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 910, 600));

        JP_AddSales.setLayout(null);

        jTableAddSales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "product_id", "product_title", "product_category", "product_price", "date_added", "user_added"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneAddSales.setViewportView(jTableAddSales);

        JP_AddSales.add(jScrollPaneAddSales);
        jScrollPaneAddSales.setBounds(10, 300, 890, 290);

        JBAddProduct.setBackground(new java.awt.Color(255, 255, 255));
        JBAddProduct.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBAddProduct.setForeground(new java.awt.Color(255, 255, 255));
        JBAddProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBAddProduct.setText("Add Product");
        JBAddProduct.setBorder(null);
        JBAddProduct.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBAddProduct.setOpaque(false);
        JBAddProduct.setPreferredSize(new java.awt.Dimension(148, 44));
        JBAddProduct.setVerifyInputWhenFocusTarget(false);
        JBAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBAddProductActionPerformed(evt);
            }
        });
        JP_AddSales.add(JBAddProduct);
        JBAddProduct.setBounds(730, 240, 150, 44);

        jTproductUser1.setEditable(false);
        JP_AddSales.add(jTproductUser1);
        jTproductUser1.setBounds(320, 240, 160, 30);

        jDproductDate1.setForeground(new java.awt.Color(255, 45, 45));
        jDproductDate1.setDateFormatString("yyyy/MM/dd HH:mm:ss");
        jDproductDate1.setEnabled(false);
        JP_AddSales.add(jDproductDate1);
        jDproductDate1.setBounds(90, 240, 140, 30);

        jLabel77.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel77.setText("Added user");
        JP_AddSales.add(jLabel77);
        jLabel77.setBounds(240, 240, 80, 30);

        jLabel83.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel83.setText("Added Date");
        JP_AddSales.add(jLabel83);
        jLabel83.setBounds(10, 240, 80, 30);

        jLabel84.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel84.setText("Quantity");
        JP_AddSales.add(jLabel84);
        jLabel84.setBounds(10, 180, 80, 30);

        jLabel85.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel85.setText("Product Price");
        JP_AddSales.add(jLabel85);
        jLabel85.setBounds(10, 120, 100, 30);

        jTproductprice1.setEditable(false);
        jTproductprice1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTproductprice1ActionPerformed(evt);
            }
        });
        JP_AddSales.add(jTproductprice1);
        jTproductprice1.setBounds(110, 120, 130, 30);

        jLabel88.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel88.setText("Product category");
        JP_AddSales.add(jLabel88);
        jLabel88.setBounds(240, 120, 120, 30);

        JP_AddSales.add(jCproductcategory1);
        jCproductcategory1.setBounds(370, 120, 180, 30);

        jLabel89.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel89.setText("Product brand");
        JP_AddSales.add(jLabel89);
        jLabel89.setBounds(570, 120, 100, 30);

        JP_AddSales.add(JCproductBrand1);
        JCproductBrand1.setBounds(670, 120, 220, 30);

        jTproductbarcode1.setEditable(false);
        JP_AddSales.add(jTproductbarcode1);
        jTproductbarcode1.setBounds(600, 60, 190, 30);

        jLabel90.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel90.setText("Product name");
        JP_AddSales.add(jLabel90);
        jLabel90.setBounds(500, 60, 100, 30);

        jTproducttitle1.setEditable(false);
        JP_AddSales.add(jTproducttitle1);
        jTproducttitle1.setBounds(310, 60, 160, 30);

        TFSearchProduct1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        TFSearchProduct1.setToolTipText("Search");
        TFSearchProduct1.setBorder(null);
        TFSearchProduct1.setCaretColor(new java.awt.Color(255, 255, 255));
        TFSearchProduct1.setOpaque(false);
        TFSearchProduct1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TFSearchProduct1KeyTyped(evt);
            }
        });
        JP_AddSales.add(TFSearchProduct1);
        TFSearchProduct1.setBounds(450, 10, 410, 30);

        jCselectSearch1.setBackground(new java.awt.Color(240, 240, 240));
        jCselectSearch1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Title", "Brand", "Category", "Price", "Addedd user", "Barcode", "Description", " " }));
        jCselectSearch1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(240, 240, 240), 4, true));
        JP_AddSales.add(jCselectSearch1);
        jCselectSearch1.setBounds(250, 10, 190, 40);

        JTproductID1.setEditable(false);
        JP_AddSales.add(JTproductID1);
        JTproductID1.setBounds(60, 60, 140, 30);

        jLabel95.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel95.setText("Sale ID");
        JP_AddSales.add(jLabel95);
        jLabel95.setBounds(10, 60, 70, 30);

        jLabel96.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel96.setText("Product ID");
        JP_AddSales.add(jLabel96);
        jLabel96.setBounds(230, 60, 70, 30);

        JBSearchProduct1.setBackground(new java.awt.Color(255, 255, 255));
        JBSearchProduct1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/magnifier.png"))); // NOI18N
        JBSearchProduct1.setBorder(null);
        JBSearchProduct1.setOpaque(false);
        JBSearchProduct1.setPreferredSize(new java.awt.Dimension(24, 24));
        JP_AddSales.add(JBSearchProduct1);
        JBSearchProduct1.setBounds(870, 10, 24, 30);

        jLabel97.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/line.png"))); // NOI18N
        JP_AddSales.add(jLabel97);
        jLabel97.setBounds(450, 40, 410, 3);

        jTproductprice2.setEditable(false);
        jTproductprice2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTproductprice2ActionPerformed(evt);
            }
        });
        JP_AddSales.add(jTproductprice2);
        jTproductprice2.setBounds(110, 180, 130, 30);

        jPanel1.add(JP_AddSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 910, 600));

        JP_users.setLayout(null);

        jTUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Firstname", "Last name", "Salary", "position", "User name", "Password", "Address", "ContactNo", "Email"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTUserMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTUser);

        JP_users.add(jScrollPane5);
        jScrollPane5.setBounds(10, 290, 890, 280);

        JBadd_user.setBackground(new java.awt.Color(255, 255, 255));
        JBadd_user.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBadd_user.setForeground(new java.awt.Color(255, 255, 255));
        JBadd_user.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBadd_user.addActionListener(this);
        JBadd_user.setText("Add new user");
        JBadd_user.setToolTipText("");
        JBadd_user.setBorder(null);
        JBadd_user.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBadd_user.setOpaque(false);
        JBadd_user.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_users.add(JBadd_user);
        JBadd_user.setBounds(10, 10, 148, 44);

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/line.png"))); // NOI18N
        JP_users.add(jLabel32);
        jLabel32.setBounds(450, 40, 410, 3);

        TFSearchUser.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        TFSearchUser.setToolTipText("Search");
        TFSearchUser.setBorder(null);
        TFSearchUser.setCaretColor(new java.awt.Color(255, 255, 255));
        TFSearchUser.setOpaque(false);
        TFSearchUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TFSearchUserKeyTyped(evt);
            }
        });
        JP_users.add(TFSearchUser);
        TFSearchUser.setBounds(450, 10, 410, 30);

        jCselectUserSearch.setBackground(new java.awt.Color(240, 240, 240));
        jCselectUserSearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "First name", "Last name", "Salary", "Position", "User name", "Address", "Contact No", "Email", " " }));
        jCselectUserSearch.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(240, 240, 240), 4, true));
        JP_users.add(jCselectUserSearch);
        jCselectUserSearch.setBounds(250, 10, 190, 40);

        JBSearchUser.setBackground(new java.awt.Color(255, 255, 255));
        JBSearchUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/magnifier.png"))); // NOI18N
        JBSearchUser.setBorder(null);
        JBSearchUser.addActionListener(this);
        JBSearchUser.setOpaque(false);
        JBSearchUser.setPreferredSize(new java.awt.Dimension(24, 24));
        JP_users.add(JBSearchUser);
        JBSearchUser.setBounds(870, 10, 24, 30);

        jLabel35.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel35.setText("address");
        JP_users.add(jLabel35);
        jLabel35.setBounds(10, 180, 80, 30);

        jTUserAddress.setColumns(20);
        jTUserAddress.setRows(5);
        jScrollPane6.setViewportView(jTUserAddress);

        JP_users.add(jScrollPane6);
        jScrollPane6.setBounds(80, 170, 330, 60);

        jLabel36.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel36.setText("username");
        JP_users.add(jLabel36);
        jLabel36.setBounds(430, 120, 70, 30);

        jTUserUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTUserUsernameActionPerformed(evt);
            }
        });
        JP_users.add(jTUserUsername);
        jTUserUsername.setBounds(500, 120, 160, 30);

        jLabel37.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel37.setText("position");
        JP_users.add(jLabel37);
        jLabel37.setBounds(190, 120, 70, 30);

        jCUserPosition.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "select", "admin", "analyst", "reviewer", "Inventory officer", "seller" }));
        JP_users.add(jCUserPosition);
        jCUserPosition.setBounds(250, 120, 170, 30);
        JP_users.add(jTUserLname);
        jTUserLname.setBounds(610, 60, 240, 30);

        jLabel39.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel39.setText("Last name");
        JP_users.add(jLabel39);
        jLabel39.setBounds(540, 60, 70, 30);
        JP_users.add(jTUserFname);
        jTUserFname.setBounds(270, 60, 260, 30);

        jLabel40.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel40.setText("First name");
        JP_users.add(jLabel40);
        jLabel40.setBounds(190, 60, 70, 30);

        JTUserID.setEditable(false);
        JP_users.add(JTUserID);
        JTUserID.setBounds(50, 60, 130, 30);

        jLabel41.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel41.setText("ID");
        JP_users.add(jLabel41);
        jLabel41.setBounds(10, 60, 70, 30);

        JBDelete_User.setBackground(new java.awt.Color(255, 255, 255));
        JBDelete_User.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBDelete_User.setForeground(new java.awt.Color(255, 255, 255));
        JBDelete_User.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBDelete_User.setText("Delete");
        JBDelete_User.setToolTipText("");
        JBDelete_User.addActionListener(this);
        JBDelete_User.setBorder(null);
        JBDelete_User.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBDelete_User.setOpaque(false);
        JBDelete_User.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_users.add(JBDelete_User);
        JBDelete_User.setBounds(740, 240, 148, 44);

        JBedit_User.setBackground(new java.awt.Color(255, 255, 255));
        JBedit_User.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBedit_User.setForeground(new java.awt.Color(255, 255, 255));
        JBedit_User.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBedit_User.setText("Edit");
        JBedit_User.setToolTipText("");
        JBedit_User.addActionListener(this);
        JBedit_User.setActionCommand("");
        JBedit_User.setBorder(null);
        JBedit_User.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBedit_User.setOpaque(false);
        JBedit_User.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_users.add(JBedit_User);
        JBedit_User.setBounds(580, 240, 148, 44);

        JBUserclock.setBackground(new java.awt.Color(255, 255, 255));
        JBUserclock.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBUserclock.setForeground(new java.awt.Color(255, 255, 255));
        JBUserclock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lock.png"))); // NOI18N
        JBUserclock.addActionListener(this);
        JBUserclock.setToolTipText("");
        JBUserclock.setBorder(null);
        JBUserclock.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBUserclock.setOpaque(false);
        JBUserclock.setPreferredSize(new java.awt.Dimension(34, 34));
        JP_users.add(JBUserclock);
        JBUserclock.setBounds(530, 245, 34, 34);

        jTUserSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTUserSalaryActionPerformed(evt);
            }
        });
        JP_users.add(jTUserSalary);
        jTUserSalary.setBounds(60, 120, 120, 30);

        jLabel42.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel42.setText("Salary");
        JP_users.add(jLabel42);
        jLabel42.setBounds(10, 120, 70, 30);

        jTUserPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTUserPasswordActionPerformed(evt);
            }
        });
        JP_users.add(jTUserPassword);
        jTUserPassword.setBounds(740, 120, 160, 30);

        jLabel43.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel43.setText("Password");
        JP_users.add(jLabel43);
        jLabel43.setBounds(670, 120, 70, 30);

        jTUserContactNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTUserContactNoActionPerformed(evt);
            }
        });
        JP_users.add(jTUserContactNo);
        jTUserContactNo.setBounds(520, 180, 250, 30);

        jLabel44.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel44.setText("ContactNo");
        JP_users.add(jLabel44);
        jLabel44.setBounds(440, 180, 70, 30);

        jTUserEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTUserEmailActionPerformed(evt);
            }
        });
        JP_users.add(jTUserEmail);
        jTUserEmail.setBounds(80, 240, 330, 30);

        jLabel45.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel45.setText("Email");
        JP_users.add(jLabel45);
        jLabel45.setBounds(10, 240, 70, 30);

        jPanel1.add(JP_users, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 910, 600));

        JP_AddSupplier.setLayout(null);

        jTAddSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Address", "Post code", "City", "Contact", "Added date", "Added user"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTAddSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTAddSupplierMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(jTAddSupplier);

        JP_AddSupplier.add(jScrollPane9);
        jScrollPane9.setBounds(10, 260, 890, 320);

        jTAddSupplierUser.setEditable(false);
        JP_AddSupplier.add(jTAddSupplierUser);
        jTAddSupplierUser.setBounds(320, 200, 160, 30);

        jLabel49.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel49.setText("Added user");
        JP_AddSupplier.add(jLabel49);
        jLabel49.setBounds(240, 200, 80, 30);

        jDAddSupplierDate.setForeground(new java.awt.Color(255, 45, 45));
        jDAddSupplierDate.setDateFormatString("yyyy/MM/dd HH:mm:ss");
        jDAddSupplierDate.setEnabled(false);
        JP_AddSupplier.add(jDAddSupplierDate);
        jDAddSupplierDate.setBounds(90, 200, 140, 30);

        jLabel54.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel54.setText("Added Date");
        JP_AddSupplier.add(jLabel54);
        jLabel54.setBounds(10, 200, 80, 30);

        jLabel55.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel55.setText("Address");
        JP_AddSupplier.add(jLabel55);
        jLabel55.setBounds(10, 70, 70, 30);

        jTAddSupplieraddress.setColumns(20);
        jTAddSupplieraddress.setRows(5);
        jScrollPane10.setViewportView(jTAddSupplieraddress);

        JP_AddSupplier.add(jScrollPane10);
        jScrollPane10.setBounds(90, 60, 610, 60);

        jLabel56.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel56.setText("Post code");
        JP_AddSupplier.add(jLabel56);
        jLabel56.setBounds(10, 130, 70, 30);

        jTAddSupplierPostcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTAddSupplierPostcodeActionPerformed(evt);
            }
        });
        JP_AddSupplier.add(jTAddSupplierPostcode);
        jTAddSupplierPostcode.setBounds(90, 130, 130, 30);
        JP_AddSupplier.add(jTAddSupplierContact);
        jTAddSupplierContact.setBounds(600, 20, 290, 30);

        jLabel57.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel57.setText("Contact");
        JP_AddSupplier.add(jLabel57);
        jLabel57.setBounds(540, 20, 60, 30);
        JP_AddSupplier.add(jTAddSupplierName);
        jTAddSupplierName.setBounds(230, 20, 300, 30);

        jLabel58.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel58.setText("Name");
        JP_AddSupplier.add(jLabel58);
        jLabel58.setBounds(190, 20, 40, 30);

        JTAddSupplierID.setEditable(false);
        JP_AddSupplier.add(JTAddSupplierID);
        JTAddSupplierID.setBounds(50, 20, 130, 30);

        jLabel59.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel59.setText("ID");
        JP_AddSupplier.add(jLabel59);
        jLabel59.setBounds(10, 20, 20, 30);

        JBAddNewSupplier.setBackground(new java.awt.Color(255, 255, 255));
        JBAddNewSupplier.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBAddNewSupplier.setForeground(new java.awt.Color(255, 255, 255));
        JBAddNewSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBAddNewSupplier.addActionListener(this);
        JBAddNewSupplier.setText("Add");
        JBAddNewSupplier.setToolTipText("");
        JBAddNewSupplier.setBorder(null);
        JBAddNewSupplier.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBAddNewSupplier.setOpaque(false);
        JBAddNewSupplier.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_AddSupplier.add(JBAddNewSupplier);
        JBAddNewSupplier.setBounds(740, 190, 148, 44);

        jTAddSupplierCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTAddSupplierCityActionPerformed(evt);
            }
        });
        JP_AddSupplier.add(jTAddSupplierCity);
        jTAddSupplierCity.setBounds(330, 130, 130, 30);

        jLabel60.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel60.setText("City");
        JP_AddSupplier.add(jLabel60);
        jLabel60.setBounds(270, 130, 60, 30);

        jPanel1.add(JP_AddSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 910, 600));

        JP_Addusers.setLayout(null);

        jTAddUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Firstname", "Last name", "Salary", "position", "User name", "Password", "Address", "ContactNo", "Email", "added date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTAddUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTAddUserMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(jTAddUser);

        JP_Addusers.add(jScrollPane11);
        jScrollPane11.setBounds(10, 240, 890, 340);

        jLabel61.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel61.setText("address");
        JP_Addusers.add(jLabel61);
        jLabel61.setBounds(10, 140, 80, 30);

        jTAddUserAddress.setColumns(20);
        jTAddUserAddress.setRows(5);
        jScrollPane12.setViewportView(jTAddUserAddress);

        JP_Addusers.add(jScrollPane12);
        jScrollPane12.setBounds(80, 130, 330, 60);

        jLabel62.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel62.setText("username");
        JP_Addusers.add(jLabel62);
        jLabel62.setBounds(430, 80, 70, 30);

        jTAddUserUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTAddUserUsernameActionPerformed(evt);
            }
        });
        JP_Addusers.add(jTAddUserUsername);
        jTAddUserUsername.setBounds(500, 80, 160, 30);

        jLabel63.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel63.setText("position");
        JP_Addusers.add(jLabel63);
        jLabel63.setBounds(190, 80, 70, 30);

        jCAddUserPosition.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "select", "admin", "analyst", "reviewer", "Inventory officer", "seller" }));
        JP_Addusers.add(jCAddUserPosition);
        jCAddUserPosition.setBounds(250, 80, 170, 30);
        JP_Addusers.add(jTAddUserLname);
        jTAddUserLname.setBounds(610, 20, 240, 30);

        jLabel64.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel64.setText("Last name");
        JP_Addusers.add(jLabel64);
        jLabel64.setBounds(540, 20, 70, 30);
        JP_Addusers.add(jTAddUserFname);
        jTAddUserFname.setBounds(270, 20, 260, 30);

        jLabel65.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel65.setText("First name");
        JP_Addusers.add(jLabel65);
        jLabel65.setBounds(190, 20, 70, 30);

        JTAddUserID.setEditable(false);
        JP_Addusers.add(JTAddUserID);
        JTAddUserID.setBounds(50, 20, 130, 30);

        jLabel66.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel66.setText("ID");
        JP_Addusers.add(jLabel66);
        jLabel66.setBounds(10, 20, 70, 30);

        JBAddNew_User.setBackground(new java.awt.Color(255, 255, 255));
        JBAddNew_User.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBAddNew_User.setForeground(new java.awt.Color(255, 255, 255));
        JBAddNew_User.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBAddNew_User.addActionListener(this);
        JBAddNew_User.setText("Add");
        JBAddNew_User.setToolTipText("");
        JBAddNew_User.setBorder(null);
        JBAddNew_User.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBAddNew_User.setOpaque(false);
        JBAddNew_User.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_Addusers.add(JBAddNew_User);
        JBAddNew_User.setBounds(740, 190, 148, 44);

        jTAddUserSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTAddUserSalaryActionPerformed(evt);
            }
        });
        JP_Addusers.add(jTAddUserSalary);
        jTAddUserSalary.setBounds(60, 80, 120, 30);

        jLabel67.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel67.setText("Salary");
        JP_Addusers.add(jLabel67);
        jLabel67.setBounds(10, 80, 70, 30);

        jTAddUserPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTAddUserPasswordActionPerformed(evt);
            }
        });
        JP_Addusers.add(jTAddUserPassword);
        jTAddUserPassword.setBounds(740, 80, 160, 30);

        jLabel68.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel68.setText("Password");
        JP_Addusers.add(jLabel68);
        jLabel68.setBounds(670, 80, 70, 30);

        jTAddUserContactNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTAddUserContactNoActionPerformed(evt);
            }
        });
        JP_Addusers.add(jTAddUserContactNo);
        jTAddUserContactNo.setBounds(520, 140, 250, 30);

        jLabel69.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel69.setText("ContactNo");
        JP_Addusers.add(jLabel69);
        jLabel69.setBounds(440, 140, 70, 30);

        jTAddUserEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTAddUserEmailActionPerformed(evt);
            }
        });
        JP_Addusers.add(jTAddUserEmail);
        jTAddUserEmail.setBounds(80, 200, 330, 30);

        jLabel70.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel70.setText("Email");
        JP_Addusers.add(jLabel70);
        jLabel70.setBounds(10, 200, 70, 30);

        jDAddUserDate.setForeground(new java.awt.Color(255, 45, 45));
        jDAddUserDate.setDateFormatString("yyyy/MM/dd HH:mm:ss");
        jDAddUserDate.setEnabled(false);
        JP_Addusers.add(jDAddUserDate);
        jDAddUserDate.setBounds(520, 200, 140, 30);

        jLabel48.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel48.setText("Added Date");
        JP_Addusers.add(jLabel48);
        jLabel48.setBounds(440, 200, 80, 30);

        jPanel1.add(JP_Addusers, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 910, 600));

        JP_CategoryBrand.setToolTipText("");
        JP_CategoryBrand.setLayout(null);

        jTableCategory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Category ID", "Category name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCategoryMouseClicked(evt);
            }
        });
        JP_CategoryAndBrand.setViewportView(jTableCategory);

        JP_CategoryBrand.add(JP_CategoryAndBrand);
        JP_CategoryAndBrand.setBounds(460, 260, 420, 320);

        JBSearchCustomer1.setBackground(new java.awt.Color(255, 255, 255));
        JBSearchCustomer1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/magnifier.png"))); // NOI18N
        JBSearchCustomer1.setBorder(null);
        JBSearchCustomer1.setOpaque(false);
        JBSearchCustomer1.setPreferredSize(new java.awt.Dimension(24, 24));
        JP_CategoryBrand.add(JBSearchCustomer1);
        JBSearchCustomer1.setBounds(730, 30, 24, 30);

        JBDeleteCustomer1.setBackground(new java.awt.Color(255, 255, 255));
        JBDeleteCustomer1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBDeleteCustomer1.setForeground(new java.awt.Color(255, 255, 255));
        JBDeleteCustomer1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBDeleteCustomer1.setText("Delete");
        JBDeleteCustomer1.setToolTipText("");
        JBDeleteCustomer1.setBorder(null);
        JBDeleteCustomer1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBDeleteCustomer1.setOpaque(false);
        JBDeleteCustomer1.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_CategoryBrand.add(JBDeleteCustomer1);
        JBDeleteCustomer1.setBounds(360, 210, 70, 44);

        jCselectSearchCustomer1.setBackground(new java.awt.Color(240, 240, 240));
        jCselectSearchCustomer1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "select", "Brand ID", "Brand Name", "Category ID", "Category Name", " " }));
        jCselectSearchCustomer1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(240, 240, 240), 4, true));
        JP_CategoryBrand.add(jCselectSearchCustomer1);
        jCselectSearchCustomer1.setBounds(110, 30, 190, 40);

        jLabel79.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/line.png"))); // NOI18N
        JP_CategoryBrand.add(jLabel79);
        jLabel79.setBounds(310, 60, 410, 3);

        jLabel86.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel86.setText("ID");
        JP_CategoryBrand.add(jLabel86);
        jLabel86.setBounds(10, 170, 70, 30);

        TFSearchCustomer1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        TFSearchCustomer1.setToolTipText("Search");
        TFSearchCustomer1.setBorder(null);
        TFSearchCustomer1.setCaretColor(new java.awt.Color(255, 255, 255));
        TFSearchCustomer1.setOpaque(false);
        TFSearchCustomer1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TFSearchCustomer1KeyTyped(evt);
            }
        });
        JP_CategoryBrand.add(TFSearchCustomer1);
        TFSearchCustomer1.setBounds(310, 30, 410, 30);

        JBInsertNewCustomer1.setBackground(new java.awt.Color(255, 255, 255));
        JBInsertNewCustomer1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBInsertNewCustomer1.setForeground(new java.awt.Color(255, 255, 255));
        JBInsertNewCustomer1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBInsertNewCustomer1.setText("New brand");
        JBInsertNewCustomer1.setToolTipText("");
        JBInsertNewCustomer1.setBorder(null);
        JBInsertNewCustomer1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBInsertNewCustomer1.setOpaque(false);
        JBInsertNewCustomer1.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_CategoryBrand.add(JBInsertNewCustomer1);
        JBInsertNewCustomer1.setBounds(10, 210, 120, 44);

        JBSaveCustomer1.setBackground(new java.awt.Color(255, 255, 255));
        JBSaveCustomer1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBSaveCustomer1.setForeground(new java.awt.Color(255, 255, 255));
        JBSaveCustomer1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBSaveCustomer1.setText("Save");
        JBSaveCustomer1.setToolTipText("");
        JBSaveCustomer1.setActionCommand("");
        JBSaveCustomer1.setBorder(null);
        JBSaveCustomer1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBSaveCustomer1.setOpaque(false);
        JBSaveCustomer1.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_CategoryBrand.add(JBSaveCustomer1);
        JBSaveCustomer1.setBounds(140, 210, 70, 44);

        jLabel87.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel87.setText("Name");
        JP_CategoryBrand.add(jLabel87);
        jLabel87.setBounds(140, 170, 40, 30);

        JTCBrandID.setEditable(false);
        JP_CategoryBrand.add(JTCBrandID);
        JTCBrandID.setBounds(40, 170, 90, 30);
        JP_CategoryBrand.add(JTCBrandname);
        JTCBrandname.setBounds(190, 170, 230, 30);

        JBClearCustomer1.setBackground(new java.awt.Color(255, 255, 255));
        JBClearCustomer1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBClearCustomer1.setForeground(new java.awt.Color(255, 255, 255));
        JBClearCustomer1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBClearCustomer1.setText("Clear");
        JBClearCustomer1.setToolTipText("");
        JBClearCustomer1.setActionCommand("");
        JBClearCustomer1.setBorder(null);
        JBClearCustomer1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBClearCustomer1.setOpaque(false);
        JBClearCustomer1.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_CategoryBrand.add(JBClearCustomer1);
        JBClearCustomer1.setBounds(220, 210, 60, 44);

        JBUpdateCustomer1.setBackground(new java.awt.Color(255, 255, 255));
        JBUpdateCustomer1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBUpdateCustomer1.setForeground(new java.awt.Color(255, 255, 255));
        JBUpdateCustomer1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBUpdateCustomer1.setText("Edit");
        JBUpdateCustomer1.setToolTipText("");
        JBUpdateCustomer1.setActionCommand("");
        JBUpdateCustomer1.setBorder(null);
        JBUpdateCustomer1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBUpdateCustomer1.setOpaque(false);
        JBUpdateCustomer1.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_CategoryBrand.add(JBUpdateCustomer1);
        JBUpdateCustomer1.setBounds(290, 210, 60, 44);

        jTableBrand.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Brand ID", "Brand Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableBrand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableBrandMouseClicked(evt);
            }
        });
        JP_CategoryAndBrand1.setViewportView(jTableBrand);

        JP_CategoryBrand.add(JP_CategoryAndBrand1);
        JP_CategoryAndBrand1.setBounds(10, 260, 420, 320);

        JBDeleteCustomer2.setBackground(new java.awt.Color(255, 255, 255));
        JBDeleteCustomer2.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBDeleteCustomer2.setForeground(new java.awt.Color(255, 255, 255));
        JBDeleteCustomer2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBDeleteCustomer2.setText("Delete");
        JBDeleteCustomer2.setToolTipText("");
        JBDeleteCustomer2.setBorder(null);
        JBDeleteCustomer2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBDeleteCustomer2.setOpaque(false);
        JBDeleteCustomer2.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_CategoryBrand.add(JBDeleteCustomer2);
        JBDeleteCustomer2.setBounds(810, 210, 70, 44);

        JBUpdateCustomer2.setBackground(new java.awt.Color(255, 255, 255));
        JBUpdateCustomer2.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBUpdateCustomer2.setForeground(new java.awt.Color(255, 255, 255));
        JBUpdateCustomer2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBUpdateCustomer2.setText("Edit");
        JBUpdateCustomer2.setToolTipText("");
        JBUpdateCustomer2.setActionCommand("");
        JBUpdateCustomer2.setBorder(null);
        JBUpdateCustomer2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBUpdateCustomer2.setOpaque(false);
        JBUpdateCustomer2.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_CategoryBrand.add(JBUpdateCustomer2);
        JBUpdateCustomer2.setBounds(740, 210, 60, 44);

        JBClearCustomer2.setBackground(new java.awt.Color(255, 255, 255));
        JBClearCustomer2.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBClearCustomer2.setForeground(new java.awt.Color(255, 255, 255));
        JBClearCustomer2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBClearCustomer2.setText("Clear");
        JBClearCustomer2.setToolTipText("");
        JBClearCustomer2.setActionCommand("");
        JBClearCustomer2.setBorder(null);
        JBClearCustomer2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBClearCustomer2.setOpaque(false);
        JBClearCustomer2.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_CategoryBrand.add(JBClearCustomer2);
        JBClearCustomer2.setBounds(670, 210, 60, 44);

        JBSaveCustomer2.setBackground(new java.awt.Color(255, 255, 255));
        JBSaveCustomer2.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBSaveCustomer2.setForeground(new java.awt.Color(255, 255, 255));
        JBSaveCustomer2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBSaveCustomer2.setText("Save");
        JBSaveCustomer2.setToolTipText("");
        JBSaveCustomer2.setActionCommand("");
        JBSaveCustomer2.setBorder(null);
        JBSaveCustomer2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBSaveCustomer2.setOpaque(false);
        JBSaveCustomer2.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_CategoryBrand.add(JBSaveCustomer2);
        JBSaveCustomer2.setBounds(590, 210, 70, 44);

        JBInsertNewCustomer2.setBackground(new java.awt.Color(255, 255, 255));
        JBInsertNewCustomer2.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        JBInsertNewCustomer2.setForeground(new java.awt.Color(255, 255, 255));
        JBInsertNewCustomer2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button.png"))); // NOI18N
        JBInsertNewCustomer2.setText("New category");
        JBInsertNewCustomer2.setToolTipText("");
        JBInsertNewCustomer2.setBorder(null);
        JBInsertNewCustomer2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBInsertNewCustomer2.setOpaque(false);
        JBInsertNewCustomer2.setPreferredSize(new java.awt.Dimension(148, 44));
        JP_CategoryBrand.add(JBInsertNewCustomer2);
        JBInsertNewCustomer2.setBounds(460, 210, 120, 44);

        JTCcategoryID.setEditable(false);
        JP_CategoryBrand.add(JTCcategoryID);
        JTCcategoryID.setBounds(490, 170, 90, 30);
        JP_CategoryBrand.add(JTCCategoryName);
        JTCCategoryName.setBounds(640, 170, 230, 30);

        jLabel91.setFont(new java.awt.Font("Roboto", 1, 48)); // NOI18N
        jLabel91.setText("brand");
        JP_CategoryBrand.add(jLabel91);
        jLabel91.setBounds(120, 70, 260, 120);

        jLabel92.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel92.setText("ID");
        JP_CategoryBrand.add(jLabel92);
        jLabel92.setBounds(460, 170, 70, 30);

        jLabel93.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel93.setText("Name");
        JP_CategoryBrand.add(jLabel93);
        jLabel93.setBounds(590, 170, 40, 30);

        jLabel94.setFont(new java.awt.Font("Roboto", 1, 48)); // NOI18N
        jLabel94.setText("category");
        JP_CategoryBrand.add(jLabel94);
        jLabel94.setBounds(580, 90, 190, 70);

        jPanel1.add(JP_CategoryBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 910, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1100, 650));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitMousePressed
        // TODO add your handling code here:   

        int selected = JOptionPane.showConfirmDialog(null, "Are you sure", "Alert", JOptionPane.YES_NO_OPTION);
        if (selected == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_exitMousePressed
    private boolean CheckFiledisEmptyCustomer() {
        if (JTCid.getText().trim().equals("") || JTCname.getText().trim().equals("")
                || JTACaddress.getText().trim().equals("") || JTCcontact.getText().trim().equals("")
                || JTCemail.getText().trim().equals("") || JDCC_DOB == null || JDCC_dateadded == null
                || JTCpointsEarned.getText().trim().equals("") || JTCuserAdded.getText().trim().equals("")) {
            return true;
        } else {
            return false;
        }

    }

    public void updateCustomertable() {
        Dbconnection db = new Dbconnection();
        String sql1 = "select * from customer";
        try {
            Statement st = db.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql1);
            DefaultTableModel tmm = (DefaultTableModel) jTableCustomer.getModel();
            tmm.setRowCount(0);
            while (rs.next()) {

                Object ob[] = {rs.getInt("customer_id"), rs.getString("customer_name"), rs.getString("customer_address"), rs.getString("customer_contact"),
                    rs.getString("customer_email"), rs.getDate("customer_DOB"), rs.getDouble("customer_points_earned"),
                    rs.getDate("date_added") + " " + rs.getTime("date_added"), rs.getString("user_added")};
                tmm.addRow(ob);
            }

            db.getConnection().close();
        } catch (SQLException e) {
            //errorlogin.setText("error");
        }

    }

    public void updateproduct_table() {

        String sql1 = "select * from product";

        try {
            Statement Statement2 = connect.getConnection().createStatement();
            ResultSet ResultSet2 = null;
            ResultSet2 = Statement2.executeQuery(sql1);
            DefaultTableModel tmm = (DefaultTableModel) jTproduct.getModel();
            tmm.setRowCount(0);
            while (ResultSet2.next()) {

                Object o[] = {ResultSet2.getInt("product_id"), ResultSet2.getString("product_title"), ResultSet2.getString("product_brand"), ResultSet2.getString("product_category"), ResultSet2.getFloat("product_price"),
                    ResultSet2.getDate("date_added") + " " + ResultSet2.getTime("date_added"), ResultSet2.getString("user_added"), ResultSet2.getString("product_barcode"), ResultSet2.getString("product_description")};
                tmm.addRow(o);

            }

        } catch (SQLException e) {
            //errorlogin.setText("error");
        }

    }

    public void updateSupplier_table() {

        String sql1 = "select * from supplier";

        try {
            Statement Statement2 = connect.getConnection().createStatement();
            ResultSet ResultSet2 = null;
            ResultSet2 = Statement2.executeQuery(sql1);
            DefaultTableModel tmm = (DefaultTableModel) jTSupplier.getModel();
            tmm.setRowCount(0);
            while (ResultSet2.next()) {

                Object o[] = {ResultSet2.getInt("supplier_id"), ResultSet2.getString("supplier_name"), ResultSet2.getString("supplier_address"), ResultSet2.getString("supplier_post_code"),
                    ResultSet2.getString("supplier_city"), ResultSet2.getString("supplier_contact"),
                    ResultSet2.getDate("date_added") + " " + ResultSet2.getTime("date_added"), ResultSet2.getString("user_added")};
                tmm.addRow(o);

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error" + e);
        }

    }

    public void clearCustomerFileds() {

        JTCid.setText("");
        JTCname.setText("");
        JTACaddress.setText("");
        JTCcontact.setText("");
        JTCemail.setText("");
        JDCC_DOB.setDate(null);
        JTCpointsEarned.setText("");
        JDCC_dateadded.setDate(null);
        JTCuserAdded.setText("");
        TFSearchCustomer.setText("");
        updateCustomertable();
        JBSaveCustomer.setEnabled(false);
    }

    public void updateUser_table() {

        String sql1 = "select * from Ra_user";

        try {
            Statement Statement2 = connect.getConnection().createStatement();
            ResultSet ResultSet2 = null;
            ResultSet2 = Statement2.executeQuery(sql1);
            DefaultTableModel tmm = (DefaultTableModel) jTUser.getModel();
            tmm.setRowCount(0);
            while (ResultSet2.next()) {

                Object o[] = {ResultSet2.getInt("uid"), ResultSet2.getString("user_fname"), ResultSet2.getString("user_lname"), ResultSet2.getDouble("Salary"), ResultSet2.getString("position"),
                    ResultSet2.getString("username"), ResultSet2.getString("password"),
                    ResultSet2.getString("user_address"), ResultSet2.getString("user_contact_no"), ResultSet2.getString("user_email"), ResultSet2.getString("date_added")};
                tmm.addRow(o);

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error" + e);
        }

    }

    public String getUseraAcess() {

        String sql = "select * from Ra_user where Ra_user.username='" + user_name.getText() + "' and Ra_user.password='" + password.getText() + "'";

        try {
            Statement Statement2 = connect.getConnection().createStatement();
            ResultSet ResultSet2 = null;
            ResultSet2 = Statement2.executeQuery(sql);
            while (ResultSet2.next()) {

                Object o = ResultSet2.getString("position");
                return o.toString();
            }
        } catch (SQLException e) {
            //errorlogin.setText("error");
        }
        return null;
    }


    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:

        for (double i = 0.0; i <= 1.0; i = i + 0.1) {
            String value = i + "";
            float f = Float.valueOf(value);
            this.setOpacity(f);
            try {
                Thread.sleep(50);
            } catch (Exception e) {

            }
        }
    }//GEN-LAST:event_formWindowOpened

    private void JLProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLProductsMouseClicked
        // TODO add your handling code here:

        JBedit_product.setEnabled(false);
        JBDelete_product.setEnabled(false);
        if ("Login Successfully".equals(errorlogin.getText().trim()) && ("admin".equals(getUseraAcess())
                || "Inventory officer".equals(getUseraAcess()) || "reviewer".equals(getUseraAcess())
                || "reviewer".equals(getUseraAcess()))) {
            setUnVisible(); // set all JPanel to Unvisible
            JP_product.setVisible(true);

            setColorToBlue(JLProducts, JLDashboard, JLCustomers, JLSales, JLStock, JLSupplier, JLCategory, JLloginRec, JLuser);
            updateproduct_table();
        }

    }//GEN-LAST:event_JLProductsMouseClicked

    private void JLDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLDashboardMouseClicked
        // TODO add your handling code here:

        //JLDashboard.setActionMap;               
        if ("Login Successfully".equals(errorlogin.getText().trim()) && ("admin".equals(getUseraAcess())
                || "seller".equals(getUseraAcess()) || "Inventory officer".equals(getUseraAcess()) || "reviewer".equals(getUseraAcess())
                || "reviewer".equals(getUseraAcess()))) {
            setUnVisible(); // set all JPanel to Unvisible
            JP_Dashboard.setVisible(true);

            setColorToBlue(JLDashboard, JLProducts, JLCustomers, JLSales, JLStock, JLSupplier, JLCategory, JLloginRec, JLuser);
            barchart();
            piechart();
        }
    }//GEN-LAST:event_JLDashboardMouseClicked

    private void JLCustomersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLCustomersMouseClicked
        // TODO add your handling code here:

        if ("Login Successfully".equals(errorlogin.getText().trim()) && ("admin".equals(getUseraAcess())
                || "seller".equals(getUseraAcess()) || "reviewer".equals(getUseraAcess())
                || "reviewer".equals(getUseraAcess()))) {
            setUnVisible(); // set all JPanel to Unvisible
            JP_Customers.setVisible(true);
            updateCustomertable();
            clearCustomerFileds();
            setColorToBlue(JLCustomers, JLProducts, JLDashboard, JLSales, JLStock, JLSupplier, JLCategory, JLloginRec, JLuser);

            /////////            
        }
    }//GEN-LAST:event_JLCustomersMouseClicked

    private void JLStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLStockMouseClicked
        // TODO add your handling code here:
        if ("Login Successfully".equals(errorlogin.getText().trim()) && ("admin".equals(getUseraAcess())
                || "Inventory officer".equals(getUseraAcess()) || "reviewer".equals(getUseraAcess())
                || "reviewer".equals(getUseraAcess()))) {
            setUnVisible(); // set all JPanel to Unvisible
            JP_Stock.setVisible(true);

            setColorToBlue(JLStock, JLProducts, JLCustomers, JLSales, JLDashboard, JLSupplier, JLCategory, JLloginRec, JLuser);
        }
    }//GEN-LAST:event_JLStockMouseClicked

    private void JLSalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLSalesMouseClicked
        // TODO add your handling code here:
        if ("Login Successfully".equals(errorlogin.getText().trim()) && ("admin".equals(getUseraAcess())
                || "seller".equals(getUseraAcess()) || "reviewer".equals(getUseraAcess()))) {
            setUnVisible(); // set all JPanel to Unvisible
            JP_Sales.setVisible(true);

            setColorToBlue(JLSales, JLProducts, JLCustomers, JLStock, JLDashboard, JLSupplier, JLCategory, JLloginRec, JLuser);
        }
    }//GEN-LAST:event_JLSalesMouseClicked

    private void JLSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLSupplierMouseClicked
        // TODO add your handling code here:
        if ("Login Successfully".equals(errorlogin.getText().trim()) && ("admin".equals(getUseraAcess())
                || "Inventory officer".equals(getUseraAcess()) || "reviewer".equals(getUseraAcess())
                || "reviewer".equals(getUseraAcess()))) {
            JBedit_Supplier.setEnabled(false);
            JBDelete_Supplier.setEnabled(false);
            setUnVisible(); // set all JPanel to Unvisible
            JP_Suppiler.setVisible(true);

            setColorToBlue(JLSupplier, JLProducts, JLCustomers, JLStock, JLDashboard, JLSales, JLCategory, JLloginRec, JLuser);
            updateSupplier_table();
        }
    }//GEN-LAST:event_JLSupplierMouseClicked

    private void JLHideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLHideMouseClicked
        // TODO add your handling code here:
        password.setEchoChar((char) 0);
        ImageIcon imageUpdate = new ImageIcon(getClass().getResource("/images/vision show.png"));
        JLShow.setIcon(imageUpdate);
        JLShow.setVisible(true);
        JLShow.setEnabled(true);
        JLHide.setVisible(false);
        JLHide.setEnabled(false);

    }//GEN-LAST:event_JLHideMouseClicked

    private void JLShowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLShowMouseClicked
        // TODO add your handling code here:
        password.setEchoChar((char) 8226);
        JLShow.setVisible(false);
        JLShow.setEnabled(false);
        JLHide.setVisible(true);
        JLHide.setEnabled(true);

    }//GEN-LAST:event_JLShowMouseClicked

    private void passwordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) { // when press enter
            User login = new User(user_name.getText(), password.getText());
            if (user_name.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username is Mandotory");
            } else if (password.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Password is Mandotory");
            } else {

                if (login.verifylogin() == true) {
                    errorlogin.setText("Login Successfully");
                    JLUser.setText(user_name.getText());

                    switch (getUseraAcess()) {
                        case "admin": {
                            setUnVisible(); // set all JPanel to Unvisible
                            jp_Login.setVisible(false);
                            JLStock.setEnabled(true);
                            JLSupplier.setEnabled(true);
                            JLCustomers.setEnabled(true);
                            JLDashboard.setEnabled(true);
                            JLProducts.setEnabled(true);
                            JLSales.setEnabled(true);                       
                            JLCategory.setEnabled(true);   
                            JLloginRec.setEnabled(true);
                           
                            JLuser.setEnabled(true);
                            JP_Dashboard.setVisible(true);
                            break;
                        }
                        case "seller": {
                            setUnVisible(); // set all JPanel to Unvisible
                            labelsunEnabled();// set all labels to uneable
                            jp_Login.setVisible(false);
                            JLCustomers.setEnabled(true);
                            JLDashboard.setEnabled(true);
                            JLSales.setEnabled(true);
                            JP_Dashboard.setVisible(true);
                            break;
                        }
                        case "Inventory officer": {
                            setUnVisible(); // set all JPanel to Unvisible
                            labelsunEnabled();// set all labels to uneable
                            jp_Login.setVisible(false);
                            JLStock.setEnabled(true);
                            JLSupplier.setEnabled(true);
                            JLDashboard.setEnabled(true);
                            JLProducts.setEnabled(true);
                            
                            JLCategory.setEnabled(true);
                            
                            JP_Dashboard.setVisible(true);
                            break;
                        }
                        case "reviewer": {
                            setUnVisible(); // set all JPanel to Unvisible
                            labelsunEnabled();// set all labels to uneable
                            jp_Login.setVisible(false);
                            JLStock.setEnabled(true);
                            JLSupplier.setEnabled(true);
                            JLCustomers.setEnabled(true);
                            JLDashboard.setEnabled(true);
                            JLProducts.setEnabled(true);
                            JLCategory.setEnabled(true);
                            
                            JP_Dashboard.setVisible(true);
                            break;
                        }
                        case "analyst": {
                            setUnVisible(); // set all JPanel to Unvisible
                            labelsunEnabled();// set all labels to uneable
                            jp_Login.setVisible(false);
                            JLStock.setEnabled(true);
                            JLCustomers.setEnabled(true);
                            JLDashboard.setEnabled(true);
                            JLProducts.setEnabled(true);
                            JLSales.setEnabled(true);
                            JLSupplier.setEnabled(true);
                            JLCategory.setEnabled(true);
                            
                            JP_Dashboard.setVisible(true);
                            break;
                        }

                    }
                    setColorToBlue(JLDashboard, JLProducts, JLCustomers, JLSales, JLStock, JLSupplier, JLCategory, JLloginRec, JLuser);
                    barchart();
                    piechart();
                } else {
                    errorlogin.setText("invalid Login");
                }
            }
        } else {

        }
    }//GEN-LAST:event_passwordKeyPressed

    private void user_nameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_user_nameKeyTyped
        // TODO add your handling code here:
        if (user_name.getText().length() > 15) {
            JOptionPane.showMessageDialog(null, "You have reached the limit of Username");
        } else if (user_name.getText().length() != 0 && password.getText().length() != 0) {
            JBLogin.setEnabled(true);
        }
    }//GEN-LAST:event_user_nameKeyTyped

    private void passwordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyTyped
        // TODO add your handling code here:

        if (password.getText().length() > 15) {
            JOptionPane.showMessageDialog(null, "You have reached the limit of password");
        } else if (user_name.getText().length() != 0 && password.getText().length() != 0) {
            JBLogin.setEnabled(true);
        }

    }//GEN-LAST:event_passwordKeyTyped

    private void JP_HeaderMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JP_HeaderMousePressed
        // TODO add your handling code here:
        mouseX = evt.getX();
        mouseY = evt.getY();

    }//GEN-LAST:event_JP_HeaderMousePressed

    private void JP_HeaderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JP_HeaderMouseDragged
        // TODO add your handling code here:

        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        setLocation((x - mouseX), (y - mouseY));
    }//GEN-LAST:event_JP_HeaderMouseDragged

    private void TFSearchProductKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TFSearchProductKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_TFSearchProductKeyTyped

    private void JBAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBAddProductActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JBAddProductActionPerformed

    private void JBAddProducttoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBAddProducttoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JBAddProducttoActionPerformed

    private void jTAddproductpriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTAddproductpriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTAddproductpriceActionPerformed

    private void jTproductpriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTproductpriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTproductpriceActionPerformed

    private void jTproductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTproductMouseClicked

        //Display selected row in JTextFileds
        int i = jTproduct.getSelectedRow();
        TableModel TM = jTproduct.getModel();

        JTproductID.setText(TM.getValueAt(i, 0).toString());
        jTproducttitle.setText(TM.getValueAt(i, 1).toString());
        getBrandsFromDB_forP(); // to get all the brands from db to the combobox then the next line of code will select based on table 
        JCproductBrand.setSelectedItem(TM.getValueAt(i, 2).toString());
        getcategoryFromDB_forP();// to get all the categories from db to the combobox then the next line of code will select based on table 
        jCproductcategory.setSelectedItem(TM.getValueAt(i, 3).toString());
        System.out.println(TM.getValueAt(i, 3).toString());
        jTproductprice.setText(TM.getValueAt(i, 4).toString());
        try {

            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(TM.getValueAt(i, 5).toString());
            jDproductDate.setDate(date);

        } catch (ParseException ex) {
            Logger.getLogger(JRAInventario.class.getName()).log(Level.SEVERE, null, ex);

        }

        jTproductUser.setText(TM.getValueAt(i, 6).toString());
        jTproductbarcode.setText(TM.getValueAt(i, 7).toString());

        jTproductDesc.setText(TM.getValueAt(i, 8).toString());

        /*
        JTCpointsEarned.setText(TM.getValueAt(i, 6).toString());

        try {

            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(TM.getValueAt(i, 7).toString());

            JDCC_dateadded.setDate(date);
            System.out.println(JDCC_dateadded.getDate());

        } catch (ParseException ex) {
            Logger.getLogger(JRAInventario.class.getName()).log(Level.SEVERE, null, ex);

        }

        JTCuserAdded.setText(TM.getValueAt(i, 8).toString());

         */
        // TODO add your handling code here:
    }//GEN-LAST:event_jTproductMouseClicked

    private void jTUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTUserMouseClicked
        // TODO add your handling code here:
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Display selected row in JTextFileds
        int i = jTUser.getSelectedRow();
        TableModel TM = jTUser.getModel();
        JTUserID.setText(TM.getValueAt(i, 0).toString());
        jTUserFname.setText(TM.getValueAt(i, 1).toString());
        jTUserLname.setText(TM.getValueAt(i, 2).toString());
        jTUserSalary.setText(TM.getValueAt(i, 3).toString());
        jCUserPosition.setSelectedItem(TM.getValueAt(i, 4).toString());
        jTUserUsername.setText(TM.getValueAt(i, 5).toString());
        jTUserPassword.setText(TM.getValueAt(i, 6).toString());
        jTUserAddress.setText(TM.getValueAt(i, 7).toString());
        jTUserContactNo.setText(TM.getValueAt(i, 8).toString());
        jTUserEmail.setText(TM.getValueAt(i, 9).toString());


    }//GEN-LAST:event_jTUserMouseClicked

    private void TFSearchUserKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TFSearchUserKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_TFSearchUserKeyTyped

    private void jTUserUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTUserUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTUserUsernameActionPerformed

    private void jTUserSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTUserSalaryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTUserSalaryActionPerformed

    private void jTUserPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTUserPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTUserPasswordActionPerformed

    private void jTUserContactNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTUserContactNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTUserContactNoActionPerformed

    private void jTUserEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTUserEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTUserEmailActionPerformed

    private void JLCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLCategoryMouseClicked
        // TODO add your handling code here:

        if ("Login Successfully".equals(errorlogin.getText().trim()) && ("admin".equals(getUseraAcess())
                || "Inventory officer".equals(getUseraAcess()) || "reviewer".equals(getUseraAcess())
                || "reviewer".equals(getUseraAcess()))) {
            setUnVisible(); // set all JPanel to Unvisible

            JP_CategoryBrand.setVisible(true);

            setColorToBlue(JLCategory, JLProducts, JLCustomers, JLSales, JLDashboard, JLSupplier, JLStock, JLloginRec, JLuser);
        }


    }//GEN-LAST:event_JLCategoryMouseClicked

    private void JLloginRecMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLloginRecMouseClicked
        // TODO add your handling code here:
        if ("Login Successfully".equals(errorlogin.getText().trim()) && ("admin".equals(getUseraAcess()))) {
            setUnVisible(); // set all JPanel to Unvisible
            JOptionPane.showMessageDialog(null, "still not finish");
            //.setVisible(true);

            setColorToBlue(JLloginRec, JLProducts, JLCustomers, JLSales, JLDashboard, JLSupplier, JLStock, JLCategory, JLuser);
        }
    }//GEN-LAST:event_JLloginRecMouseClicked

    private void JLuserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLuserMouseClicked
        // TODO add your handling code here:
        if ("Login Successfully".equals(errorlogin.getText().trim()) && ("admin".equals(getUseraAcess()))) {
            setUnVisible(); // set all JPanel to Unvisible
            JBedit_User.setEnabled(false);
            JBDelete_User.setEnabled(false);
            JP_users.setVisible(true);
            updateUser_table();
            setColorToBlue(JLuser, JLProducts, JLCustomers, JLSales, JLDashboard, JLSupplier, JLStock, JLCategory, JLloginRec);
        }
    }//GEN-LAST:event_JLuserMouseClicked

    private void jTSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTSupplierMouseClicked
        // TODO add your handling code here:

        //Display selected row in JTextFileds
        int i = jTSupplier.getSelectedRow();
        TableModel TM = jTSupplier.getModel();

        JTSupplierID.setText(TM.getValueAt(i, 0).toString());
        jTSupplierName.setText(TM.getValueAt(i, 1).toString());
        jTSupplieraddress.setText(TM.getValueAt(i, 2).toString());
        jTSupplierPostcode.setText(TM.getValueAt(i, 3).toString());
        jTSupplierCity.setText(TM.getValueAt(i, 4).toString());
        jTSupplierContact.setText(TM.getValueAt(i, 5).toString());
        try {

            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(TM.getValueAt(i, 6).toString());
            jDSupplierDate.setDate(date);

        } catch (ParseException ex) {
            Logger.getLogger(JRAInventario.class.getName()).log(Level.SEVERE, null, ex);

        }
        jTSupplierUser.setText(TM.getValueAt(i, 7).toString());

    }//GEN-LAST:event_jTSupplierMouseClicked

    private void TFSearchSupplierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TFSearchSupplierKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_TFSearchSupplierKeyTyped

    private void jTSupplierPostcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTSupplierPostcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTSupplierPostcodeActionPerformed

    private void jTSupplierCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTSupplierCityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTSupplierCityActionPerformed

    private void jTAddSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTAddSupplierMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTAddSupplierMouseClicked

    private void jTAddSupplierPostcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTAddSupplierPostcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTAddSupplierPostcodeActionPerformed

    private void jTAddSupplierCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTAddSupplierCityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTAddSupplierCityActionPerformed

    private void jTAddUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTAddUserMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTAddUserMouseClicked

    private void jTAddUserUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTAddUserUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTAddUserUsernameActionPerformed

    private void jTAddUserSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTAddUserSalaryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTAddUserSalaryActionPerformed

    private void jTAddUserPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTAddUserPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTAddUserPasswordActionPerformed

    private void jTAddUserContactNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTAddUserContactNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTAddUserContactNoActionPerformed

    private void jTAddUserEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTAddUserEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTAddUserEmailActionPerformed

    private void JTCemailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTCemailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTCemailActionPerformed

    private void TFSearchCustomerKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TFSearchCustomerKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_TFSearchCustomerKeyTyped

    private void JTCpointsEarnedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTCpointsEarnedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTCpointsEarnedActionPerformed

    private void jTableCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCustomerMouseClicked
        // TODO add your handling code here:
        
         // TODO add your handling code here:
        //Display selected row in JTextFileds
        JBSaveCustomer.setEnabled(false);        
        int i = jTableCustomer.getSelectedRow();
        TableModel TM = jTableCustomer.getModel();
        JTCid.setText(TM.getValueAt(i, 0).toString());
        JTCname.setText(TM.getValueAt(i, 1).toString());    
        JTACaddress.setText(TM.getValueAt(i, 2).toString());
        JTCcontact.setText(TM.getValueAt(i, 3).toString());
        JTCemail.setText(TM.getValueAt(i, 4).toString());

        try {

            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(TM.getValueAt(i, 5).toString());
            JDCC_DOB.setDate(date);

        } catch (ParseException ex) {
            Logger.getLogger(JRAInventario.class.getName()).log(Level.SEVERE, null, ex);

        }

        JTCpointsEarned.setText(TM.getValueAt(i, 6).toString());

        try {

            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(TM.getValueAt(i, 7).toString());

            JDCC_dateadded.setDate(date);
            

        } catch (ParseException ex) {
            Logger.getLogger(JRAInventario.class.getName()).log(Level.SEVERE, null, ex);

        }

        JTCuserAdded.setText(TM.getValueAt(i, 8).toString());


    }//GEN-LAST:event_jTableCustomerMouseClicked

    private void jTableCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCategoryMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableCategoryMouseClicked

    private void TFSearchCustomer1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TFSearchCustomer1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_TFSearchCustomer1KeyTyped

    private void jTableBrandMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBrandMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableBrandMouseClicked

    private void jTproductprice1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTproductprice1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTproductprice1ActionPerformed

    private void TFSearchProduct1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TFSearchProduct1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_TFSearchProduct1KeyTyped

    private void jTproductprice2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTproductprice2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTproductprice2ActionPerformed

    private void jTproduct1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTproduct1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTproduct1MouseClicked

    private void TFSearchProduct2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TFSearchProduct2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_TFSearchProduct2KeyTyped

    private void jTproductprice3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTproductprice3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTproductprice3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JRAInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JRAInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JRAInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JRAInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JRAInventario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBAddNewSupplier;
    private javax.swing.JButton JBAddNew_User;
    private javax.swing.JButton JBAddProduct;
    private javax.swing.JButton JBAddProductto;
    private javax.swing.JButton JBAdd_Sales;
    private javax.swing.JButton JBClearCustomer;
    private javax.swing.JButton JBClearCustomer1;
    private javax.swing.JButton JBClearCustomer2;
    private javax.swing.JButton JBDeleteCustomer;
    private javax.swing.JButton JBDeleteCustomer1;
    private javax.swing.JButton JBDeleteCustomer2;
    private javax.swing.JButton JBDelete_Supplier;
    private javax.swing.JButton JBDelete_User;
    private javax.swing.JButton JBDelete_product;
    private javax.swing.JButton JBDelete_product1;
    private javax.swing.JButton JBInsertNewCustomer;
    private javax.swing.JButton JBInsertNewCustomer1;
    private javax.swing.JButton JBInsertNewCustomer2;
    private javax.swing.JButton JBLogin;
    private javax.swing.JButton JBSaveCustomer;
    private javax.swing.JButton JBSaveCustomer1;
    private javax.swing.JButton JBSaveCustomer2;
    private javax.swing.JButton JBSearchCustomer;
    private javax.swing.JButton JBSearchCustomer1;
    private javax.swing.JButton JBSearchProduct;
    private javax.swing.JButton JBSearchProduct1;
    private javax.swing.JButton JBSearchProduct2;
    private javax.swing.JButton JBSearchSupplier;
    private javax.swing.JButton JBSearchUser;
    private javax.swing.JButton JBSupplierlock;
    private javax.swing.JButton JBUpdateCustomer;
    private javax.swing.JButton JBUpdateCustomer1;
    private javax.swing.JButton JBUpdateCustomer2;
    private javax.swing.JButton JBUserclock;
    private javax.swing.JButton JBadd_product;
    private javax.swing.JButton JBadd_product1;
    private javax.swing.JButton JBadd_supplier;
    private javax.swing.JButton JBadd_user;
    private javax.swing.JButton JBedit_Supplier;
    private javax.swing.JButton JBedit_User;
    private javax.swing.JButton JBedit_product;
    private javax.swing.JButton JBedit_product1;
    private javax.swing.JButton JBproductlock;
    private javax.swing.JButton JBproductlock1;
    private javax.swing.JComboBox<String> JCAddproductBrand;
    private javax.swing.JComboBox<String> JCproductBrand;
    private javax.swing.JComboBox<String> JCproductBrand1;
    private javax.swing.JComboBox<String> JCproductBrand2;
    private com.toedter.calendar.JDateChooser JDCC_DOB;
    private com.toedter.calendar.JDateChooser JDCC_dateadded;
    private javax.swing.JLabel JLCategory;
    private javax.swing.JLabel JLCustomers;
    private javax.swing.JLabel JLDashboard;
    private javax.swing.JLabel JLHide;
    private javax.swing.JLabel JLLogin;
    private javax.swing.JLabel JLProducts;
    private javax.swing.JLabel JLSales;
    private javax.swing.JLabel JLShow;
    private javax.swing.JLabel JLStock;
    private javax.swing.JLabel JLSupplier;
    private javax.swing.JLabel JLTitle;
    private javax.swing.JLabel JLUser;
    private javax.swing.JLabel JLloginRec;
    private javax.swing.JLabel JLuser;
    private javax.swing.JPanel JP_AddSales;
    private javax.swing.JPanel JP_AddSupplier;
    private javax.swing.JPanel JP_Addproduct;
    private javax.swing.JPanel JP_Addusers;
    private javax.swing.JPanel JP_Bar_chart;
    private javax.swing.JScrollPane JP_CategoryAndBrand;
    private javax.swing.JScrollPane JP_CategoryAndBrand1;
    private javax.swing.JPanel JP_CategoryBrand;
    private javax.swing.JPanel JP_Customers;
    private javax.swing.JPanel JP_Dashboard;
    private javax.swing.JPanel JP_Header;
    private javax.swing.JPanel JP_Pie_chart;
    private javax.swing.JPanel JP_Sales;
    private javax.swing.JPanel JP_Stock;
    private javax.swing.JPanel JP_Suppiler;
    private javax.swing.JPanel JP_product;
    private javax.swing.JPanel JP_users;
    private javax.swing.JTextArea JTACaddress;
    private javax.swing.JTextField JTAddSupplierID;
    private javax.swing.JTextField JTAddUserID;
    private javax.swing.JTextField JTAddproductID;
    private javax.swing.JTextField JTCBrandID;
    private javax.swing.JTextField JTCBrandname;
    private javax.swing.JTextField JTCCategoryName;
    private javax.swing.JTextField JTCcategoryID;
    private javax.swing.JTextField JTCcontact;
    private javax.swing.JTextField JTCemail;
    private javax.swing.JTextField JTCid;
    private javax.swing.JTextField JTCname;
    private javax.swing.JTextField JTCpointsEarned;
    private javax.swing.JTextField JTCuserAdded;
    private javax.swing.JTextField JTSupplierID;
    private javax.swing.JTextField JTUserID;
    private javax.swing.JTextField JTproductID;
    private javax.swing.JTextField JTproductID1;
    private javax.swing.JTextField JTproductID2;
    private javax.swing.JTextField TFSearchCustomer;
    private javax.swing.JTextField TFSearchCustomer1;
    private javax.swing.JTextField TFSearchProduct;
    private javax.swing.JTextField TFSearchProduct1;
    private javax.swing.JTextField TFSearchProduct2;
    private javax.swing.JTextField TFSearchSupplier;
    private javax.swing.JTextField TFSearchUser;
    private javax.swing.JLabel errorlogin;
    private javax.swing.JLabel exit;
    private javax.swing.JComboBox<String> jCAddUserPosition;
    private javax.swing.JComboBox<String> jCAddproductcategory;
    private javax.swing.JComboBox<String> jCUserPosition;
    private javax.swing.JComboBox<String> jCproductcategory;
    private javax.swing.JComboBox<String> jCproductcategory1;
    private javax.swing.JComboBox<String> jCproductcategory2;
    private javax.swing.JComboBox<String> jCselectSearch;
    private javax.swing.JComboBox<String> jCselectSearch1;
    private javax.swing.JComboBox<String> jCselectSearch2;
    private javax.swing.JComboBox<String> jCselectSearchCustomer;
    private javax.swing.JComboBox<String> jCselectSearchCustomer1;
    private javax.swing.JComboBox<String> jCselectSearchSupplier;
    private javax.swing.JComboBox<String> jCselectUserSearch;
    private com.toedter.calendar.JDateChooser jDAddSupplierDate;
    private com.toedter.calendar.JDateChooser jDAddUserDate;
    private com.toedter.calendar.JDateChooser jDAddproductDate;
    private com.toedter.calendar.JDateChooser jDSupplierDate;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDproductDate;
    private com.toedter.calendar.JDateChooser jDproductDate1;
    private com.toedter.calendar.JDateChooser jDproductDate2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JScrollPane jScrollPaneAddSales;
    private javax.swing.JScrollPane jScrollPaneCutomer;
    private javax.swing.JScrollPane jScrollPaneSales;
    private javax.swing.JTable jTAddSupplier;
    private javax.swing.JTextField jTAddSupplierCity;
    private javax.swing.JTextField jTAddSupplierContact;
    private javax.swing.JTextField jTAddSupplierName;
    private javax.swing.JTextField jTAddSupplierPostcode;
    private javax.swing.JTextField jTAddSupplierUser;
    private javax.swing.JTextArea jTAddSupplieraddress;
    private javax.swing.JTable jTAddUser;
    private javax.swing.JTextArea jTAddUserAddress;
    private javax.swing.JTextField jTAddUserContactNo;
    private javax.swing.JTextField jTAddUserEmail;
    private javax.swing.JTextField jTAddUserFname;
    private javax.swing.JTextField jTAddUserLname;
    private javax.swing.JTextField jTAddUserPassword;
    private javax.swing.JTextField jTAddUserSalary;
    private javax.swing.JTextField jTAddUserUsername;
    private javax.swing.JTextArea jTAddproductDesc;
    private javax.swing.JTextField jTAddproductUser;
    private javax.swing.JTextField jTAddproductbarcode;
    private javax.swing.JTextField jTAddproductprice;
    private javax.swing.JTextField jTAddproducttitle;
    private javax.swing.JTable jTSupplier;
    private javax.swing.JTextField jTSupplierCity;
    private javax.swing.JTextField jTSupplierContact;
    private javax.swing.JTextField jTSupplierName;
    private javax.swing.JTextField jTSupplierPostcode;
    private javax.swing.JTextField jTSupplierUser;
    private javax.swing.JTextArea jTSupplieraddress;
    private javax.swing.JTable jTUser;
    private javax.swing.JTextArea jTUserAddress;
    private javax.swing.JTextField jTUserContactNo;
    private javax.swing.JTextField jTUserEmail;
    private javax.swing.JTextField jTUserFname;
    private javax.swing.JTextField jTUserLname;
    private javax.swing.JTextField jTUserPassword;
    private javax.swing.JTextField jTUserSalary;
    private javax.swing.JTextField jTUserUsername;
    private javax.swing.JTable jTableAddSales;
    private javax.swing.JTable jTableBrand;
    private javax.swing.JTable jTableCategory;
    private javax.swing.JTable jTableCustomer;
    private javax.swing.JTable jTableSales;
    private javax.swing.JTable jTaddProduct;
    private javax.swing.JTable jTproduct;
    private javax.swing.JTable jTproduct1;
    private javax.swing.JTextArea jTproductDesc;
    private javax.swing.JTextArea jTproductDesc1;
    private javax.swing.JTextField jTproductUser;
    private javax.swing.JTextField jTproductUser1;
    private javax.swing.JTextField jTproductUser2;
    private javax.swing.JTextField jTproductbarcode;
    private javax.swing.JTextField jTproductbarcode1;
    private javax.swing.JTextField jTproductbarcode2;
    private javax.swing.JTextField jTproductprice;
    private javax.swing.JTextField jTproductprice1;
    private javax.swing.JTextField jTproductprice2;
    private javax.swing.JTextField jTproductprice3;
    private javax.swing.JTextField jTproducttitle;
    private javax.swing.JTextField jTproducttitle1;
    private javax.swing.JTextField jTproducttitle2;
    private javax.swing.JPanel jp_Login;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField user_name;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        // String com = e.getActionCommand();
        //e.getSource() == JBLogin ||

        if (e.getSource() == JBLogin) {

            User user = new User(user_name.getText(), password.getText());
            /*
            if (user_name.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username is Mandotory");
            } else if (password.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Password is Mandotory");
            } else {

                if (user.verifylogin() == true) {
                    errorlogin.setText("Login Successfully");
                    JLUser.setText(user_name.getText());
                    user.addLoginRec();
                    jp_Login.setVisible(false);
                    JLStock.setEnabled(true);
                    JLSupplier.setEnabled(true);
                    JLCustomers.setEnabled(true);
                    JLDashboard.setEnabled(true);
                    JLProducts.setEnabled(true);
                    JLSales.setEnabled(true);
                    JP_Dashboard.setVisible(true);
                    setColorToBlue(JLDashboard, JLProducts, JLCustomers, JLSales, JLStock, JLSupplier,JLPurchaseOrder,JLCategory,JLbrand,JLloginRec,JLuser);
                    barchart();
                    piechart();

                } else {
                    errorlogin.setText("invalid Login");
                }
            }*/
        } //////////////////second button
        else if (e.getSource() == JBadd_product) {
            jTaddProduct.removeAll(); // to clean the table to add another poducts
            Jaddproduct jfaddprod = new Jaddproduct();
            JP_Addproduct.setVisible(true);
            JP_product.setVisible(false);
            PressedTime = getCurrentTime(); // to record the time when the user click on the button
            jTAddproductUser.setText(JLUser.getText()); // to get the username who login in the system

            try {
                Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(getCurrentTime()); // to add current date to  Jdate
                jDAddproductDate.setDate(date);
            } catch (ParseException ex) {
                //Logger.getLogger(JRAInventario.class.getName()).log(Level.SEVERE, null, ex);
                //   System.out.print(jDAddproductDate.getDateFormatString());

            }
            getlastIDFromDB(); // to set id for the new product
            get_categoriesFromDB(); // to call method to set the categiries items
            get_BrandsFromDB(); // to call method to set the Brands items

            //  box.getEditor().setItem("Text Has Changed"); 
        } else if (e.getSource() == JBAdd_Sales) {
            setUnVisible();
            JP_AddSales.setVisible(true);
            JP_Sales.setVisible(false);

        } else if (e.getSource() == JBAddProductto) { // for the button which in the product interface when the user finish adding info and click on add button

            try {
                Product pro = new Product(jTAddproducttitle.getText(), JCAddproductBrand.getSelectedItem().toString(), jCAddproductcategory.getSelectedItem().toString(),
                        Float.parseFloat(jTAddproductprice.getText().toString()), getCurrentTime(), jTAddproductUser.getText(), jTAddproductbarcode.getText(), jTAddproductDesc.getText());

                pro.Add_product(); // to call the method in product class to add the product to the database 

                String sql = "select * from product where product.date_added > '" + PressedTime + "'"; // to show the product which has added at the current time 
                Statement Statement2 = connect.getConnection().createStatement();
                ResultSet ResultSet2 = null;
                ResultSet2 = Statement2.executeQuery(sql);
                DefaultTableModel tmm = (DefaultTableModel) jTaddProduct.getModel();
                tmm.setRowCount(0);
                while (ResultSet2.next()) {

                    Object o[] = {ResultSet2.getInt("product_id"), ResultSet2.getString("product_title"), ResultSet2.getString("product_brand"), ResultSet2.getString("product_category"), ResultSet2.getFloat("product_price"),
                        ResultSet2.getDate("date_added") + " " + ResultSet2.getTime("date_added"), ResultSet2.getString("user_added"), ResultSet2.getString("product_barcode"), ResultSet2.getString("product_description")};
                    tmm.addRow(o);

                }
                getlastIDFromDB(); // to set id for the next product

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "unable to add product there is some details are missing ");
                //errorlogin.setText("error");
            }

        } else if (e.getSource() == JBSearchProduct) { // for the search product button
            searchForProduct();
        } else if (e.getSource() == JBproductlock) { // to open the lock for edit and delete product
            JBDelete_product.setEnabled(true);
            JBedit_product.setEnabled(true);
        } else if (e.getSource() == JBedit_product) { // when the user click on update product
            Product edit = new Product(Integer.parseInt(JTproductID.getText()), jTproducttitle.getText(), JCproductBrand.getSelectedItem().toString(), jCproductcategory.getSelectedItem().toString(), Float.parseFloat(jTproductprice.getText()), jTproductbarcode.getText(), jTproductDesc.getText());
            edit.edit_product();
            updateproduct_table();

        } else if (e.getSource() == JBDelete_product) { // when the user click on delete product

            int selected = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this product name: " + jTproducttitle.getText() + "?", "Alert", JOptionPane.YES_NO_OPTION);
            if (selected == JOptionPane.YES_OPTION) {
                Product delete = new Product(Integer.parseInt(JTproductID.getText()));
                delete.delete_product();
                updateproduct_table();
            }

        } else if (e.getSource() == JBedit_Supplier) {
            Supplier suppedit = new Supplier(Integer.parseInt(JTSupplierID.getText()), jTSupplierName.getText(), jTSupplieraddress.getText(), jTSupplierPostcode.getText(), jTSupplierCity.getText(), jTSupplierContact.getText());
            suppedit.UpdateToDB();
            updateUser_table();

        } else if (e.getSource() == JBDelete_Supplier) {
            int selected = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this supplier name: " + jTSupplierName.getText() + "?", "Alert", JOptionPane.YES_NO_OPTION);
            if (selected == JOptionPane.YES_OPTION) {
                Supplier suppedelete = new Supplier(Integer.parseInt(JTSupplierID.getText()));
                suppedelete.DeleteFromDB();
                updateUser_table();
            }

        } else if (e.getSource() == JBSearchSupplier) {

            searchForSupplier();
        } else if (e.getSource() == JBadd_supplier) {

            jTaddProduct.removeAll(); // to clean the table to add another poducts
            Jaddproduct jfaddprod = new Jaddproduct();
            JP_AddSupplier.setVisible(true);
            JP_Suppiler.setVisible(false);
            PressedTime = getCurrentTime(); // to record the time when the user click on the button
            jTAddSupplierUser.setText(JLUser.getText()); // to get the username who login in the system

            try {
                Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(getCurrentTime()); // to add current date to  Jdate
                jDAddSupplierDate.setDate(date);
            } catch (ParseException ex) {
                //Logger.getLogger(JRAInventario.class.getName()).log(Level.SEVERE, null, ex);
                //   System.out.print(jDAddproductDate.getDateFormatString());

            }
            Supplier supp = new Supplier();
            int ID = supp.getlastIDFromDB();
            JTAddSupplierID.setText(Integer.toString(ID)); // to set id for the next supplier

        } else if (e.getSource() == JBAddNewSupplier) {/////////////////////////////////////////////
            try {
                Supplier addsupp = new Supplier(jTAddSupplierName.getText(), jTAddSupplieraddress.getText(), jTAddSupplierPostcode.getText(), jTAddSupplierCity.getText(), jTAddSupplierContact.getText(), getCurrentTime(), jTAddSupplierUser.getText());

                addsupp.addToDB();// to call the method in product class to add the supplier to the database 

                String sql = "select * from supplier where supplier.date_added > '" + PressedTime + "'"; // to show the supplier which has added at the current time 
                Statement Statement2 = connect.getConnection().createStatement();
                ResultSet ResultSet2 = null;
                ResultSet2 = Statement2.executeQuery(sql);
                DefaultTableModel tmm = (DefaultTableModel) jTAddSupplier.getModel();
                tmm.setRowCount(0);
                while (ResultSet2.next()) {

                    Object o[] = {ResultSet2.getInt("supplier_id"), ResultSet2.getString("supplier_name"), ResultSet2.getString("supplier_address"), ResultSet2.getString("supplier_post_code"),
                        ResultSet2.getString("supplier_city"), ResultSet2.getString("supplier_contact"),
                        ResultSet2.getDate("date_added") + " " + ResultSet2.getTime("date_added"), ResultSet2.getString("user_added")};
                    tmm.addRow(o);

                }
                Supplier supp = new Supplier();
                int ID = supp.getlastIDFromDB();
                JTAddSupplierID.setText(Integer.toString(ID)); // to set id for the next supplier

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "unable to add product there is some details are missing ");
                //errorlogin.setText("error");
            }

        } else if (e.getSource() == JBSupplierlock) {
            JBedit_Supplier.setEnabled(true);
            JBDelete_Supplier.setEnabled(true);
        } else if (e.getSource() == JBUserclock) {
            JBDelete_User.setEnabled(true);
            JBedit_User.setEnabled(true);
        } else if (e.getSource() == JBedit_User) {
            User useredit = new User(Integer.parseInt(JTUserID.getText()), jTUserUsername.getText(), jTUserPassword.getText(), jTUserFname.getText(), jTUserLname.getText(), jTUserAddress.getText(), jTUserEmail.getText(), jTUserContactNo.getText(), jCUserPosition.getSelectedItem().toString(), Float.parseFloat(jTUserSalary.getText()));
            useredit.editUser();
            updateUser_table();
        } else if (e.getSource() == JBDelete_User) {
            User userDelete = new User(Integer.parseInt(JTUserID.getText()));
            userDelete.deleteUser();
            updateUser_table();
        } else if (e.getSource() == JBadd_user) {

            jTAddUser.removeAll(); // to clean the table to add another poducts
            Jaddproduct jfaddprod = new Jaddproduct();
            JP_Addusers.setVisible(true);
            JP_users.setVisible(false);
            PressedTime = getCurrentTime(); // to record the time when the user click on the button
            try {
                Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(getCurrentTime()); // to add current date to  Jdate
                jDAddUserDate.setDate(date);
            } catch (ParseException ex) {
            }
            User userid = new User();
            int ID = userid.getlastIDFromDB();
            JTAddUserID.setText(Integer.toString(ID)); // to set id for the next user

        } else if (e.getSource() == JBAddNew_User) {

            User useradd = new User(jTAddUserUsername.getText(), jTAddUserPassword.getText(), jTAddUserFname.getText(), jTAddUserLname.getText(), jTAddUserAddress.getText(), jTAddUserEmail.getText(), jTAddUserContactNo.getText(), jCAddUserPosition.getSelectedItem().toString(), Float.parseFloat(jTAddUserSalary.getText()), getCurrentTime());

            useradd.AddUser();// to call the method in user class to add the user to the database 

            try {

                String sql = "select * from Ra_user where Ra_user.date_added > '" + PressedTime + "'"; // to show the user which has added at the current time 
                Statement Statement2 = connect.getConnection().createStatement();
                ResultSet ResultSet2 = null;
                ResultSet2 = Statement2.executeQuery(sql);
                DefaultTableModel tmm = (DefaultTableModel) jTAddUser.getModel();
                tmm.setRowCount(0);
                while (ResultSet2.next()) {

                    Object o[] = {ResultSet2.getInt("uid"), ResultSet2.getString("user_fname"), ResultSet2.getString("user_lname"), ResultSet2.getDouble("Salary"), ResultSet2.getString("position"),
                        ResultSet2.getString("username"), ResultSet2.getString("password"), ResultSet2.getString("user_address"), ResultSet2.getString("user_contact_no"), ResultSet2.getString("user_email"), ResultSet2.getString("date_added")};
                    tmm.addRow(o);

                }
                getlastIDFromDB(); // to set id for the next user

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "unable to add user there is some details are missing ");
                //errorlogin.setText("error");

            }

        } else if (e.getSource() == JBSearchUser) { // for the search product button

            searchForuser();
        } else if (e.getSource() == JBSaveCustomer) {
            if (CheckFiledisEmptyCustomer() == true) {
                JOptionPane.showMessageDialog(null, "You need first to Insert customer infromation to Save");
            } else {

                SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
                String DOB = ft.format(JDCC_DOB.getDate());

                Customer custadd = new Customer(JTCname.getText(), JTACaddress.getText(), JTCcontact.getText(), JTCemail.getText(), DOB, Double.parseDouble(JTCpointsEarned.getText()), getCurrentTime(), JTCuserAdded.getText());
                custadd.addToDB();
                updateCustomertable();
                clearCustomerFileds();
            }
        } else if (e.getSource() == JBInsertNewCustomer) {//////This for insert new cutomer ID , User , and date add before click to Save 

            clearCustomerFileds();
            JBSaveCustomer.setEnabled(true);
            Customer custID = new Customer();
            int cID = custID.lastIDEnterd() + 1; // after get the last id we will add 1 to get the next 
            JTCid.setText(String.valueOf(cID));
            JTCuserAdded.setText(JLUser.getText());
            JTCpointsEarned.setText("0.0");
            try {
                Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(getCurrentTime()); // to add current date to  Jdate
                JDCC_dateadded.setDate(date);

            } catch (ParseException ex) {
                Logger.getLogger(JRAInventario.class.getName()).log(Level.SEVERE, null, ex);

            }
        } else if (e.getSource() == JBClearCustomer) {
            clearCustomerFileds();

        } else if (e.getSource() == JBUpdateCustomer) {//Update Customer Info To Database
            if (CheckFiledisEmptyCustomer() == true) {
                JOptionPane.showMessageDialog(null, "You need first to select customer infromation to Update");
            } else {
                SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
                String DOB = ft.format(JDCC_DOB.getDate());
                Customer custUpdate = new Customer(Integer.parseInt(JTCid.getText()), JTCname.getText(), JTACaddress.getText(), JTCcontact.getText(), JTCemail.getText(), DOB);
                custUpdate.UpdateToDB();
                updateCustomertable();
                clearCustomerFileds();
            }

        } else if (e.getSource() == JBDeleteCustomer) {//Delete Customer Info from Database

            if (CheckFiledisEmptyCustomer() == true) {
                JOptionPane.showMessageDialog(null, "You need first to select customer infromation to delete");
            } else {
                int selected = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete Customer?\nCustomer name: " + JTCname.getText(), "Alert", JOptionPane.YES_NO_OPTION);
                if (selected == JOptionPane.YES_OPTION) {
                    Customer custDelete = new Customer(Integer.parseInt(JTCid.getText()));
                    custDelete.DeleteFromDB();
                    updateCustomertable();
                    clearCustomerFileds();
                }
            }

        }else if (e.getSource() == JBSearchCustomer) {
        
        searchForCustomer();
        }
        
        

    }
}
