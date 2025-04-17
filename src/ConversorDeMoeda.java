import com.google.gson.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConversorDeMoeda {
    private int opcao;
    private double value;

    public ConversorDeMoeda(int opcao, double value) {
        this.opcao = opcao;
        this.value = value;
    }

    public int getOpcao() {
        return opcao;
    }

    public double getValue() {
        return value;
    }

    public String buscaUrl() {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");

        if (opcao == 1 || opcao == 3 || opcao == 5) {
            return "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/USD";
        } else if (opcao == 2) {
            return "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/ARS";
        } else if (opcao == 4) {
            return "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/BRL";
        } else {
            return "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/COP";
        }
    }

    public JsonObject buscaTaxas() {
        try {
            String url = buscaUrl();
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            JsonElement element = JsonParser.parseString(response.body());
            JsonObject jsonObject = element.getAsJsonObject();

            JsonObject taxas = jsonObject.getAsJsonObject("conversion_rates");
            return taxas;
        } catch (Exception e) {
            throw new RuntimeException("Nao foi possível obter os valores");
        }
    }

    public String converteValor(DadosDeTaxa dadosDeTaxa) {
        if (opcao == 1) {
            return "O Valor " + value + " [USD] corresponde a =>> " + String.format("%.4f", value * dadosDeTaxa.ars()).replace(",", ".") + " [ARS]";
        } else if (opcao == 2) {
            return "O Valor " + value + " [ARS] corresponde a =>> " + String.format("%.4f", value * dadosDeTaxa.usd()).replace(",", ".") + " [USD]";
        } else if (opcao == 3) {
            return "O Valor " + value + " [USD] corresponde a =>> " + String.format("%.4f", value * dadosDeTaxa.brl()).replace(",", ".") + " [BRL]";
        } else if (opcao == 4) {
            return "O Valor " + value + " [BRL] corresponde a =>> " + String.format("%.4f", value * dadosDeTaxa.usd()).replace(",", ".") + " [USD]";
        } else if (opcao == 5) {
            return "O Valor " + value + " [USD] corresponde a =>> " + String.format("%.4f", value * dadosDeTaxa.cop()).replace(",", ".") + " [COP]";
        } else {
            return "O Valor " + value + " [COP] corresponde a =>> " + String.format("%.4f", value * dadosDeTaxa.usd()).replace(",", ".") + " [USD]";
        }
    }
}
