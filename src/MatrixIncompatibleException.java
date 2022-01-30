////////////////////////////////////////////////////////////////
//                                                            //
//  Antonia Bandeira de Melo Coimbra                          //
//  Nº USP: 10875951                                          //
//                                                            //
//  Bruno Leite de Andrade                                    //
//  Nº USP 11369642                                           //
//                                                            //
//                                                            //
////////////////////////////////////////////////////////////////

public class MatrixIncompatibleException extends Exception{
    public MatrixIncompatibleException(String operation){
        super(String.format("Matrices given incompatible with %s operation!", operation));
    }
}
