import java.io.*;
import java.net.*;
import java.util.*;

public class ExamServer {
    private static final int PORT = 12345;
    private static Map<String, String> examQuestions = new HashMap<>();
    private static Map<String, String> correctAnswers = new HashMap<>();

    public static void main(String[] args) {
        loadExam();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Exam server started at = " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection accepted.");

                // Handle each client in a separate thread
                Thread clientThread = new ClientHandler(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadExam() {
        // Load exam questions and answers (this could be from a database or a file)
        examQuestions.put("Q1", "What is the capital of France?");
        correctAnswers.put("Q1", "Paris");

        examQuestions.put("Q2", "What is 2 + 2?");
        correctAnswers.put("Q2", "4");

        examQuestions.put("Q3", "What is the square root of 16?");
        correctAnswers.put("Q3", "4");

        examQuestions.put("Q4", "What is the chemical symbol for water?");
        correctAnswers.put("Q4", "H2O");
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Welcome to the online exam system.");
                out.println("You have " + examQuestions.size() + " questions to answer.");
                out.println("Write your ID:");

                String studentID = in.readLine();  // Reading the student ID
                out.println("Your ID is: " + studentID + ". Starting the exam...");

                System.out.println("Student ID: " + studentID); // Print the student ID

                int score = 0;

                for (Map.Entry<String, String> entry : examQuestions.entrySet()) {
                    out.println(entry.getValue());
                    String answer = in.readLine();
                    if (correctAnswers.get(entry.getKey()).equalsIgnoreCase(answer)) {
                        score++;
                    }
                }

                out.println("Exam finished. Your score is: " + score + "/" + examQuestions.size());
                System.out.println("Student ID: " + studentID + ", Score: " + score + "/" + examQuestions.size());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
