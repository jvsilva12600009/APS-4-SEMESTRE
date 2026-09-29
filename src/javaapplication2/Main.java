
package javaapplication2;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        while (true) {
            int algoritmo = escolherAlgoritmo(entrada);

            if (algoritmo == 0) {
                break;
            }

            System.out.println("1 - 1000_numbers.txt");
            System.out.println("2 - 5000_numbers.txt");
            System.out.println("3 - 10000_numbers.txt");

            int opcao = lerOpcao(entrada, 1, 3);

            String[] arquivos = {
                "1000_numbers.txt",
                "5000_numbers.txt",
                "10000_numbers.txt"
            };

            String arquivo = arquivos[opcao - 1];

            try {
                int[] dados = lerArquivo(arquivo);

                if (dados.length == 0) {
                    System.out.println("Arquivo vazio.");
                    continue;
                }

                long tempo = medirTempo(dados, algoritmo);

                System.out.println("Arquivo: " + arquivo);
                System.out.println("Elementos: " + dados.length);
                System.out.println("Algoritmo: " + algoritmo);
                System.out.println("Tempo (ns): " + tempo);

                System.out.printf(
                    java.util.Locale.ROOT,
                    "Tempo (ms): %.6f%n",
                    tempo / 1_000_000.0
                );

                System.out.println("Primeiro: " + dados[0]);
                System.out.println("Ultimo: " + dados[dados.length - 1]);

            } catch (IOException | NumberFormatException e) {
                System.out.println(
                    "Falha na leitura: " + e.getMessage()
                );
            }
        }

        entrada.close();
    }

    static int escolherAlgoritmo(Scanner entrada) {
        System.out.println("1 - HeapSort");
        System.out.println("2 - Binary Insertion Sort");
        System.out.println("3 - Selection Sort");
        System.out.println("0 - Sair");

        return lerOpcao(entrada, 0, 3);
    }

    static int lerOpcao(Scanner entrada, int min, int max) {

        while (true) {
            System.out.print("Escolha: ");

            try {
                int valor = Integer.parseInt(
                    entrada.nextLine().trim()
                );

                if (valor >= min && valor <= max) {
                    return valor;
                }

            } catch (NumberFormatException e) {
                // Uma nova escolha sera solicitada.
            }

            System.out.println("Opcao invalida.");
        }
    }

    static int[] lerArquivo(String nome) throws IOException {
        List<String> linhas = Files.readAllLines(
            Paths.get(nome),
            StandardCharsets.UTF_8
        );

        int[] dados = new int[linhas.size()];

        for (int i = 0; i < linhas.size(); i++) {
            dados[i] = Integer.parseInt(
                linhas.get(i).trim()
            );
        }

        return dados;
    }

    static long medirTempo(int[] dados, int algoritmo) {
        long inicio;
        long fim;

        switch (algoritmo) {

            case 1:
                inicio = System.nanoTime();
                heapSort(dados);
                fim = System.nanoTime();
                break;

            case 2:
                inicio = System.nanoTime();
                binaryInsertionSort(dados);
                fim = System.nanoTime();
                break;

            case 3:
                inicio = System.nanoTime();
                selectionSort(dados);
                fim = System.nanoTime();
                break;

            default:
                throw new IllegalArgumentException(
                    "Algoritmo invalido."
                );
        }

        return fim - inicio;
    }

    static void heapSort(int[] dados) {
        int n = dados.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            ajustarHeap(dados, n, i);
        }

        for (int fim = n - 1; fim > 0; fim--) {
            trocar(dados, 0, fim);
            ajustarHeap(dados, fim, 0);
        }
    }

    static void ajustarHeap(int[] dados, int n, int raiz) {

        while (raiz < n / 2) {
            int filho = 2 * raiz + 1;

            if (
                filho + 1 < n
                && dados[filho + 1] > dados[filho]
            ) {
                filho++;
            }

            if (dados[raiz] >= dados[filho]) {
                break;
            }

            trocar(dados, raiz, filho);
            raiz = filho;
        }
    }

    static void binaryInsertionSort(int[] dados) {

        for (int i = 1; i < dados.length; i++) {
            int chave = dados[i];
            int inicio = 0;
            int fim = i;

            while (inicio < fim) {
                int meio = inicio + (fim - inicio) / 2;

                if (chave < dados[meio]) {
                    fim = meio;
                } else {
                    inicio = meio + 1;
                }
            }

            for (int j = i; j > inicio; j--) {
                dados[j] = dados[j - 1];
            }

            dados[inicio] = chave;
        }
    }

    static void selectionSort(int[] dados) {

        for (int i = 0; i < dados.length - 1; i++) {
            int menor = i;

            for (int j = i + 1; j < dados.length; j++) {

                if (dados[j] < dados[menor]) {
                    menor = j;
                }
            }

            if (menor != i) {
                trocar(dados, i, menor);
            }
        }
    }

    static void trocar(int[] dados, int a, int b) {
        int auxiliar = dados[a];
        dados[a] = dados[b];
        dados[b] = auxiliar;
    }
}