package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Fraction num1 = new Fraction(5, 4);
        num1.division(num1);
        num1.print();
        System.out.println();
        String filePath = "C:/matrix.txt";
        List<Fraction []> filler = new ArrayList<>();
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while((line = reader.readLine()) != null){
                    String [] numbers = line.split(" ");
                    Fraction [] row = new Fraction[numbers.length];
                    for(int i = 0; i < numbers.length; i++)
                    {
                        String [] fparts = numbers[i].split("/");
                        if(fparts.length == 1)
                            row[i] = new Fraction(Integer.parseInt(fparts[0]));
                        else
                            row[i] = new Fraction(Integer.parseInt(fparts[0]), Integer.parseInt(fparts[1]));
                    }
                    filler.add(row);
                }
            }
        } catch (IOException e) {
            System.out.println("Reading file \"" + filePath + "\" error.");
            return;
        }
        Fraction [][] matrix = new Fraction[filler.size()][];
        for(int i = 0; i < filler.size(); i++)
            matrix[i] = filler.get(i);

        for (Fraction[] fractions : matrix) {
            for (Fraction fraction : fractions) fraction.print();
            System.out.println();
        }
        System.out.println();

        for(int i = 0; i < matrix.length; i++) {
            int setElem = pickSetElem(matrix, i);
            Fraction[] tmp = matrix[i];
            matrix[i] = matrix[setElem];
            matrix[setElem] = tmp;
            Fraction div = matrix[i][i];
            for(int c = matrix[i].length - 1; c >= i; c--)
                matrix[i][c].division(div);

            for(int j = i + 1; j < matrix.length; j++){
                for(int k = i + 1; k < matrix[i].length; k++) {
                    Fraction prod = new Fraction(-1);
                    prod.multiply(matrix[i][k]);
                    prod.multiply(matrix[j][i]);
                    matrix[j][k].add(prod);
                }
            }
            for(int z = i + 1; z < matrix.length; z++){
                matrix[z][i] = new Fraction(0);
            }

            for (Fraction[] fractions : matrix) {
                for (Fraction fraction : fractions) fraction.print();
                System.out.println();
            }
            System.out.println();
        }
    }

    public static int pickSetElem(Fraction [][] matrix, int column) {
        int elem = column;
        double max = Double.MIN_VALUE;
        for(int i = column; i < matrix.length; i++)
        {
            if(Math.abs(max) < Math.abs(matrix[i][column].num())){
                max = matrix[i][column].num();
                elem = i;
            }
        }
        return elem;
    }

}