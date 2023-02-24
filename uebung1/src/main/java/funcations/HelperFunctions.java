package funcations;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Contains all Helperfuncations for the Main, for example all the Input Functions.
 */
public class HelperFunctions {
    /**
     * Input method for integer
     * @param i
     * @param j
     * @return
     */
    public static int integerInputs(int i, int j){
        Scanner sc = new Scanner(System.in);
        int CHOICE = -1;
        while (CHOICE < 0 || CHOICE > j+1) {
            try {
                System.out.println("\nChoose a session number between " + i  + " and " + j + ": ");
                CHOICE = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Your Input was incorrect!");
            }
        }
        return CHOICE;
    }
    /**
     *
     * Input function for strings.
     * @return
     */
    public static String Input() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the Name you are searching for: ");
        String sr = sc.nextLine();

        return sr;
    }

    /**
     * Input from for Task2Menu.
     * @return
     */
    public static int InputTask2(){
        Scanner sc = new Scanner(System.in);
        int CHOICE = -1;
        while (CHOICE < 0 || CHOICE > 8) {
            try {
                System.out.println("\nChoose a number between 1 and 9: ");
                CHOICE = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Your Input was incorrect!");
            }
        }
        return CHOICE;
    }
    public static int InputTask3(){
        Scanner sc = new Scanner(System.in);
        int CHOICE = -1;
        while (CHOICE < 0 || CHOICE > 10) {
            try {
                System.out.println("\nChoose a number between 1 and 7: ");
                CHOICE = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Your Input was incorrect!");
            }
        }
        return CHOICE;
    }

    /**
     * Input function which will return the integer enterd into the console
     * @return
     */
    public static int getInput() {
        Scanner sc = new Scanner(System.in);
        int CHOICE = -1;
        while (CHOICE < 0 || CHOICE > 4) {
            try {
                System.out.println("\nChoose a number between 1 and 3: ");
                CHOICE = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Your Input was incorrect!");
            }
        }
        return CHOICE;

    }

    /**
     *
     * Input function for StammdatenFilePath.
     * @return
     */
    public static String StammdatenFilePath() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your absolute Stammdaten file directory: ");
        String sr = sc.nextLine();
        while(true){
            if(sr.endsWith("MDB_STAMMDATEN.XML")){
                return sr;

            }else{
                System.out.println("File not found, please check again and enter again: ");
                System.out.println("The Path should have the form: C:\\...\\....\\....\\.....\\....\\MDB_STAMMDATEN.XML\\");
                sr = sc.nextLine();

            }
        }
    }

    /**
     *
     * Input function for XMLFilePath.
     * @return
     */
    public static String XMLFilePath() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your  absolute XML file directory: ");
        String sr = sc.nextLine();
        while(true){
            if(sr.endsWith("_19\\")){
                return sr;

            }else{
                System.out.println("File not found, please check again and enter again: ");
                System.out.println("The Path should have the form: C:\\...\\....\\....\\.....\\....\\Protokolle_Bundestag_19\\");
                sr = sc.nextLine();

            }
        }
    }




}

