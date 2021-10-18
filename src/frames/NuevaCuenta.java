package frames;

import dataBase.ConnectionMySql;
import design.JPanelConFondo;
import design.designManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class NuevaCuenta extends JFrame {

    //declare
    private final ConnectionMySql sql = new ConnectionMySql();
    private final Connection conn = sql.connectionMySql();

    private JComboBox<String> comboBox;

    private JTextField textField_cantidad;

    private String user;
    private String password;
    private int x,y;

    NuevaCuenta(String user, String password){

        this.user = user;
        this.password = password;

        config();
        startLayout();

    }

    void config(){
        this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(300,250);
        this.setLocationRelativeTo(null);
        this.setBackground(new Color(0,0,0,0));
        setFrameIcon();

    }
    void setFrameIcon(){
        Toolkit tk = Toolkit.getDefaultToolkit();
        try {
            Image myIcon = tk.getImage("src/images/Logo_Icon.png");
            setIconImage(myIcon);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        repaint();
    }

    void startLayout(){
        JPanelConFondo panel = new JPanelConFondo("src/images/background_2.png");
        panel.setLayout(null);
        panel.setBounds(0,0,this.getWidth(),this.getHeight());
        this.add(panel);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                label_1MousePressed(e);
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                label_1MouseDragged(e);
            }
        });

        JLabel close_icon = new JLabel();
        close_icon.setIcon(new ImageIcon("src/images/exit_icon.png"));
        close_icon.setBounds(this.getWidth()-35, 5, 30, 30);
        close_icon.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                closeOperation();
            }

            @Override
            public void mouseEntered(MouseEvent e){
                close_icon.setIcon(new ImageIcon("src/images/exit_icon_2.png"));
            }

            @Override
            public void mouseExited(MouseEvent e){
                close_icon.setIcon(new ImageIcon("src/images/exit_icon.png"));
            }
        });
        close_icon.setBorder(null);
        close_icon.setBackground(new Color(0, 0, 0, 0));
        panel.add(close_icon);

        //////////////////////////////////////////////////////////////////////////////////////////


        comboBox = new JComboBox<>();
        comboBox.setFont(designManager.font.deriveFont(Font.BOLD,20));
        comboBox.setBounds(30,50,panel.getWidth()-60,30);
        comboBox.setForeground(designManager.spaceGray);
        comboBox.setBackground(designManager.darkGray);
        comboBox.addItem("Ahorro");
        comboBox.addItem("Credito");

        textField_cantidad = new JTextField();
        textField_cantidad.setBounds(30,comboBox.getY()+comboBox.getHeight()*2,panel.getWidth()-60,30);
        textField_cantidad.setBackground(designManager.darkGray);
        textField_cantidad.setFont(designManager.font.deriveFont(Font.BOLD,20));
        textField_cantidad.setForeground(designManager.spaceGray);
        textField_cantidad.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                textField_cantidad.setText("");
            }
        });

        panel.add(comboBox);
        panel.add(textField_cantidad);

        //////////////////////////////////////////////////////////////////////////////////////////
        JButton button_ok = new JButton("");
        button_ok.setIcon(new ImageIcon("src/images/button_ok.png"));
        button_ok.setBorder(null);
        button_ok.setBackground(new Color(0,0,0,0));
        button_ok.setBounds(panel.getWidth()/2 - 30,panel.getHeight()-50,60,26);
        button_ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateAccount();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        button_ok.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button_ok.setIcon(new ImageIcon("src/images/button_ok_2.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button_ok.setIcon(new ImageIcon("src/images/button_ok.png"));
            }
        });

        JLabel label_ok = new JLabel("");
        label_ok.setText("    OK");
        label_ok.setForeground(designManager.whiteGray);
        label_ok.setFont(designManager.getfont().deriveFont(Font.BOLD,15));
        button_ok.add(label_ok);

        panel.add(button_ok);

        repaint();
        validate();
    }

    private void closeOperation(){
        dispose();
    }

    private void label_1MousePressed(MouseEvent e){
        x = e.getX();
        y = e.getY();
    }

    private void label_1MouseDragged(MouseEvent e){
        int x = e.getXOnScreen();
        int y = e.getYOnScreen();

        this.setLocation(x-this.x,y-this.y);
    }


    private void updateAccount() throws SQLException {

        if (user == null || password == null){
            user = "w";
            password = "w";
        }

        String cantidad = textField_cantidad.getText();

        if (textField_cantidad.getText().equals("")){
            Notify.text = "Ingresar un valor";
            Notify notify = new Notify();
            notify.setVisible(true);
        }else {
            String getid = "select id from usuarios where nombre = '"+user+"'";
            PreparedStatement statement = conn.prepareStatement(getid);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");


                System.out.println(user);
                System.out.println(id);
                String insertcuenta = "call sp_insertar_cuenta(?,?,?)";


                PreparedStatement insert_cuenta = conn.prepareStatement(insertcuenta);
                insert_cuenta.setDouble(1, Double.parseDouble(cantidad));
                insert_cuenta.setString(2, id);
                if(Objects.equals(comboBox.getSelectedItem(), "Ahorro")){
                    insert_cuenta.setString(3, "Ahorro");
                }else {
                    insert_cuenta.setString(3, "Credito");
                }
                insert_cuenta.executeQuery();

                Notify.text = "        Cuenta creada";
                Notify notify = new Notify();
                notify.setVisible(true);
                this.dispose();
            }
        }
    }
}