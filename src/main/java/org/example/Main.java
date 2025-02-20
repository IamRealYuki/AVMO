package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static boolean isStopped = false;
    public static void main(String[] args) {
        String filePath = "src/main/resources/matrix.txt";
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

        printMatrix(matrix);

        solve(matrix, true);

        printMatrix(matrix);
        String resultStr = "One solution";
        for(Fraction[] row: matrix)
        {
            for(int e = 0; e < row.length - 1; e++) {
                if (row[e].num() != 0)
                    break;
                if (e == row.length - 2 && row[e + 1].num() != 0)
                {
                    System.out.println("No solutions");
                    return;
                }
                else if(e == row.length - 2 && row[e + 1].num() == 0)
                    resultStr = "Infinite solutions";
            }
        }

        System.out.println(resultStr);

        for (Fraction[] fractions : matrix) {
            if (Arrays.stream(fractions).allMatch(f -> f.num() == 0))
                continue;

            int pivot = -1;
            for (int o = 0; o < fractions.length - 1; o++) {
                if (fractions[o].num() != 0) {
                    pivot = o;
                    break;
                }
            }

            if (pivot == -1) {
                continue;
            }

            System.out.printf("x%d = ", pivot + 1);

            boolean isFirst = true;
            for (int o = 0; o < fractions.length - 1; o++) {
                if (o == pivot || fractions[o].num() == 0)
                    continue;

                if (fractions[o].num() > 0) {
                    System.out.print(" - ");
                } else if (fractions[o].num() < 0 && !isFirst) {
                    System.out.print(" + ");
                }

                if (Math.abs(fractions[o].num()) != 1) {
                    fractions[o].printAbs();
                }
                System.out.printf("x%d", o + 1);
                isFirst = false;
            }

            if (fractions[fractions.length - 1].num() > 0 && !isFirst) {
                System.out.print(" + ");
            }
            fractions[fractions.length - 1].print();
            System.out.println("\n");
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
                System.out.println(permutation);
                permCheck(permutation, matrix, rang);
                permutation.set(p - 1, permutation.get(p - 1) + 1);
                for(int i = p; i < rang; i++)
                    permutation.set(i, permutation.get(i - 1) + 1);
                p = rang;
            }
        }
        System.out.println(permutation);
        permCheck(permutation, matrix, rang);
    }

    public static void permCheck(List<Integer> basis, Fraction [][] matrix, int rang) {
        for(int i = 0; i < rang; i++)
        {
            if(matrix[i][basis.get(i) - 1].num() == 0) {
                boolean stepBack = true;
                while (stepBack) {
                    if(i < 0){
                        System.out.println("is not a basics solution");
                        return;
                    }
                    for (int j = i + 1; j < matrix.length; j++) {
                        if (matrix[j][basis.get(i) - 1].num() != 0) {
                            Fraction[] tmp = matrix[j];
                            matrix[j] = matrix[i];
                            matrix[i] = tmp;
                            stepBack = false;
                            break;
                        }
                    }
                    i--;
                }
            }
        }
        Fraction [][] solution = new Fraction[rang][basis.size() + 1];
        for(int i = 0; i < rang; i++) {
            for(int j = 0; j < basis.size(); j++) {
                int num = matrix[i][basis.get(j) - 1].getNumerator();
                int den = matrix[i][basis.get(j) - 1].getDenominator();
                solution[i][j] = new Fraction(num, den);
            }
            int num = matrix[i][matrix[i].length - 1].getNumerator();
            int den = matrix[i][matrix[i].length - 1].getDenominator();
            solution[i][basis.size()] = new Fraction(num, den);
        }
        solve(solution, false);
        System.out.println("solved");
        System.out.print("[");
        for(int i = 0; i < basis.size(); i++) {
            System.out.print("x" + basis.get(i) + " = ");
            solution[i][basis.size()].print();
            if(i != basis.size() - 1)
                System.out.print("; ");
        }
        System.out.println("]\n");
    }
    public static Fraction [][] solve(Fraction [][] matrix, boolean print) {
        int i = 0, j = 0;
        while(i < matrix.length && j < matrix[i].length -1) {
            int setElem = pickSetElem(matrix, i, j);
            if (matrix[setElem][j].num() == 0) {
                if (j == matrix[i].length - 2 && matrix[i][j + 1].num() != 0) {
                    break;
                }
            } else {
                Fraction[] tmp = matrix[i];
                matrix[i] = matrix[setElem];
                matrix[setElem] = tmp;

                Fraction div = matrix[i][j];
                for (int c = matrix[i].length - 1; c >= i; c--)
                    matrix[i][c].division(div);
                if(print)
                    printMatrix(matrix);

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
            }
            j++;
        }
        return matrix;
    }

    public static void printMatrix(Fraction[][] matrix) {
        for (Fraction[] fractions : matrix) {
            for (Fraction fraction : fractions)
                fraction.print();
            System.out.println();
        }
        System.out.println();
    }
}