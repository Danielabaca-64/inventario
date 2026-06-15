# InventarioTech

Aplicación de escritorio (Java Swing) para la gestión de inventario.

Contenido del proyecto:

- Código fuente Java en paquetes: `dao`, `modelo`, `util`, `vista`.
- Archivo SQL para crear la base de datos: `BD_InventarioTech.sql`.

Requisitos
- Java 11 o superior
- Maven (opcional, hay scripts sencillos incluidos)
- MySQL (base de datos)

Configuración de la base de datos
1. Crear la base de datos y tablas usando `BD_InventarioTech.sql`.
2. Ajustar credenciales en `util/Conexion.java` (usuario y contraseña).

Compilación y ejecución
Opciones:

- Usando Maven (recomendado):

  Windows:

  ```bat
  build.bat
  run.bat
  ```

  También puedes usar el Maven Wrapper si lo tienes: en Windows ejecuta `mvnw.cmd`, en Unix `./mvnw`.
  Si no existe el wrapper, puedes generarlo (con Maven instalado) desde la raíz del proyecto:

  ```bash
  mvn -N io.takari:maven:wrapper
  ```

  Unix/macOS:

  ```bash
  ./build.sh
  ./run.sh
  ```

  Los scripts invocan `mvn package` y generan un jar ejecutable en `target/inventario-1.0.0.jar`.

- Sin Maven: compilar manualmente con `javac` incluyendo el conector MySQL en el classpath.

Notas
- Actualiza `util/Conexion.java` para apuntar a tu servidor MySQL si no usas `root:root`.
- Si necesitas que reorganice el proyecto en estructura `src/main/java`, dímelo y lo preparo.

Soporte
Si hay errores de compilación o ejecución, pega el mensaje de error aquí y lo corrijo.
