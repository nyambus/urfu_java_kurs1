#include <iostream>
#include <vector>
#include <chrono>
#include <algorithm>
#include <random>
#include <iomanip>
#include <fstream>
#include <functional>

using namespace std;
using namespace std::chrono;

// --- Алгоритмы сортировки ---

void bubbleSort(vector<int>& arr, size_t& comparisons, size_t& swaps) {
    int n = arr.size();
    comparisons = 0; swaps = 0;
    for (int i = 0; i < n - 1; i++) {
        bool swapped = false;
        for (int j = 0; j < n - i - 1; j++) {
            comparisons++;
            if (arr[j] > arr[j + 1]) {
                swap(arr[j], arr[j + 1]);
                swaps++;
                swapped = true;
            }
        }
        if (!swapped) break;
    }
}

void selectionSort(vector<int>& arr, size_t& comparisons, size_t& swaps) {
    int n = arr.size();
    comparisons = 0; swaps = 0;
    for (int i = 0; i < n - 1; i++) {
        int min_idx = i;
        for (int j = i + 1; j < n; j++) {
            comparisons++;
            if (arr[j] < arr[min_idx]) {
                min_idx = j;
            }
        }
        if (min_idx != i) {
            swap(arr[i], arr[min_idx]);
            swaps++;
        }
    }
}

void insertionSort(vector<int>& arr, size_t& comparisons, size_t& swaps) {
    int n = arr.size();
    comparisons = 0; swaps = 0;
    for (int i = 1; i < n; i++) {
        int key = arr[i];
        int j = i - 1;
        while (j >= 0) {
            comparisons++;
            if (arr[j] > key) {
                arr[j + 1] = arr[j];
                swaps++;
                j--;
            } else break;
        }
        arr[j + 1] = key;
    }
}

// --- Генерация тестовых данных ---

vector<int> randomArray(int n) {
    vector<int> arr(n);
    mt19937 rng(42);
    uniform_int_distribution<int> dist(1, 100000);
    for (int i = 0; i < n; i++) arr[i] = dist(rng);
    return arr;
}

vector<int> sortedArray(int n) {
    vector<int> arr(n);
    for (int i = 0; i < n; i++) arr[i] = i + 1;
    return arr;
}

vector<int> reversedArray(int n) {
    vector<int> arr(n);
    for (int i = 0; i < n; i++) arr[i] = n - i;
    return arr;
}

// --- Запуск замера ---

struct Result {
    double time_us;
    size_t comparisons;
    size_t swaps;
};

typedef void (*SortFn)(vector<int>&, size_t&, size_t&);

Result measure(SortFn sortFn, vector<int> data) {
    size_t comparisons, swaps;
    auto start = high_resolution_clock::now();
    sortFn(data, comparisons, swaps);
    auto end = high_resolution_clock::now();
    double time_us = duration_cast<microseconds>(end - start).count();
    return {time_us, comparisons, swaps};
}

// --- main ---

int main() {
    vector<int> sizes = {100, 500, 1000, 2000, 5000, 10000};
    vector<pair<string, SortFn>> sorts = {
        {"Bubble", bubbleSort},
        {"Selection", selectionSort},
        {"Insertion", insertionSort}
    };
    vector<pair<string, function<vector<int>(int)>>> generators = {
        {"random", randomArray},
        {"sorted", sortedArray},
        {"reversed", reversedArray}
    };

    ofstream fout("cpp_results.txt");
    fout << "C++ Benchmark Results\n";
    fout << "=====================\n\n";
    fout << "Size\tType\tAlgorithm\tTime(us)\tComparisons\tSwaps\n";

    for (int n : sizes) {
        for (auto& [genName, genFn] : generators) {
            vector<int> base = genFn(n);
            for (auto& [sortName, sortFn] : sorts) {
                vector<int> copy = base;
                Result r = measure(sortFn, copy);
                fout << n << "\t" << genName << "\t" << sortName << "\t"
                     << fixed << setprecision(1) << r.time_us << "\t"
                     << r.comparisons << "\t" << r.swaps << endl;
                cout << n << " " << genName << " " << sortName
                     << " -> " << r.time_us << " us" << endl;
            }
        }
    }
    fout.close();
    cout << "\nDone. Results in cpp_results.txt" << endl;
    return 0;
}
