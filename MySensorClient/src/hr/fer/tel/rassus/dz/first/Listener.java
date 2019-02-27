/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rassus.dz.first;

import java.util.Scanner;

/**
 *
 * @author marinco
 */
public class Listener implements Runnable{
    static int PORT;
    private String username;

    @Override
    public void run() {
        Scanner sc=new Scanner(System.in);
        Thread t=null;
        Client c=new Client();
        c.setPORT(PORT);
        c.setUsername(username);
        while(sc.hasNext()){
            String s=sc.nextLine();
            System.out.println("hr.fer.tel.rassus.dz.first.Listener.run()");
            if(s.equals("a")){
                System.out.println("My name is "+username+ " and i want to access port "+PORT);
                t=new Thread(c);
                t.start();
                break;
            }
            if(s.equals("b")){
                t.stop();
            }
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
