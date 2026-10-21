package mochilaramificacionpoda;

import java.util.*;

public class MochilaRamificacionPoda {


    // Clase para representar cada objeto
    static class Item {
        int valor;
        int peso;
        int indice;
        double ratio;

        Item(int valor, int peso, int indice) {
            this.valor = valor;
            this.peso = peso;
            this.indice = indice;
            this.ratio = (double) valor / peso;
        }
    }

    static int n;
    static int capacidad;
    static Item[] items;
    static int mejorValor = 0;
    static boolean[] seleccionActual;
    static boolean[] mejorSeleccion;

    public static void main(String[] args) {
        int[] valores = {79, 32, 47, 18, 26, 85, 33, 40, 45, 59};
        int[] pesos   = {85, 26, 48, 21, 22, 95, 43, 45, 55, 52};

        capacidad = 101;
        n = valores.length;

        items = new Item[n];

        for (int i = 0; i < n; i++) {
            items[i] = new Item(valores[i], pesos[i], i + 1);
        }

        // Ordenar por densidad valor/peso de mayor a menor
        Arrays.sort(items, (a, b) -> Double.compare(b.ratio, a.ratio));

        seleccionActual = new boolean[n + 1];
        mejorSeleccion = new boolean[n + 1];

        dfs(0, 0, 0);

        List<Integer> seleccion = new ArrayList<>();
        int pesoTotal = 0;
        int valorTotal = 0;

        for (int i = 1; i <= n; i++) {
            if (mejorSeleccion[i]) {
                seleccion.add(i);
                pesoTotal += pesos[i - 1];
                valorTotal += valores[i - 1];
            }
        }

        System.out.println("Objetos seleccionados: " + seleccion);
        System.out.println("Peso total: " + pesoTotal);
        System.out.println("Valor total: " + valorTotal);
        System.out.println("Mejor valor: " + mejorValor);
    }

    // Cota superior usando fracciones de objetos
    static double cotaSuperior(int i, int pesoActual, int valorActual) {
        if (pesoActual >= capacidad) {
            return valorActual;
        }

        double cota = valorActual;
        int peso = pesoActual;
        int j = i;

        while (j < n && peso + items[j].peso <= capacidad) {
            peso += items[j].peso;
            cota += items[j].valor;
            j++;
        }

        if (j < n) {
            int espacio = capacidad - peso;
            cota += espacio * items[j].ratio;
        }

        return cota;
    }

    // Búsqueda con Ramificación y Poda
    static void dfs(int i, int pesoActual, int valorActual) {
        if (pesoActual > capacidad) {
            return;
        }

        if (i == n) {
            if (valorActual > mejorValor) {
                mejorValor = valorActual;
                mejorSeleccion = seleccionActual.clone();
            }
            return;
        }

        // Si la cota no supera el mejor valor actual, se descarta la rama (PODA)
        if (cotaSuperior(i, pesoActual, valorActual) <= mejorValor + 1e-9) {
            return;
        }

        Item item = items[i];

        // Incluir el objeto (RAMA1)
        if (pesoActual + item.peso <= capacidad) {
            seleccionActual[item.indice] = true;
            dfs(i + 1, pesoActual + item.peso, valorActual + item.valor);
            seleccionActual[item.indice] = false;
        }

        // Excluir el objeto (RAMA2)
        dfs(i + 1, pesoActual, valorActual);
    }
}