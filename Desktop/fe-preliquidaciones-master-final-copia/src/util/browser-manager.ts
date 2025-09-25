import { chromium, firefox, webkit, Browser, BrowserContext, LaunchOptions } from "@playwright/test";
import * as dotenv from "dotenv";

// Carga las variables de entorno desde el archivo .env.qa
dotenv.config({ path: ".env.qa" });

let browser: Browser | null = null;

// Función para obtener las opciones de lanzamiento del navegador
function getLaunchOptions(browserType: string, headless: boolean): LaunchOptions {
    const useZapProxy = process.env.USE_ZAP_PROXY === "true";
    const launchOptions: LaunchOptions = {
        headless,
        args: [
            '--ignore-certificate-errors', // Ignorar errores de certificado

        ],
    };

    if (useZapProxy) {
        console.log("⚡ Usando el proxy de OWASP ZAP");
        launchOptions.proxy = { server: "http://localhost:9199" };
    } else {
        console.log("🚀 Ejecutando sin proxy");
    }

    return launchOptions;
}

// Función para lanzar el navegador
export async function launchBrowser(browserType: string, headless: boolean): Promise<Browser> {
    const launchOptions = getLaunchOptions(browserType, headless);

    switch (browserType.toLowerCase()) {
        case "chromium":
        case "chrome": // Maneja "Chrome" como un alias de "chromium"
            browser = await chromium.launch(launchOptions);
            break;
        case "firefox":
            browser = await firefox.launch(launchOptions);
            break;
        case "webkit":
            browser = await webkit.launch(launchOptions);
            break;
        default:
            throw new Error(`Browser type "${browserType}" no soportado.`);
    }

    return browser;
}

// Función para crear un contexto del navegador
export async function createContext(): Promise<BrowserContext> {
    if (!browser) {
        throw new Error("El navegador no está inicializado. Llama a launchBrowser primero.");
    }
    return browser.newContext({ ignoreHTTPSErrors: true });
}

// Función para cerrar el navegador
export async function closeBrowser(): Promise<void> {
    if (browser) {
        await browser.close();
        browser = null;
    }
}