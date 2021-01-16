package cn.edu.hcen.client;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class GetMessageThread extends Thread{
    DatagramSocket ds;
    JTextArea ta;  //窗口区域
    JComboBox cb;  //下拉链表
    public GetMessageThread(ChatThreadWindow ctw)
    {
        this.ds=ctw.ds;
        this.ta=ctw.ta;
        this.cb=ctw.cb;
    }
    public void run()
    {
        try{
            while (true)
            {
                byte buff[]=new byte[1024];
                DatagramPacket dp=new DatagramPacket(buff,200);
                ds.receive(dp);
                String  message=new String(buff,0,dp.getLength());
                ta.append(message);
                if (message.contains("进入了聊天室"))
                {
                    message=message.replace("进入了聊天室","");
                    System.out.println(message);
                }
                /*
                wjl进入聊天室
                1.分割拿到用户名,如wjl
                2.然后使用JComboBox把用户名拉进下拉框。
                */
                cb.addItem(message);
                System.out.println("UDP收到的消息：" + message);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
