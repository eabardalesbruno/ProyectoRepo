# repo-template-pacifico
Repositorio plantilla con configuración base para usarse en toda solicitud de creación de repositorios.

## Configuración de un Nuevo Proyecto

Para configurar un nuevo proyecto utilizando esta plantilla, sigue los siguientes pasos:

1. **Clonar el Repositorio**

   Clona este repositorio en tu máquina local usando el siguiente comando:

   ```bash
   git clone https://github.com/your-organization/repo-template-pacifico.git nombre-de-tu-proyecto
   ```

2. **Cambiar al Directorio del Proyecto**

    Una vez clonado el repositorio, cambia al directorio del nuevo proyecto:

    ```bash
    cd nombre-de-tu-proyecto
    ```
3. **Eliminar el Vínculo con el Repositorio Original**

    Para evitar conflictos y asegurarte de que puedes iniciar un nuevo repositorio desde esta plantilla, elimina el vínculo con el repositorio original:

    ```bash
    git remote remove origin
    ```

4. **Inicializar un Nuevo Repositorio Git**
    
    Inicializa un nuevo repositorio git para comenzar a versionar tu proyecto desde cero:

    ```bash
    git init
    ```

5. **Agregar un Nuevo Repositorio Remoto**

    Agrega el nuevo repositorio remoto donde se alojará tu proyecto. Asegúrate de reemplazar `url-del-nuevo-repositorio` con la URL de tu nuevo repositorio en GitHub o el sistema de control de versiones que estés utilizando:

    ```bash
    git remote add origin url-del-nuevo-repositorio
    ```

6. **Instalar Dependencias**

    Instala las dependencias necesarias para el proyecto ejecutando:

    ```bash
    npm install
    ```
7. **Configuración Inicial del Proyecto**

    Configura las variables de entorno y cualquier otra configuración inicial requerida por tu proyecto. Esto puede incluir la configuración de archivos `.env`, ajustes en archivos de configuración

8. **Primer Commit**

    Realiza tu primer commit para guardar el estado inicial de tu proyecto:

    ```bash
    git add .
    git commit -m "Configuración inicial del proyecto basado en repo-template-pacifico"
    ```

9. **Subir el Proyecto al Repositorio Remoto**

    Finalmente, sube tu proyecto al repositorio remoto:

    ```bash
    git push -u origin master
    ```

Con estos pasos, tendrás configurado un nuevo proyecto utilizando como base la plantilla repo-template-pacifico. Asegúrate de personalizar tu proyecto según las necesidades específicas de tu desarrollo.

## Ejecución del proyecto

Para ejecutar el proyecto, utiliza el siguiente comando en la terminal:

```bash
npm test --tags="@happyPath" --env="qa" --tbrowser="Firefox"
```

### Opciones de Navegador

Puedes especificar el navegador en el que deseas ejecutar tus pruebas utilizando la opción `--tbrowser`. Las opciones disponibles son:

| Navegador | Valor    |
|-----------|----------|
| CHROME    | Chrome   |
| FIREFOX   | Firefox  |
| WEBKIT    | Webkit   |
| EDGE      | Edge     |

> [!TIP]
> Por defecto, el navegador configurado es `Chrome` y el entorno es `qa`.

## Subir los resultados a Qmetry

Para subir los resultados a Qmetry, sigue estos pasos:

1. Configura los valores en el archivo `.env.qa` o `.env.dev` según sea el caso (certificación o desarrollo).
2. Ejecuta el siguiente comando:

```bash
npx ts-node ./src/reports/report.ts --env="qa"
```
- El parámetro --env indica qué propiedades tomar. Esto es solo para el caso de ejecución manual.
- Para que los resultados se suban de manera automática mediante GitHub Actions, mantén los valores como están configurados en los properties, utilizando variables de entorno en el formato ${nombreVariable}.
- Al subir cambios al repositorio, asegúrate de no incluir información sensible en los archivos de configuración.

## Visualización de Traces

Para visualizar los traces de tus pruebas, tienes dos opciones:

1. **Visualización Online:** A través de [Playwright Trace Viewer](https://playwright.dev/docs/trace-viewer), puedes subir y visualizar el archivo de trace. Ten en cuenta que, al ser una ruta pública, es importante no subir archivos que contengan datos confidenciales. Para mantener la seguridad de la información, se recomienda utilizar la opción de visualización local.

2. **Visualización Local:** Para una revisión segura y privada de los traces, ejecuta el siguiente comando en tu terminal:

    ```bash
    npx playwright show-trace test-results/trace/c809d09b-7f2d-4e15-b60b-008a35bbedc8.zip
    ```

    En este comando, `test-results/trace/714c9758-d9a0-4201-80aa-74caa5a5aa39.zip` representa la ruta y el nombre del archivo de trace que deseas visualizar. Asegúrate de reemplazar esta ruta con la correspondiente a tu archivo de trace específico.

## Guardar una Sesión de Autenticación

Para optimizar las pruebas automatizadas reutilizando sesiones de autenticación, sigue estos pasos:

1. **Ejecuta el Comando de Playwright Codegen:** Abre una sesión interactiva en el navegador que te permitirá realizar acciones manuales, como iniciar sesión en la aplicación. Utiliza el siguiente comando en tu terminal:

    ```bash
    npx playwright codegen https://github.com/enterprises/pacifico-seguros/sso --save-storage=resources/auth/auth.json
    ```

    - `https://github.com/enterprises/pacifico-seguros/sso` es la URL de la aplicación para la cual deseas guardar la sesión.
    - `--save-storage=resources/auth/auth.json` especifica la ruta y el nombre del archivo donde se guardará la sesión.

2. **Realiza Acciones Manuales en el Navegador:** Una vez ejecutado el comando, se abrirá una ventana del navegador. Inicia sesión o realiza las acciones necesarias para establecer el estado deseado de la sesión.

3. **Guarda la Sesión:** Al completar las acciones en el navegador, ciérralo. Playwright automáticamente guardará el estado de la sesión en el archivo especificado (`resources/auth/auth.json`).

4. **Reutilización de Sesiones Autenticadas:** Para agilizar las pruebas automatizadas, es posible reutilizar sesiones autenticadas previamente guardadas. Esto elimina la necesidad de autenticación manual en cada ejecución de prueba, optimizando significativamente el tiempo de ejecución. Para implementar esta estrategia, simplemente incluye el tag `@auth` en los escenarios de prueba que requieran autenticación. A continuación, se muestra un ejemplo de cómo aplicarlo:

    ```gherkin
    Feature: Consulta del estado de repositorios en GitHub

    @auth
    Scenario: Verificar la existencia del directorio src en el branch master de un repositorio
        Given el usuario accede a la página de GitHub
        When el usuario busca el repositorio "frameworkpruebasautomatizadas-be"
        Then el usuario verifica la existencia de la carpeta "src" en el branch "master"
    ```

    Utilizar el tag `@auth` permite a Playwright cargar la sesión de autenticación desde el archivo especificado previamente, facilitando el acceso a áreas restringidas de la aplicación sin necesidad de ingresar credenciales en cada prueba. Esta práctica no solo ahorra tiempo sino que también mejora la seguridad al evitar la exposición repetida de credenciales durante las pruebas automatizadas.