import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage {
    private Stage primaryStage;
    private BorderPane view;

    public LoginPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.view = createView();
    }

    public BorderPane getView() {
        return view;
    }

    private BorderPane createView() {
        BorderPane borderPane = new BorderPane();
        borderPane.setBackground(
                new Background(new BackgroundFill(Color.web("#F6E9B2"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Title label centered at the top
        Text titleLabel = new Text("Welcome to Burrito King");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 20, 0));
        borderPane.setCenter(titleBox);

        // Buttons for Login and Register aligned to top right
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setBackground(
                new Background(new BackgroundFill(Color.rgb(171, 163, 41), CornerRadii.EMPTY, Insets.EMPTY)));

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(100);
        loginButton.setStyle("-fx-background-color: #0A6847; -fx-text-fill: white;");
        loginButton.setOnAction(e -> showLoginForm());

        Button registerButton = new Button("Register");
        registerButton.setPrefWidth(100);
        registerButton.setStyle("-fx-background-color: #7ABA78; -fx-text-fill: white;");
        registerButton.setOnAction(e -> showRegisterForm());

        buttonBox.getChildren().addAll(loginButton, registerButton);
        borderPane.setTop(buttonBox);

        return borderPane;
    }

    private void showLoginForm() {
        GridPane loginForm = new GridPane();
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setHgap(10);
        loginForm.setVgap(10);
        loginForm.setPadding(new Insets(25, 25, 25, 25));

        Label usernameLabel = new Label("Username:");
        loginForm.add(usernameLabel, 0, 0);

        TextField usernameField = new TextField();
        loginForm.add(usernameField, 1, 0);

        Label passwordLabel = new Label("Password:");
        loginForm.add(passwordLabel, 0, 1);

        PasswordField passwordField = new PasswordField();
        loginForm.add(passwordField, 1, 1);

        Button loginSubmitButton = new Button("Login");
        loginSubmitButton.setStyle("-fx-background-color: #0A6847; -fx-text-fill: white;");
        loginSubmitButton.setOnAction(e -> loginUser(usernameField.getText(), passwordField.getText()));
        loginForm.add(loginSubmitButton, 1, 2);

        view.setCenter(loginForm);
    }

    private void showRegisterForm() {
        GridPane registerForm = new GridPane();
        registerForm.setAlignment(Pos.CENTER);
        registerForm.setHgap(10);
        registerForm.setVgap(10);
        registerForm.setPadding(new Insets(25, 25, 25, 25));

        Label firstNameLabel = new Label("First Name:");
        registerForm.add(firstNameLabel, 0, 0);

        TextField firstNameField = new TextField();
        registerForm.add(firstNameField, 1, 0);

        Label lastNameLabel = new Label("Last Name:");
        registerForm.add(lastNameLabel, 0, 1);

        TextField lastNameField = new TextField();
        registerForm.add(lastNameField, 1, 1);

        Label usernameLabel = new Label("Username:");
        registerForm.add(usernameLabel, 0, 2);

        TextField usernameField = new TextField();
        registerForm.add(usernameField, 1, 2);

        Label passwordLabel = new Label("Password:");
        registerForm.add(passwordLabel, 0, 3);

        PasswordField passwordField = new PasswordField();
        registerForm.add(passwordField, 1, 3);

        Button registerSubmitButton = new Button("Register");
        registerSubmitButton.setStyle("-fx-background-color: #7ABA78; -fx-text-fill: white;");
        registerSubmitButton.setOnAction(e -> registerUser(firstNameField.getText(), lastNameField.getText(),
                usernameField.getText(), passwordField.getText()));
        registerForm.add(registerSubmitButton, 1, 4);

        view.setCenter(registerForm);
    }

    private void registerUser(String firstName, String lastName, String username,
            String password) {
        String url = "jdbc:mysql://localhost:3306/bkrestrauntdb";
        String user = "root";
        String dbPassword = "root";

        String query = "INSERT INTO users (FIRST_NAME, LAST_NAME, USER_NAME, PASSWORD) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);

            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful!",
                        "You have been registered successfully. Please login.");
                showLoginForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Registration failed. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Could not connect to the database. Please try again later.");
        }
    }

    private void loginUser(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/bkrestrauntdb";
        String user = "root";
        String dbPassword = "root";

        String query = "SELECT * FROM users WHERE USER_NAME = ? AND PASSWORD = ?";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful",
                        "Welcome " + resultSet.getString("first_name") + "!");
                // Proceed to the next stage or main application screen
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Could not connect to the database. Please try again later.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}
