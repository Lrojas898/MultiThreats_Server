package org.example;
import java.io.* ;
import java.net.* ;
import java.util.* ;

public final class webServer {

    public static void main(String argv[]) throws Exception
    {
        int port = 6789;

        ServerSocket listener = new ServerSocket(port);

        System.out.println("Server Web has started in port: "+port);

        while(true){
            Socket connection = listener.accept();
            System.out.println("New connection accepted");
            httpRequest request= new httpRequest(connection);
            Thread thread = new Thread(request);
            thread.start();



        }





    }


}

