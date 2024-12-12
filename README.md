# Práctica 2
## Algorítmica y Modelos de Computación

### Diciembre de 2024  

**Autor:**  
Jose Manuel Domínguez Segura  

---

## Objetivo de la práctica

La práctica consiste en realizar un programa en lenguaje Java con el que podamos resolver el famoso problema del viajante. Para ello, empleamos distintas estrategias algorítmicas y realizamos un estudio de cada uno de los algoritmos usados, verificando cuál resulta más eficiente y gasta menos tiempo computacional.

---

## Estrategias

### 1. Algoritmo Voraz Exhaustivo Unidireccional

Este método evalúa una ciudad en relación con el resto de las ciudades presentes en el conjunto de datos. Aunque es efectivo en ciertos casos, esta estrategia se vuelve ineficiente cuando se trabaja con un gran volumen de datos, ya que implica calcular las distancias entre todas las parejas de puntos posibles.

Este algoritmo nos provee de una solución válida y, en ocasiones, óptima. Sin embargo, no garantiza siempre el mejor resultado, ya que suele devolver rutas que son un 25% más largas que la solución óptima.

### 2. Algoritmo Voraz con Poda Unidireccional

A rasgos generales, este algoritmo opera como el anteriormente mencionado, con la única salvedad de que incorpora una técnica de poda. El objetivo de esta optimización es reducir el gasto computacional y disminuir el tiempo de ejecución.

En este caso, la condición para que un nodo sea podado es que la distancia entre las coordenadas X de los puntos a evaluar sea mayor que la distancia mínima ya encontrada.

### 3. Algoritmo Voraz Exhaustivo Bidireccional

Este algoritmo extiende el enfoque del voraz unidireccional al calcular rutas desde dos extremos simultáneamente, reduciendo el espacio de búsqueda. Comienza operando de manera unidireccional, ya que, al principio tan solo contamos con un punto de partida. Tras la primera iteración sabremos cuál es el punto más cercano desde el punto de partida y comenzaremos la búsqueda bidireccional.

Al operar en ambas direcciones, el algoritmo puede detectar soluciones óptimas más rápido, aunque aún realiza múltiples comparaciones. Su objetivo principal es mejorar la eficiencia sin comprometer la validez de los resultados. Sin embargo, sigue siendo costoso para grandes conjuntos de datos, ya que requiere cálculos exhaustivos desde dos frentes.

### 4. Algoritmo Voraz con Poda Bidireccional

A diferencia del algoritmo exhaustivo bidireccional, que evalúa todas las combinaciones posibles desde dos extremos, esta estrategia incorpora una técnica de poda que descarta rutas innecesarias. Esto permite ahorrar cálculos al enfocarse únicamente en las opciones más prometedoras, reduciendo significativamente el gasto computacional y el tiempo de ejecución.


