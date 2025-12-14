import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        final String url = "https://api.coinlore.net/api/tickers/";

        System.out.println("Introduce el nombre o símbolo de una criptomoneda:");
        String input = scanner.nextLine().trim().toLowerCase();

        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse <String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.body());
            JsonNode dataArray = rootNode.path("data");

            boolean encontrado = false;

            for (JsonNode coin: dataArray) {
                String name = coin.get("name").asText().toLowerCase();
                String simbolo = coin.get("symbol").asText().toLowerCase();

                if (name.equals(input) || simbolo.equals(input)) {

                    System.out.println("Nombre: " + coin.get("name").asText());
                    System.out.println("Símbolo: " + coin.get("symbol").asText());
                    System.out.println("Precio USD: $" + coin.get("price_usd").asText());
                    System.out.println("Ranking: " + coin.get("rank").asText());

                    if (coin.get("percent_change_24h").asDouble() >= 0) {
                        System.out.println("Cambio 24h: " + GREEN + coin.get("percent_change_24h").asText() + "%" + "\u001B[0m");
                        } else {
                        System.out.println("Cambio 24h: " + RED + coin.get("percent_change_24h").asText() + "%" + "\u001B[0m");

                    }
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                System.out.println("Moneda no encontrada");
            }


        } catch (IOException e) {
            System.out.println("Error de red: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("La solicitud fue interrumpida: " + e.getMessage());
        }

    }
}