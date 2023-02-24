package funcations;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.SerialFormat;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.CasIOUtils;

import java.io.*;
import java.util.Scanner;

/**
 * Contains all Helperfuncations for the Main, for example all the Input Functions.
 * @author Jawwad Khan
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
                System.out.println("\nChoose a number number between " + i  + " and " + j + ": ");
                CHOICE = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Your Input was incorrect!");
            } catch (ArrayIndexOutOfBoundsException a){
                System.out.println("Your Input was incorrect!");
            }
        }
        return CHOICE;
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
                System.out.println("\nChoose a number between 1 and 7: ");
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
                System.out.println("\nChoose a number between 1 and 9: ");
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
        System.out.println("The Path should have the form: C:\\...\\....\\....\\.....\\....\\MDB_STAMMDATEN.XML");
        String sr = sc.nextLine();
        while(true){
            if(sr.endsWith("MDB_STAMMDATEN.XML")){
                File file = new File(sr);
                if(file.exists()){
                    return sr;
                }
                else{
                    System.out.println("File not found, please check again and enter again: ");
                    System.out.println("The Path should have the form: C:\\...\\....\\....\\.....\\....\\MDB_STAMMDATEN.XML");
                    sr = sc.nextLine();
                }

            }else{
                System.out.println("File not found, please check again and enter again: ");
                System.out.println("The Path should have the form: C:\\...\\....\\....\\.....\\....\\MDB_STAMMDATEN.XML");
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
        System.out.println("Important! The file should end with Protokolle_Bundestag_19");
        String sr = sc.nextLine();
        while(true){
            if(sr.endsWith("Protokolle_Bundestag_19")){
                File file = new File(sr);
                if(file.exists()){
                    return sr;
                }
                else{
                    System.out.println("File not found, please check again and enter again: ");
                    System.out.println("The Path should have the form: C:\\...\\....\\....\\.....\\....\\Protokolle_Bundestag_19");
                    sr = sc.nextLine();
                }

            }else{
                System.out.println("File not found, please check again and enter again: ");
                System.out.println("The Path should have the form: C:\\...\\....\\....\\.....\\....\\Protokolle_Bundestag_19");
                sr = sc.nextLine();

            }
        }
    }
    /**
     *
     * Input function for Fractions.
     * @return
     */
    public static String FractionInput() {
        System.out.println("Here is a List of available fractions:[CDU/CSU, AfD, fraktionslos, SPD, DIE LINKE, BÜDNIS 90/DIE GRÜNEN, FDP]");
        System.out.println("Just copy the name from the List.");
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the fraction you are searching for: ");
        String sr = sc.nextLine();

        return sr;
    }
    public static String PartyInput() {
        System.out.println("Here is a List of available fractions:[CDU, CSU, AfD, fraktionslos, SPD, DIE LINKE, BÜDNIS 90/DIE GRÜNEN, FDP]");
        System.out.println("Just copy the name from the List.");
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the fraction you are searching for: ");
        String sr = sc.nextLine();

        return sr;
    }

    /**
     * Input for date
     * @return
     */
    public static String dateInput(){
        System.out.println("Please enter a date in the form: yyyy-MM-dd");

        Scanner sc = new Scanner(System.in);
        String sr = sc.nextLine();

        return sr;
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
     *
     * Input function for MongoDB credentials
     * @return
     */
    public static String credentialsMongo() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Important! if you want to use the my Database just press Enter");
        System.out.println("Please enter your PRG_WiSe22_220.txt: ");
        String sr = sc.nextLine();
        while(true) {
            if (sr.equals("")) {
                return "src/main/resources/PRG_WiSe22_220.txt";
            } else {
                if (sr.endsWith(".txt")) {
                    File file = new File(sr);
                    if (file.exists()) {
                        return sr;
                    } else {
                        System.out.println("File not found, please check again and enter again: ");
                        System.out.println("The Path should have the form: PRG_WiSe22_220.txt:");
                        System.out.println("Important! if you want to use the my Database just press Enter");
                        sr = sc.nextLine();
                    }

                } else {
                    System.out.println("File not found, please check again and enter again: ");
                    System.out.println("The Path should have the form: PRG_WiSe22_220.txt:");
                    System.out.println("Important! if you want to use the my Database just press Enter");
                    sr = sc.nextLine();

                }
            }
        }
    }
    /**
     *
     * Input function for DDC3 csv
     * @return
     */
    public static String pathDDC3() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Important! if you want to use the my DDC3 just press Enter");
        System.out.println("Please enter your ddc3-names-de.csv: ");
        String sr = sc.nextLine();
        while(true) {
            if (sr.equals("")) {
                return "src/main/resources/ddc3-names-de.csv";
            } else {
                if (sr.endsWith(".txt")) {
                    File file = new File(sr);
                    if (file.exists()) {
                        return sr;
                    } else {
                        System.out.println("File not found, please check again and enter again: ");
                        System.out.println("The Path should have the form: src/main/resources/ddc3-names-de.csv");
                        System.out.println("Important! if you want to use the my Database just press Enter");
                        sr = sc.nextLine();
                    }

                } else {
                    System.out.println("File not found, please check again and enter again: ");
                    System.out.println("The Path should have the form: src/main/resources/ddc3-names-de.csv");
                    System.out.println("Important! if you want to use the my Database just press Enter");
                    sr = sc.nextLine();

                }
            }
        }
    }

    /**
     *
     * Input function for PosMap txt
     * @return
     */
    public static String pathPosMap() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Important! if you want to use the my DDC3 just press Enter");
        System.out.println("Please enter your am_posmap.txt: ");
        String sr = sc.nextLine();
        while(true) {
            if (sr.equals("")) {
                return "am_posmap.txt";
            } else {
                if (sr.endsWith(".txt")) {
                    File file = new File(sr);
                    if (file.exists()) {
                        return sr;
                    } else {
                        System.out.println("File not found, please check again and enter again: ");
                        System.out.println("The Path should have the form: src/main/resources/am_posmap.txt");
                        System.out.println("Important! if you want to use the my Database just press Enter");
                        sr = sc.nextLine();
                    }

                } else {
                    System.out.println("File not found, please check again and enter again: ");
                    System.out.println("The Path should have the form: src/main/resources/am_posmap.txt");
                    System.out.println("Important! if you want to use the my Database just press Enter");
                    sr = sc.nextLine();

                }
            }
        }
    }

    /**
     * Helperfunctions which creates a XMi from a jcas
     * @param RedeCas
     */
    public static String toXMI(JCas RedeCas){
        CAS cas = RedeCas.getCas();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        String text = null;
        try{
           CasIOUtils.save(cas, byteArray, SerialFormat.XMI);
           text = byteArray.toString();
        }catch (IOException e){
            e.printStackTrace();
        }

        return text;
    }


    /**
     * Helperfunctions which transforms a XMI into an JCAS
     * @param XMI
     * @return
     * @throws CASException
     */
    public JCas toJcas(String XMI) throws CASException {
        ByteArrayInputStream byteArray = new ByteArrayInputStream(XMI.getBytes());
        CAS cas = null;
        try{
            CasIOUtils.load(byteArray, cas);
        }catch (IOException e){
            e.printStackTrace();
        }
        return cas.getJCas();
    }

    /**
     * Creates a .txt and writes text into it
     * @param fileName
     * @param text
     */
    public static void createTxT(String fileName, String text){
        File file = new File("Antworten", fileName  + ".txt");

        try {
            if (!file.createNewFile()){
                file.delete();
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file.getAbsolutePath());
            writer.write(text);
            writer.close();
        }catch(IOException ex){
            ex.printStackTrace();
            return;
        }
    }





}

