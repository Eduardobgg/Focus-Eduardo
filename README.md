# Focus-Eduardo

## Descripción de la práctica
En esta práctica configuré Android Studio y el SDK para trabajar en Android, creé dos emuladores un smartphone con API 34 y una tablet con API 26 para poder probar aplicaciones, usé Git con un repositorio remoto en GitHub, hice el primer commit subiendo el proyecto, trabajé con ramas feature/user-name y feature/setup-logic y practiqué la colaboración resolviendo un conflicto en MainActivity.java y haciendo merge a la rama main, al final se etiquetó la versión v1.0.0, por último, implementé una clase en Java llamada TaskManager para manejar una lista de tareas sugeridas con CRUD agregar, listar, actualizar y eliminar

## ¿Tuviste problemas con la aceleración de hardware o la creación de los AVD? Describe la solución
Sí, tuve problemas porque al instalar y configurar los dos emuladores la tablet y el celular, mi computadora se lageó y se puso muy lenta, ya que estas descargas y el emulador consumen mucha RAM y CPU, mi solución fue esperar a que terminara la instalación completa del SDK y de las imágenes del sistema, cerrar programas para liberar memoria y cuando se quedaba demasiado lento reiniciar Android Studio y volver a abrir el AVD

## ¿Por qué elegiste ArrayList sobre otras opciones?
Elegí ArrayList porque es fácil de usar, ya que puedo guardar las tareas en orden, agregar nuevas tareas y acceder por índice para actualizar o eliminar

## Si las tareas se guardaran en un servidor remoto, ¿qué cambiaría en el manejo de excepciones de tu función?
Si las tareas se guardaran en un servidor remoto, no solo tendría errores por datos inválidos, también tendría que manejar errores de red, como no tener internet, tiempo de espera, servidor caído, se tendría que validar lo que regresa el servidor y manejar casos como respuestas incompletas o formato incorrecto, en ese caso, el manejo de errores sería más cuidadoso porque dependería de conexión y respuestas externas, no solo del código local
