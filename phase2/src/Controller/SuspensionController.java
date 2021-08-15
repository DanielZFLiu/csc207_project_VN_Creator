package Controller;

import Presenter.GamePresenter;
import Presenter.SuspensionPresenter;
import UseCase.UserManager;

import java.util.ArrayList;
import java.util.Scanner;

public class SuspensionController {
    private String username;

    public SuspensionController(){

    }

    public void run(){
        while (true) {
            GamePresenter gamePresenter = new GamePresenter();
            ArrayList<String> choices = new ArrayList<>();
            choices.add("1. suspend a User?(cannot suspend an admin)");
            choices.add("2. unsuspend a User?");
            choices.add("3. quit");
            int choice = 1 + gamePresenter.displayChoices(this, choices, "Hello, what would you like to do?");
            if (choice == 1){
                suspend();
            } else if (choice == 2){
                unsuspend();
            } else if (choice == 3){
                break;
            } else {
                gamePresenter.displayTextScene(this, "BACK", username + "Invalid choice. Please try again!");
            }
        }
    }

    public void suspend(){
        GamePresenter gamePresenter = new GamePresenter();
        ArrayList<String> inputs = new ArrayList<>();
        inputs.add("Please input the username you need to suspend:");
        String choice = (String)gamePresenter.displayInputs(this, inputs).get(0);
        username = choice;
        if (!username.startsWith("Admin_")){UserManager tempum = new UserManager();
            boolean rst = tempum.suspendUser(username);
            if (rst){
                gamePresenter.displayTextScene(this, "CONTINUE", username + "has been successfully unsuspended");
            } else {gamePresenter.displayTextScene(this, "BACK", "Username not found");}} else {gamePresenter.displayTextScene(this, "BACK", "You cannot suspend another Admin !");}

    }

    public void unsuspend(){
        GamePresenter gamePresenter = new GamePresenter();
        ArrayList<String> inputs = new ArrayList<>();
        inputs.add("Please input the username you need to unsuspend:");
        String choice = (String)gamePresenter.displayInputs(this, inputs).get(0);
        username = choice;
        UserManager tempum = new UserManager();
        boolean rst = tempum.unsuspendUser(username);
        if (rst){gamePresenter.displayTextScene(this, "CONTINUE", username + "has been successfully unsuspended");}
        else {gamePresenter.displayTextScene(this, "BACK", "Username not found");}
    }

    public String getUsername() {
        return username;
    }


}
