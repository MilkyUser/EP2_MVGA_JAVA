public class MatrixIncompatibleException extends Exception{
    public MatrixIncompatibleException(String operation){
        super(String.format("Matrices given incompatible with %s operation!", operation));
    }
}
