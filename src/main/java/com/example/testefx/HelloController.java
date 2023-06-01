package com.example.testefx;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.Scene;




public class HelloController {

    @FXML
    private Button entrar;

    @FXML
    private TextField user;

    @FXML
    private PasswordField senha;

    @FXML
    private TreeTableView tb;

    @FXML
    private TreeTableColumn nome;

    @FXML
    private TreeTableColumn preco;

    @FXML
    private TreeTableColumn estq;

    private Stage stage;
    private Scene scene;
    private Parent root;


    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }
    public void entrar(ActionEvent event) throws NoSuchAlgorithmException {

        String json = "{'rick': '7fd3f05696873f826ef9fdc571402bfe901e08ee1725c9ef21097817b8327425'}";
        JSONObject my_obj = new JSONObject(json);


        String users = user.getText();
        String senhas = toHexString(getSHA(senha.getText()));
        try {
            if (my_obj.get(users).equals(senhas)) {
                Parent root = FXMLLoader.load(getClass().getResource("telaincial.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else {
                System.out.println("senha invalida");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    public void searchDirectory(ActionEvent event) throws IOException {
        Scanner sc = new Scanner(System.in);

        FileChooser fC = new FileChooser();

        fC.getExtensionFilters().add(new FileChooser.ExtensionFilter("Planilhas","*.csv", "*.xlsx", "*.xls"));
        List<File> f = fC.showOpenMultipleDialog(null);//stores files in f object list of type: File
        if (f != null){
            for (File file : f) {
                FileInputStream filea = new FileInputStream(file.getAbsolutePath());

                HSSFSheet sheet = null;
                switch (FilenameUtils.getExtension(file.getAbsolutePath())) {
                    case "xls":
                        HSSFWorkbook workbook = new HSSFWorkbook(filea);

                        sheet = workbook.getSheetAt(0);
                        int rows = sheet.getLastRowNum();
                        int cols = sheet.getRow(1).getLastCellNum();
                        break;

                    case "xlsx":
                        XSSFWorkbook workbook2 = new XSSFWorkbook(filea);

                        XSSFSheet sheet2 = workbook2.getSheetAt(0);
                        break;
                    case "csv":
                        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
                        Reader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);

                        CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).withSkipLines(1).build();

                        List<String[]> csv = csvReader.readAll();
                        break;
                }






            }
        }
            }
        }

