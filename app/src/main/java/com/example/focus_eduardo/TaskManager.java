package com.example.focus_eduardo;

import java.util.ArrayList;

public class TaskManager {

    // Se guardan las tareas
    ArrayList<String> tareas = new ArrayList<String>();

    //CRUD para la lista de tareas
    // agregar una tarea
    public void agregarTarea(String tarea) {
        tareas.add(tarea);
    }

    // imprimir todas las tareas
    public void listarTareas() {
        for (int i = 0; i < tareas.size(); i++) {
            System.out.println((i + 1) + ". " + tareas.get(i));
        }
    }

    // actualizar una tarea por su posición
    public void actualizarTarea(int indice, String nuevaTarea) {
        tareas.set(indice, nuevaTarea);
    }

    // eliminar una tarea por su posición
    public void eliminarTarea(int indice) {
        tareas.remove(indice);
    }
}