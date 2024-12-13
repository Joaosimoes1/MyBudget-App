package org.example.gps_g22.view;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CalculateValorizationView {
    @FXML
    private TextField montanteField;

    @FXML
    private TextField precoInicialField;

    @FXML
    private TextField precoFinalField;

    @FXML
    private Button calcularButton;

    @FXML
    private Label resultadoLabel;

    @FXML
    protected void handleCalcularValorizacao() {
        try {
            // Recupera os valores dos campos
            double precoInicial = Double.parseDouble(precoInicialField.getText());
            double precoFinal = Double.parseDouble(precoFinalField.getText());

            // Validação
            if (precoInicial <= 0) {
                resultadoLabel.setText("Initial price must be greater than zero.");
                return;
            }

            // Calcula valorização ou desvalorização
            double variacao = ((precoFinal - precoInicial) / precoInicial) * 100;

            // Atualiza o rótulo com o resultado
            if (variacao > 0) {
                resultadoLabel.setText(String.format("Apreeciation of %.2f%%", variacao));
                resultadoLabel.setStyle("-fx-text-fill: green;");
            } else if (variacao < 0) {
                resultadoLabel.setText(String.format("Depreciation of -%.2f%%", Math.abs(variacao)));
                resultadoLabel.setStyle("-fx-text-fill: red;");
            } else {
                resultadoLabel.setText("No appreciation or depreciation.");
            }
        } catch (NumberFormatException e) {
            resultadoLabel.setText("Please, enter valid numbers.");
        }
    }
}
