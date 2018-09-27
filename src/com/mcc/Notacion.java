package com.mcc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Notacion {

  private String simbolos = "&|=>";
  private String operandos = "pqrst";
  private String regexContiene = "[-()" + simbolos + operandos + "]*";
  private String regexFormulaInCorrecta =
      ".*[" + operandos + "]{2,}.*|.*[-]{2,}.*|.*[" + simbolos + "]{2,}.*";
  private String regexMatchSimbolos = "[-" + simbolos + "]";

  public Notacion() {}

  public boolean estaBienFormada(String infijo) {
    boolean bienFormada = true;

    if (!parentesisBalanceados(infijo)) {
      bienFormada = false;
    } else if (!infijo.matches(regexContiene)) {
      bienFormada = false;
    } else if (infijo.matches(regexFormulaInCorrecta)) {
      bienFormada = false;
    }

    return bienFormada;
  }

  private boolean parentesisBalanceados(String infijo) {
    Stack<String> pila = new Stack<>();

    for (int i = 0; i < infijo.length(); i++) {
      if (infijo.charAt(i) == '(') {
        pila.push(")");
      } else if (infijo.charAt(i) == ')') {
        if (pila.isEmpty()) {
          return false;
        } else {
          pila.pop();
        }
      }
    }

    return pila.isEmpty();
  }

  private Boolean[][] matrizDeVerdad(Set<String> caracteres) {

    int rows = (int) Math.pow(2, caracteres.size());
    int cols = caracteres.size();

    Boolean[][] matriz = new Boolean[rows][cols + 1];

    //System.out.println(caracteres.toString().replaceAll("[]\\,\\[]", ""));

    for (int i = 0; i < rows; i++) {
      for (int j = cols - 1; j >= 0; j--) {
        //System.out.print((i / (int) Math.pow(2, j)) % 2 == 0 ? "V " : "F ");
        matriz[i][j] = (i / (int) Math.pow(2, j)) % 2 == 0;
      }
      //System.out.println();
    }

    return matriz;
  }

  public String evaluarPostfijo(String postfijo) {
    String postfijoSinSimbolos = postfijo;
    postfijoSinSimbolos = postfijoSinSimbolos.replaceAll(regexMatchSimbolos, "");
    Set<String> caracteres = new HashSet<>(Arrays.asList(postfijoSinSimbolos.split("")));

    Boolean[][] matriz = matrizDeVerdad(caracteres);
    int countVerdaderos = 0;
    Stack<String> postfijoPila;
    char caracter;

    for (int j = 0; j < matriz.length; j++) {
      postfijoPila = new Stack<>();

      for (int x = 0; x < postfijo.length(); x++) {
        caracter = postfijo.charAt(x);

        if (postfijo.length() == 1 && matriz[j][caracteres.size()-1]) {
          countVerdaderos++;
        } else if (Character.isAlphabetic(caracter) || Character.toString(caracter).equals("-")) {
          postfijoPila.push(Character.toString(caracter));
        } else {

          String lineaCaracteres = caracteres.toString().replaceAll("[]\\,\\[ ]", "");
          boolean valorUno;
          boolean valorDos = false;

          if (postfijoPila.size() > 1) {
            if (postfijoPila.peek().equals("-")) {
              postfijoPila.pop();
              valorUno = !matriz[j][lineaCaracteres.indexOf(postfijoPila.pop())];
            } else {
              valorUno = matriz[j][lineaCaracteres.indexOf(postfijoPila.pop())];
            }

            if (postfijoPila.peek().equals("-")) {
              postfijoPila.pop();
              valorDos = !matriz[j][lineaCaracteres.indexOf(postfijoPila.pop())];
            } else {
              valorDos = matriz[j][lineaCaracteres.indexOf(postfijoPila.pop())];
            }

          } else {
            valorUno = matriz[j][lineaCaracteres.indexOf(postfijoPila.pop())];
            if (matriz[j][caracteres.size()] != null) {
              valorDos = matriz[j][caracteres.size()];
            }
          }
          matriz[j][caracteres.size()] =
              tipoOperador(valorUno, valorDos, Character.toString(caracter));
          if (x == postfijo.length() - 1 && matriz[j][caracteres.size()]) {
            countVerdaderos++;
          }
        }
      }
    }

    String evaluacion;
    if (countVerdaderos == matriz.length) {
      evaluacion = "Tautologia";
    } else if (countVerdaderos == 0) {
      evaluacion = "Contradicción";
    } else {
      evaluacion = "Contingencia";
    }

    return evaluacion;
  }

  private boolean tipoOperador(boolean valorUno, boolean valorDos, String operador) {
    boolean operacion = false;
    switch (operador) {
      case "&":
        operacion = valorUno && valorDos;
        break;
      case "|":
        operacion = valorUno || valorDos;
        break;
      case "=":
        operacion = valorUno == valorDos;
        break;
      case ">":
        operacion = (!valorDos || valorUno);
        break;
    }

    return operacion;
  }

  public String infijoAPostfijo(String infijo) {

    StringBuilder postfijo = new StringBuilder();

    if (infijo != null) {
      infijo = "(" + infijo + ")";
      String[] listadoInfijo = infijo.split("");
      postfijo = new StringBuilder();

      Stack<String> operadores = new Stack<>();

      for (String caracterActual : listadoInfijo) {

        if (Character.isAlphabetic(caracterActual.charAt(0)) && operadores.peek().equals("-")) {
          postfijo.append(caracterActual);
          postfijo.append(operadores.pop());
        } else if (Character.isAlphabetic(caracterActual.charAt(0))) {
          postfijo.append(caracterActual);
        } else {
          // se recorre toda la pila hasta que encuentre un (
          if (caracterActual.equals(")")) {
            while (!operadores.peek().equals("(")) {
              postfijo.append(operadores.pop());
            }
            operadores.pop();

            if (operadores.size() >= 1 && operadores.peek().equals("-")) {
              postfijo.append(operadores.pop());
            }
          } else {
            operadores.push(caracterActual);
          }
        }
      }
    }

    return postfijo.toString();
  }
}
