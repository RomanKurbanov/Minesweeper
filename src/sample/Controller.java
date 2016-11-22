package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Random;




public class Controller { //Это класс-контроллер
    Image mineIcon = new Image(getClass().getResourceAsStream("32icon.png"));//Это иконка мины
    Image flagIcon = new Image(getClass().getResourceAsStream("flagBig.png"));//А эта - флага
    @FXML Label timerLabel=new Label();
    @FXML public void initialize(){ //При запуске формы вызывается этот метод
        timerLabel.setText("0");
        dotsLeft=80;
        minesFlagged=20;
        field = CreateNewField(10,10,20); //Создается новый массив с кноками
        mineIcon=new Image(getClass().getResourceAsStream("32icon.png"));
        flagIcon=new Image(getClass().getResourceAsStream("flagBig.png"));//Загружаются большие иконки для флагов и мин
        for (int s=0;s<9;s++){
            currentNumberSet[s]= new Image(getClass().getClassLoader().getResourceAsStream("res/NumbersBig/"+Integer.toString(s+1)+".png")); //Большие иконки вместо цифр
        }
        mineCountLabel.setText(Integer.toString(minesFlagged)); //Обновляется надпись, которая играет роль счетчика помеченных флажками мин для игрока
        IsFirstButtonPressed = false; //Обновляется фактор первого нажатия на кнопку, чтобы не попасть на мину на первом же ходе
        setMineFieldPane(mineFieldPane, field); //Обновляется поле
        startButton.setText("Заново"); //Изменяется текст кнопкидля старта
    }


