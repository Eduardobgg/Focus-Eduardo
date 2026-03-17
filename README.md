# Focus Timer - Práctica 2

## Descripción

En esta práctica desarrollé una aplicación de productividad basada en la 
técnica Pomodoro, la idea era que tendriamos 25 minutos para enfocarnos en 
una tarea, luego descansar 5 minutos y después de completar 4 sesiones 
de enfoque te ganas un descanso largo de 15 minutos.
Implementé la interfaz con Chips para seleccionar el modo, un timer grande  en pantalla, puntos de progreso que se van llenando conforme completas  sesiones, botones para iniciar, pausar, reiniciar y saltar, además de vibración y frases motivacionales al terminar cada sesión.

---

## Preguntas

### 1. ¿Cuál fue el mayor reto al gestionar el CountDownTimer?

El mayor reto fue darme cuenta de que si el usuario presionaba 
el botón de Iniciar varias veces seguidas, se creaban varios timers al 
mismo tiempo y el tiempo bajaba mucho más rápido de lo normal, no lo vi al principio.
Lo que aprendí a hacer fue cancelar siempre el timer anterior antes de crear uno nuevo, con `countDownTimer.cancel()`. De esta forma aunque el usuario presione el botón muchas veces, solo existe un timer activo a la vez. 
También guardo el tiempo restante en `timeLeftInMillis` para que al 
pausar y reanudar continúe desde donde se quedó y no desde el principio.

### 2. ¿Por qué usar LinearLayout con addView en lugar de 4 ImageViews estáticos?

Al principio pensé que era más fácil poner 4 ImageViews directo en el XML, 
pero entendí que el problema es que eso es inflexible, ya que si el ciclo del 
Pomodoro cambiara a 6 sesiones tendría que modificar el XML manualmente y 
agregar más elementos, pero con `addView` los puntos se generan desde el código con un ciclo `for`, 
entonces si quisiera cambiar el número de puntos solo cambio una variable 
`totalDots = 6` y listo

### 3. ¿Qué cambiaría para permitir tiempos personalizados?

Actualmente los tiempos están definidos como constantes fijas en el código:
```java
private static final long FOCUS_TIME = 25 * 60 * 1000L;
private static final long BREAK_TIME =  5 * 60 * 1000L;
private static final long REST_TIME  = 15 * 60 * 1000L;
```

Para hacerlos personalizables, creo que lo primero sería quitar el 
`static final` para que puedan cambiar, luego usaría `SharedPreferences` 
para guardar los valores que el usuario elija y los leería desde ahí 
cada vez que inicia la app, el botón de Settings que ya está en la 
interfaz sería la pantalla donde el usuario ingresa sus tiempos, aún no 
sé implementarlo del todo, pero entiendo la idea general de por dónde iría.

### 4. ¿Cómo mantener el tiempo si el usuario minimiza la app?

Esto lo resolví implementando `onSaveInstanceState()`, que lo que hace es 
guardar las variables más importantes, como el tiempo restante, el estado 
actual, si estaba corriendo o no y las sesiones completadas, en un 
Bundle antes de que Android destruya la Activity, asi cuando el usuario regresa a la app, `onRestoreInstanceState()` recupera esos valores y restaura todo como estaba, 
si el timer estaba corriendo cuando se minimizó, se retoma automáticamente 
desde el tiempo que quedaba

---

## Extras implementados

- **Frases motivacionales:** al terminar una sesión de enfoque se muestra 
  una frase aleatoria de un array de Strings, algunas frases las tomé de 
  un personaje de Overwatch que me gusta llamado Zenyatta
- **Navegación por Chips:** al tocar un chip aparece un AlertDialog 
  preguntando si se quiere cambiar de modo. Si acepta, el timer cambia 
  inmediatamente.
- **Persistencia:** lo implemente con `onSaveInstanceState()` y 
  `onRestoreInstanceState()`.
