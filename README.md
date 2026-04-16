# rincon-post1-u6

## Resultados JMH — Knapsack Benchmark

Máquina: JDK 17, JMH 1.37 | Unidad: microsegundos (µs) | Modo: AverageTime (menor = mejor)

| Benchmark               |    W  |    n | Score (µs)  | Error (µs)  |
|-------------------------|-------|------|-------------|-------------|
| bench01                 |  1000 |  100 |      76,640 |  ±   7,145  |
| bench01                 |  1000 |  500 |     371,556 |  ±   2,919  |
| bench01                 |  1000 | 1000 |     759,414 |  ±  18,728  |
| bench01                 |  5000 |  100 |     364,424 |  ±  39,632  |
| bench01                 |  5000 |  500 |   1.876,354 |  ±  45,634  |
| bench01                 |  5000 | 1000 |   3.811,402 |  ±  68,803  |
| bench01                 | 10000 |  100 |     735,999 |  ±  60,480  |
| bench01                 | 10000 |  500 |   3.933,876 |  ± 133,326  |
| bench01                 | 10000 | 1000 |   7.719,726 |  ± 112,576  |
| benchMemOpt             |  1000 |  100 |       5,885 |  ±   0,121  |
| benchMemOpt             |  1000 |  500 |      29,074 |  ±   0,563  |
| benchMemOpt             |  1000 | 1000 |      61,881 |  ±   2,632  |
| benchMemOpt             |  5000 |  100 |      22,070 |  ±   0,354  |
| benchMemOpt             |  5000 |  500 |     106,483 |  ±   0,985  |
| benchMemOpt             |  5000 | 1000 |     216,475 |  ±   6,560  |
| benchMemOpt             | 10000 |  100 |      49,880 |  ±   5,299  |
| benchMemOpt             | 10000 |  500 |     269,981 |  ±  12,455  |
| benchMemOpt             | 10000 | 1000 |     532,741 |  ±   4,349  |

## Análisis

### (a) Diferencia entre solve01 y solveMemOpt

A pesar de que ambas variantes tienen la misma complejidad temporal teórica O(n·W),
los resultados muestran diferencias drásticas en la práctica. Para n=1000 y W=10000,
`bench01` tardó 7.719,726 µs mientras que `benchMemOpt` tardó solo 532,741 µs —
una diferencia de aproximadamente **14,5×**. Esta brecha se explica por el uso de
memoria: `solve01` aloca una matriz (n+1)×(W+1) de enteros, lo que para n=1000 y
W=10000 representa ~40 MB. Acceder a esta estructura genera frecuentes cache misses
en L2/L3. En cambio, `solveMemOpt` trabaja sobre un único arreglo de W+1 enteros
(~40 KB para W=10000), que cabe cómodamente en la caché del procesador, reduciendo
drásticamente la latencia de acceso a memoria.

### (b) Escalado al aumentar n y W

Ambas variantes escalan conforme a O(n·W). Con W=1000 fijo, al pasar de n=100 a
n=1000 (10×), `bench01` escala de 76,640 µs a 759,414 µs (~9,9×) y `benchMemOpt`
de 5,885 µs a 61,881 µs (~10,5×). Con n=100 fijo, al pasar de W=1000 a W=10000
(10×), `bench01` escala de 76,640 µs a 735,999 µs (~9,6×) y `benchMemOpt` de
5,885 µs a 49,880 µs (~8,5×). El escalado bilineal queda confirmado empíricamente en
ambos casos. El factor constante de `benchMemOpt` es significativamente menor debido
a la localidad de referencia.

### (c) Implicaciones del trade-off espacio/tiempo

`solve01` requiere O(n·W) espacio, lo que puede ser prohibitivo para instancias grandes
(n=10000, W=100000 implicaría ~4 GB). `solveMemOpt` reduce esto a O(W), sacrificando
la posibilidad de hacer backtracking directo sobre la tabla (para reconstruir la solución
se necesita la matriz completa, como en `reconstruct`). Por lo tanto, la decisión práctica
es: usar `solveMemOpt` cuando solo se necesita el valor óptimo y la memoria es un
recurso crítico; usar `solve01` cuando se requiere reconstruir los ítems seleccionados.
En entornos embebidos o con restricciones de heap, `solveMemOpt` es la única opción
viable para instancias medianas o grandes.