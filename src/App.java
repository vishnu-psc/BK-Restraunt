import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    // Application started from here and first login page will open
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Burrito King Restaurant");
        LoginPage loginPage = new LoginPage(primaryStage);
        Scene scene = new Scene(loginPage.getView(), 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
