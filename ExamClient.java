import java.io.*;
import java.net.*;

public class ExamClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String serverMessage;

            while ((serverMessage = in.readLine()) != null) {
                System.out.println("Server: " + serverMessage);
                if (serverMessage.endsWith("ID:") || serverMessage.startsWith("What") || serverMessage.startsWith("Exam finished")) {
                    String userAnswer = stdIn.readLine();
                    out.println(userAnswer);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