    @FXML Label mineCountLabel=new Label(); //Надпись, которая показывает сколько мин осталось пометить
    Image[] currentNumberSet = new Image[9]; //Это массив с иконками цифр
    static boolean IsFirstButtonPressed = false; //Первая кнопка должна быть без мины
    int dotsLeft=380, minesFlagged=100; //dotsLeft считает, сколько еще кнопок должен нажать игрок, чтобы выиграть
    //Когда он нажмет на все ячейки, в которых нет мин, он победит.
    //minesFlagged показывает юзеру, сколько мнимых мин осталось. Тоесть когда он поставит флажок,
    //Кол-во оставшихся мин, которых он не отметил уменьшится на счетчике, чтобы не держать их в уме.
    //На победу minesFlagged никак не влияет.
    @FXML Button startButton;//Кнопка, запускающая и перезапускающая игру.
    @FXML ToggleButton difficulty = new ToggleButton(); //Кнопка-тумблер. Определяет сложность.
    @FXML private GridPane mineFieldPane; //Панель с ячейками с кнопками с минами.
    public void setMineFieldPane(GridPane mineFieldPane, mineButton[][] mineField) {
        this.mineFieldPane = mineFieldPane;
        this.mineFieldPane.getChildren().clear();
        for(mineButton[] line : mineField){
            for(mineButton dot : line){
                mineFieldPane.add(dot,dot.x,dot.y); //Кнопки из массива добавляются на панель
            }
        }
    }
    //Снизу метод, который создает поле с нужными параметрами.
    //Х - кнопки по горизонтали, у - кнопки по вертикали, mines - кол-во мин на поле.
    private mineButton[][] CreateNewField(int x, int y, int mines){
        //Создается переменная field, это массив из объектов класса MineButton
        mineButton[][] field = new mineButton[x][y];
        Random random = new Random(); //Random - это объект.
        x=0; //Эти переменные уже не нужны, но все еще занимают память
        y=0; //Я испольюзую их, чтобы не объявлять новые.
        //Поле нужно рандомно заполнить.
        while (mines>0){ //Пока нужно добавить на поле мины
            for (mineButton[] line : field){ //Счетчик проходит по столбцам
                for(mineButton dot : line){ //А здесь по строке столбца
                    // IF ниже проверяет, существует ли кнопка в массиве, так как хоть ты и создал массив,
                    // он все равно пуст и пока не содержит ни кнопок, вообще ничего, поэтому они создаются
                    // и заполняются здесь, снизу.
                    if (dot==null){ //Если кнопка не существует
                        dot = new mineButton(x,y,difficulty.isSelected()); //На её место призывается новая кнопка, которой дают собственные координаты
                        dot = SetMineButton(dot); //Здесь же настраивается её обработчик событий
                        dot.setDisable(false);//Настроенные кнопки должны быть активированы.
                    }
                    //Если кнопка не содержит мину и нужно накинуть на поле еще мин, то она добавляется
                    //Или не добавляется, как повезет
                    if ((!dot.isMineHere) && mines>0){
                        if (random.nextInt(5)==3){ //Вместо шанса 50% теперь шанс 20% (Шанс того, что случайное число от 0 до 4 будет тройкой)
                                                   //Шанс пропорционален кол-ву мин и кнопок на поле.
                            dot.isMineHere = true; //На кнопку добавляется мина
                            mines--; //Кол-во мин, которое нужно добавить на поле уменьшается на 1 с каждой миной
                        }
                    }
                    field[y][x]=dot;//Кнопка ДОБАВЛЯЕТСЯ в возвращаемый массив
                    x++; //После заполнения ячейки нужно перейти на следующую ячейку
                }
                y++; //После заполнения строки нужно перейти на следующую
                x=0; //После перехода на следующую строку нужно перейти на первую ячейку в строке
            }
            x=0; //После заполнения всего поля нужно перейти на первую ячейку
            y=0; //Первой строки
        }
        return field; //Возвращается заполненый кнопками и минами массив.
    }
    public mineButton[][] field = CreateNewField(30, 16, 100); //Создается массив из кнопок с минами с помощью метода выше
    @FXML
    public void StartNewGame(){ //Этот метод вызывается при нажатии на самую большущую кнопку сверху, вызов описан в FXML-файле
        if (difficulty.isSelected()){
            dotsLeft=380; //Обновляем dotsLeft
            minesFlagged=100; //Обновляется minesFlagged
            field = CreateNewField(16, 30, 100); //Переставляются мины
            startButton.getScene().getWindow().setWidth(610); //Нужно изменить размер окна.
            mineIcon=new Image(getClass().getResourceAsStream("icon.png"));
            flagIcon=new Image(getClass().getResourceAsStream("flag.png"));//Загружаются маленькие иконки
            for (int s=0;s<9;s++){ //Загружаются иконки с цифрами
                currentNumberSet[s]= new Image(getClass().getClassLoader().getResourceAsStream("res/NumbersSmall/"+Integer.toString(s+1)+".png"));//Маленькие иконки вместо цифр из папки с ресурсами
            }
        } else{
            dotsLeft=80;
            minesFlagged=20;
            field = CreateNewField(10,10,20); //Создается новый массив с кноками
            startButton.getScene().getWindow().setWidth(355); //Изменяется размер окна
            mineIcon=new Image(getClass().getResourceAsStream("32icon.png"));
            flagIcon=new Image(getClass().getResourceAsStream("flagBig.png"));//Загружаются большие иконки для флагов и мин
            for (int s=0;s<9;s++){
                currentNumberSet[s]= new Image(getClass().getClassLoader().getResourceAsStream("res/NumbersBig/"+Integer.toString(s+1)+".png")); //Большие иконки вместо цифр
            }
        }

        mineCountLabel.setText(Integer.toString(minesFlagged)); //Обновляется надпись, которая играет роль счетчика помеченных флажками мин для игрока
        IsFirstButtonPressed = false; //Обновляется фактор первого нажатия на кнопку, чтобы не попасть на мину на первом же ходе
        setMineFieldPane(mineFieldPane, field); //Обновляется поле
        startButton.setText("Заново"); //Изменяется текст кнопкидля старта
        startButton.setLayoutX((startButton.getScene().getWindow().getWidth()/2)-(startButton.getPrefWidth()/2)); //Изменяется положение кнопки для старта. Она должна быть ровно посередине
    }

//    private void increaseTimer(Label label){
//        if (Integer.getInteger(label.getText())<999){
//            String x = label.getText();
//            x++;
//            label.setText(Integer.parseInt());
//        }
//    }

