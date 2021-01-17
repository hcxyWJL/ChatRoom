package cn.edu.hcen.client;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.*;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * 聊天线程
 */
public class ChatThreadWindow {
    private String name;
    JComboBox cb;
    private JFrame f;
    JTextArea ta;
    DatagramSocket ds;
    private JTextField tf;
    private static int total;// 在线人数统计
    JButton searchBtn;
    public ChatThreadWindow(String name,DatagramSocket ds) {
        this.ds=ds;
        this.name=name;

        f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(600, 400);
        f.setTitle("聊天室" + " - " + name + "     当前在线人数:" + ++total);
        f.setLocation(300, 200);
        ta = new JTextArea();
        JScrollPane sp = new JScrollPane(ta);
        ta.setEditable(false);
        tf = new JTextField();
        cb = new JComboBox();
        cb.addItem("All");
        JButton jb = new JButton("私聊窗口");
        JPanel pl = new JPanel(new BorderLayout());
        pl.add(cb);
        pl.add(jb, BorderLayout.WEST);
        JPanel p = new JPanel(new BorderLayout());
        p.add(pl, BorderLayout.WEST);
        p.add(tf);
        tf.addKeyListener(
                new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode()==KeyEvent.VK_ENTER)
                        {
                            String messageTO=(String)cb.getSelectedItem();
                            if ("All".equals(messageTO))
                            {
                                //群聊
                                SendALL();
                                searchBtn.doClick();
                            }
                            else {
                                //私聊
                                oneTOone();
                            }
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                }
        );
        f.getContentPane().add(p, BorderLayout.SOUTH);
        f.getContentPane().add(sp);
        f.setVisible(true);


        GetMessageThread getMessageThread=new GetMessageThread(this);
        getMessageThread.start();
        showXXXintochatromm();
        showXXXinchatromm();
    }
    public void oneTOone()
    {
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String username_db = "opts";
        String password_db = "opts1234";
        PreparedStatement pstm=null;
        Connection conn=null;
        try {
            conn= DriverManager.getConnection(url,username_db,password_db);
            String sql="SELECT  USERNAME,ip,port FROM users WHERE status='online'and USERNAME=?";
            pstm=conn.prepareStatement(sql);
            String messageTO=(String)cb.getSelectedItem();
            pstm.setString(1,messageTO);
            ResultSet rs=pstm.executeQuery();
            while (rs.next())
            {
                String username=rs.getString("USERNAME");
                String ip= rs.getString("IP");
                int port=rs.getInt("PORT");
                System.out.println(ip);
                System.out.println(port);
                byte [] ipB=new byte[4];
                String ips[]=ip.split("\\.");

                for (int i=0;i<ips.length;i++)
                {
                    ipB[i]=(byte)Integer.parseInt(ips[i]);
                }
                if (!username.equals(name))
                {
                    String message=tf.getText();
                    byte []m=message.getBytes();
                    DatagramPacket dp=new DatagramPacket(m,m.length);
                    System.out.println("");
                    dp.setAddress(InetAddress.getByAddress(ipB));
                    dp.setPort(port);
                    DatagramSocket ds=new DatagramSocket();
                    ds.send(dp);//投递
                }
            }
        } catch (SQLException | UnknownHostException | SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void SendALL()
    {
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String username_db = "opts";
        String password_db = "opts1234";
        PreparedStatement pstm=null;
        Connection conn=null;
        try {
            conn= DriverManager.getConnection(url,username_db,password_db);
            String sql="SELECT  USERNAME,ip,port FROM users WHERE status='online'";
            pstm=conn.prepareStatement(sql);
            ResultSet rs=pstm.executeQuery();
            while (rs.next())
            {
                String username=rs.getString("USERNAME");
                String ip= rs.getString("IP");
                int port=rs.getInt("PORT");
                System.out.println(ip);
                System.out.println(port);
                byte [] ipB=new byte[4];
                String ips[]=ip.split("\\.");

                for (int i=0;i<ips.length;i++)
                {
                    ipB[i]=(byte)Integer.parseInt(ips[i]);
                }
                if (!username.equals(name))
                {
                    String message=tf.getText();
                    byte []m=message.getBytes();
                    DatagramPacket dp=new DatagramPacket(m,m.length);
                    System.out.println("");
                    dp.setAddress(InetAddress.getByAddress(ipB));
                    dp.setPort(port);
                    DatagramSocket ds=new DatagramSocket();
                    ds.send(dp);//投递
                }
            }
        } catch (SQLException | UnknownHostException | SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showXXXinchatromm()
    {
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String username_db = "opts";
        String password_db = "opts1234";
        PreparedStatement pstm=null;
        Connection conn=null;
        try {
            conn= DriverManager.getConnection(url,username_db,password_db);
            String sql="SELECT  USERNAME,ip,port FROM users WHERE status='online' and  USERNAME!=?";
            pstm=conn.prepareStatement(sql);
            pstm.setString(1,name);
            ResultSet rs=pstm.executeQuery();
            while (rs.next())
            {
                String username=rs.getString("USERNAME");
                String message=username+"正在聊天室";
                ta.append(message);
                cb.addItem(username);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void showXXXintochatromm()
    {
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String username_db = "opts";
        String password_db = "opts1234";
        PreparedStatement pstm=null;
        Connection conn=null;
        try {
            conn= DriverManager.getConnection(url,username_db,password_db);
            String sql="SELECT  USERNAME,ip,port FROM users WHERE status='online'";
            pstm=conn.prepareStatement(sql);
            ResultSet rs=pstm.executeQuery();
            while (rs.next())
            {
                String username=rs.getString("USERNAME");
                String ip= rs.getString("IP");
                int port=rs.getInt("PORT");
                System.out.println(ip);
                System.out.println(port);
                byte [] ipB=new byte[4];
                String ips[]=ip.split("\\.");

                for (int i=0;i<ips.length;i++)
                {
                    ipB[i]=(byte)Integer.parseInt(ips[i]);
                }
                if (!username.equals(name))
                {
                    String message=name+"进入了聊天室";
                    byte []m=message.getBytes();
                    DatagramPacket dp=new DatagramPacket(m,m.length);
                    System.out.println("");
                    dp.setAddress(InetAddress.getByAddress(ipB));
                    dp.setPort(port);
                    DatagramSocket ds=new DatagramSocket();
                    ds.send(dp);//投递
                }
            }
        } catch (SQLException | UnknownHostException | SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}