import { Page } from "@playwright/test";

export class documentospendientesPage {
    private page: Page;

    private elements = {
        btnEstadoCuenta: "//a[@class='dropdown-toggle EstadosdeCuenta']/span[@class='caret']",
        btnDocumentosPendientes: "//a[@href='https://somoscorredorescrt.pacificotest.com.pe/group/guest/documentos-pendientes' and @data-senna-off='true']/b[text()='Documentos pendientes']",
    };

    constructor(page: Page) {
        this.page = page;
    }

    async seleccionarEstadoCuentaDocumentosPendientes() {
        try {
            console.log("🎯 Esperando que termine la navegación...");
            await this.page.waitForLoadState('networkidle', { timeout: 20000 });

            // 1. Click en el menú Estado de Cuenta
            console.log("🎯 Abriendo menú Estado de Cuenta...");
            const estadoCuentaLocator = this.page.locator(this.elements.btnEstadoCuenta);
            await estadoCuentaLocator.waitFor({ state: 'attached', timeout: 10000 });
            await estadoCuentaLocator.scrollIntoViewIfNeeded();
            await estadoCuentaLocator.click({ force: true });

            // 2. Esperar y hacer clic en Documentos Pendientes
            console.log("🎯 Esperando que el botón Documentos Pendientes esté disponible...");
            const documentosPendientesLocator = this.page.locator(this.elements.btnDocumentosPendientes);
            await documentosPendientesLocator.waitFor({ state: 'attached', timeout: 15000 });
            await documentosPendientesLocator.scrollIntoViewIfNeeded();
            await documentosPendientesLocator.click({ force: true });

            console.log("✅ Documentos Pendientes seleccionados.");
        } catch (error) {
            console.error("❌ Error al seleccionar Documentos Pendientes:", error);
        }
    }
}
