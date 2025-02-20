package org.example;

import static java.lang.Math.min;

public class Fraction {
    private int numerator;
    private int denominator;

    public Fraction() {
        this.numerator = 1;
        this.denominator = 1;
    }

    public Fraction(int numerator) {
        this.numerator = numerator;
        this.denominator = 1;
    }

    public Fraction(int numerator, int denominator) {
        if(denominator == 0){
            System.out.println("Uh-uh you, dumbass, you can't just put 0 here...\nIt has been changed to 1 by accident");
            denominator = 1;
        }
        this.denominator = denominator;
        this.numerator = numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    public int getNumerator() {
        return numerator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public void add(Fraction number) {
        int a = this.numerator * number.getDenominator();
        int b = number.getNumerator() * this.denominator;
        this.numerator = a + b;
        this.denominator *= number.getDenominator();
        this.normalize();
    }

    public void multiply(Fraction number) {
        this.numerator *= number.getNumerator();
        this.denominator *= number.getDenominator();
        this.normalize();
    }

    public void division(Fraction number) {
        int a = this.numerator * number.getDenominator();
        this.denominator *= number.getNumerator();
        this.numerator = a;
        this.normalize();
    }

    public  void normalize() {

        for(int i = 2; i <= Math.abs(this.denominator) && i <= Math.abs(this.numerator); i++)
        {
            if(this.denominator % i == 0 && this.numerator % i == 0)
            {
                this.numerator /= i;
                this.denominator /= i;
                i--;
            }
        }
        if(this.denominator < 0)
        {
            this.numerator *= -1;
            this.denominator *= -1;
        }
    }

    public void print() {
        if(this.denominator != 1 && this.numerator != 0)
            System.out.print(" " + this.numerator + "/" + this.denominator + " ");
        else
            System.out.print(this.numerator + " ");
    }

    public void printAbs() {
        if(this.denominator != 1 && this.numerator != 0)
            System.out.print(Math.abs(this.numerator) + "/" + Math.abs(this.denominator) + " ");
        else
            System.out.print(Math.abs(this.numerator) + " ");
    }

    public double num() {
        return (double)this.numerator / this.denominator;
    }
}