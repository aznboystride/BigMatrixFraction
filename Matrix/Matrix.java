package Matrix;

import Fraction.Fraction;
import java.math.BigInteger;
/**
 *
 * @author Fair Nuri Aboshehwa
 */
public class Matrix {
    
    private Fraction[][] list;
    
    @Override
    public String toString()
    {
        String res = "";
        String end;
        for(int row = 0; row < list.length; row++)
        {
            for(int col = 0; col < list[0].length; col++)
            {
                end = (col == list[0].length-1 ? "\n" : "   ");
                res += (list[row][col].toString() + end);
            }
        }
        return res;
    }
    
    protected Fraction[][] copy(Fraction[][] list)
    {
        Fraction[][] newList = new Fraction[list.length][list[0].length];
        for(int row = 0; row < list.length; row++)
        {
            for(int col = 0; col < list[0].length; col++)
            {
                newList[row][col] = list[row][col];
            }
        }
        return newList;
    }
        
    public int getRowSize()
    {
        return list.length;
    }
    
    public int getColumnSize()
    {
        return list[0].length;
    }
    
    public Fraction get(int row, int column)
    {
        return list[row][column];
    }
    
    public Matrix(Matrix m)
    {
        list = new Fraction[m.getRowSize()][m.getColumnSize()];
        for(int row = 0; row < list.length; row++)
        {
            for(int col = 0; col < list[0].length; col++)
            {
                list[row][col] = m.get(row, col);
            }
        }
    }
    
    public Matrix(Fraction[][] list)
    {
        this.list = list;
    }
    
    public Matrix multiply(Fraction num)
    {
        Fraction[][] list = new Fraction[this.list.length][this.list[0].length];
        for(int row = 0; row < this.list.length; row++)
        {
            for(int col = 0; col < this.list[0].length; col++)
            {
                list[row][col] = this.list[row][col].multiply(num);
            }
        }
        return new Matrix(list);
    }
    
    public Matrix multiply(BigInteger num)
    {
        Fraction[][] list = new Fraction[this.list.length][this.list[0].length];
        for(int row = 0; row < this.list.length; row++)
        {
            for(int col = 0; col < this.list[0].length; col++)
            {
                list[row][col] = this.list[row][col].multiply(new Fraction(num));
            }
        }
        return new Matrix(list);
    }
    
    public Matrix multiply(String num)
    {
        Fraction[][] list = new Fraction[this.list.length][this.list[0].length];
        for(int row = 0; row < this.list.length; row++)
        {
            for(int col = 0; col < this.list[0].length; col++)
            {
                list[row][col] = this.list[row][col].multiply(new Fraction(num));
            }
        }
        return new Matrix(list);
    }
    
    public Matrix subtract(Matrix m)
    {
        Fraction[][] list = new Fraction[m.getRowSize()][m.getColumnSize()];
        for(int row = 0; row < list.length; row++)
        {
            for(int col = 0; col < list[0].length; col++)
            {
                list[row][col] = this.list[row][col].subtract(m.get(row, col));
            }
        }
        return new Matrix(list);
    }
    
    public Matrix add(Matrix m)
    {
        Fraction[][] list = new Fraction[m.getRowSize()][m.getColumnSize()];
        for(int row = 0; row < list.length; row++)
        {
            for(int col = 0; col < list[0].length; col++)
            {
                list[row][col] = this.list[row][col].add(m.get(row, col));
            }
        }
        return new Matrix(list);
    }
    
    public Matrix zeroBottom()
    {
        Fraction[][] list = new Fraction[this.list.length+1][this.list[0].length];
        for(int row = 0; row < this.list.length+1; row++)
        {
            for(int col = 0; col < this.list[0].length; col++)
            {
                if(row == this.list.length)
                    list[row][col] = new Fraction("0");
                else
                    list[row][col] = this.list[row][col];
            }
        }
        return new Matrix(list);
    }
    
    public Matrix zeroRight()
    {
        Fraction[][] list = new Fraction[this.list.length][this.list[0].length+1];
        for(int row = 0; row < this.list.length; row++)
        {
            for(int col = 0; col < this.list[0].length+1; col++)
            {
                if(col == this.list[0].length)
                    list[row][col] = new Fraction("0");
                else
                    list[row][col] = this.list[row][col];
            }
        }
        return new Matrix(list);
    }
    
    // O(n^3)
    public Matrix multiply(Matrix right)
    {
        Fraction[][] zeros = new Fraction[right.getRowSize()][right.getColumnSize()];
        for(int i = 0; i < zeros.length; i++)
        {
            for(int j = 0; j < zeros[0].length; j++)
            {
                zeros[i][j] = new Fraction();
            }
        }
        for (int row = 0; row < right.getRowSize(); row++)
        {
            for (int col = 0; col < right.getRowSize(); col++)
            {
                for (int k = 0; k < right.getRowSize(); k++)
                {
                    zeros[row][col] = zeros[row][col].add(get(row, k).multiply(right.get(k,col)));
                }
            }
        }
        return new Matrix(zeros);
    }
    
    public Matrix transpose()
    {
        Fraction[][] list = new Fraction[getRowSize()][getColumnSize()];
        for(int row = 0; row < list.length; row++)
        {
            for(int col = 0; col < list[0].length; col++)
            {
                list[row][col] = this.list[col][row];
            }
        }
        return new Matrix(list);
    }
        
    public Matrix solve(Matrix m)
    {
        return new LUDecomposition(this).solve(m);
    }
}
