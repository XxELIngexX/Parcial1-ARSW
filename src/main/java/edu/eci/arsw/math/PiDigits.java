package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;


///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

    private static ArrayList<PiThread> threads = new ArrayList<>();


    /**
     * Returns a range of hexadecimal digits of pi.
     *
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int start, int count, int nThreads) throws InterruptedException {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }


        double sum = 0;

        //dividir la cantidad (count en el numero de hilos)
        int numeroPorHilo = count / nThreads;

        for (int i = 0; i < nThreads; i++) {
            int min = i * numeroPorHilo;
            int max = (i == nThreads - 1) ? count : min + numeroPorHilo;
            byte[] digits = new byte[max-min];

            //al primer hilo se le asigna el valor de start
            if (i == 0){
                threads.add(new PiThread(digits,start));
            }
            // de ahi para adelante, se le asigna como valor de start al strat inicial
            // mas las posiciones que debe calcular el hilo anterior
            else {
                start +=digits.length;
                threads.add(new PiThread(digits,start));
            }
            threads.get(i).start();
        }



        for (Thread t : threads) {
            t.join();
        }
        for (Thread t : threads) {
            System.out.println(t.isAlive());
        }

        ArrayList<Byte> toPut = new ArrayList<>();

            for(PiThread i:threads){
                for(byte j:i.getDigits()){
                    toPut.add(j);
                }
            }
        byte[] salida = new byte[toPut.size()];
            for(int i =0; i<toPut.size();i++){
                salida[i]=toPut.get(i);
            }

        return salida;
    }



}

