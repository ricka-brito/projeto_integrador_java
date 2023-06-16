package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.text.NumberFormat;


// classe principal

public class MenuOpcoes {

    // lista de produtos
    private static List<Produto> produtos = new ArrayList<>();


    // execução principal
    public static void main(String[] args) {
        lerArquivo();
        exibirMenu();
    }

    // função para ler o arquivo (com tratativa de erro)
    private static void lerArquivo() {
        int valido = 0;
        do{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o caminho completo do arquivo (xlsx, xls): ");
        String caminhoArquivo = scanner.nextLine();
            valido = 0;
            if (caminhoArquivo.endsWith(".xlsx") || caminhoArquivo.endsWith(".xls")) {
                lerPlanilha(caminhoArquivo);
            } else {
                System.out.println("Formato de arquivo inválido.");
                valido = 1;
            }
        }
        while (valido == 1);

    }

    // função para ler a planilha e transformar em objetos do tipo produto e adicionalos a uma lista
    private static void lerPlanilha(String caminhoArquivo) {
        try {
            FileInputStream arquivo = new FileInputStream(new File(caminhoArquivo));
            Workbook workbook;

            if (caminhoArquivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(arquivo);
            } else {
                workbook = new HSSFWorkbook(arquivo);
            }

            Sheet sheet = workbook.getSheetAt(0);

            int cont = 0;
            for (Row row : sheet) {
                if(cont == 0) {
                    cont++;
                    continue;
                }
                String nome = row.getCell(0).getStringCellValue();
                String tipo = row.getCell(2).getStringCellValue();
                Double preco = row.getCell(1).getNumericCellValue();
                produtos.add(new Produto(nome, tipo, preco));
            }
            workbook.close();
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    // função que exibe o menu principal
    private static void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n----- MENU -----");
            System.out.println("1. Mostrar toda a lista");
            System.out.println("2. Filtrar por tipo");
            System.out.println("3. Mostrar em ordem por nome");
            System.out.println("4. Mostrar em ordem por tipo");
            System.out.println("5. Mostrar em ordem por preço");
            System.out.println("6. Média de preços");
            System.out.println("7. Pesquisar por nome");
            System.out.println("0. Sair");
            System.out.print("Digite a opção desejada: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    mostrarLista(produtos);
                    break;
                case 2:
                    filtrarPorTipo();
                    break;
                case 3:
                    mostrarOrdemPorNome();
                    break;
                case 4:
                    mostrarOrdemPorTipo();
                    break;
                case 5:
                    mostrarOrdemPorPreco();
                    break;
                case 6:
                    calcularMediaPreco();
                    break;
                case 7:
                    pesquisarPorNome();
                    break;
                case 0:
                    System.out.println("Encerrando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }


    // Função que imprime a lista com todos os produtos, em uma tabela formatada, com as colunas tendo tamanho relativo
    private static void mostrarLista(List<Produto> produtos) {
        System.out.println("\n----- LISTA DE PRODUTOS -----");

        // Encontrar o tamanho máximo de cada coluna
        int maxNome = 0;
        int maxTipo = 0;
        int maxValor = 0;
        for (Produto produto : produtos) {
            int tamanhoNome = produto.getNome().length();
            int tamanhoTipo = produto.getTipo().length();
            int tamanhoValor = String.valueOf(NumberFormat.getCurrencyInstance().format(produto.getPreco())).length();
            if (tamanhoNome > maxNome) {
                maxNome = tamanhoNome;
            }
            if (tamanhoTipo > maxTipo) {
                maxTipo = tamanhoTipo;
            }
            if (tamanhoValor > maxValor) {
                maxValor = tamanhoValor;
            }
        }
        // Imprimir a tabela formatada
        System.out.print("+");
        for(int i = 0; i<=maxTipo+1; i++){
            System.out.print("-");
        }
        System.out.print("+");
        for(int i = 0; i<=maxNome+1; i++){
            System.out.print("-");
        }
        System.out.print("+");
        for(int i = 0; i<=maxValor+1; i++){
            System.out.print("-");
        }
        System.out.print("+");
        System.out.println();
        String format = "| %-" + maxTipo + "s | %-" + maxNome + "s | %-" + maxValor + "s |\n";
        System.out.format(format, "Tipo", "Nome", "Preço");
        System.out.print("+");
        for(int i = 0; i<=maxTipo+1; i++){
            System.out.print("-");
        }
        System.out.print("+");
        for(int i = 0; i<=maxNome+1; i++){
            System.out.print("-");
        }
        System.out.print("+");
        for(int i = 0; i<=maxValor+1; i++){
            System.out.print("-");
        }
        System.out.print("+");
        System.out.println();
        for (Produto produto : produtos) {
            System.out.format(format, produto.getTipo(), produto.getNome(), (NumberFormat.getCurrencyInstance().format(produto.getPreco())));
        }
        System.out.print("+");
        for(int i = 0; i<=maxTipo+1; i++){
            System.out.print("-");
        }
        System.out.print("+");
        for(int i = 0; i<=maxNome+1; i++){
            System.out.print("-");
        }
        System.out.print("+");
        for(int i = 0; i<=maxValor+1; i++){
            System.out.print("-");
        }
        System.out.print("+");
    }


    // função que retorna lista com produtos filtados (com tratativa de erro)
    private static void filtrarPorTipo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite a Tipo desejada: ");
        String Tipo = scanner.nextLine();

        List<Produto> produtosFiltrados = new ArrayList<>();
        for (Produto produto : produtos) {
            try {
                if (produto.getTipo().equalsIgnoreCase(Tipo)) {
                    produtosFiltrados.add(produto);
                }
            }
            catch (Exception e){
                System.out.println("Tipo não encontrado");
            }

        }

        try{
            System.out.println("\n----- PRODUTOS FILTRADOS POR TIPO -----");
            mostrarLista(produtosFiltrados);
        }
        catch (Exception e){
            System.out.println("Tipo não encontrado");
        }
    }


    // ordena a lista utilizando o .sort
    private static void mostrarOrdemPorNome() {
        List<Produto> produtosOrdenados = new ArrayList<>(produtos);
        Collections.sort(produtosOrdenados, Comparator.comparing(Produto::getNome));

        System.out.println("\n----- PRODUTOS ORDENADOS POR NOME -----");
        mostrarLista(produtosOrdenados);
    }

    // ordena a lista utilizando o .sort com base no tipo (alfabetico)
    private static void mostrarOrdemPorTipo() {
        List<Produto> produtosOrdenados = new ArrayList<>(produtos);
        Collections.sort(produtosOrdenados, Comparator.comparing(Produto::getTipo));

        System.out.println("\n----- PRODUTOS ORDENADOS POR TIPO -----");
        mostrarLista(produtosOrdenados);
    }

    // ordena a lista utilizando o .sort (com base no preco)
    private static void mostrarOrdemPorPreco() {
        List<Produto> produtosOrdenados = new ArrayList<>(produtos);
        Collections.sort(produtosOrdenados, Comparator.comparing(Produto::getPreco));

        System.out.println("\n----- PRODUTOS ORDENADOS POR PREÇO -----");
        mostrarLista(produtosOrdenados);
    }

    // ordena a lista utilizando o .sort (calcula a media de precos)
    private static void calcularMediaPreco() {
        double soma = 0;
        for (Produto produto : produtos) {
            soma += produto.getPreco();
        }

        double media = soma / produtos.size();
        System.out.printf("\nA média de preços é: R$ %.2f", media);
    }

    // retorna uma lista com base em uma palavra para filtrar por palavra
    private static void pesquisarPorNome() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do produto: ");
        String nome = scanner.nextLine();

        List<Produto> produtosEncontrados = new ArrayList<>();
        for (Produto produto : produtos) {
            if (produto.getNome().toLowerCase().contains(nome.toLowerCase())) {
                produtosEncontrados.add(produto);
            }
        }

        if (produtosEncontrados.isEmpty()) {
            System.out.println("Nenhum produto encontrado com o nome: " + nome);
        } else {
            System.out.println("\n----- PRODUTOS ENCONTRADOS -----");
            mostrarLista(produtosEncontrados);
        }
    }
}


// classe de produtos
class Produto {
    private String nome;
    private String tipo;
    private double preco;

    public Produto(String nome, String tipo, double preco) {
        this.nome = nome;
        this.tipo = tipo;
        this.preco = preco;
    }

    // getters e setters
    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public double getPreco() {
        return preco;
    }

}