import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Client {

    public static void main(String[] args) {
        try {
            //nos conectamos al servidor local, por el puerto 8080
            Socket socket = new Socket("localhost",8080);
            System.out.println("Server Enabled");
            
            //creamos streams para enviar las solicitudes y recibir las respuestas
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            //creamos la instancia con la que vamos a escribir las solicitudes
            Scanner inRequest = new Scanner(System.in);
            
            String userInput;
            String serverResponse;
            
            do {                
                //escribimos las solicitudes leídas a través de la consola
                System.out.println("Enter command List, Add or Exit");
                userInput = inRequest.nextLine();
                output.println(userInput); //envía el comando al server
                        
                while((serverResponse = input.readLine()) != null){
                    //vemos la respuesta del servidor
                    System.out.println(serverResponse);
                    break;
                    
                }
            } while (!userInput.equalsIgnoreCase("EXIT")); //terminamos la conexión si el comando es salir
            
            socket.close();
            System.out.println("Connection ended");
            
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}

