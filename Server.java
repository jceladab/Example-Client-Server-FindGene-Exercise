import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    
    private static List<String> geneList = new ArrayList<>();
    
    public static void main(String[] args) {
        //iniciamos el servidor en el puesto 8080
        try (ServerSocket serverSocket = new ServerSocket(8080)){
            System.out.println("Server Started");
            
            //creamos loop infinito para nuestro ambiente
            while(true){
                //esperamos una conexión de algún cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Enabled");
                
                //manejamos la conexión con el cliente mediante un nuevo hilo
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void handleClient(Socket clientSocket) {
        try {
            //creamos los streams para recibir los datos y devolver respuestas
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            String request;
            while((request = input.readLine()) != null){
                System.out.println("[CLIENT] Request recieved " + request);
                
                if(request.startsWith("List")){
                    //se listan los genes encontrados
                    System.out.println("[CLIENT] Gene list: ");
                    for(String gene : geneList){
                        System.out.println(gene);
                    }
                    System.out.println("[CLIENT] End gene list");
                    output.println("Gen");
                }else if(request.startsWith("Add")){
                    String gene = request.substring(4);
                    int startCodon = gene.indexOf("ATG");
                    int endCodon = gene.indexOf("TAA",startCodon+3);
                    if(startCodon == -1 || endCodon == -1){
                        output.println("[SERVER] No valid gene");
                        System.out.println("[CLIENT] No valid gene");
                    }
                    gene = gene.substring(startCodon,endCodon+3);
                    geneList.add(gene);
                    output.println("[CLIENT] Gene added");
                    System.out.println("Gene " +gene+ " added");
                    
                }else if (request.equalsIgnoreCase("EXIT")){
                    //si la soliitud del cliente es exit, cerramos la conexión
                    output.println("[SERVER] Ending connection");
                    System.out.println("[CLIENT] Ending connection");
                    break;
                }else{
                    //opción por si la solicitud es desconocida
                    System.out.println("[CLIENT] Unknown Request");
                    output.println("[SERVER] Unknown Request");
                }
            } 
            //cerramos el socket luego de procesar las solicitudes
            clientSocket.close();
            System.out.println("[CLIENT] Client disconnected");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   
}

