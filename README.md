# Turismo Rural — Sistema de Reservas de Experiencias Rurales
 
**Proyecto Final · Ingeniería de Software**
**Autores:** Catalina Clavijo · Adrián David Navarro
 
---
 
## Descripción del proyecto
 
Es un Sistema de escritorio desarrollado en Java 17 que permite gestionar turistas y realizar reservas de experiencias de turismo rural. Los datos se mantienen en memoria (sin base de datos externa); al cerrar la aplicación los cambios no persisten.
 
Funcionalidades principales:
 
- CRUD completo de turistas (Crear, Leer, Actualizar, Borrar)
- Módulo de reserva de experiencias (turista, experiencia, personas, pago, contacto de emergencia)
- Generación de voucher de confirmación
- Envío de correo de confirmación por Gmail SMTP
---
 
## Requisitos del sistema
 
| Componente | Versión mínima |
|---|---|
| Java JDK | 17 (probado también con 21) |
| Maven | 3.8+ |
| Sistema operativo | Windows 10/11, macOS 12+, Ubuntu 22.04+ |
| Conexión a internet | Solo para el envío de correo |
 
JavaFX no necesita instalarse por separado, Maven lo descarga automáticamente como dependencia.
 
---
 
## Instrucciones para ejecutar el proyecto
 
### Opción A — Con Maven
 
```bash
# 1. Clonar el repositorio
git clone https://github.com/Catacc0421/Turismo_Rural1.git
cd Turismo_Rural1
 
# 2. Compilar
mvn clean compile
 
# 3. Ejecutar
mvn javafx:run
```
### Opción B — Desde IntelliJ IDEA
 
1. File → Open → seleccionar la carpeta del proyecto
2. IntelliJ importa Maven automáticamente
3. Abrir src/main/java/co/turismo/Main.java
4. Clic derecho → Run 'Main'
---
 
## Configuración del correo
 
Las credenciales no están en el código fuente por seguridad. Para que el envío de correo funcione hay que crear el archivo `src/main/resources/config.properties` con este contenido:
 
```
gmail.usuario=tu_correo@gmail.com
gmail.password=xxxx xxxx xxxx xxxx
```
 
Para generar un App Password en Google: Cuenta de Google → Seguridad → Verificación en dos pasos → Contraseñas de aplicacion.
 
Este archivo está en el `.gitignore` y nunca se sube al repositorio.
 
---
 
## Usuarios de prueba
 
El sistema carga automáticamente estos turistas al iniciar, sin necesidad de registrar nada:
 
| Tipo Doc | Número | Nombre | Correo | Fecha Nac. |
|---|---|---|---|---|
| CC | 1098765432 | Catalina Clavijo | catacc3844@gmail.com | 1990-05-15 |
| CC | 1067890123 | Ana Maria Rodriguez Lopez | ana.rodriguez@email.com | 1985-11-22 |
| CE | E-456789 | Carlos Andres Martinez Ruiz | carlos.martinez@mail.com | 1995-03-08 |
| Pasaporte | PA1234567 | Laura Sofia Gonzalez Torres | laura.gonzalez@outlook.com | 2000-07-30 |

---
 
## Experiencias disponibles de prueba
 
| Experiencia | Precio | Cupos | Fecha |
|---|---|---|---|
| Parapente en Chicamocha | $180.000 | 8 | 2026-06-15 |
| Recorrido Parque del Café | $95.000 | 12 | 2026-06-20 |
| Cosecha de Café en Finca | $120.000 | 10 | 2026-06-25 |
| Cabalgata por Salento | $150.000 | 6 | 2026-07-01 |
| Rafting Río La Vieja | $160.000 | 4 | 2026-07-05 |
| Senderismo Cocora | $80.000 | 15 | 2026-07-10 |
| Ordeño y Queso Artesanal | $70.000 | 8 | 2026-07-12 |
| Pesca Deportiva en Laguna | $110.000 | 10 | 2026-07-15 |
| Torrentismo Cañón del Güavio | $200.000 | 6 | 2026-07-18 |
| Avistamiento de Aves Andinas | $90.000 | 8 | 2026-07-20 |
| Taller de Cerámica Indígena | $85.000 | 12 | 2026-07-22 |
| Noche de Fogón en Finca | $130.000 | 20 | 2026-07-25 |
 
