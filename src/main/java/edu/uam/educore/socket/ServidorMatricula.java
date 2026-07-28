package edu.uam.educore.socket;

import edu.uam.educore.db.Conexion;
import edu.uam.educore.db.ConfiguracionBD;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servidor de Matrícula. Recibe por socket la orden MATRICULAR &lt;archivo&gt;, lee ese CSV del
 * directorio de entrada (una matrícula por renglón: carnet,codigoSeccion) y matricula todo el lote
 * en UNA transacción: si un renglón falla, revierte el lote completo.
 */
public class ServidorMatricula {

  private final ConfiguracionBD config;
  private final Path entradaDir;

  public ServidorMatricula(ConfiguracionBD config, String entradaDir) {
    this.config = config;
    this.entradaDir = Path.of(entradaDir);
  }

  public static void main(String[] args) throws Exception {
    ConfiguracionBD config = ConfiguracionBD.desdeArchivo(".env");
    String entrada = System.getenv("ENTRADA_DIR");
    int puerto = Integer.parseInt(System.getenv("MATRICULA_PORT"));
    new ServidorMatricula(config, entrada).escuchar(puerto);
  }

  public void escuchar(int puerto) throws IOException {
    try (ServerSocket servidor = new ServerSocket(puerto)) {
      System.out.println("Matricula escuchando en " + puerto);
      while (true) {
        try (Socket cliente = servidor.accept();
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(cliente.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out =
                new PrintWriter(cliente.getOutputStream(), true, StandardCharsets.UTF_8)) {
          atender(in, out);
        } catch (IOException e) {
          System.err.println("Error atendiendo cliente: " + e.getMessage());
        }
      }
    }
  }

  private void atender(BufferedReader in, PrintWriter out) throws IOException {
    String linea = in.readLine();
    if (linea == null || !linea.startsWith("MATRICULAR ")) {
      out.println("400 comando invalido");
      return;
    }
    String archivo = linea.substring("MATRICULAR ".length()).trim();
    try {
      int k = procesarLote(archivo);
      out.println("Se procesaron correctamente" + k + " matriculas");
    } catch (Exception e) {
      out.println("400 " + e.getMessage());
    }
  }

  /**
   * TODO(estudiante · T4/T5): matricular el lote en UNA transacción.
   *
   * <p>Pasos:
   *
   * <ol>
   *   <li>Leer el CSV de entrada (entradaDir.resolve(archivo)) con un BufferedReader; cada renglón
   *       es "carnet,codigoSeccion".
   *   <li>Abrir conexión y con.setAutoCommit(false).
   *   <li>Por cada renglón: buscar el estudiante por carnet (si no existe, es un error), buscar la
   *       sección por código y su cupo (aula.capacidad), validar cupo y duplicado, e insertar en
   *       matricula.
   *   <li>Si todo pasa: con.commit() y devolver la cantidad. Si algo falla: con.rollback() y
   *       relanzar.
   * </ol>
   *
   * <p>Cómo distinguir los cuatro casos de error (carnet inexistente, sección inexistente, cupo
   * lleno, matrícula duplicada) queda a su criterio de diseño — no hay una jerarquía de excepciones
   * provista. Ver "Puntos extra" en el enunciado si quieren diseñar la suya.
   *
   * <p>Referencia del patrón JDBC: EstudianteRepoSql.
   */
  private int procesarLote(String archivo) throws Exception {

    // Ubicación completa del archivo CSV dentro de ENTRADA_DIR.
    Path ruta = entradaDir.resolve(archivo);

    // Se utiliza una sola conexión para procesar todo el lote.
    try (Connection con =
        Conexion.getConnection(config.url(), config.usuario(), config.contrasena())) {

      // RF-02.1:
      // Desactivamos el guardado automático.
      // Nada se confirma hasta terminar correctamente todo el archivo.
      con.setAutoCommit(false);

      int matriculadas = 0;

      try (BufferedReader br = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {

        String linea;
        int numeroLinea = 0;

        while ((linea = br.readLine()) != null) {

          numeroLinea++;

          // Ignorar líneas completamente vacías.
          if (linea.isBlank()) {
            continue;
          }

          // Formato esperado:
          // carnet,codigoSeccion
          String[] partes = linea.split(",", -1);

          if (partes.length != 2) {
            throw new IllegalArgumentException(
                "Formato inválido en la línea " + numeroLinea + ": " + linea);
          }

          String carnet = partes[0].trim();
          String codigoSeccion = partes[1].trim();

          if (carnet.isEmpty() || codigoSeccion.isEmpty()) {
            throw new IllegalArgumentException(
                "Carnet o código de sección vacío en la línea " + numeroLinea);
          }

          /*
           * RF-02.2
           * Buscar al estudiante por carnet.
           */
          int estudianteId;

          String sqlEstudiante = "SELECT id FROM estudiante WHERE carnet = ?";

          try (PreparedStatement psEst = con.prepareStatement(sqlEstudiante)) {

            psEst.setString(1, carnet);

            try (ResultSet rsEst = psEst.executeQuery()) {

              if (!rsEst.next()) {
                throw new IllegalArgumentException("Carnet inexistente: " + carnet);
              }

              estudianteId = rsEst.getInt("id");
            }
          }

          /*
           * RF-02.3 y RF-02.4
           * Buscar la sección por código y obtener
           * la capacidad del aula asociada.
           */
          int seccionId;
          int capacidad;

          String sqlSeccion =
              "SELECT s.id, a.capacidad "
                  + "FROM seccion s "
                  + "JOIN aula a ON s.aula_id = a.id "
                  + "WHERE s.codigo = ?";

          try (PreparedStatement psSec = con.prepareStatement(sqlSeccion)) {

            psSec.setString(1, codigoSeccion);

            try (ResultSet rsSec = psSec.executeQuery()) {

              if (!rsSec.next()) {
                throw new IllegalArgumentException("Sección inexistente: " + codigoSeccion);
              }

              seccionId = rsSec.getInt("id");
              capacidad = rsSec.getInt("capacidad");
            }
          }

          /*
           * RF-02.4
           * Contar las matrículas actuales de la sección.
           *
           * Las inserciones anteriores del mismo lote también
           * son visibles porque utilizan la misma conexión.
           */
          int inscritos;

          String sqlConteo =
              "SELECT COUNT(*) AS total " + "FROM matricula " + "WHERE seccion_id = ?";

          try (PreparedStatement psCount = con.prepareStatement(sqlConteo)) {

            psCount.setInt(1, seccionId);

            try (ResultSet rsCount = psCount.executeQuery()) {

              rsCount.next();
              inscritos = rsCount.getInt("total");
            }
          }

          if (inscritos >= capacidad) {
            throw new IllegalArgumentException("Cupo lleno en la sección: " + codigoSeccion);
          }

          /*
           * RF-02.5
           * Verificar que el estudiante no esté matriculado
           * previamente en la misma sección.
           */
          String sqlDuplicado =
              "SELECT 1 " + "FROM matricula " + "WHERE estudiante_id = ? " + "AND seccion_id = ?";

          try (PreparedStatement psDup = con.prepareStatement(sqlDuplicado)) {

            psDup.setInt(1, estudianteId);
            psDup.setInt(2, seccionId);

            try (ResultSet rsDup = psDup.executeQuery()) {

              if (rsDup.next()) {
                throw new IllegalArgumentException(
                    "Matrícula duplicada: " + carnet + " ya está inscrito en " + codigoSeccion);
              }
            }
          }

          /*
           * RF-02.1 y RF-02.6
           * Todas las validaciones de esta línea pasaron.
           * Insertamos la matrícula usando la misma conexión.
           */
          String sqlInsertar =
              "INSERT INTO matricula " + "(estudiante_id, seccion_id) " + "VALUES (?, ?)";

          try (PreparedStatement psInsert = con.prepareStatement(sqlInsertar)) {

            psInsert.setInt(1, estudianteId);
            psInsert.setInt(2, seccionId);
            psInsert.executeUpdate();
          }

          matriculadas++;
        }

        /*
         * RF-02.1 y RF-02.6
         * Solo se confirma si todas las líneas fueron válidas.
         */
        con.commit();

        return matriculadas;

      } catch (Exception e) {

        /*
         * RF-02.2 a RF-02.5
         * Si una sola línea falla, se revierten todas
         * las matrículas insertadas durante este lote.
         */
        con.rollback();
        throw e;

      } finally {

        // Restaurar el modo normal antes de cerrar la conexión.
        try {
          con.setAutoCommit(true);
        } catch (Exception ignored) {
          // La conexión puede encontrarse cerrada después de un error.
        }
      }
    }
  }
}
