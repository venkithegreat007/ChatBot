import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Scanner;

public class AIChatBot {
    private static final String API_KEY = "API_KEY";
    private static final String API_URL = "API_URL";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        OkHttpClient client = new OkHttpClient();

        System.out.println("🤖 AI ChatBot: Hello! Ask me anything. Type 'bye' to exit.");

        while (true) {
            System.out.print("You: ");
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("bye")) {
                System.out.println("🤖 AI ChatBot: Goodbye!");
                break;
            }
            String reply = getAIResponse(client, userInput);
            System.out.println("🤖 AI ChatBot: " + reply);
        }

        scanner.close();
    }

    private static String getAIResponse(OkHttpClient client, String userInput) throws IOException {
        JSONObject message = new JSONObject()
                .put("role", "user")
                .put("content", userInput);

        JSONObject body = new JSONObject()
                .put("model", "gpt-3.5-turbo")
                .put("messages", new org.json.JSONArray().put(message));

        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(body.toString(), MediaType.get("application/json; charset=utf-8")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "⚠️ Error: " + response;
            }
            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();
        }
    }
}

