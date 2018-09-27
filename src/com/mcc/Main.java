package com.mcc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    try {
      Scanner input = new Scanner(new File("formulas.txt"));
      String infijo;
      Notacion notacion = new Notacion();

      while (input.hasNextLine()) {
        infijo = input.nextLine();
        boolean esBienFormada = notacion.estaBienFormada(infijo);
        if (esBienFormada) {
          System.out.println("Infijo:" + infijo);
          String postfijoConvertido = notacion.infijoAPostfijo(infijo);
          System.out.println("Postfijo:" + postfijoConvertido);
          String evaluacion = notacion.evaluarPostfijo(postfijoConvertido);
          System.out.println("Es:" + evaluacion);
        } else {
          System.out.println("Formula mal formada:" + infijo);
        }
        System.out.println();
      }

    } catch (FileNotFoundException e) {
      System.out.println("No se encontro el archivo. " + e.toString());
    }
  }
}
