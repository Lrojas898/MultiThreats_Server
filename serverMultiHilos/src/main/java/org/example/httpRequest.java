package org.example;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public final class httpRequest implements Runnable {

    final static String CRLF = "\r\n";
    Socket socket;

    public httpRequest(Socket socket){

        this.socket=socket;
    }


    @Override
    public void run() {

        try{

            processRequest();




        }catch (Exception anyException){

            System.out.println("There has been something wrong...");
            System.out.println(anyException);
        }

    }


    private void processRequest() throws Exception{

        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String requestLine = bufferedReader.readLine();

        System.out.println("HTTP REQUEST: ");
        System.out.println(requestLine);

        String headerLine = null;

        while ((headerLine = bufferedReader.readLine()).length() != 0) {
            System.out.println(headerLine);
        }

        StringTokenizer lineParts = new StringTokenizer(requestLine);
        lineParts.nextToken();
        String fileName = lineParts.nextToken();



        fileName = "./www" + fileName;
        if (fileName.equals("/") || fileName.equals("/index.html")) {
            fileName = "./www/index.html";
        }


        FileInputStream fis = null;
        boolean isFileExisting= true;
        try {
            fis = new FileInputStream(fileName);

        }catch (FileNotFoundException fileNotFound ){

            System.out.println(fileNotFound);
            isFileExisting = false;

        }

        String statusLine = null;
        String contentLineType = null;
        String messageLine = null;

        if (isFileExisting){

            statusLine = "HTTP/1.0 200 OK" + CRLF;
            contentLineType = "Content-Type: "+ contentType(fileName) + CRLF;



        }else{

            statusLine = "HTTP/1.0 404 Not Found" + CRLF;
            contentLineType = "Content-Type: text/html" + CRLF;
            messageLine = "<HTML><HEAD><TITLE>404 Not Found</TITLE></HEAD>" +
                    "<BODY><b>404</b> Not Found</BODY></HTML>";
        }

        outputStream.writeBytes(statusLine);
        outputStream.writeBytes(contentLineType);
        outputStream.writeBytes("Content-Length: " + (isFileExisting ? fis.available() : messageLine.length()) + CRLF);
        outputStream.writeBytes(CRLF);

        if(isFileExisting){
            sendBytes(fis, outputStream);
            fis.close();

        }else{

            outputStream.writeBytes(messageLine);

        }

        outputStream.close();
        bufferedReader.close();
        socket.close();

    }

    private static void sendBytes(FileInputStream fis, OutputStream outputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes = 0;

        while ((bytes = fis.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytes);
        }
    }


    private static String contentType(String fileName) {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }
        if (fileName.endsWith(".jpg")) {
            return "image/jpeg";
        }
        if (fileName.endsWith(".gif")) {
            return "image/gif";
        }
        if (fileName.endsWith(".png")) {
            return "image/png";
        }
        if (fileName.endsWith(".txt")) {
            return "text/plain";
        }
        return "application/octet-stream";
    }



}