    private void CheckOtherButtons(mineButton thisButton){//Метод, который проверяет, есть ли мины в соседних кнопках.
        {
            //код снизу проверяет все в массиве рядом с данной, если они конечно есть.
            if (((thisButton.y - 1 >= 0 && thisButton.y - 1 < field.length) && (thisButton.x - 1 >= 0 && thisButton.x - 1 < field[0].length)) && (!field[thisButton.y - 1][thisButton.x - 1].isChecked)) CheckButton(field[thisButton.y - 1][thisButton.x - 1]); //Сверху слева
            if (((thisButton.y - 1 >= 0 && thisButton.y - 1 < field.length) && (thisButton.x >= 0 && thisButton.x < field[0].length)) && (!field[thisButton.y - 1][thisButton.x].isChecked)) CheckButton(field[thisButton.y - 1][thisButton.x]); //Сверху
            if (((thisButton.y - 1 >= 0 && thisButton.y - 1 < field.length) && (thisButton.x + 1 >= 0 && thisButton.x + 1 < field[0].length)) && (!field[thisButton.y - 1][thisButton.x + 1].isChecked)) CheckButton(field[thisButton.y - 1][thisButton.x+1]);  //Сверху справа
            if (((thisButton.y >= 0 && thisButton.y < field.length) && (thisButton.x - 1 >= 0 && thisButton.x - 1 < field[0].length)) && (!field[thisButton.y][thisButton.x - 1].isChecked)) CheckButton(field[thisButton.y][thisButton.x - 1]);    //Слева
            if (((thisButton.y >= 0 && thisButton.y < field.length) && (thisButton.x + 1 >= 0 && thisButton.x + 1 < field[0].length)) && (!field[thisButton.y][thisButton.x + 1].isChecked)) CheckButton(field[thisButton.y][thisButton.x + 1]);    //Справа
            if (((thisButton.y + 1 >= 0 && thisButton.y + 1 < field.length) && (thisButton.x - 1 >= 0 && thisButton.x - 1 < field[0].length)) && (!field[thisButton.y + 1][thisButton.x-1].isChecked)) CheckButton(field[thisButton.y + 1][thisButton.x - 1]);  //Слева снизу
            if (((thisButton.y + 1 >= 0 && thisButton.y + 1 < field.length) && (thisButton.x >= 0 && thisButton.x < field[0].length)) && (!field[thisButton.y + 1][thisButton.x].isChecked)) CheckButton(field[thisButton.y + 1][thisButton.x]);    //Снизу
            if (((thisButton.y + 1 >= 0 && thisButton.y + 1 < field.length) && (thisButton.x + 1 >= 0 && thisButton.x + 1 < field[0].length)) && (!field[thisButton.y + 1][thisButton.x + 1].isChecked)) CheckButton(field[thisButton.y + 1][thisButton.x + 1]);  //Справа снизу
            thisButton.isChecked=true; //Кнопка помечается как проверенная
            thisButton.setDisable(true); //И деактивируется
        }
        //return thisButton;
    }

