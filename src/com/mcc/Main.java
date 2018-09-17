package com.mcc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Main {

  private static final String rutaArchivo =
      "/home/dessis-aux23/Documentos/maestria/notacionPostfija/formulas.txt";

  public static void main(String[] args) {

    FileReader fileReader = null;
    BufferedReader bufferedReader = null;

    try {

      fileReader = new FileReader(Main.rutaArchivo);
      bufferedReader = new BufferedReader(fileReader);

      String infijo;
      String[] listadoInfijo;
      StringBuilder postfijo;
      Stack<String> operadores;

      while ((infijo = bufferedReader.readLine()) != null) {
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
      System.out.println("No se encontro el archivo en esa ruta. " + e.toString());
    } catch (IOException e) {
      System.out.println("Ha ocurrido un error." + e.toString());
    } finally {
      try {
        if (fileReader != null) {
          fileReader.close();
        }
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      } catch (IOException e) {
        System.out.println("Ha ocurrido un error." + e.toString());
      }
    }
  }
}
