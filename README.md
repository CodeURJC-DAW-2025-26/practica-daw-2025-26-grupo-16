# PowerGym

## 👥 Miembros del Equipo
| Nombre y Apellidos | Correo URJC | Usuario GitHub |
|:--- |:--- |:--- |
| **Adrián Esteban Martín** | a.estebanm.2021@alumnos.urjc.es | [aadri-2003](https://github.com/aadri-2003) |
| **Laura Pineda Ballesteros**  | l.pineda.2022@alumnos.urjc.es | [lauraxpb](https://github.com/lauraxpb) |

---

## 🎭 **Preparación 1: Definición del Proyecto**

### **Descripción del Tema**
Nuestra aplicación trata sobre un gimnasio, en la que los usuarios tienen acceso a entrenamientos y nutriciones para mejorar su forma física.

### **Entidades**
Las principales entidades de la aplicación son:

1. **Usuario**: Clientes del gimnasio que pueden acceder a entrenamientos y planes de nutrición.
2. **Nutrición**: Planes alimenticios personalizados según los objetivos del usuario.
3. **Entrenamiento**: Rutinas de ejercicios personalizadas para cada usuario.

**Relaciones entre entidades:**
La relación entre las diferentes entidades la representamos con el siguiente diagrama

![image](https://github.com/user-attachments/assets/4bc5973a-0ea9-4801-81b8-f486ba7dab55)

### **Permisos de los Usuarios**

* **Usuario Anónimo**: 
  - Permisos: Puede ver los diferentes entreamientos y rutinas disponibles, no puede acceder a los comentarios personalizados de entrenamiento ni a los comentarios de nutrición, puede registrarse
  - No es dueño de ninguna entidad

* **Usuario Registrado**: 
  - Permisos: Puede ver los diferentes comentarios publicados, puede publicar un comentario,  puede acceder a sus planes de entrenamiento, puede solicitar planes personalizados o automáticos de entrenamiento, puede acceder a sus planes de nutrición, puede solicitar planes personalizados o automáticos de nutrición]
  - Es dueño de: Sus propios Entrenamientos, sus propias Nutriciones, sus Comentarios]

* **Administrador**: 
  - Permisos: Tiene todos los permisos de un usuario registrado y permisos para crear planes de entrenamiento y de nutrición, además, podrá eliminar tanto dietas y rutinas(y sus respectivos comentarios).
  - Es dueño de: Todas las entidades.

### **Imágenes**
Las entidades tienen asignadas las siguientes imágenes:

- **Usuario**: Los usuarios podrán tener foto de perfil
- **Entrenamiento**: Los entrenamientos tendrán fotos asignadas
- **Nutrición**: Las nutriciones tendrán fotos asignadas

### **Gráficos**
La entidad de nutrición tendrá un gráfico circular:

- **Gráfico 1**: el plan de nutrición tendrá un gráfico para mostrar información de la dieta de forma más sencilla y atractiva para el usuario.

### **Tecnología Complementaria**
Añadiremos las siguientes tecnologías complementarias:

- **Entrenamiento**: Generará PDFs con planes de entrenamiento
- **Nutrición**: Generará PDFs con planes de nutrición.

### **Algoritmo o Consulta Avanzada**

- **Algoritmo/Consulta**: Generar plan
- **Descripción**: Implementaremos un algoritmo que genere un plan de entrenamiento y un plan de nutrición en base a los objetivos y a la información del usuario.

---

## 🛠 **Práctica 1: Web con HTML generado en servidor y AJAX**

### **Vídeo de Demostración**
📹 **[Enlace al vídeo en YouTube](https://youtu.be/CbPh8-VOIcY)**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Navegación y Capturas de Pantalla**
## 💻 Pantallas

## Pantalla de Inicio:
Esta es la página de inicio de nuestra aplicación web, con un diseño moderno y atractivo. En el centro vemos información clave sobre el gimnasio, como su dirección, datos de contacto y horario de apertura. Incluye un menú de navegación para acceder a las opciones de entrenamiento, nutrición, registro e inicio de sesión.

<img width="1606" height="879" alt="localhost_8443_ (1)" src="https://github.com/user-attachments/assets/fd5c8e3e-d9ad-45b9-81fd-0fc2588c3da3" />

## Pantalla de Registro: 
Esta página corresponde al registro de usuarios de PowerGym. Cuenta con un sencillo formulario para crear una cuenta, en el que se solicita el nombre, la dirección de correo electrónico y la contraseña.

<img width="1606" height="879" alt="localhost_8443_register" src="https://github.com/user-attachments/assets/6f3faf1e-c0e0-4968-82c9-d563a1844e5a" />

## Pantalla de Inicio de Sesión: 
Esta página corresponde a la pantalla de inicio de sesión de PowerGym. Cuenta con un formulario en el que los usuarios pueden introducir su nombre de usuario (prefijo de correo electrónico) y contraseña para acceder a su cuenta.

<img width="1606" height="879" alt="localhost_8443_login" src="https://github.com/user-attachments/assets/d086c8dc-22fa-4e65-977e-a807d23f400b" />

## Pantalla de Perfil: 
Esta página corresponde a la sección del perfil de usuario de PowerGym. Permite a los usuarios ver y actualizar su información personal, incluyendo la posibilidad de subir una imagen de perfil y editar datos como su nombre y correo electrónico.

<img width="1606" height="879" alt="localhost_8443_profileUser" src="https://github.com/user-attachments/assets/f44080b0-5770-4223-87a1-01c51f8b99e8" />

## Pantalla de Progreso: 
Esta página corresponde a la sección My Progress de PowerGym. En ella, el usuario puede visualizar un resumen de su actividad, incluyendo entrenamientos y planes de nutrición suscritos, tiempo promedio de entrenamiento y calorías promedio consumidas. Además, muestra un indicador de consistencia con nivel actual y porcentaje de avance, junto con un gráfico que permite visualizar el progreso general.

<img width="1606" height="879" alt="localhost_8443_progress" src="https://github.com/user-attachments/assets/68d2d8c1-c04d-4d2d-8e47-5046c9dcb815" />

## Pantalla de Nutriciones: 
Esta página corresponde a la pantalla Planes de nutrición de PowerGym. Muestra diferentes opciones de dieta, cada una presentada en formato de tarjeta con una imagen y una breve descripción de las comidas incluidas. Los usuarios pueden ver los detalles clave de las comidas y hacer clic en el botón «Más información» para obtener más detalles sobre cada plan de nutrición.

<img width="1606" height="879" alt="localhost_8443_nutritions" src="https://github.com/user-attachments/assets/948f9e5a-1623-4da6-8a10-841d2c14e16b" />

## Pantalla de Detalles de Nutrición: 
Esta página corresponde a la pantalla Detalles nutricionales de PowerGym. Proporciona información detallada sobre un plan de dieta seleccionado, incluyendo su nombre, el total de calorías diarias y el objetivo principal. La página también muestra una imagen representativa y un desglose de las comidas del día, lo que permite a los usuarios revisar el plan nutricional completo.

<img width="1606" height="879" alt="localhost_8443_nutritions_1" src="https://github.com/user-attachments/assets/6037d1f1-cfd0-4051-94ef-10a9b268c375" />

## Pantalla de Creación de Nutrición:
Esta página corresponde a la pantalla Crear nueva nutrición en PowerGym. Proporciona un formulario en el que los usuarios pueden cargar una imagen de nutrición e introducir datos clave como el nombre de la nutrición, las calorías totales (kcal), el objetivo y una descripción de las comidas incluidas en el plan.

<img width="1606" height="879" alt="localhost_8443_createNutrition" src="https://github.com/user-attachments/assets/7331967a-0f61-4938-8756-8ee3d05002b6" />

## Pantalla de Edición de Nutrición:
Esta página corresponde a la pantalla de edición de una nutrición en PowerGym. Incluye un formulario donde se pueden modificar el nombre del plan, las calorías, el objetivo nutricional y las comidas que lo componen. A la izquierda se muestra una imagen representativa del plan alimenticio que también se puede editar.

<img width="1606" height="879" alt="localhost_8443_editNutrition_1" src="https://github.com/user-attachments/assets/632e0131-a6bc-4f50-bb25-27dbd7bdcaa2" />

## Pantalla de Entrenamientos: 
Esta página corresponde a la sección Training Plans de PowerGym. En ella se muestran distintos planes de entrenamiento organizados en tarjetas, cada uno con una breve descripción de los ejercicios incluidos. Cada plan cuenta con una imagen representativa y un botón de “More info” para consultar más detalles.

<img width="1606" height="879" alt="localhost_8443_trainings" src="https://github.com/user-attachments/assets/4026f55e-815a-442c-a03c-fd313a5a91a3" />

## Pantalla de Detalles de Entrenamiento:
Esta página corresponde a la sección Training Details de PowerGym. En ella se muestran los detalles específicos de un plan de entrenamiento seleccionado, incluyendo su imagen representativa, nombre, duración y objetivo. También se presenta la lista de ejercicios que componen la rutina.

<img width="1606" height="879" alt="localhost_8443_trainings_1" src="https://github.com/user-attachments/assets/9229d8bf-a05c-419c-b7ce-fb75cfd69b4b" />

## Pantalla de Creación de Entrenamiento:
Esta página corresponde a la pantalla de creación de un nuevo entrenamiento de PowerGym. Presenta un formulario donde el usuario puede introducir el nombre del entrenamiento, la duración en minutos y el objetivo, además de describir los ejercicios que lo componen. También permite adjuntar una imagen representativa del entrenamiento.

<img width="1606" height="879" alt="localhost_8443_createTraining" src="https://github.com/user-attachments/assets/95cb25fe-438e-445e-ae3a-aff35b89e217" />

## Pantalla de Edición de Entrenamiento: 
Esta página corresponde a la pantalla de edición de un entrenamiento en PowerGym. Permite modificar el nombre del entrenamiento, la duración en minutos, el objetivo y los ejercicios que lo componen. A la izquierda se muestra una imagen representativa del entrenamiento seleccionado que también se puede editar.

<img width="1606" height="879" alt="localhost_8443_editTraining_1" src="https://github.com/user-attachments/assets/a7d6e3e0-f96a-4bd0-b88c-87795c6a9416" />

## Pantalla de Listado de Usuarios: 
Esta página corresponde a la sección de Gestión de Usuarios de PowerGym. Muestra un listado de usuarios registrados en el sistema, donde cada administrador dispone de un botón para acceder a los perfiles. Desde esta pantalla, el administrador puede consultar la información individual de cada usuario de forma rápida y sencilla. 

<img width="1606" height="879" alt="localhost_8443_admin_users" src="https://github.com/user-attachments/assets/18c08c61-d875-426c-b7c9-cd9d54e09f04" />

## Pantalla de Información de Usuarios:
Esta página corresponde a la pantalla de Información Usuario de PowerGym. Muestra la información detallada del usuario seleccionado, incluyendo nombre, nombre de usuario, correo electrónico y rol asignado dentro del sistema. Los datos se presentan en tarjetas informativas organizadas de forma clara para facilitar su consulta. Desde aquí el admin puede visualizar la información personal y los permisos asociados a cada usuario.

<img width="1606" height="879" alt="localhost_8443_admin_users_1" src="https://github.com/user-attachments/assets/0e5a99a2-115e-4b55-9ef9-733798e56663" />

## Pantalla de Error: 
Esta página corresponde a la pantalla de error del sistema de PowerGym. Muestra un mensaje indicando un error, junto con una breve descripción técnica del problema ocurrido. Además, incluye un botón para regresar a la página principal y continuar navegando por la aplicación. Su objetivo es informar al usuario de que se ha producido un fallo interno en el servidor.

<img width="1606" height="879" alt="localhost_8443_nutritions_10" src="https://github.com/user-attachments/assets/84eb1a77-355d-4263-8798-7f582c5271f1" />

#### **Diagrama de Navegación**
- **Azul**: Todos los usuarios.
- **Verde**: Usuario registrado y admin.
- **Rojo**: Solo admin.
- **Nota**: Desde todas las páginas se puede acceder a la pantalla de error.

<img width="1164" height="811" alt="image" src="https://github.com/user-attachments/assets/8e4fd618-c950-49c7-8973-940a2790201c" />


### **Instrucciones de Ejecución**

#### **Requisitos Previos**
- **Java**: versión 21 o superior
- **Maven**: versión 3.9 o superior
- **MySQL**: versión 8.0 o superior
- **Git**: para clonar el repositorio

#### **Pasos para ejecutar la aplicación**

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16.git
   cd practica-daw-2025-26-grupo-16/backend
   ```

2. **Configurar la Base de Datos**
   La aplicación está configurada para conectarse con la siguiente información:
   - Base de datos: DAW16
   - Usuario: root
   - Contraseña: Daw2026
   - Puerto: 3306
     
3. **Construir el proyecto**
   Desde la carpeta backend se debe ejecutar:
   mvn clean install
   
4. **Ejecutar la aplicación**
   En el IDE ejecutar la clase Application

5. **Acceder a la aplicación**
   Desde el navegador buscar:
   https://localhost:8443

#### **Credenciales de prueba**
- **Usuario Admin**: usuario: `admin`, contraseña: `admin`
- **Usuario Registrado**: usuario: `user`, contraseña: `user`

### **Diagrama de Entidades de Base de Datos**

Diagrama mostrando las entidades, sus campos y relaciones:

<img width="955" height="1259" alt="DiagramaBBDD-DAW2026" src="https://github.com/user-attachments/assets/2dd0f3c9-c757-4e9a-bcf9-5b89e9214cfd" />

> [Descripción opcional: Ej: "El diagrama muestra las 3 entidades principales: Usuario, Nutrición y Entrenamiento, con sus respectivos atributos y relaciones 1:N y N:M."]

### **Diagrama de Clases y Templates**

Diagrama de clases de la aplicación con diferenciación por colores o secciones:

<img width="1235" height="640" alt="image" src="https://github.com/user-attachments/assets/ab85297d-927b-456a-8469-92ed87fed977" />


### **Participación de Miembros en la Práctica 1**

#### **Alumno 1 - Adrián Esteban Martín**


| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Add subscription implementation](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/e905d1528ca89a22db8ea9d862194ddd0c11c337)  | [UserWebController](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/UserWebController.java)   |
|2| [Implementation of the registration function](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/990c5d5b24efa51192de4c70577cc4cdaa2afdfe)  | [NutritionController](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/NutritionController.java)   |
|3| [Fix generation pdf](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/d7ebaee900f6ef4bb0f662ed0c583bdb085755af)  | [TrainingController](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/TrainingController.java)   |
|4| [Implement object owner based acces control](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/a04599c896b3af1493e158131e6b836a76eb6da6)  | [Nutrition](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/model/Nutrition.java)   |
|5| [Ability to edit profile](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/b209f38e64ae900d0cd1236aaea4db550b6bed98)  | [Training](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/model/Training.java)   |

---

#### **Alumno 2 - Laura Pineda Ballesteros**


| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Add nutrition related pages + mustache](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/0db6bfb33fd49f9575d2f67c4d4b024a300929d7)   | [NutritionWebController](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/NutritionWebController.java)   |
|2| [Add nutrition and dating CRUD + logging in](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/31db6f46b3b36485b996a5841ac7d54d7d49efe5)  | [TrainingWebController](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/TrainingWebController.java)   |
|3| [Add load more ajax logic](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/237238d10b8689429f3130c1dea0586bd5e4b5d0)  | [TrainingWebController](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/TrainingWebController.java)   |
|4| [Add admin list of users](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/e5f59ca635cc31460400b459900054973f542e09)  | [UserWebController](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/UserWebController.java)   |
|5| [Add generate pdf](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/22e8eff7a0d643af7c92e0ea913b4770a17e0b50)  | [TrainingController](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/TrainingController.java)   |

---

#### **Alumno 3 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 4 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

## 🛠 **Práctica 2: Incorporación de una API REST a la aplicación web, despliegue con Docker y despliegue remoto**

### **Vídeo de Demostración**
📹 **[Enlace al vídeo en YouTube](https://youtu.be/vdy9yo8ZqdQ)**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Documentación de la API REST**

#### **Especificación OpenAPI**
📄 **[Especificación OpenAPI (YAML)](backend\api-docs\api-docs.yaml)**

#### **Documentación HTML**
📖 **[Documentación API REST (HTML)](backend\api-docs\api-docs.html)**

> La documentación de la API REST se encuentra en la carpeta `/api-docs` del repositorio. Se ha generado automáticamente con SpringDoc a partir de las anotaciones en el código Java.

### **Diagrama de Clases y Templates Actualizado**

Diagrama actualizado incluyendo los @RestController y su relación con los @Service compartidos:

<img width="1229" height="711" alt="image" src="https://github.com/user-attachments/assets/d56b3e6c-241b-47e5-b82f-95e4b024894e" />


### **Instrucciones de Ejecución con Docker**

#### **Requisitos previos:**
- Docker instalado (versión 20.10 o superior)
- Docker Compose instalado (versión 2.0 o superior)

#### **Pasos para ejecutar con docker-compose:**

1. **Clonar el repositorio** (si no lo has hecho ya):
   ```bash
   git clone https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16.git
   cd practica-daw-2025-26-grupo-16/docker
   ```

2. **Iniciar sesión en docker**:
  docker login

3. **Levantar la aplicación con Docker Compose**:
  docker compose up -d

4. **Comprobar el funcionamiento de los contenedores**:
  docker ps

5. **Acceder a la aplicación desde el navegador**:
  https://localhost:8443

6. **Acceder API REST en local** 
   https://localhost:8443/api/v1

### **Construcción de la Imagen Docker**

#### **Requisitos:**
- Docker instalado en el sistema

#### **Pasos para construir y publicar la imagen:**

1. **Navegar al directorio de Docker**:
   cd practica-daw-2025-26-grupo-16/docker

2. **Construir la imagen Docker en local**:
  ./scripts/build_and_push_image.sh aadri2003 v1

3. **Publicar la imagen en DockerHub**:


4. **Imagen publicada**: 

### **Despliegue en Máquina Virtual**

#### **Requisitos:**
- Acceso a la máquina virtual (SSH)
- Clave privada para autenticación
- Conexión a la red correspondiente o VPN configurada

#### **Pasos para desplegar:**

1. **Conectar a la máquina virtual**:
   ssh -i ssh-keys/appWeb07.key vmuser@appweb07.dawgis.etsii.urjc.es

2. **Ir al directorio de despliegue remoto**:


3. **Iniciar sesión en Docker dentro de la máquina virtual**:
  sudo docker login

4. **Levantar la aplicación utilizando el OCI Artifact publicado en Docker Hub**:


5. **Comprobar los contenedores**:
  sudo docker ps


### **URL de la Aplicación Desplegada**

🌐 **URL de acceso**: `https://appweb16.dawgis.etsii.urjc.es:8443`

#### **Credenciales de Usuarios de Ejemplo**

| Rol | Usuario | Contraseña |
|:---|:---|:---|
| Administrador | admin | admin |
| Usuario Registrado | user | user|

### **Participación de Miembros en la Práctica 2**

#### **Alumno 1 - Adrián Esteban Martín**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Initialize DTOs and mappers](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/921db2393fddc4e3588d51ab8cf118da32c1bdbb)  | [NutritionRestController.java](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/NutritionRestController.java)   |
|2| [Start of API REST](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/62c16907ca38b945f03b38cfd8f487a2d6593e21)  | [TrainingRestController.java](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/TrainingRestController.java)   |
|3| [Add user endpoint](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/2e877fc352fb10a96bacb3da74c924eb993af4a1)  | [UserRestController.java](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/UserRestController.java)   |
|4| [Implement training endpoints and fix others](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/2d4ba34e8c04243376b29b36876d5f4eae9d04b1)  | [NutritionDTO.java](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/dto/NutritionDTO.java)   |
|5| [Add image permissions](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/90dc4381e2cf2e16fa6a79d8f7ba8357974b44d3)  | [TrainingDTO.java](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/dto/TrainingDTO.java)   |

---

#### **Alumno 2 - Laura Pineda Ballesteros**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [dockerfile and docker-compose config](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/f98e0f52389bb72dbc6b701bbdb83ed7ce83d179)  | [docker-compose.yml](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/docker/docker-compose.yml)   |
|2| [add endpoint to handle progress chart data](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/72e1a3f43819b3db4a956fa03bf09913bd1e3b33)  | [ProgressRestController.java](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/ProgressRestController.java)   |
|3| [refactor Docker and deployment scripts](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/57f1d24acc3937ad88e569563b7f3f5619d51d81)  | [NutritionRestController.java](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/NutritionRestController.java)   |
|4| [add subscription and unsubscription endpoints for nutrition and training](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/8767c76d2bbf8fe74a5cf4254a509e5126a33264)  | [TrainingRestController.java](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/TrainingRestController.java)   |
|5| [Merge branch 'main' of https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/55fc80db44811386743fe0c141ce27279f12e741)  | [UserRestController.java](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/backend/src/main/java/es/codeurjc/daw/powergym/controller/UserRestController.java)   |

---

#### **Alumno 3 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 4 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

## 🛠 **Práctica 3: Implementación de la web con arquitectura SPA**

### **Vídeo de Demostración**
📹 **[Enlace al vídeo en YouTube](https://youtu.be/wA4UQ3u8QnY  )**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Preparación del Entorno de Desarrollo**

#### **Requisitos Previos**
- **Node.js**: versión 20.x o superior
- **npm**: versión 10.x o superior (incluido con Node.js)
- **Java**: version 21.x o superior
- **Maven**: version 3.9.x o superior
- **MySQL**: version 8.0.x o superior

#### **Pasos para configurar el entorno de desarrollo**

1. **Clonar el repositorio** (si no lo has hecho ya)

   git clone https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16.git
   cd practica-daw-2025-26-grupo-16

2. **Configurar y arrancar el backend (Spring Boot)**

   -e MYSQL_ROOT_PASSWORD=DAW2026
  -e MYSQL_DATABASE=DAW16 \

3. **Configurar y arrancar el frontend (React SPA)**

   cd frontend
   npm install       # instala las dependencias (solo la primera vez)
   npm run dev       # arranca el servidor de desarrollo con Vite

4. **Comandos útiles del frontend**

  npm run dev : Servidor de desarrollo con hot reload (HMR)
  npm run typecheck :	Verifica los tipos TypeScript

5. **Despliegue**

  La aplicación queda accesible en:
  Web MVC (Mustache): https://localhost:8443/
  Web SPA (React): https://localhost:8443/new/

### **Diagrama de Clases y Templates de la SPA**

Diagrama mostrando los componentes React, hooks personalizados, servicios y sus relaciones:

![Diagrama de Componentes React](images/spa-classes-diagram.png)

### **Participación de Miembros en la Práctica 3**

#### **Alumno 1 - Adrián Esteban Martín**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Implement function edit](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/96c73d6730bb1e5b62ce5948c042df6fe2274837)  | [nutrition-detail.tsx](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/frontend/app/routes/nutrition-detail.tsx)   |
|2| [Implement delete function](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/b1d5b6ed049e92c541b1f0e604b946e611ef7110)  | [training-detail.tsx](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/frontend/app/routes/training-detail.tsx)   |
|3| [Implement subscribe/unsubscribe nutrition](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/cdfc996d12322cd7023e60625344e944961bb9cd)  | [profileUser.tsx](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/frontend/app/routes/profileUser.tsx)   |
|4| [Implement the creation of new entities](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/751ab3d09eadc39b64240e472b2285b711fa8090)  | [nutritions-service.ts](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/frontend/app/services/nutritions-service.ts)   |
|5| [Implemente users management](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/3428935dc45ec9062956880ca1bc228f4c1947ca)  | [trainings-service.ts](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/blob/main/frontend/app/services/trainings-service.ts)   |

---

#### **Alumno 2 - [Laura Pineda Ballesteros]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [SPA and background image frontend fix](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/f2bf14c4179b0f2eb8faf244e3ff391f67146bc4)  | [login-service.ts](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/f2bf14c4179b0f2eb8faf244e3ff391f67146bc4#diff-5401a61f154480d7736b4b9783497dc50314b95c5e8b4e60ade74cc773d1eb03URL_archivo_1)   |
|2| [Error handling and fallback controller](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/7b52814f987a3ba06594421bbd34cc5a17d70261)  | [FallbackController.java](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/7b52814f987a3ba06594421bbd34cc5a17d70261#diff-3015d32b3e8a5a9838552d2de5035839c30c19f6966ef9cb4d082c3e83c58dc4)   |
|3| [User auth and registration](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/d875be49b7ff1ebd637bcb595efd0b8c39c9a18f)  | [login.tsx](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/d875be49b7ff1ebd637bcb595efd0b8c39c9a18f#diff-0c53fd023f612cd77b500c70166c4799d96da6ab8a7f7c7839f8c69be24a0c39)   |
|4| [Docker setup](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/4ac70702ec0d6b29c5d5e5b6301d7a008e8a0655)  | [Dockerfile](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/4ac70702ec0d6b29c5d5e5b6301d7a008e8a0655#diff-fed51f49a9f26cb93cc870efdc9419d425b9422354ae41bb651c3333c8bff486)   |
|5| [Progress tracking](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/480128e4973c7f0c038f762a4683a93ab46b7992)  | [progress.tsx](https://github.com/CodeURJC-DAW-2025-26/practica-daw-2025-26-grupo-16/commit/480128e4973c7f0c038f762a4683a93ab46b7992#diff-04b5519ad3156f837dc27003acde982bff0ac68987418476428dbf74079d9baf)   |

---

#### **Alumno 3 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 4 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

