////////////////////////////////////////////////////////////////
//                                                            //
// Numero USP - Nome Completo                                 //
//                                                            //
////////////////////////////////////////////////////////////////

public class Matrix {

    public static final double SMALL = 0.000001;    // constante usada na comparação entre valores double

    private int lin, col;
    private double[][] m;

    // constroi uma matriz com as entradas iguais a zero

    public Matrix(int lin, int col) {

        this.lin = lin;
        this.col = col;
        this.m = new double[this.lin][this.col];
    }

    // metodo estatico que cria uma matriz identidade de tamanho n x n.

    public static Matrix identity(int n) {

        Matrix mat = new Matrix(n, n);

        for (int i = 0; i < mat.lin; i++) mat.m[i][i] = 1.0;

        return mat;
    }

    // constroi uma matriz e inicia as entradas a partir do vetor values.

    public Matrix(int lin, int col, double[] values) {

        int k = 0;

        this.lin = lin;
        this.col = col;
        this.m = new double[this.lin][this.col];

        for (int i = 0; i < this.lin; i++) {

            for (int j = 0; j < this.col; j++) {

                this.m[i][j] = values[k++];
            }
        }
    }

    // metodo que devolve uma copia da matriz.

    Matrix copy() {

        Matrix copy = new Matrix(this.lin, this.col);

        for (int i = 0; i < this.lin; i++) {

            for (int j = 0; j < this.col; j++) {

                copy.m[i][j] = this.m[i][j];
            }
        }

        return copy;
    }

    // metodo que imprime a matriz.

    void print() {

        for (int i = 0; i < this.lin; i++) {

            for (int j = 0; j < this.col; j++) {

                System.out.printf("%7.2f ", this.m[i][j]);
            }

            System.out.println();
        }
    }

    // devolve o valor da entrada na linha i e coluna j.

    public double get(int i, int j) {

        return this.m[i][j];
    }

    // define um novo valor para a entrada na coluna i e linha j.

    public void set(int i, int j, double value) {

        this.m[i][j] = value;
    }

    // metodo que calcula o determinante de uma matriz 2x2.

    public double det2x2() {

        if (this.lin == 2 && this.col == 2) {

            return this.m[0][0] * this.m[1][1] - this.m[0][1] * this.m[1][0];
        }

        throw new IllegalStateException("Invalid matrix size!");
    }

    // metodo que calcula o determinante de uma matriz 3x3.

    double det3x3() {

        if (this.lin == 3 && this.col == 3) {

            return this.m[0][0] * this.cofactor3x3(0, 0) + this.m[0][1] * this.cofactor3x3(0, 1) + this.m[0][2] * this.cofactor3x3(0, 2);
        }

        throw new IllegalStateException("Invalid matrix size!");
    }

    // metodo que calcula o cofator para os indices (i, j) de uma matriz 3x3.

    public double cofactor3x3(int i, int j) {

        if (this.lin == 3 && this.col == 3) {

            int k = 0;
            double[] values = new double[4];

            for (int lin = 0; lin < this.lin; lin++) {

                for (int col = 0; col < this.col; col++) {

                    if (lin != i && col != j) values[k++] = this.m[lin][col];
                }
            }

            Matrix tmp = new Matrix(2, 2, values);

            double cof = ((i + j) % 2 == 0 ? 1 : -1) * tmp.det2x2();

            return cof;

        }

        throw new IllegalStateException("Invalid matrix size!");
    }

    // funcao que calcula a inversa de uma matriz 3x3.

    public Matrix invert3x3() {

        double det = this.det3x3();

        if (Math.abs(det) < SMALL) {

            throw new IllegalStateException("Singular matrix!\n");
        }

        Matrix inverse = new Matrix(3, 3);

        for (int i = 0; i < inverse.lin; i++) {

            for (int j = 0; j < inverse.col; j++) {

                inverse.m[j][i] = this.cofactor3x3(i, j) / det;
            }
        }

        return inverse;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                                 //
    // Funcoes relacionadas a classe Matrix que precisam ser implementadas para o programa funcionar da forma esperada //
    //                                                                                                                 //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Matrix multiply(Matrix m) throws MatrixIncompatibleException {

        if(this.col != m.lin) throw new MatrixIncompatibleException("multiplication");

        Matrix product = new Matrix(this.lin, m.col);
        for (int i=0; i < this.lin; i++){
            for (int j=0; j < m.col; j++){
                for (int k=0; k < this.col; k++){
                    product.set(i, j, product.get(i, j) + this.get(i, k) * m.get(k, j));
                }
            }
        }
        return product;
    }

    public Vector transform(Vector v) throws MatrixIncompatibleException {

        Matrix tempM;
        if (this.lin == 3){
            tempM = new Matrix(3, 1, new double[]{v.getX(), v.getY(), 1});
            tempM = multiply(tempM);
        } else {
            tempM = new Matrix(2, 1, new double[]{v.getX(), v.getY()});
            tempM = multiply(tempM);
        }
        return new Vector(
                SMALL > Math.abs(tempM.m[0][0]) ? 0 : tempM.m[0][0],
                SMALL > Math.abs(tempM.m[1][0]) ? 0 : tempM.m[1][0]
        );
    }

    public static Matrix get_rotation_matrix(double theta) {
        theta = theta * Math.PI / 180;
        Matrix rotationMatrix = new Matrix(2, 2);
        rotationMatrix.set(0,0, Math.cos(theta));
        rotationMatrix.set(0,1, -Math.sin(theta));
        rotationMatrix.set(1,0, Math.sin(theta));
        rotationMatrix.set(1,1, Math.cos(theta));
        return rotationMatrix;
    }

    public static Matrix get_scale_matrix(double k) {

        return new Matrix(2, 2, new double[]{k, 0, 0, k});
    }

    public static Matrix get_translation_matrix(Vector v) {

        return new Matrix(3, 3, new double[]{
                1, 0, v.getX(),
                0, 1, v.getY(),
                0, 0, 1
            }
        );
    }

    public static Matrix get_transformation_matrix(Vector e1, Vector e2, Vector t) {

        return new Matrix(3, 3, new double[]{
                e1.getX(), e2.getX(), t.getX(),
                e1.getY(), e2.getY(), t.getY(),
                0, 0, 1
            }
        );
    }

    public static Matrix get_observer_matrix(Vector position, Vector direction) throws MatrixIncompatibleException {
        Vector right = get_rotation_matrix(-90).transform(direction);
        return new Matrix(3, 3, new double[]{
                right.getX(), direction.getX(), position.getX(),
                right.getY(), direction.getY(), position.getY(),
                0, 0, 1
            }
        ).invert3x3();
    }
}
