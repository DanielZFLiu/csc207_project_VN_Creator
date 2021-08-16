package Presenter;
import Controller.MessageController;
import Entity.Message;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class GamePresenter{

    Stage window;
    Object userChoice;
    String styleSheet = getClass().getResource("GamePresenter.css").toExternalForm();
    TableView<Message> messageTableView;
    // = findFile("GamePresenter.css", System.getProperty("user.dir"));

    public GamePresenter(){
        // To do Platform.runlater, the toolkits must be initialized, see
        // https://staticfinal.blog/2015/04/04/javafx-toolkit-not-initialized-solved/
        new JFXPanel();

        Platform.runLater(() -> {
            this.window = new Stage();
            window.setTitle("VN Creator");
            window.setOnCloseRequest(e -> {
                System.out.println("Are you sure you want to exit? " +
                        "Nothing will be saved!");
                e.consume();
            });
            Platform.setImplicitExit(false);
        });
    }

    public Integer displayChoices(Object suspendedObject, List choices){
        Platform.runLater(() -> {
            VBox choicesLayout = wrapChoices(suspendedObject, choices);

            BorderPane layout = new BorderPane();
            layout.setCenter(choicesLayout);

            displayScene(layout);
        });

        suspendThread(suspendedObject);
        if (userChoice == null){
            return -1;
        }
        return (int) userChoice;
    }

    public void displayMessages(Object suspendedObject, String username, List<Message> messages) {
        Platform.runLater(() -> {
            window = new Stage();
            window.setTitle(username + "'s Message Box");

            //ID column
            TableColumn<Message, String> idColumn = new TableColumn<>("ID");
            idColumn.setMinWidth(200);
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            //Time column
            TableColumn<Message, Date> timeColumn = new TableColumn<>("Time");
            timeColumn.setMinWidth(200);
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
            //from column
            TableColumn<Message, String> fromColumn = new TableColumn<>("From");
            fromColumn.setMinWidth(100);
            fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
            //to column
            //TableColumn<Message, String> toColumn = new TableColumn<>("To");
            //toColumn.setMinWidth(100);
            //toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
            //msg column
            TableColumn<Message, String> messageColumn = new TableColumn<>("Message");
            messageColumn.setMinWidth(700);
            messageColumn.setCellValueFactory(new PropertyValueFactory<>("msg"));
            //status column
            TableColumn<Message, Boolean> statusColumn = new TableColumn<>("READ");
            statusColumn.setMinWidth(100);
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> deleteButtonClicked(suspendedObject));
            Button quitButton = new Button("QUIT");
            quitButton.setOnAction(e -> quitButtonClicked(suspendedObject));

            //HBox hBox = new HBox();
            //hBox.getChildren().addAll(deleteButton, quitButton);

            messageTableView = new TableView<>();
            messageTableView.setItems(getMessages(messages));
            messageTableView.getColumns().addAll(timeColumn, fromColumn,messageColumn,statusColumn);

            VBox vbox = new VBox();
            vbox.getChildren().addAll(messageTableView, deleteButton, quitButton);

            //addExitButton(suspendedObject, vbox);

            Scene scene = new Scene(vbox);
            window.setScene(scene);
            window.show();
        });

        suspendThread(suspendedObject);


    }

    public void deleteButtonClicked(Object suspendedObject){
        ObservableList<Message> messagesSelected;
        messagesSelected = messageTableView.getSelectionModel().getSelectedItems();
        for (Message msg: messagesSelected){
            MessageController messageController = (MessageController)suspendedObject;
            messageController.removeMessage(msg.getid());
        }
    }

    public void quitButtonClicked(Object suspendedObject){
        window.hide();
        synchronized (suspendedObject) {
            suspendedObject.notify();
        }
    }

    public ObservableList<Message> getMessages(List<Message> messages){
        ObservableList<Message> messages1 = FXCollections.observableArrayList();
        for (Message m: messages) {
            messages1.add(m);
        }
        return messages1;
    }

    public int displayChoices(Object suspendedObject, List choices, String text) {
        Platform.runLater(() -> {
            VBox textLayout = wrapDialogue(text);
            VBox choicesLayout = wrapChoices(suspendedObject, choices);

            BorderPane layout = new BorderPane();
            layout.setCenter(choicesLayout);
            layout.setBottom(textLayout);

            displayScene(layout);
        });

        suspendThread(suspendedObject);
        if (userChoice == null){
            return -1;
        }
        return (int) userChoice;
    }

    public List displayInputs(Object suspendedObject, List inputTags){
        Platform.runLater(() -> {
            VBox inputLayout = wrapInputs(suspendedObject, inputTags);

            BorderPane layout = new BorderPane();
            layout.setCenter(inputLayout);

            displayScene(layout);
        });

        suspendThread(suspendedObject);
        return (List) userChoice;
    }

    public List displayInputs(Object suspendedObject, List inputTags, String text){
        Platform.runLater(() -> {
            VBox inputLayout = wrapInputs(suspendedObject, inputTags);
            VBox textLayout = wrapDialogue(text);

            BorderPane layout = new BorderPane();
            layout.setCenter(inputLayout);
            layout.setBottom(textLayout);

            displayScene(layout);
        });

        suspendThread(suspendedObject);
        return (List) userChoice;
    }

    public void displayTextScene(Object suspendedObject, String dialogue) {
        Platform.runLater(() -> {
            VBox dialogueLayout = wrapDialogue(dialogue);
            VBox pictureLayout = wrapChoices(null, new ArrayList());
            addExitButton(suspendedObject, dialogueLayout);

            BorderPane layout = new BorderPane();
            layout.setBottom(dialogueLayout);
            layout.setCenter(pictureLayout);

            displayScene(layout);
        });

        suspendThread(suspendedObject);
    }

    public void displayTextScene(Object suspendedObject, String dialogue, String textArt) {
        Platform.runLater(() -> {
            VBox pictureLayout = wrapDialogue(textArt);
            VBox dialogueLayout = wrapDialogue(dialogue);
            addExitButton(suspendedObject, dialogueLayout);

            BorderPane layout = new BorderPane();
            layout.setCenter(pictureLayout);
            layout.setBottom(dialogueLayout);

            displayScene(layout);
        });

        suspendThread(suspendedObject);
    }

    public void displayPictureScene(Object suspendedObject, String dialogue, String name) {
        String picturePath = findFile(name, System.getProperty("user.dir"));
        Platform.runLater(() -> {
            ImageView imageView = wrapImage(picturePath);
            VBox dialogueLayout = wrapDialogue(dialogue);
            addExitButton(suspendedObject, dialogueLayout);

            BorderPane layout = new BorderPane();
            layout.setCenter(imageView);
            layout.setBottom(dialogueLayout);

            displayScene(layout);
        });

        suspendThread(suspendedObject);
    }

    public void terminateGUI(){
        Platform.setImplicitExit(true);
        Platform.runLater(() -> {
            window.close();
        });
    }

    private void suspendThread(Object suspendedObject) {
        synchronized (suspendedObject){
            try{
                suspendedObject.wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception; object.wait failed.");
            }
        }
    }

    private VBox wrapChoices(Object suspendedObject, List choices) {
        VBox layout = new VBox();
        layout.setMinHeight(650);
        layout.setMaxHeight(800);
        layout.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        layout.setAlignment(Pos.CENTER);
        for(int i = 0; i < choices.size(); i++){
            final int counter = i;
            Button button = new Button(choices.get(i).toString());
            button.setOnAction(event -> {
                userChoice = counter;
                window.hide();
                synchronized (suspendedObject) {
                    suspendedObject.notify();
                }
            });
            layout.getChildren().add(button);
        }
        return layout;
    }

    private VBox wrapInputs(Object suspendedObject, List choices) {
        ArrayList inputList = new ArrayList();
        VBox layout = new VBox();
        layout.setMinHeight(650);
        layout.setMaxHeight(800);
        layout.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        layout.setAlignment(Pos.CENTER);
        for(int i = 0; i < choices.size(); i++){
            Label inputLabel = new Label(choices.get(i).toString());
            TextField input = new TextField();
            input.setMaxWidth(400);
            inputList.add(input);
            layout.getChildren().addAll(inputLabel, input);
        }
        Button button = new Button("Finish Entering");
        button.setOnAction(event -> {
            for(int i = 0; i < inputList.size(); i++){
                inputList.set(i, ((TextField) inputList.get(i)).getText());
            }
            userChoice = inputList;
            window.hide();
            synchronized (suspendedObject) {
                suspendedObject.notify();
            }
        });
        layout.getChildren().add(button);
        return layout;
    }

    private VBox wrapDialogue(String dialogue) {
        VBox dialogueLayout = new VBox();
        dialogueLayout.setMinHeight(150);
        dialogueLayout.setMaxHeight(650);
        dialogueLayout.setAlignment(Pos.CENTER);
        dialogueLayout.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        dialogueLayout.setPadding(new Insets(20));
        Label dialogueLable = new Label(dialogue);
        dialogueLable.setWrapText(true);
        dialogueLayout.getChildren().add(dialogueLable);
        return dialogueLayout;
    }

    private ImageView wrapImage(String picturePath) {
        InputStream stream;
        try {
            stream = new FileInputStream(picturePath);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found!");
            return null;
        }
        Image image = new Image(stream);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setX(0);
        imageView.setY(0);
        imageView.setFitWidth(1200);
        imageView.setFitHeight(650);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    // System.getProperty("user.dir"))
    private String findFile(String name, String filePath){
        File dir = new File(filePath);
        File[] directoryListing = dir.listFiles();
        String foundPath = "";

        if (directoryListing != null) {
            for (File child : directoryListing) {
                if(child.getAbsolutePath().contains("data") && child.getAbsolutePath().contains(name)){
                    return child.getAbsolutePath();
                }
                else{
                    String path = findFile(name, child.getAbsolutePath());
                    if (!path.equals("")){
                        return path;
                    }
                }
            }
        }
        return foundPath;
    }

    private void addExitButton(Object suspendedObject, VBox dialogueLayout) {
        Button button = new Button("Next");
        button.setOnAction(event -> {
            window.hide();
            synchronized (suspendedObject) {
                suspendedObject.notify();
            }
        });
        dialogueLayout.getChildren().add(button);
    }

    private void displayScene(BorderPane layout) {
        Scene scene = new Scene(layout, 1200, 800);
        scene.getStylesheets().add(styleSheet);
        window.setScene(scene);
        window.show();
    }
}
