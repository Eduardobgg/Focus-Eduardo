package com.example.focus_eduardo;
/**
 * @author Eduardo Biali García Gómez
 */

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

public class MainActivity extends AppCompatActivity {

    // Estados: 0 = Enfoque, 1 = Descanso corto, 2 = Descanso largo
    private static final int STATE_FOCUS = 0;
    private static final int STATE_BREAK = 1;
    private static final int STATE_REST  = 2;

    // Tiempos en milisegundos
    // 1 minuto = 60,000 ms -> 25 minutos = 25 * 60 * 1000
    private static final long FOCUS_TIME = 25 * 60 * 1000L;
    private static final long BREAK_TIME =  5 * 60 * 1000L;
    private static final long REST_TIME  = 15 * 60 * 1000L;

    // VISTAS
    private TextView tvTimerDisplay;
    private TextView tvSessionStatus;
    private TextView tvSessionsCompleted;
    private TextView tvMotivationalQuote;
    private MaterialButton btnStartStop;
    private MaterialButton btnReset;
    private MaterialButton btnSkip;
    private Chip chipFocus, chipBreak, chipRest;
    private LinearLayout sessionDotsContainer;

    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private int currentState = STATE_FOCUS;
    private long timeLeftInMillis;
    private int focusSessionsCompleted = 0; // contador de sesiones

    // frases que se muestran al iniciar un descanso, algunas las saque de un gran personaje de Overwatch
    private final String[] motivationalQuotes = {"¡Buen trabajo! Descansa un momento.", "Cada descanso te hace más productivo.",
            "El dolor es un excelente maestro", "Respira, recarga y vuelve con más fuerza.",
            "Repara tus heridas", "La constancia supera al talento.", "Caerse es una oportunidad para levantarse de nuevo",
            "Que la tranquilidad te envuelva", "Tómate un momento para meditar", "Encuentra la paz dentro de ti", "Experimenta la tranquilidad",
            "La tentación de rendirse es mayor justo antes de la victoria"};

    // onCreate se ejecuta al abrir la app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Conectar variables con sus elementos del XML por ID
        tvTimerDisplay = findViewById(R.id.tvTimerDisplay);
        tvSessionStatus = findViewById(R.id.tvSessionStatus);
        tvSessionsCompleted = findViewById(R.id.tvSessionsCompleted);
        tvMotivationalQuote = findViewById(R.id.tvMotivationalQuote);
        btnStartStop = findViewById(R.id.btnStartStop);
        btnReset = findViewById(R.id.btnReset);
        btnSkip = findViewById(R.id.btnSkip);
        chipFocus = findViewById(R.id.chipFocus);
        chipBreak = findViewById(R.id.chipBreak);
        chipRest = findViewById(R.id.chipRest);
        sessionDotsContainer = findViewById(R.id.sessionDotsContainer);

        // Estado inicial al abrir la app
        timeLeftInMillis = FOCUS_TIME;
        updateTimerDisplay(timeLeftInMillis); // Muestra "25:00"
        updateChipSelection(); // Resalta chip Enfoque
        updateDots(); // dibuja los 4 puntos vacíos

        // Acción del botón Iniciar/Pausar
        btnStartStop.setOnClickListener(v -> {
            if (isRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        // Reset: detiene el timer y regresa al tiempo inicial del modo actual
        btnReset.setOnClickListener(v -> resetTimer());

        // Skip: salta al siguiente estado sin esperar que termine
        btnSkip.setOnClickListener(v -> skipToNextState());
    }

    // Timer
    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                isRunning = false;
                onSessionFinished();
            }

        }.start();

