import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bem vindo ao conversor de Moedas");
        String menu = """
                1) Dólar =>> Peso argentino
                2) Peso argentino =>> Dólar
                3) Dólar =>> Real brasileiro
                4) Real brasileiro =>> Dólar
                5) Dólar =>> Peso colombiano
                6) Peso colombiano =>> Dólar
                7) Sair
                Escolha uma opção válida:
                *************************************
                """;
        System.out.println(menu);

        Scanner entrada = new Scanner(System.in);
        var opcao = 0;
        var valorConversao = 0.00;

        while (opcao != 7) {

            System.out.println("Escolha uma opção entrada:");
            opcao = entrada.nextInt();

            if (opcao == 7) {
                System.out.println("Finalizado!!!");
                break;
            }

            try {
                if (opcao > 0 && opcao <= 6) {

                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES).create();

                    System.out.println("Digite o valor de conversão:");
                    valorConversao = entrada.nextDouble();

                    ConversorDeMoeda conversorDeMoeda = new ConversorDeMoeda(opcao, valorConversao);
                    DadosDeTaxa dadosDeTaxa = gson.fromJson(conversorDeMoeda.buscaTaxas(), DadosDeTaxa.class);
                    System.out.println(conversorDeMoeda.converteValor(dadosDeTaxa));
                } else {
                    System.out.println("Digite um numero válido");
                }
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
