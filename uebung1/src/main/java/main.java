import funcations.HelperFunctions;
import org.xml.sax.SAXException;
import ui.UserInterface;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * This is the main of the whole project, in order to start the programm you have to start the main. The Main will take
 * the file paths to the required Documents for the Task sheet and check if the Path are correct.
 */
public class main {
    public  static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        //LepTop = C:\Users\jawwa\OneDrive\5.Semester\ProgrammierPraktikum\1.Blatt (Aufgaben)\MdB-Stammdaten-data\MDB_STAMMDATEN.XML
        //Desktop = C:\Users\jawwa\Desktop\5.Semester\ProgrammierPraktikum\1.Blatt (Aufgaben)\MdB-Stammdaten-data\MDB_STAMMDATEN.XML
        String StammDatenBlatt = HelperFunctions.StammdatenFilePath();

        //LepTop = C:\Users\jawwa\OneDrive\5.Semester\ProgrammierPraktikum\1.Blatt (Aufgaben)\MdB-Stammdaten-data\MDB_STAMMDATEN.XML
        //Desktop = C:\Users\jawwa\Desktop\5.Semester\ProgrammierPraktikum\1.Blatt (Aufgaben)\MdB-Stammdaten-data\MDB_STAMMDATEN.XML

        String XMLFiles = HelperFunctions.XMLFilePath();
        //Desktop = C:\Users\jawwa\Desktop\5.Semester\ProgrammierPraktikum\Protokolle_Bundestag_19\
        //Leptop = C:\Users\jawwa\OneDrive\5.Semester\ProgrammierPraktikum\Protokolle_Bundestag_19\

        UserInterface.main3(StammDatenBlatt, XMLFiles);

    }
}