        isRunning = true;
        btnStartStop.setText("Pausar");
    }

    /**
     * Pausa el timer guardando el tiempo que quedaba
     * Así al cuando reanude continúa desde donde se quedo
     */
    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRunning = false;
        btnStartStop.setText("Reanudar");
    }

    // detiene el timer y regresa al tiempo inicial del modo actual
    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRunning = false;
        timeLeftInMillis = getTimeForState(currentState); // tiempo inicial del modo actual
        updateTimerDisplay(timeLeftInMillis);
        btnStartStop.setText("Iniciar");
    }

    // cancela el timer actual y avanza al siguiente estado
    private void skipToNextState() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRunning = false;
        goToNextState();
    }

    // se ejecuta cuando el timer llega a cero
    // Si era enfoque, sube el contador y actualiza los puntos
    private void onSessionFinished() {
        vibrate();

        if (currentState == STATE_FOCUS) {
            focusSessionsCompleted++;
            tvSessionsCompleted.setText("Sesiones completadas: " + focusSessionsCompleted);
            updateDots();

            int indiceAleatorio = (int)(Math.random() * motivationalQuotes.length);
            tvMotivationalQuote.setText("\"" + motivationalQuotes[indiceAleatorio] + "\"");

            Toast.makeText(this, "¡Sesión completada! Tómate un descanso 🎉",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "¡Descanso terminado! Bien hecho 💪",
                    Toast.LENGTH_SHORT).show();
            tvMotivationalQuote.setText("");
        }

        goToNextState();
    }

    /**
     * Determina el estado y lo activa automáticamente
     * Regla Pomodoro:
     * Después de ENFOQUE:
     *     Si completamos 4 sesiones, tendremos un DESCANSO LARGO (15 min)
     *     Si no, tendremos un DESCANSO CORTO (5 min)
     */
    private void goToNextState() {
        if (currentState == STATE_FOCUS) {
            if (focusSessionsCompleted % 4 == 0 && focusSessionsCompleted > 0) {
                currentState = STATE_REST;
            } else {
                currentState = STATE_BREAK;
            }
        } else {
            currentState = STATE_FOCUS;
        }

        timeLeftInMillis = getTimeForState(currentState);
        updateTimerDisplay(timeLeftInMillis);
        updateChipSelection();
        updateStatusText();
        btnStartStop.setText("Iniciar");
    }

    /**
     * Devuelve el tiempo correcto segun el estado
     * use un switch para los diferentes casos segun el estado
     */
    private long getTimeForState(int state) {
        switch (state) {
            case STATE_FOCUS: return FOCUS_TIME;
            case STATE_BREAK: return BREAK_TIME;
            case STATE_REST:  return REST_TIME;
            default:          return FOCUS_TIME;
        }
    }

    /**
     * Convierte milisegundos a MM:SS y lo muestra en pantalla
     * Ejemplo:
     * millis = 1,500,000
     * segundosTotales = 1500
     * minutos = 1500 / 60 = 25
     * segundos = 1500 % 60 = 0
     * resultado = 25:00
     */
    private void updateTimerDisplay(long millis) {
        int minutes = (int)(millis / 1000) / 60;
        int seconds = (int)(millis / 1000) % 60;
        tvTimerDisplay.setText(String.format("%02d:%02d", minutes, seconds));
    }

    // Los demás chips quedan con borde gris normal
    private void updateChipSelection() {
        ColorStateList borderNormal = ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.color_border));
        ColorStateList borderAccent = ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.color_border_accent));

        chipFocus.setChipStrokeColor(borderNormal);
        chipBreak.setChipStrokeColor(borderNormal);
        chipRest.setChipStrokeColor(borderNormal);

        switch (currentState) {
            case STATE_FOCUS:
                chipFocus.setChipStrokeColor(borderAccent);
                chipFocus.setChecked(true);
                break;
            case STATE_BREAK:
                chipBreak.setChipStrokeColor(borderAccent);
                chipBreak.setChecked(true);
                break;
            case STATE_REST:
                chipRest.setChipStrokeColor(borderAccent);
                chipRest.setChecked(true);
                break;
        }
    }

    // Actualiza el texto de estado
    private void updateStatusText() {
        switch (currentState) {
            case STATE_FOCUS: tvSessionStatus.setText("Sesión de Enfoque"); break;
            case STATE_BREAK: tvSessionStatus.setText("Descanso Corto");    break;
            case STATE_REST:  tvSessionStatus.setText("Descanso Largo");    break;
        }
    }

    // dibuja los 4 puntos de sesión dinámicamente
    // Los puntos rellenos = sesiones completadas en el ciclo actual
    private void updateDots() {
        sessionDotsContainer.removeAllViews();

        int totalDots  = 4;
        int filledDots = focusSessionsCompleted % 4;

        for (int i = 0; i < totalDots; i++) {
            ImageView dot = new ImageView(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
            params.setMargins(10, 0, 10, 0);
            dot.setLayoutParams(params);

            if (i < filledDots) {
                dot.setImageResource(R.drawable.dot_session_completed);
            } else {
                dot.setImageResource(R.drawable.dot_session_empty);
            }

            sessionDotsContainer.addView(dot);
        }
    }

    // hace vibrar el teléfono 500ms al terminar una sesión
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(android.os.VibrationEffect.createOneShot(
                        500, android.os.VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(500);
            }
        }
    }
}