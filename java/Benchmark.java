import java.io.*;
import java.util.*;

public class Benchmark {
    // --- Алгоритмы ---

    static class Counts {
        long comparisons, swaps;
    }

    static void bubbleSort(int[] arr, Counts c) {
        int n = arr.length;
        c.comparisons = 0; c.swaps = 0;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                c.comparisons++;
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    c.swaps++;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    static void selectionSort(int[] arr, Counts c) {
        int n = arr.length;
        c.comparisons = 0; c.swaps = 0;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                c.comparisons++;
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                int temp = arr[minIdx];
                arr[minIdx] = arr[i];
                arr[i] = temp;
                c.swaps++;
            }
        }
    }

    static void insertionSort(int[] arr, Counts c) {
        int n = arr.length;
        c.comparisons = 0; c.swaps = 0;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0) {
                c.comparisons++;
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    c.swaps++;
                    j--;
                } else break;
            }
            arr[j + 1] = key;
        }
    }

    // --- Генерация ---

    static int[] randomArray(int n) {
        Random rng = new Random(42);
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = rng.nextInt(100000) + 1;
        return arr;
    }

    static int[] sortedArray(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = i + 1;
        return arr;
    }

    static int[] reversedArray(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = n - i;
        return arr;
    }

    // --- Замер ---

    interface SortFn {
        void sort(int[] arr, Counts c);
    }

    static class Result {
        double timeUs;
        long comparisons, swaps;
    }

    static Result measure(SortFn sortFn, int[] data) {
        Counts c = new Counts();
        long start = System.nanoTime();
        sortFn.sort(data, c);
        long end = System.nanoTime();
        Result r = new Result();
        r.timeUs = (end - start) / 1000.0;
        r.comparisons = c.comparisons;
        r.swaps = c.swaps;
        return r;
    }

    public static void main(String[] args) throws Exception {
        int[] sizes = {100, 500, 1000, 2000, 5000, 10000};
        String[] sortNames = {"Bubble", "Selection", "Insertion"};
        SortFn[] sorts = {Benchmark::bubbleSort, Benchmark::selectionSort, Benchmark::insertionSort};
        String[] genNames = {"random", "sorted", "reversed"};

        PrintWriter out = new PrintWriter("java_results.txt");
        out.println("Java Benchmark Results");
        out.println("======================");
        out.println("Size\tType\tAlgorithm\tTime(us)\tComparisons\tSwaps");

        // Warmup JIT
        for (int i = 0; i < 5; i++) {
            int[] w = randomArray(5000);
            Counts dum = new Counts();
            bubbleSort(w.clone(), dum);
            selectionSort(w.clone(), dum);
            insertionSort(w.clone(), dum);
        }

        for (int n : sizes) {
            for (int g = 0; g < genNames.length; g++) {
                int[] base;
                switch (g) {
                    case 0: base = randomArray(n); break;
                    case 1: base = sortedArray(n); break;
                    default: base = reversedArray(n); break;
                }
                for (int s = 0; s < sorts.length; s++) {
                    int[] copy = base.clone();
                    Result r = measure(sorts[s], copy);
                    out.printf("%d\t%s\t%s\t%.1f\t%d\t%d%n",
                        n, genNames[g], sortNames[s],
                        r.timeUs, r.comparisons, r.swaps);
                    System.out.printf("%d %s %s -> %.1f us%n",
                        n, genNames[g], sortNames[s], r.timeUs);
                }
            }
        }
        out.close();
        System.out.println("\nDone. Results in java_results.txt");
    }
}
