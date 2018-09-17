package Matrix;

import Fraction.Fraction;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Fair Nuri Aboshehwa
 */
public class LUDecomposition {
    
    private Matrix lowerTriangular;
    private Matrix upperTriangular;
    
    private class Key
    {
        int row;
        int col;

        Key(int row, int col)
        {
            this.row = row;
            this.col = col;
        }

        @Override
        public int hashCode()
        {
            return row ^ col;
        }

        @Override
        public boolean equals(Object obj)
        {
            if(this == obj)
                return true;
            if(obj == null)
                return false;
            if(getClass() != obj.getClass())
                return false;
            Key oth = (Key) obj;
            if(row != oth.row)
                return false;
            if(col != oth.col)
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "row: " + row + " col: " + col;
        }
    }
    
    public LUDecomposition(Matrix m)
    {
        decompose(m);
    }
    
    private void decompose(Matrix m)
    {
        int mainRow = 0;
        
        Fraction[][] list = new Fraction[m.getRowSize()][m.getColumnSize()];
        Fraction[][] upper = new Fraction[m.getRowSize()][m.getColumnSize()];
        HashMap<Key, Fraction> operands = new HashMap<>();
        
        for(int row = 0; row < m.getRowSize(); row++)
        {
            for(int col = 0; col < m.getColumnSize(); col++)
            {
                list[row][col] = m.get(row,col);
            }
        }
        for(int col = 0; col < list[0].length; col++)
        {
            for(int row = col; row < list.length; row++)
            {
                if(row == col)
                {
                    if(list[row][col].toString().equals("0"))
                    {
                        for(int trow = row; trow < list.length; trow++)
                        {
                            if(!list[trow][col].toString().equals("0"))
                            {
                                swap(list[row], list[trow]);
                                break;
                            }
                        }
                    }
                    mainRow = row;
                }
                else
                {
                    Fraction constant = list[row][col].divide(list[mainRow][col]);
                    operands.put(new Key(row,col), constant);
                    for(int column = col; column < list[0].length; column++)
                    {
                        list[row][column] = list[row][column].subtract(list[mainRow][column].multiply(constant));
                    }
                }
            }
        }
        for(int row = 0; row < upper.length; row++)
        {
            for(int col = 0; col < upper[0].length; col++)
            {
                if(col == row)
                    upper[row][col] = new Fraction("1");
                else if(col > row)
                    upper[row][col] = new Fraction();
                else if(operands.containsKey(new Key(row, col)))
                    upper[row][col] = operands.get(new Key(row, col));
                else
                    upper[row][col] = new Fraction();
            }
        }
        lowerTriangular = new Matrix(list);
        upperTriangular = new Matrix(upper);
    }
    
    private Matrix getUpperTriangular()
    {
        return lowerTriangular;
    }
    
    private Matrix getLowerTriangular()
    {
        return upperTriangular;
    }
    
    private Matrix solveForY(Matrix left, Matrix right)
    {
        ArrayList<Fraction> list = new ArrayList<>();
        for(int row = 0; row < right.getRowSize(); row++)
        {
            Fraction solution = right.get(row, 0);
            for(int col = 0; col < row; col++)
            {
                solution = solution.subtract(list.get(col).multiply(left.get(row,col)));
            }
            list.add(solution);
        }
        Fraction[][] result = new Fraction[list.size()][list.size()];
        for(int row = 0; row < result.length; row++)
        {
            for(int col = 0; col < result[0].length; col++)
            {
                result[row][col] = new Fraction();
            }
        }
        for(int row = 0; row < result.length; row++)
        {
            result[row][0] = list.get(row);
        }
        return new Matrix(result);
    }
    
    private Matrix solveForX(Matrix left, Matrix right)
    {
        ArrayList<Fraction> list = new ArrayList<>();
        for(int row = left.getRowSize()-1; row >= 0; row--)
        {
            Fraction solution = right.get(row, 0);
            for(int col = left.getColumnSize()-1; col > row; col--)
            {
                solution = solution.subtract(list.get(left.getColumnSize()-1 - col).multiply(left.get(row, col)));
            }
            solution = solution.divide(left.get(row,row));
            list.add(solution);
        }
        Fraction[][] result = new Fraction[list.size()][list.size()];
        for(int row = 0; row < result.length; row++)
        {
            for(int col = 0; col < result[0].length; col++)
            {
                result[row][col] = new Fraction();
            }
        }
        for(int row = 0; row < result.length; row++)
        {
            result[row][0] = list.get(list.size()-row-1);
        }
        return new Matrix(result);
    }
    
    public Matrix solve(Matrix m)
    {
        Matrix ySolution = solveForY(getLowerTriangular(), m);
        return solveForX(getUpperTriangular(), ySolution);
    }
    
    private void swap(Fraction[] a, Fraction[] b)
    {
        for(int row = 0; row < a.length; row++)
        {
            Fraction temp = a[row];
            a[row] = b[row];
            b[row] = temp;
        }
    }
}
