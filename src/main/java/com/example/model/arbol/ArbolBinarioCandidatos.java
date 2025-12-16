package com.example.model.arbol;

import java.util.List;

public class ArbolBinarioCandidatos {

    // Nodo de árbol binario
    public static class Nodo {
        public int valor;
        public Nodo izq;
        public Nodo der;

        public Nodo(int valor) {
            this.valor = valor;
        }
    }

    private Nodo raiz;

    public Nodo getRaiz() {
        return raiz;
    }

    // Insert simple (BST) para que se vea clarísimo "árbol"
    public void insertar(int valor) {
        raiz = insertarRec(raiz, valor);
    }

    private Nodo insertarRec(Nodo actual, int valor) {
        if (actual == null) return new Nodo(valor);

        if (valor < actual.valor) actual.izq = insertarRec(actual.izq, valor);
        else actual.der = insertarRec(actual.der, valor);

        return actual;
    }

    public static ArbolBinarioCandidatos desdeLista(List<Integer> candidatos) {
        ArbolBinarioCandidatos arbol = new ArbolBinarioCandidatos();
        for (int v : candidatos) arbol.insertar(v);
        return arbol;
    }

    // Imprimir árbol en forma simple (preorder)
    public String imprimirPreorden() {
        StringBuilder sb = new StringBuilder();
        imprimirPreordenRec(raiz, sb, "");
        return sb.toString();
    }

    private void imprimirPreordenRec(Nodo n, StringBuilder sb, String prefijo) {
        if (n == null) return;
        sb.append(prefijo).append("• ").append(n.valor).append("\n");
        imprimirPreordenRec(n.izq, sb, prefijo + "  L-");
        imprimirPreordenRec(n.der, sb, prefijo + "  R-");
    }
}
