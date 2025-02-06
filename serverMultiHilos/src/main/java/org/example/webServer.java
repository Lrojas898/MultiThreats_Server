package org.example;
import java.io.* ;
import java.net.* ;
import java.util.* ;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public final class webServer {

    public static void main(String argv[]) throws Exception
    {
        int port = 6789;

        ServerSocket listener = new ServerSocket(port);

        System.out.println("Server Web has started in port: "+port);

        //tengo 4 trabajadores disponibles -hilos- funcionales que se encargarán de todas las peticiones

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);



        while(true){
            Socket connection = listener.accept();
            System.out.println("New connection accepted...");


            executor.execute(new httpRequest(connection));



        }





    }



}

