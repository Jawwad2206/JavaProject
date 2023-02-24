
import exceptions.WrongInputException;
import funcations.HelperFunctions;
import org.apache.uima.UIMAException;
import org.xml.sax.SAXException;
import ui.UserInterface;
import ui.UserInterfaceMongoDB;
import ui.UserInterfaceNLP;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;

/**
 * This is the main of the whole project, in order to start the programm you have to start the main. The Main will take
 * the file paths to the required Documents for the Task sheet and check if the Path are correct.
 * @author Jawwad Khan
 */
public class main {
    public  static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, WrongInputException, ParseException, UIMAException {

        System.out.println("Important! Please make sure, that all the File exits!");

        //String StammDatenBlatt = HelperFunctions.StammdatenFilePath();// checks for the path and if it is correct

        System.out.println("\n");

        //String XMLFiles = HelperFunctions.XMLFilePath(); //checks for the path and if it is correct

        //UserInterface.main2(StammDatenBlatt, XMLFiles); //starts the Userinterface with the paths entered.

        String txtpath = HelperFunctions.credentialsMongo();//gets and checks if the file for the credentials are correct

        System.out.println("\n");

        String ddcPath = HelperFunctions.pathDDC3(); //get and checks if the file for the ddc3 is correct

        System.out.println("\n");

        String posMap = HelperFunctions.pathPosMap();
        //UserInterfaceMongoDB.main3(txtpath, StammDatenBlatt, XMLFiles); // starts the Userinterface for the MongoDB task

        UserInterfaceNLP.main4(txtpath,ddcPath, posMap);

    }

}
