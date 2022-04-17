package br.com.matheus.geradorcores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MainController {
    ObservableList<String> formatList = FXCollections.observableArrayList("HTML", "TXT");

    @FXML
    private ChoiceBox comboFormat;
    @FXML
    private TextField redValue;
    @FXML
    private TextField greenValue;
    @FXML
    private TextField blueValue;
    @FXML
    private TextField frase;

    @FXML
    private void initialize() {
        comboFormat.setItems(formatList);
        comboFormat.setValue(formatList.get(0));
    }

    @FXML
    public void onClickExecuteButton() throws IOException {
        Alert a = new Alert(Alert.AlertType.NONE);

        if(validateColors(a)) return;

        if(comboFormat.getValue().equals("TXT")) {
            try {
                generateFileTxt();
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setContentText("Arquivo TXT gerado, verificar pasta OUTPUTS");
                a.show();
            }
            catch (Exception ex){
                a.setAlertType(Alert.AlertType.ERROR);
                a.setContentText(ex.getMessage());
                a.show();
            }
        }

        if(comboFormat.getValue().equals("HTML")) {
            if(frase.getText() == "") {
                a.setAlertType(Alert.AlertType.ERROR);
                a.setContentText("Escreva uma frase");
                a.show();
                return;
            }

            try {
                generateFileHTML();
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setContentText("Arquivo HTML gerado, verificar pasta OUTPUTS");
                a.show();
            }
            catch (Exception ex){
                a.setAlertType(Alert.AlertType.ERROR);
                a.setContentText(ex.getMessage());
                a.show();
            }
        }

        clearFields();
    }

    private boolean validateColors(Alert a) {
        Integer red = Integer.parseInt(redValue.getText());
        Integer green = Integer.parseInt(greenValue.getText());
        Integer blue = Integer.parseInt(blueValue.getText());

        if(red < 0 || green < 0 || blue < 0 || red > 255 || green > 255 || blue > 255) {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Por favor, informe apenas valores entre 0 e 255");
            a.show();
            return true;
        }
        return false;
    }

    private String generateHexadecimal(Integer red, Integer green, Integer blue) {
        String redHex = createHexPair(red);
        String greenHex = createHexPair(green);
        String blueHex = createHexPair(blue);

        return redHex + greenHex + blueHex;
    }

    private String createHexPair(Integer colorValue) {
        if(Integer.toHexString(colorValue).length() == 1) {
            return "0" + Integer.toHexString(colorValue);
        }
        return Integer.toHexString(colorValue);
    }

    private void generateFileTxt() throws IOException {
        verifyDir("outputs");

        File txtFile = new File("outputs\\colored.txt");
        FileWriter fw = new FileWriter(txtFile);
        PrintWriter pw = new PrintWriter(fw);

        pw.write("Arquivos de texto plano NÃO tem formatação de cor\n");
        pw.write("Código hexadecimal gerado: #" + generateHexadecimal(
                Integer.parseInt(redValue.getText()),
                Integer.parseInt(greenValue.getText()),
                Integer.parseInt(blueValue.getText())));
        pw.close();
    }

    private void generateFileHTML() throws IOException {
        verifyDir("outputs");

        File htmlFile = new File("outputs\\colored.html");
        FileWriter fw = new FileWriter(htmlFile);
        PrintWriter pw = new PrintWriter(fw);

        pw.write("<p style="
                +"color:#"+generateHexadecimal(
                Integer.parseInt(redValue.getText()),
                Integer.parseInt(greenValue.getText()),
                Integer.parseInt(blueValue.getText()))+"; font-size:50px;"+">"+frase.getText()+"</p>");
        pw.close();
    }

    private void verifyDir(String dirName) {
        File dir = new File(dirName);

        if(!dir.exists()){
            dir.mkdir();
        }
    }

    private void clearFields() {
        redValue.setText("");
        greenValue.setText("");
        blueValue.setText("");
        frase.setText("");
    }
}