    private void CheckButton(mineButton thisButton){
        {
            if (!thisButton.isFlagged) { //Если мина не помечена флагом
                //То код снизу проверяет наличие мин в соседних кнопках, если эти кнопки есть.
                if (((thisButton.y - 1 >= 0 && thisButton.y - 1 < field.length) && (thisButton.x - 1 >= 0 && thisButton.x - 1 < field[0].length)) && (field[thisButton.y - 1][thisButton.x - 1].isMineHere))
                    thisButton.minesNearby++; //Сверху слева
                if (((thisButton.y - 1 >= 0 && thisButton.y - 1 < field.length) && (thisButton.x >= 0 && thisButton.x < field[0].length)) && field[thisButton.y - 1][thisButton.x].isMineHere)
                    thisButton.minesNearby++; //Сверху
                if (((thisButton.y - 1 >= 0 && thisButton.y - 1 < field.length) && (thisButton.x + 1 >= 0 && thisButton.x + 1 < field[0].length)) && field[thisButton.y - 1][thisButton.x + 1].isMineHere)
                    thisButton.minesNearby++;  //Сверху справа
                if (((thisButton.y >= 0 && thisButton.y < field.length) && (thisButton.x - 1 >= 0 && thisButton.x - 1 < field[0].length)) && field[thisButton.y][thisButton.x - 1].isMineHere)
                    thisButton.minesNearby++;    //Слева
                if (((thisButton.y >= 0 && thisButton.y < field.length) && (thisButton.x + 1 >= 0 && thisButton.x + 1 < field[0].length)) && field[thisButton.y][thisButton.x + 1].isMineHere)
                    thisButton.minesNearby++;    //Справа
                if (((thisButton.y + 1 >= 0 && thisButton.y + 1 < field.length) && (thisButton.x - 1 >= 0 && thisButton.x - 1 < field[0].length)) && field[thisButton.y + 1][thisButton.x - 1].isMineHere)
                    thisButton.minesNearby++;  //Слева снизу
                if (((thisButton.y + 1 >= 0 && thisButton.y + 1 < field.length) && (thisButton.x >= 0 && thisButton.x < field[0].length)) && field[thisButton.y + 1][thisButton.x].isMineHere)
                    thisButton.minesNearby++;    //Снизу
                if (((thisButton.y + 1 >= 0 && thisButton.y + 1 < field.length) && (thisButton.x + 1 >= 0 && thisButton.x + 1 < field[0].length)) && field[thisButton.y + 1][thisButton.x + 1].isMineHere)
                    thisButton.minesNearby++;  //Справа снизу
                if (!thisButton.isMineHere) { //Если мины нет
                    thisButton.isChecked = true; //Кнопка помечается как проверенная
                    thisButton.setDisable(true); //Кнопка деактивируется
                    thisButton.setOpacity(0.50);;
                    dotsLeft--; //Уменьшается кол-во мин, которые нужно пометить
                    if (thisButton.minesNearby > 0) { //Если рядом с кнопкой есть мины
                        thisButton.setGraphic(new ImageView(currentNumberSet[thisButton.minesNearby-1])); //То показывается прямо на кнопке сколько, иконкой цифры
                    } else { //А если нет
                        CheckOtherButtons(thisButton); //Этим методом проверяются остальные кнопки
                        thisButton.setOpacity(0.10);
                    }
                    if (dotsLeft <= 0) startButton.setText("Ты победил");//Если игрок нажал на все кнопки, на которых нет мин, стартовая кнопка сообщает ему, что он победил
                    //Интересно, что даже после победы игрок всегда может убрать флажок и нажать на мину, в таком случае он проиграет
                    //Я думал убрать такую возможность, но решил оставить. Потому что всегда можно потерять то, к чему так долго шел.
                }
            }
        }
    }

