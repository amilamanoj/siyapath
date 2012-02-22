package org.siyapath.sample;

public class CalcDemo {
    public static void main(String[] args) {
        CalcDemo calc = new CalcDemo();
        calc.processSampleJob();
    }

    public void processSampleJob(){

        int i, j;
        double temp, result;
        for(i=0; i<=50000000; i++){
            for(j=0; j<=1000000; j++){
                System.out.println("i: " + i);
                System.out.println("j: " + j);
                temp= (i + ++j)*i++;
                System.out.println("temp: " + temp);
                result = temp++ + temp/7.5674324;
                System.out.println("result: " + result);
            }
        }
    }
}
