package exceptions;

/**
 * WrongInputException extends Exception and is thrown when and wrong Input is entered.
 * @author Jawwad Khan
 */
public class WrongInputException extends Exception{

    public WrongInputException(){

    }

    public WrongInputException(String Warning){
        super(Warning);
    }


}
