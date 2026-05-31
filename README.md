# Сравнение алгоритмов квадратичной сортировки

Курсовой проект, 2 семестр. Дисциплина «Программирование», УрФУ РТФ, группа РИЗ-150110у.

## Структура

```
cpp/        — реализации на C++ (Bubble, Selection, Insertion) + бенчмарк
java/       — реализации на Java (Bubble, Selection, Insertion) + бенчмарк
python/     — реализации на Python (Bubble, Selection, Insertion) + бенчмарк
```

## Алгоритмы

| Алгоритм | Лучший случай | Средний | Худший | Память | Устойчивость |
|----------|:------------:|:-------:|:------:|:------:|:------------:|
| Bubble Sort | O(n) | O(n²) | O(n²) | O(1) | Да |
| Selection Sort | O(n²) | O(n²) | O(n²) | O(1) | Нет |
| Insertion Sort | O(n) | O(n²) | O(n²) | O(1) | Да |

## Запуск бенчмарков

```bash
# C++
cd cpp && g++ -O2 -std=c++17 -o benchmark benchmark.cpp && ./benchmark

# Java (требуется JDK 17+)
cd java && javac Benchmark.java && java Benchmark

# Python
cd python && python3 benchmark.py
```

Результаты сохраняются в `*_results.txt`.

## Результаты

Подробные таблицы с временами выполнения (мкс) для размеров 100–10 000 на случайных, отсортированных и обратных массивах — в файлах `cpp/cpp_results.txt`, `java/java_results.txt`, `python/python_results.txt`.

Краткие выводы:
- **Insertion Sort** — быстрее всех на случайных и почти отсортированных данных
- **Selection Sort** — меньше всего обменов (полезно при дорогой записи)
- **Bubble Sort** — только на отсортированных данных с оптимизацией `swapped`
- C++ быстрее Python в 15–60 раз на квадратичных сортировках
- Java с JIT-компиляцией приближается к C++ и местами обходит