---
 
## Módulos implementados
 
### Módulo 1 — CRUD de Turistas
 
| Operación |
|---|
| Crear turista | 
| Listar turistas en tabla | 
| Editar turista | 
| Borrar turista  | 
 
El repositorio de turistas usa patrón Singleton para que los turistas creados en el módulo CRUD estén disponibles inmediatamente en el módulo de Reservas sin reiniciar la aplicación.
 
### Módulo 2 — Reserva de Experiencias
 
| Funcionalidad |
|---|
| Selección de turista y experiencia | 
| Validación de cupos disponibles (RN-001) |
| Validación de mayoría de edad (RN-005) | 
| Datos de emergencia obligatorios (RN-004) | 
| Aceptación de términos y póliza | 
| Cálculo automático del valor total | 
| Generación de voucher de confirmación | 
| Descuento automático de cupos | 
| Envío de correo HTML por Gmail SMTP |
 
---
 
## Requisitos no funcionales
 
### RNF-01 · Rendimiento
 
La interfaz debe responder a cualquier acción del usuario en menos de 500 ms.
 
**Estado:** La tabla usa ObservableList de JavaFX que se actualiza de forma inmediata. El envío de correo se ejecuta en un hilo separado para no bloquear la interfaz mientras se procesa.
 
### RNF-02 · Validaciones en cliente
 
El sistema valida los datos antes de procesarlos y muestra mensajes de error claros y específicos.
 
**Estado:** Se validan campos vacíos, formato de correo, fecha de nacimiento, documentos duplicados, correos duplicados, cupos disponibles, mayoría de edad, contacto de emergencia y aceptación de términos.
 
### RNF-03 · Validaciones en capa de repositorio
 
La lógica de integridad debe estar en la capa de repositorio, no solo en la vista.
 
**Estado:** TuristaRepo valida duplicados de documento y correo antes de insertar, y realiza borrado lógico (estadoActivo = false) en lugar de eliminar el registro físicamente.
 
### RNF-04 · Accesibilidad básica
 
La interfaz debe ser usable con etiquetas descriptivas, mensajes de error comprensibles y contraste visual adecuado.
 
**Estado:** Todos los campos tienen etiqueta y texto de ayuda con ejemplo. Los botones se habilitan y deshabilitan según el contexto. Se muestra confirmación antes de borrar o editar.
 
---
 
## Estructura del proyecto
 
```
Turismo_Rural1/
├── pom.xml
├── src/
│   └── main/
│       ├── java/co/turismo/
│       │   ├── Main.java
│       │   ├── modelo/
│       │   │   ├── Turista.java
│       │   │   ├── Experiencia.java
│       │   │   └── Reserva.java
│       │   ├── repositorio/
│       │   │   ├── TuristaRepo.java       # Singleton — CRUD en memoria
│       │   │   ├── ExperienciaRepo.java   # Singleton — 12 experiencias demo
│       │   │   └── ReservaRepo.java       # Singleton — almacén de reservas
│       │   ├── servicio/
│       │   │   └── EmailService.java      # Correo HTML por Gmail SMTP
│       │   └── vista/
│       │       ├── MainApp.java           # Menú principal
│       │       ├── TuristaVista.java      # Módulo CRUD Turistas
│       │       └── ReservaVista.java      # Módulo Reserva de Experiencias
│       └── resources/
│           └── config.properties          # No incluido en el repositorio (.gitignore)
└── .gitignore
```
 
---
 
## Limitaciones conocidas
 
- **Persistencia:** Los datos solo existen en memoria. Al cerrar la aplicación se pierden. Una mejora futura sería integrar SQLite o un archivo JSON.
- **RN-003 (tiempo límite de reserva):** No implementado. Requeriría un temporizador de 15 minutos con liberación automática de cupos.
- **RN-002 (pago real):** No se integró pasarela de pagos real. El sistema simula el pago como siempre aprobado.