    public mineButton SetMineButton(final mineButton thisButton) { //Метод настраивает кнопку, получая саму кнопку
    thisButton.setOnMouseClicked(new EventHandler<MouseEvent>() { //Если на кнопку кликнут мышкой
        @Override
        public void handle(MouseEvent mouseEvent) { //Обработчик получает данные о нажатии на кнопку, это переменная mouseEvent
            if (mouseEvent.getButton()== MouseButton.SECONDARY){ //Если кнопка была нажата Правой Кнопкой Мыши
                if (!thisButton.isFlagged) { //И если она не была помечена флагом
                    thisButton.isFlagged = true;  //То она маркируется флагом
                    thisButton.setGraphic(new ImageView(flagIcon)); //И показывает флажок
                    minesFlagged--; //Увеличивается кол-во помеченных флагом мин, тоесть уменьшается кол-во мин, которое нужно флагнуть
                    mineCountLabel.setText(Integer.toString(minesFlagged)); //Счетчик обновляется, показывая новое кол-во помеченных флагом кнопок с минами
                }
                else { //А если кнопка уже была помечена флагом
                    thisButton.isFlagged = false; //То параметр флага с неё убирается
                    thisButton.setGraphic(null); //И иконка
                    minesFlagged++; //Кол-во помеченных флагом мин уменьшается
                    mineCountLabel.setText(Integer.toString(minesFlagged));//Счетчик обновляется
                }
            }
            else //Если кнопка была нажата при помощи какой-либо другой кнопки мыши
            if (!thisButton.isFlagged) { //То кнопка проверяется на предмет флага
                if (!IsFirstButtonPressed) {  //Если это первая нажатая кнопка
                    if (thisButton.isMineHere) { //И тут находится мина
                        thisButton.isMineHere = false; //То мина с неё убирается
                        minesFlagged--; //Счетчик мин, которые нужно пометить уменьшается
                        mineCountLabel.setText(Integer.toString(minesFlagged)); //Счетчик обновляется, показывая новое значение
                        dotsLeft++; //А еще игроку придется нажать на кнопку больше взамен этой мины
                    }
                    IsFirstButtonPressed = true; //Мы нажали первую кнопку
                }
                if (thisButton.isMineHere) GameOver(); //Если на кнопке, на которую мы нажали была мина, то игра закончена.
                else CheckButton(thisButton); //Если мины нет - нужно проверить, сколько мин рядом
            }

        }
    }
    );
        return thisButton; //Возвращает настроенную кнопку
    }

    @FXML
    public void GameOver(){
        for(mineButton[] line : field){//Проходится по столбцу
            for (mineButton dot : line){//Проходится по строке
                dot.setText(""); //Очищает текст кнопки
                dot.setGraphic(null); //Убирает флажок
                if (dot.isMineHere) { //Если на кнопке была мина
                    dot.setGraphic(new ImageView(mineIcon)); //То эта мина показывается, картинкой.
                }
                dot.setDisable(true); //Вырубает каждую кнопку
                dot.setOpacity(1.00);
            }
        }
        startButton.setText("Ты проиграл"); //Главная кнопка показывает, что игрок проиграл
        //Теперь он может только нажать на главную кнопку и начать игру заново
    }


}

class mineButton extends Button { //Объявляется новый класс, наследуемый от обычной кнопки
    int x,y,minesNearby=0; // Координаты кнопки и кол-во мин рядом.
    mineButton(int x, int y,boolean difficult){ //Билдер кнопки, если при создании кнопки переданы аргументы, здесь это координаты,
        //То кнопки создадутся сразу с ними
        this.x=x;
        this.y=y;
        this.setText("");       //Очищается текст кнопки при создании.
        this.setDisabled(false);//Кнопка после создания активирована.
        Font s = new Font(13);
        if (difficult){
            this.setMaxSize(35, 35); //Размеры кнопки
            this.setMinSize(20, 20);
            this.setPrefSize(20, 20);
            this.setFont(new Font(8));
        } else {
            this.setMaxSize(35,35);
            this.setMinSize(33,33);
            this.setPrefSize(34,34);
        }
        this.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color;\n" +
                "    -fx-background-insets: 0, 1, 2;\n" +
                "    -fx-background-radius: 5, 4, 3;" +
                " -fx-text-fill: white; -fx-font-weight: bold;"
        ); //Нужно чтобы при фокусе кнопка не светилась
    }

    boolean isMineHere = false; //Наличие мины на кнопке. По умолчанию её нет.
    boolean isFlagged = false; //Поставлен ли на мине флажок.
    boolean isChecked = false; //Костыль. Проверяет была ли нажата кнопка, чтобы автоматика не проверяла её несколько раз
}
