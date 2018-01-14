package com.izdep.app.runner;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class PropertiesManager
{

   private JFrame frame;
   private JTextField textField_jdbcUrl;
   private JTextField textField_Username;
   private JTextField textField_Password;
   private JTextField textField_Domain;
   private JTextField textField_MaxUrls;
   private Properties props;
   private JTextField textField_Root;
   private JCheckBox chckbxResetCrawl;

   /**
    * Launch the application.
    */
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
      {
         public void run()
         {
            try
            {
               PropertiesManager window = new PropertiesManager();
               window.frame.setVisible(true);
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
         }
      });
   }

   /**
    * Create the application.
    * @throws IOException 
    */
   public PropertiesManager() throws IOException
   {
      initialize();
      readProperties();
   }
   
   /**
    * Get all the information from the properties file to fill all the UI 
    * elements
    * @throws IOException
    */
   public void readProperties() throws IOException
   {
      props = new Properties();
      FileInputStream in = new FileInputStream("src/main/resources/application.properties");
      props.load(in);
      in.close();
      
      this.textField_jdbcUrl.setText(props.getProperty("jdbc.url"));
      this.textField_Username.setText(props.getProperty("jdbc.username"));
      this.textField_Password.setText(props.getProperty("jdbc.password"));
      this.textField_Root.setText(props.getProperty("crawler.root"));
      this.textField_Domain.setText(props.getProperty("crawler.domain"));
      this.textField_MaxUrls.setText(props.getProperty("crawler.maxurls"));
      if(props.getProperty("crawler.reset").equals("YES"))
         chckbxResetCrawl.setSelected(true);
      else
         chckbxResetCrawl.setSelected(false);
      
   }
   
   /**
    * Save the new settings entered in the application to the properties file
    * @throws IOException
    */
   public void  setProperties() throws IOException
   {
      props.setProperty("jdbc.url",this.textField_jdbcUrl.getText());
      props.setProperty("jdbc.username",this.textField_Username.getText());
      props.setProperty("jdbc.password",this.textField_Password.getText());
      props.setProperty("crawler.root",this.textField_Root.getText());
      props.setProperty("crawler.domain",this.textField_Domain.getText());
      props.setProperty("crawler.maxurls",this.textField_MaxUrls.getText());
      
      if(chckbxResetCrawl.isSelected())
         props.setProperty("crawler.reset","YES");
      else
         props.setProperty("crawler.reset","NO");
      
      FileOutputStream out = new FileOutputStream("src/main/resources/application.properties");
      props.store(out, null);
   }

   /**
    * Initialize the contents of the frame.
    */
   private void initialize()
   {
      // Frame
      frame = new JFrame();
      frame.setTitle("izdep.kz Properties Manager");
      frame.setBounds(100, 100, 487, 460);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      // Panel with Group Layout
      JPanel panel = new JPanel();
      GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
      groupLayout.setHorizontalGroup(
         groupLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(panel, GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
      );
      groupLayout.setVerticalGroup(
         groupLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(panel, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
      );
      panel.setLayout(null);
      
      /*
       * UI Elements
       */
      JLabel lblJdbcUrl = new JLabel("JDBC URL");
      lblJdbcUrl.setBounds(43, 69, 61, 16);
      panel.add(lblJdbcUrl);
      
      JLabel lblUsername = new JLabel("Username");
      lblUsername.setBounds(43, 118, 78, 16);
      panel.add(lblUsername);
      
      JLabel lblPassword = new JLabel("Password");
      lblPassword.setBounds(43, 163, 61, 16);
      panel.add(lblPassword);
      
      textField_jdbcUrl = new JTextField();
      textField_jdbcUrl.setBounds(133, 63, 309, 28);
      panel.add(textField_jdbcUrl);
      textField_jdbcUrl.setColumns(10);
      
      textField_Username = new JTextField();
      textField_Username.setColumns(10);
      textField_Username.setBounds(133, 112, 309, 28);
      panel.add(textField_Username);
      
      textField_Password = new JTextField();
      textField_Password.setColumns(10);
      textField_Password.setBounds(133, 157, 309, 28);
      panel.add(textField_Password);
      
      JLabel lblDomain = new JLabel("Domain");
      lblDomain.setBounds(43, 308, 61, 16);
      panel.add(lblDomain);
      
      JLabel lblMaxUrls = new JLabel("Max URLs");
      lblMaxUrls.setBounds(43, 355, 61, 16);
      panel.add(lblMaxUrls);
      
      JLabel lblUrlList = new JLabel("Root");
      lblUrlList.setBounds(43, 259, 61, 16);
      panel.add(lblUrlList);
      
      textField_Domain = new JTextField();
      textField_Domain.setColumns(10);
      textField_Domain.setBounds(133, 302, 309, 28);
      panel.add(textField_Domain);
      
      textField_MaxUrls = new JTextField();
      textField_MaxUrls.setColumns(10);
      textField_MaxUrls.setBounds(133, 349, 309, 28);
      panel.add(textField_MaxUrls);
      
      JLabel lblDatabase = new JLabel("Database");
      lblDatabase.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
      lblDatabase.setToolTipText("");
      lblDatabase.setBounds(43, 17, 96, 28);
      panel.add(lblDatabase);
      
      JLabel lblCrawler = new JLabel("Crawler");
      lblCrawler.setToolTipText("");
      lblCrawler.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
      lblCrawler.setBounds(43, 209, 96, 28);
      panel.add(lblCrawler);
      
      // OK button, saves and ends program
      JButton btnOk = new JButton("OK");
      btnOk.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent arg0) {
            try
            {
               setProperties();
            }
            catch (IOException e)
            {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
            frame.dispose();
         }
      });
      btnOk.setBounds(358, 399, 84, 29);
      panel.add(btnOk);
      
      // Apply button, saves but does not close program
      JButton btnApply = new JButton("Apply");
      btnApply.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent arg0) {
            try
            {
               setProperties();
            }
            catch (IOException e)
            {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      });
      btnApply.setBounds(262, 399, 84, 29);
      panel.add(btnApply);
      
      textField_Root = new JTextField();
      textField_Root.setText((String) null);
      textField_Root.setColumns(10);
      textField_Root.setBounds(133, 253, 309, 28);
      panel.add(textField_Root);
      
      chckbxResetCrawl = new JCheckBox("Reset Crawl");
      chckbxResetCrawl.setBounds(133, 400, 128, 23);
      panel.add(chckbxResetCrawl);
      frame.getContentPane().setLayout(groupLayout);
   }
}

