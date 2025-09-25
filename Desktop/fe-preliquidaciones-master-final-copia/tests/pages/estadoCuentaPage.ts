import { Page } from "@playwright/test";

export class estadoCuentaPage {
    private page: Page;

    private elements = {
        btnEstadoCuenta: "//a[@class='dropdown-toggle EstadosdeCuenta']/span[@class='caret']",
        btnCuotas: "//li[@class='border-comition ']/a[@data-senna-off='true' and @href='https://somoscorredorescrt.pacificotest.com.pe/group/guest/pga/cuotas']/b[text()='Cuotas']",
        inputNumeroPoliza: "//input[@name='inputBuscador']", // Ajusta el selector según tu HTML
        btnBuscar: "//i[@id='busqueda' and @class='fa fa-search search-filters hidden-xs']", // Ajusta el selector según tu HTML
        polizaEncontrada: (numeroPoliza: string) => `//a[@class='href-numPoliza' and @data-numpoliza='${numeroPoliza}']`
    };

    constructor(page: Page) {
        this.page = page;
    }

    async seleccionarEstadoDeCuenta() {
        try {
            console.log("🎯 Seleccionando la opción Estado de Cuenta...");
            const estadoCuentaLocator = this.page.locator(this.elements.btnEstadoCuenta);
            await estadoCuentaLocator.click();
            console.log("✅ Opción Estado de Cuenta seleccionada.");
        } catch (error) {
            console.error("❌ Error al seleccionar la opción Estado de Cuenta:", error);
        }
    }

    async hacerClicEnCuotas() {
        try {
            console.log("🎯 Haciendo clic en la opción Cuotas...");
            const cuotasLocator = this.page.locator(this.elements.btnCuotas);
    
            // Agrega un retraso manual
            await this.page.waitForTimeout(2000);
    
            await cuotasLocator.waitFor({ state: 'visible' });
            await cuotasLocator.click();
            console.log("✅ Opción Cuotas seleccionada.");
        } catch (error) {
            console.error("❌ Error al hacer clic en la opción Cuotas:", error);
            await this.page.screenshot({ path: 'error-cuotas.png' });
        }
    }

    async escribirNumeroPoliza(numeroPoliza: string) {
        try {
            console.log(`🎯 Escribiendo el número de póliza: ${numeroPoliza}...`);
            await this.page.locator(this.elements.inputNumeroPoliza).fill(numeroPoliza);
            console.log("✅ Número de póliza escrito.");
        } catch (error) {
            console.error(`❌ Error al escribir el número de póliza ${numeroPoliza}:`, error);
        }
    }

    async hacerClicEnBuscar() {
        try {
            console.log("🎯 Haciendo clic en el botón Buscar...");
            await this.page.locator(this.elements.btnBuscar).click();
            console.log("✅ Botón Buscar clickeado.");
        } catch (error) {
            console.error("❌ Error al hacer clic en el botón Buscar:", error);
        }
    }

    async seleccionarPoliza(numeroPoliza: string) {
        console.log(`🎯 Buscando la póliza con número ${numeroPoliza}...`);
    
        // Localizar el elemento de la póliza usando el selector dinámico
        const polizaLocator = this.page.locator(`//a[@class='href-numPoliza' and @data-numpoliza='${numeroPoliza}']`);
    
        // Verificar si hay coincidencias
        const count = await polizaLocator.count();
        if (count === 1) {
            throw new Error(`❌ No se encontró ninguna póliza con número ${numeroPoliza}`);
        }
    
        console.log(`⚠️ Se encontraron ${count} coincidencias para la póliza ${numeroPoliza}. Seleccionando el Primer Valor.`);
    
        // Hacer clic en la primera coincidencia
        await polizaLocator.first().click();
    
        console.log(`✅ Póliza con número ${numeroPoliza} seleccionada.`);
    }
}