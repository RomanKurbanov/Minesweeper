package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml")); //Файл с разметкой формы
        primaryStage.setTitle("Minesweeper"); //Установка тайтла, тоесть названия окна
        primaryStage.setScene(new Scene(root, 355, 405)); //Настройка размеров окна
        Image icon = new Image(getClass().getClassLoader().getResourceAsStream("res/Mine.png"));//Иконка загружается в память, теперь уже через объявленную папку с ресурсами
        primaryStage.getIcons().add(icon); //И устанавливается как иконка окна
        primaryStage.setResizable(false); //Размер окошка теперь не изменяется пользователем
        primaryStage.show(); //Окно показывается пользователю
    }


    public static void main(String[] args) {
        launch(args);
    }
}
