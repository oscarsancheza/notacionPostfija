package com.mcc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class Main {

  public static void main(String[] args) {

    try {
      Scanner input = new Scanner(new File("formulas.txt"));
      String infijo;
      String[] listadoInfijo;
      StringBuilder postfijo;
      Stack<String> operadores;

      while (input.hasNextLine()) {
        infijo = input.nextLine();
        infijo = "(" + infijo + ")";
        listadoInfijo = infijo.split("");
        postfijo = new StringBuilder();

        operadores = new Stack<>();
        operadores.push("(");

        for (String caracterActual : listadoInfijo) {

          if (Character.isAlphabetic(caracterActual.charAt(0)) || caracterActual.equals("-")) {
            postfijo.append(caracterActual);
          } else {

            if (caracterActual.equals(")")) {
              while (!operadores.peek().equals("(")) {
                postfijo.append(operadores.pop());
              }
              operadores.pop();
            } else if (operadores.peek().equals("(") || caracterActual.equals("(")) {
              operadores.push(caracterActual);
            } else {
              postfijo.append(operadores.pop());
              operadores.push(caracterActual);
            }
          }
        }

        System.out.println(postfijo);
      }

    } catch (FileNotFoundException e) {
      System.out.println("No se encontro el archivo. " + e.toString());
    }
  }
}
