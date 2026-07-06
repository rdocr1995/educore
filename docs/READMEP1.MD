# Edu Core - Sistema de Gestión Educativa

##EQUISITOS

Antes de ejecutar el proyecto necesitas:

- Java 21 instalado
- Apache NetBeans instalado
- Git (opcional si usas repositorio)

---

PASO 1 — Abrir NetBeans

1. Busca en tu computadora:
   
   Apache NetBeans

2. Ábrelo

3. Espera a que cargue completamente


PASO 2 — Abrir el proyecto

1. En NetBeans, ve a:

   File → Open Project

2. Busca la carpeta donde tienes el proyecto (EduCore)

3. Selecciona la carpeta

4. Presiona:

   Open Project

5. Verás el proyecto en el panel izquierdo (Projects)

 PASO 3 — Verificar estructura del proyecto

En el panel izquierdo:

Source Packages

Ahí deben estar paquetes como:

- model
- dao
- controller
- view

---

PASO 4 — Ejecutar el programa

1. Click derecho sobre el nombre del proyecto

2. Selecciona:

   Run Project

   o presiona:

   Shift + F6

3. Se abrirá la consola en la parte inferior

---

PASO 5 — Ver el menú principal

Al ejecutar verás:

1. Gestión de Estudiantes  
2. Gestión de Empleados  
3. Gestión Académica  
0. Salir  

---

PASO 6 — Uso del sistema

 Ejemplo: Módulo de Empleados

1. Escribe:

   2

2. Presiona Enter

3. Verás el menú de empleados:

- Registrar
- Listar
- Buscar
- Actualizar
- Eliminar

4.En esta seccion puedes crear, listar, buscar, actualizar y eliminar registros empleados
---



Ejemplo: Módulo de Edificios

1. Ir a:

   3 → Gestión Académica

2. Luego:

   Edificios

3. Verás opciones:

- Registrar edificio
- Listar edificios
- Buscar edificio
- Eliminar edificio

4.En esta seccion puedes crear, listar, buscar y eliminar edificios

---

# VALIDACIONES DEL SISTEMA

El sistema valida:

- Campos vacíos
- Email válido (empleados)
- Salario no negativo
- No eliminar edificios con aulas asociadas

---

 ARQUITECTURA

El sistema sigue el patrón MVC:

- Model → datos
- View → interacción con usuario
- Controller → lógica del sistema
- DAO → almacenamiento en memoria

---

NOTAS IMPORTANTES

- Los datos se guardan en memoria
- Si cierras el programa, se pierden
- Estamos trabajando fuertemente para tener el prograa totalmente funcional

---
ESTADO DEL SISTEMA

- Módulo Empleados → completo
- Módulo Edificios → completo
- Gestión académica → en progreso
Secciones estará pronto disponible

---

 AUTORES

Proyecto académico  
Curso: Programación III  
Integrantes 
Ricardo Alvarez 
Josue Jimenez