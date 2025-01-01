package a.v.g.wordApp.exceptions;

public class CodeNotFoundException  extends Exception{
    public CodeNotFoundException(String msg){
        super(msg);
    }

    public CodeNotFoundException(String msg, Throwable cause){
        super(msg, cause);
    }
}