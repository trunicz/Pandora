package frames;

import dataBase.ConnectionMySql;
import design.JPanelConFondo;
import design.designManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.LocalDate;

public class EnviarDinero extends JFrame {

    public static void main ( String[] args ) {
        EnviarDinero enviarDinero = new EnviarDinero ("w");
        enviarDinero.setVisible ( true );
    }


    //declare
    private final ConnectionMySql sql = new ConnectionMySql();
    private final Connection conn = sql.connectionMySql();
    private JTextField textField_cantidad,textField_id_cuenta_destinatario,
            textField_id_cuenta_remitente, textField_description;
    private int x,y;
    private final String user;

    EnviarDinero(String user){
        this.user = user;
        config();
        startLayout();
    }

    void config(){
        this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(300,300);
        this.setLocationRelativeTo(null);
        this.setBackground(new Color (0,0,0,0));
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

    void startLayout() {

        JPanelConFondo panel = new JPanelConFondo ( "src/images/background_2.png" );
        panel.setLayout ( null );
        panel.setBounds ( 0 , 0 , this.getWidth ( ) , this.getHeight ( ) );
        this.add ( panel );

        panel.addMouseListener ( new MouseAdapter ( ) {
            @Override
            public void mousePressed ( MouseEvent e ) {
                label_1MousePressed ( e );
            }
        } );
        panel.addMouseMotionListener ( new MouseAdapter ( ) {
            @Override
            public void mouseDragged ( MouseEvent e ) {
                label_1MouseDragged ( e );
            }
        } );

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


        //////////////////////////////////////////////////////////////////////////////////////////

        textField_id_cuenta_destinatario = new JTextField("DESTINATARIO CUENTA ID");
        textField_id_cuenta_destinatario.setBounds(30,14 * 2 + 10,panel.getWidth()-60,30);
        textField_id_cuenta_destinatario.setBackground( designManager.darkGray);
        textField_id_cuenta_destinatario.setFont(designManager.font.deriveFont(Font.BOLD,15));
        textField_id_cuenta_destinatario.setForeground(designManager.spaceGray);
        textField_id_cuenta_destinatario.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                textField_id_cuenta_destinatario.setText("");
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////

        textField_id_cuenta_remitente = new JTextField("REMITENTE CUENTA ID");
        textField_id_cuenta_remitente.setBounds(30,textField_id_cuenta_destinatario.getY () * 2 + 10,panel.getWidth()-60,30);
        textField_id_cuenta_remitente.setBackground( designManager.darkGray);
        textField_id_cuenta_remitente.setFont(designManager.font.deriveFont(Font.BOLD,15));
        textField_id_cuenta_remitente.setForeground(designManager.spaceGray);
        textField_id_cuenta_remitente.addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                textField_id_cuenta_remitente.setText("");
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////

        textField_cantidad = new JTextField("CANTIDAD");
        textField_cantidad.setBounds(30,textField_id_cuenta_destinatario.getY () * 3 + 20,panel.getWidth()-60,30);
        textField_cantidad.setBackground( designManager.darkGray);
        textField_cantidad.setFont(designManager.font.deriveFont(Font.BOLD,15));
        textField_cantidad.setForeground(designManager.spaceGray);
        textField_cantidad.addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                textField_cantidad.setText("");
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////

        textField_description = new JTextField("DESCRIPCION");
        textField_description.setBounds(30,textField_id_cuenta_destinatario.getY () * 4 + 30,panel.getWidth()-60,30);
        textField_description.setBackground( designManager.darkGray);
        textField_description.setFont(designManager.font.deriveFont(Font.BOLD,15));
        textField_description.setForeground(designManager.spaceGray);
        textField_description.addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                textField_description.setText("");
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////

        JButton button_ok = new JButton("");
        button_ok.setIcon(new ImageIcon("src/images/button_ok.png"));
        button_ok.setBorder(null);
        button_ok.setBackground(new Color(0,0,0,0));
        button_ok.setBounds(panel.getWidth()/2 - 30,panel.getHeight()-50,60,26);
        button_ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed( ActionEvent e) {

                try{
                    if(sendMoney(textField_id_cuenta_destinatario.getText(),textField_id_cuenta_remitente.getText(),textField_cantidad.getText())){
                        close();
                        Notify.text = "    TRANSACCION COMPLETADA";
                        Notify notify = new Notify();
                        notify.setVisible(true);
                    }else {
                        Notify.text = "    VALORES NO ENCONTRADOS";
                        Notify notify = new Notify();
                        notify.setVisible(true);
                    }
                }catch(SQLException ex){
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

        //////////////////////////////////////////////////////////////////////////////////////////

        panel.add ( textField_id_cuenta_destinatario );
        panel.add ( textField_id_cuenta_remitente );
        panel.add( textField_description );
        panel.add ( textField_cantidad );
        panel.add(button_ok);
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

    void close(){
        this.dispose();
    }

    boolean sendMoney(String id_cuenta_destinatario,String id_cuenta_remitente, String cantidad) throws SQLException {

        String info = "select * from usuarios where nombre = '"+user+"'";
        PreparedStatement statement1 = conn.prepareStatement(info);
        ResultSet rss = statement1.executeQuery();
        rss.next();

        String id = rss.getString("id");

        boolean close = false;
        
        if(!id_cuenta_destinatario.equals("DESTINATARIO CUENTA ID") || !id_cuenta_remitente.equals("REMITENTE CUENTA ID") || !cantidad.equals("CANTIDAD")){
            String getcantremitente = "select cantidad from cuentas where id = '"+id_cuenta_remitente+"'";
            PreparedStatement statement = conn.prepareStatement(getcantremitente);
            ResultSet rs = statement.executeQuery();

            String getcantdestinatario = "select cantidad from cuentas where id = '"+id_cuenta_destinatario+"'";
            PreparedStatement statement2 = conn.prepareStatement(getcantdestinatario);
            ResultSet rs2 = statement2.executeQuery();

            if(rs.next() && rs2.next()) {
                close = true;
            }

            double cantidad_rem, cantidad_des;

            cantidad_rem = rs.getDouble("cantidad");
            cantidad_des = rs2.getDouble("cantidad");

            cantidad_rem -= Double.parseDouble(cantidad);
            cantidad_des += Double.parseDouble(cantidad);

            PreparedStatement statement3 = conn.prepareStatement("call sp_update_cuenta(?,?)");
            statement3.setDouble(1, cantidad_rem);
            statement3.setString(2, id_cuenta_remitente);
            PreparedStatement statement4 = conn.prepareStatement("call sp_update_cuenta(?,?)");
            statement4.setDouble(1, cantidad_des);
            statement4.setString(2, id_cuenta_destinatario);

            statement3.execute();
            statement4.execute();

            LocalDate date = LocalDate.now();

            PreparedStatement statement5 = conn.prepareStatement("call sp_insertar_historial('"+id_cuenta_remitente+
                    "','"+id_cuenta_destinatario+"',"+cantidad+",'"+date+"','"+textField_description.getText().toUpperCase()+"','"+id+"');");
            statement5.execute();
        }

        return close;
    }
}
