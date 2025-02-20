package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/resources/matrix.txt";
        boolean isStopped = false;
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
        int i = 0, j = 0;
        while(i < matrix.length && j < matrix[i].length -1) {
            int setElem = pickSetElem(matrix, i, j);
            if (matrix[setElem][j].num() == 0) {
                if (j == matrix[i].length - 2 && matrix[i][j + 1].num() != 0) {
                    isStopped = true;
                    break;
                }
                j++;
            } else {
                Fraction[] tmp = matrix[i];
                matrix[i] = matrix[setElem];
                matrix[setElem] = tmp;
                Fraction div = matrix[i][j];

                for (Fraction[] fractions : matrix) {
                    for (Fraction fraction : fractions) fraction.print();
                    System.out.println();
                }
                System.out.println("\nafter division");
                for (int c = matrix[i].length - 1; c >= i; c--)
                    matrix[i][c].division(div);

                for (Fraction[] fractions : matrix) {
                    for (Fraction fraction : fractions) fraction.print();
                    System.out.println();
                }
                System.out.println();

                for (int u = 0; u < matrix.length; u++) {
                    if (u == i)
                        continue;
                    for (int k = j + 1; k < matrix[i].length; k++) {
                        Fraction prod = new Fraction(-1);
                        prod.multiply(matrix[i][k]);
                        prod.multiply(matrix[u][j]);
                        matrix[u][k].add(prod);
                    }
                }
                for (int z = 0; z < matrix.length; z++) {
                    if (z == i)
                        continue;
                    matrix[z][j] = new Fraction(0);
                }
                i++;
                j++;
            }
        }

            for (Fraction[] fractions : matrix) {
                for (Fraction fraction : fractions) fraction.print();
                System.out.println();
            }
            System.out.println();

            if(isStopped)
            {
                System.out.println("No solutions");
                return;
            }
            System.out.println("Infinite solutions");
            for(int p = 0; p < matrix.length; p++)
            {
                if (Arrays.stream(matrix[p]).allMatch(f -> f.num() == 0))
                    continue;
                boolean isFirst = true;
                for(int o = 0; o < matrix[p].length - 1; o++)
                {
                    if(matrix[p][o].num() == 0)
                        continue;
                    if(matrix[p][o].num() > 0 && !isFirst) {
                        System.out.print(" + ");
                    }
                    matrix[p][o].print();
                    System.out.printf("*x%d", o + 1);
                    isFirst = false;
                }

                System.out.print(" = ");
                matrix[p][matrix[p].length - 1].print();
                System.out.println();
            }
            findAllBasicsSolutions(matrix);
        }


    public static int pickSetElem(Fraction [][] matrix, int row, int column) {
        int elem = row;
        double max = Double.MIN_VALUE;
        for(int i = row; i < matrix.length; i++)
        {
            if(Math.abs(max) < Math.abs(matrix[i][column].num())){
                max = matrix[i][column].num();
                elem = i;
            }
        }
        return elem;
    }

    public static int rang(Fraction [][] matrix) {
        int rang = 0;
        for (Fraction[] fractions : matrix) {
            for(int i = 0; i < fractions.length - 1; i++) {
                if(fractions[i].num() != 0) {
                    rang++;
                    break;
                }
            }
        }
        return rang;
    }

    public static void findAllBasicsSolutions(Fraction [][] matrix) {
        int rang = rang(matrix);
        int Xes = matrix[0].length - 1;
        List<Integer> permutation = new ArrayList<>();
        for(int i = 0; i < rang; i++)
            permutation.add(i + 1);
        int p = rang;
        while(p > 0) {
            if(permutation.get(p - 1) == Xes + p - rang)
                p--;
            else {
                System.out.println(permutation + " p: " + p);
                permutation.set(p - 1, permutation.get(p - 1) + 1);
                for(int i = p; i < rang; i++)
                    permutation.set(i, permutation.get(i - 1) + 1);
                p = rang;
                permCheck(permutation);
            }
        }
        System.out.println(permutation + " p: " + p);
    }

    public static void permCheck(List<Integer> basis) {

    }

}