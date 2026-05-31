import time
import random
import sys

# --- Алгоритмы ---

def bubble_sort(arr):
    n = len(arr)
    comparisons = swaps = 0
    for i in range(n - 1):
        swapped = False
        for j in range(n - i - 1):
            comparisons += 1
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
                swaps += 1
                swapped = True
        if not swapped:
            break
    return comparisons, swaps

def selection_sort(arr):
    n = len(arr)
    comparisons = swaps = 0
    for i in range(n - 1):
        min_idx = i
        for j in range(i + 1, n):
            comparisons += 1
            if arr[j] < arr[min_idx]:
                min_idx = j
        if min_idx != i:
            arr[i], arr[min_idx] = arr[min_idx], arr[i]
            swaps += 1
    return comparisons, swaps

def insertion_sort(arr):
    n = len(arr)
    comparisons = swaps = 0
    for i in range(1, n):
        key = arr[i]
        j = i - 1
        while j >= 0:
            comparisons += 1
            if arr[j] > key:
                arr[j + 1] = arr[j]
                swaps += 1
                j -= 1
            else:
                break
        arr[j + 1] = key
    return comparisons, swaps

# --- Генерация ---

def random_array(n):
    rng = random.Random(42)
    return [rng.randint(1, 100000) for _ in range(n)]

def sorted_array(n):
    return list(range(1, n + 1))

def reversed_array(n):
    return list(range(n, 0, -1))

# --- Замер ---

def measure(sort_fn, data):
    t0 = time.perf_counter()
    comparisons, swaps = sort_fn(data)
    t1 = time.perf_counter()
    return (t1 - t0) * 1_000_000, comparisons, swaps

# --- main ---

sizes = [100, 500, 1000, 2000, 5000, 10000]
sorts = [("Bubble", bubble_sort), ("Selection", selection_sort), ("Insertion", insertion_sort)]
generators = [("random", random_array), ("sorted", sorted_array), ("reversed", reversed_array)]

with open("python_results.txt", "w") as f:
    f.write("Python Benchmark Results\n")
    f.write("========================\n\n")
    f.write("Size\tType\tAlgorithm\tTime(us)\tComparisons\tSwaps\n")
    for n in sizes:
        for gen_name, gen_fn in generators:
            base = gen_fn(n)
            for sort_name, sort_fn in sorts:
                copy = base[:]
                time_us, comps, sw = measure(sort_fn, copy)
                f.write(f"{n}\t{gen_name}\t{sort_name}\t{time_us:.1f}\t{comps}\t{sw}\n")
                print(f"{n} {gen_name} {sort_name} -> {time_us:.1f} us")

print("\nDone. Results in python_results.txt")
