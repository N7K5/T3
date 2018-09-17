
class T3Logic {

    int data[][];
    int rows, cols;

    //: 0 means nothing, 1 means X, 2 means O;
    String xo[]= {"", "X", "O"}; 
    int X= 1, O= 0;

    T3Logic(int _rows, int _cols) {
        data= new int[_rows][_cols];
        rows= _rows;
        cols= _cols;
        reset();
    }

    String get(int row, int col) {
        if(row<rows && col<cols) {
            return xo[data[row][col]];
        }
        return "";
    }

    boolean set(int row, int col, String symbol) {
        if(row<rows && col<cols) {
            if(isEmpty(row, col)) {
                if(symbol.equals("X") || symbol.equals("x")) {
                    data[row][col]= X;
                    return true;
                }
                if(symbol.equals("O") || symbol.equals("o")) {
                    data[row][col]= O;
                    return true;
                }
            }
        }
        return false;
    }

    boolean isEmpty(int row, int col) {
        if(row<rows && col<cols && data[row][col]==0) {
            return true;
        }
        return false;
    }

    void reset() {
        for(int row=0; row<rows; row++) {
            for(int col=0; col<cols; col++) {
                data[row][col]= 0;
            }
        }
    }


}