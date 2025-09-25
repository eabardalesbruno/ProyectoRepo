import { Page, Locator } from "@playwright/test";

export class LoginPortalPage {
    private page: Page;

    private elements: { [key: string]: string } = {
        txtUsername: "//input[@type='email' and @name='loginfmt' and @id='i0116']",
        btnSiguiente: "#idSIButton9",
        txtPassword: "//input[@id='passwordInput']",
        btnIniciarSesion: "#submitButton",
        btnSubmitSi: "//input[@id='idSIButton9']",
        btnMiPortafolio: "//a[@role='menuitem' and contains(text(), 'Mi portafolio')]",
        btnSubMenuSomosCorredores: "//div[contains(text(),'Somos Corredores')]",
        btnComisiones: "//a[@class='dropdown-toggle' and contains(text(), 'Comisiones')]"
    };

    constructor(page: Page) {
        this.page = page;
    }

    private getLocator(selector: string): Locator {
        return this.page.locator(selector);
    }

    async click(elementKey: string) {
        const element = this.getLocator(this.elements[elementKey]);
        await element.waitFor({ state: "visible", timeout: 20000 });
        await element.click();
    }

    async type(elementKey: string, text: string) {
        const element = this.getLocator(this.elements[elementKey]);
        await element.waitFor({ state: "visible", timeout: 20000 });
        await element.fill(text);
    }

    async navigateToPortalPage() {
        const url = process.env.URL_AMBIENTE || "https://somoscorredorescrt.pacificotest.com.pe/";
        await this.page.goto(url);
        await this.page.setViewportSize({ width: 1280, height: 720 });
        console.log("🚀 Navegado a URL:", url);
    }

    async seleccionarSomosCorredores() {
        try {
            
            await this.page.click(this.elements.btnMiPortafolio);
            console.log("✅ Click en Mi Portafolio");

            await this.page.click(this.elements.btnSubMenuSomosCorredores);
            console.log("✅ Click en Somos Corredores");
        } catch (error) {
            console.error("❌ No se pudo ingresar al portal", error);
            throw new Error("Error al seleccionar Somos Corredores");
        }
    }

    async signInSuccess(user: string, password: string) {
        try {
            await this.type("txtUsername", user);
            console.log("📝 Ingresando usuario...");
            await this.click("btnSiguiente");

            await this.type("txtPassword", password);
            console.log("🔐 Ingresando contraseña...");
            await this.click("btnIniciarSesion");

            await this.page.click(this.elements.btnSubmitSi);
            console.log("✅ Inicio de sesión exitoso");
        

            // Validación post-login
           // await this.page.waitForFunction(() => document.querySelector("#userProfile"), null, { timeout: 100 });
        } catch (error) {
            console.error("❌ Error en el proceso de inicio de sesión:", error);
            await this.page.screenshot({ path: 'error.png' }); // Captura para depuración
            throw new Error("Error al iniciar sesión");
        }
    }

   

}