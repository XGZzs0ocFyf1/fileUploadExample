package a.v.g.wordApp.exceptions;

public class FileNotFoundInBucketException extends Exception{
    public FileNotFoundInBucketException(String msg){
        super(msg);
    }

    public FileNotFoundInBucketException(String msg, Throwable cause){
        super(msg, cause);
    }
}