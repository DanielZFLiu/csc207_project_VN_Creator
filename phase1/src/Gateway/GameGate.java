package Gateway;
import Interface.LoadSave;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameGate implements LoadSave {
    public static void main(String[] args) {
        new GameGate();
    }

    /**
     *
     * A Gateway class used to implement <I>LoadSave</I> for loading and saving games.
     * The save method takes a /List</Hashmap>> which represents the games
     * and saves it to a serialized txt file.
     * The load method reads the serialized txt file and returns a /List</Hashmap>>
     * which represents the games.
     *
     * @param args
     */


    /*
     * Each hashmap in the list of hashmaps should have this specific format:
     *  _______________________________________
     * |Key             | Value                |
     * |_______________________________________|
     * |-4 (Name)       | "Bruhther's Broments"|
     * |-3 (Author)     | "Le Bruh"            |
     * |-2 (Public)     | "false"              |
     * |-1 (ChoiceLimit)| "4"                  |
     * |0               | "Dialogue 0"         |
     * |1               | "Dialogue 1"         |
     * |2               | "Dialogue 2"         |
     * |...             | ...                  |
     * |_______________________________________|
     * Remember that java hashmap is just dict in python
     * */
    String myPath;

    public GameGate(){
        this.myPath = findSaveGameFile(System.getProperty("user.dir"));
    }

    private String findSaveGameFile(String filePath){
        File dir = new File(filePath);
        File[] directoryListing = dir.listFiles();
        String foundPath = "";

        if (directoryListing != null) {
            for (File child : directoryListing) {
                if(child.getAbsolutePath().contains("data") && child.getAbsolutePath().contains("SerialGames.txt")){
                    return child.getAbsolutePath();
                }
                else{
                    String path = findSaveGameFile(child.getAbsolutePath());
                    if (!path.equals("")){
                        return path;
                    }
                }
            }
        }
        return foundPath;
    }

    // remember to return an arraylist
    public List<HashMap> load() {

        // Source: https://www.geeksforgeeks.org/how-to-serialize-hashmap-in-java/

        List<HashMap> myMaps = new ArrayList<>();

        try {
//            File gameFile = new File(myPath);
//            gameFile.createNewFile();

            FileInputStream fileInput = new FileInputStream(myPath);

            ObjectInputStream objectInput
                    = new ObjectInputStream(fileInput);

            myMaps = (List<HashMap>) objectInput.readObject();

            objectInput.close();
            fileInput.close();
        }

        catch (IOException obj1) {
            System.out.println("Loading...\n" +
                    "No Saved Games.");
//            obj1.printStackTrace();

        }

        catch (ClassNotFoundException obj2) {
            System.out.println("Class not found!");
//            obj2.printStackTrace();

        }

        return myMaps;
    }

    // an arraylist will be passed here
    public void save(List<HashMap> myMap){
        // Source: https://www.geeksforgeeks.org/how-to-serialize-hashmap-in-java/
        try {
            FileOutputStream myFileOutStream
                    = new FileOutputStream(myPath);

            ObjectOutputStream myObjectOutStream
                    = new ObjectOutputStream(myFileOutStream);

            myObjectOutStream.writeObject(myMap);

            // closing FileOutputStream and
            // ObjectOutputStream
            myObjectOutStream.close();
            myFileOutStream.close();
        }
        catch (IOException e) {
            System.out.println(e);
//            e.printStackTrace();
        }
    }

}
