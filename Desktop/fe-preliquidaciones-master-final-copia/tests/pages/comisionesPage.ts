import { Page, Locator } from "@playwright/test";

export class ComisionesPage {
    private page: Page;

    private elements: { [key: string]: string } = {
        btnComisiones:        "//a[@class='dropdown-toggle' and contains(text(), 'Comisiones')]",
        btnsegurosGenerales:  "//li[@class='dropdown dropdown-span']//span[@class='pdng']/b[contains(text(), 'Seguros Generales')]",
        btnSegurosVida: "//li[@class='dropdown dropdown-span']//span[@class='pdng']/b[contains(text(), 'Seguros de Vida')]",
        txttipoMonedaPENGenerales: "//li[@class='dropdown dropdown-span']//a[@data-senna-off='true' and @href='https://somoscorredorescrt.pacificotest.com.pe/group/guest/pga/comisiones/v2/seguros-generales/soles' and text()='Soles']",
        txttipoMonedaUSDGenerales: "//li[@class='dropdown dropdown-span']//a[@data-senna-off='true' and @href='https://somoscorredorescrt.pacificotest.com.pe/group/guest/pga/comisiones/v2/seguros-generales/dolares' and text()='Dolares']",
        txttipoMonedaPENSegurosdeVida: "//li[@class='dropdown dropdown-span']//a[@data-senna-off='true' and @href='https://somoscorredorescrt.pacificotest.com.pe/group/guest/pga/comisiones/v2/seguros-vida/soles' and text()='Soles']",
        txttipoMonedaUSDSegurosdeVida: "//li[@class='dropdown dropdown-span']//a[@data-senna-off='true' and @href='https://somoscorredorescrt.pacificotest.com.pe/group/guest/pga/comisiones/v2/seguros-vida/dolares' and text()='Dolares']",        
        //btnSegurosEps: "//a[@class='main-navbar__menu-item sub-main-tittle' and contains(text(), 'Pacífico EPS y planes de salud')]",
        btnSegurosEps: "//li[@class='dropdown dropdown-span']//span[@class='pdng']/b[contains(text(), 'Pacífico EPS y planes de salud')]",
        txttipoMonedaPEN_EpsySalud: "//li[@class='dropdown dropdown-span']//a[@data-senna-off='true' and @href='https://somoscorredorescrt.pacificotest.com.pe/group/guest/pga/comisiones/v2/planes-salud/soles' and text()='Soles']",
        txttipoMonedaUSD_EpsySalud: "//li[@class='dropdown dropdown-span']//a[@data-senna-off='true' and @href='https://somoscorredorescrt.pacificotest.com.pe/group/guest/pga/comisiones/v2/planes-salud/dolares' and text()='Dolares']",       
        btndescargarpdf:      "//td[@class='desktop']//a[@data-tipoarc='pdf' and contains(text(), 'PDF')]",
        btndescargarexcel:    "//td[@class='desktop']//a[@data-tipoarc='excel' and contains(text(), 'Excel')]"
    };

    constructor(page: Page) {
        this.page = page;
    }

    private getLocator(selector: string): Locator {
        return this.page.locator(selector);
    }

    async click(elementKey: string) {
        try {
            const element = this.getLocator(this.elements[elementKey]);
            await element.waitFor({ state: "visible", timeout: 3000 });
            await element.click();
        } catch (error) {
            throw new Error('❌ Error al hacer clic en ${elementKey}:, ${error}');
           
        }
    }

    async seleccionarComisionesPreliquidacionesGenerales() {
        console.log("🎯 Seleccionando botón: Comisiones Preliquidaciones Generales");
        try {
            await this.page.waitForLoadState("networkidle"); // Esperar carga completa
    
            // Si hay un loading spinner, espera a que desaparezca
            await this.page.waitForFunction(() => !document.querySelector("#loadingPage"), null, { timeout: 5000 });
    
            await this.click("btnComisiones");
            console.log("✅ Botón Comisiones seleccionado Preliquidaciones Generales");
        } catch (error) {
            console.error("❌ Error al seleccionar Comisiones Preliquidaciones Generales:, ${error}");
            throw new Error("No se pudo seleccionar Comisiones Preliquidaciones Generales");
        }
    }

    
    async seleccionarTipodeMonedaGenerales(tipoMoneda: string) {
        console.log(`🎯 Seleccionando tipo de moneda: ${tipoMoneda} Generales`);
        try {
            await this.page.waitForLoadState("networkidle");
            await this.page.waitForFunction(() => !document.querySelector("#loadingPage"), null, { timeout: 5000 });

            console.log("📌 Haciendo hover en 'Seguros Generales'...");
            const segurosGeneralesLocator = this.getLocator(this.elements.btnsegurosGenerales);
            await segurosGeneralesLocator.hover();
            await this.page.waitForTimeout(2000); // Espera para que se despliegue el submenu

            // Verificar si el menú de monedas es visible
            const monedaPENLocator = this.getLocator(this.elements.txttipoMonedaPENGenerales);
            if (!(await monedaPENLocator.isVisible())) {
                throw new Error("❌ No se visualiza el menú de monedas después del hover");
            }
            console.log("✅ Menú de monedas visible");

            // Determinar el selector correcto según la moneda
            const monedaSelectorKey = tipoMoneda.toUpperCase() === "USD"
                ? "txttipoMonedaUSDGenerales"
                : tipoMoneda.toUpperCase() === "PEN"
                    ? "txttipoMonedaPENGenerales"
                    : null;

            if (!monedaSelectorKey || !this.elements[monedaSelectorKey]) {
                throw new Error(`❌ Tipo de moneda desconocido: ${tipoMoneda} Generales`);
            }

            console.log(`🔍 Selector encontrado para ${tipoMoneda}: ${this.elements[monedaSelectorKey]}`);
            const monedaLocator = this.getLocator(this.elements[monedaSelectorKey]);

            // Esperar que el botón sea visible y esté habilitado antes de hacer clic
            await monedaLocator.waitFor({ state: "visible", timeout: 5000 });
            await monedaLocator.scrollIntoViewIfNeeded();
            await this.page.waitForSelector(this.elements[monedaSelectorKey], { state: "visible", timeout: 5000 });
            await monedaLocator.isEnabled(); // Verifica si el botón está habilitado antes de hacer clic
            // Intentar hacer clic
            await monedaLocator.click({ force: true });
            console.log(`✅ Moneda ${tipoMoneda} Generales seleccionada correctamente`);

            // Navegar a la URL correspondiente
            const url = tipoMoneda.toUpperCase() === "USD"
                ? "https://somoscorredorescrt.pacificotest.com.pe/group/guest/pga/comisiones/v2/seguros-generales/dolares"
                : "https://somoscorredorescrt.pacificotest.com.pe/group/guest/pga/comisiones/v2/seguros-generales/soles";
            await this.page.goto(url);
            console.log(`✅ Navegado a la URL: ${url}`);
        } catch (error) {
            console.error(`❌ Error al seleccionar la moneda ${tipoMoneda}: ${error}`);
            throw new Error(`No se pudo seleccionar la moneda ${tipoMoneda} en Generales`);
        }
    }

    async seleccionarComisionesPreliquidacionesSegurosVida() {
        console.log("🎯 Seleccionando botón: Comisiones Preliquidaciones Seguro Vida");
        try {
            await this.page.waitForLoadState("networkidle"); // Esperar carga completa
    
            // Si hay un loading spinner, espera a que desaparezca
            await this.page.waitForFunction(() => !document.querySelector("#loadingPage"), null, { timeout: 5000 });
    
            await this.click("btnComisiones");
            console.log("✅ Botón Comisiones seleccionado Preliquidaciones Seguro Vida");
        } catch (error) {
            console.error("❌ Error al seleccionar Comisiones Preliquidaciones Seguro Vida:, ${error}");
            throw new Error("No se pudo seleccionar Comisiones Preliquidaciones Seguro Vida");
        }
    }



    async seleccionarTipodeMonedaSegurosdeVida(tipoMoneda: string) {
        console.log(`🎯 Seleccionando tipo de moneda: ${tipoMoneda} Seguros Vida`);
        try {
            await this.page.waitForLoadState("networkidle");
            await this.page.waitForFunction(() => !document.querySelector("#loadingPage"), null, { timeout: 5000 });

            console.log("📌 Haciendo hover en 'Seguros Vida'...");
            const segurosVidaLocator = this.getLocator(this.elements.btnSegurosVida);
            await segurosVidaLocator.hover();
            await this.page.waitForTimeout(2000); // Espera para que se despliegue el submenu

            // Verificar si el menú de monedas es visible
            const monedaPENLocator = this.getLocator(this.elements.txttipoMonedaPENSegurosdeVida); 
            if (!(await monedaPENLocator.isVisible())) {
                throw new Error("❌ No se visualiza el menú de monedas después del hover");
            }
            console.log("✅ Menú de monedas visible");

            // Determinar el selector correcto según la moneda
            const monedaSelectorKey = tipoMoneda.toUpperCase() === "USD"
                ? "txttipoMonedaUSDSegurosdeVida"
                : tipoMoneda.toUpperCase() === "PEN"
                    ? "txttipoMonedaPENSegurosdeVida"
                    : null;

            if (!monedaSelectorKey || !this.elements[monedaSelectorKey]) {
                throw new Error(`❌ Tipo de moneda desconocido: ${tipoMoneda} Seguros Vida`);
            }

            console.log(`🔍 Selector encontrado para ${tipoMoneda}: ${this.elements[monedaSelectorKey]}`);
            const monedaLocator = this.getLocator(this.elements[monedaSelectorKey]);

            // Esperar que el botón sea visible y esté habilitado antes de hacer clic
            await monedaLocator.waitFor({ state: "visible", timeout: 5000 });
            await monedaLocator.scrollIntoViewIfNeeded();
            await this.page.waitForSelector(this.elements[monedaSelectorKey], { state: "visible", timeout: 5000 });
            await monedaLocator.isEnabled(); // Verifica si el botón está habilitado antes de hacer clic
            // Intentar hacer clic
            await monedaLocator.click({ force: true });
            console.log(`✅ Moneda ${tipoMoneda} Seguros Vida seleccionada correctamente`);

            // Navegar a la URL correspondiente
            const url = tipoMoneda.toUpperCase() === "USD"
                ? "https://somoscorredorescrt.pacificotest.com.pe/group/guest/pga/comisiones/v2/seguros-vida/dolares"
                : "https://somoscorredorescrt.pacificotest.com.pe/group/guest/pga/comisiones/v2/seguros-vida/soles";
                
            await this.page.goto(url);
            console.log(`✅ Navegado a la URL: ${url}`);
        } catch (error) {
            console.error(`❌ Error al seleccionar la moneda ${tipoMoneda}: ${error}`);
            throw new Error(`No se pudo seleccionar la moneda ${tipoMoneda} en Seguros Vida`);
        }
    }


    async seleccionarComisionesPreliquidacionesEpsySalud() {
        console.log("🎯 Seleccionando botón: Comisiones Preliquidaciones Eps y Salud");
        try {
            await this.page.waitForLoadState("networkidle"); // Esperar carga completa
    
            // Si hay un loading spinner, espera a que desaparezca
            await this.page.waitForFunction(() => !document.querySelector("#loadingPage"), null, { timeout: 5000 });
    
            await this.click("btnComisiones");
            console.log("✅ Botón Comisiones Preliquidaciones Eps y Salud");
        } catch (error) {
            console.error("❌ Error al seleccionar Comisiones Preliquidaciones Eps y Salud:, ${error}");
            throw new Error("No se pudo seleccionar Comisiones Preliquidaciones Eps y Salud");
        }
    }


    async seleccionarTipodeMonedaEpsySalud(tipoMoneda: string) {
        console.log(`🎯 Seleccionando tipo de moneda: ${tipoMoneda} Eps y Salud`);
        try {
            await this.page.waitForLoadState("networkidle");
            await this.page.waitForFunction(() => !document.querySelector("#loadingPage"), null, { timeout: 5000 });

            console.log("📌 Haciendo hover en 'Eps y Salud'...");
            const epssaludLocator = this.getLocator(this.elements.btnSegurosEps);
            await epssaludLocator.hover();
            await this.page.waitForTimeout(2000); // Espera para que se despliegue el submenu

            // Verificar si el menú de monedas es visible
            const monedaPENLocator = this.getLocator(this.elements.txttipoMonedaPEN_EpsySalud); 
            if (!(await monedaPENLocator.isVisible())) {
                throw new Error("❌ No se visualiza el menú de monedas después del hover");
            }
            console.log("✅ Menú de monedas visible");

            // Determinar el selector correcto según la moneda
            const monedaSelectorKey = tipoMoneda.toUpperCase() === "USD"
                ? "txttipoMonedaUSD_EpsySalud"
                : tipoMoneda.toUpperCase() === "PEN"
                    ? "txttipoMonedaPEN_EpsySalud"
                    : null;

            if (!monedaSelectorKey || !this.elements[monedaSelectorKey]) {
                throw new Error(`❌ Tipo de moneda desconocido: ${tipoMoneda} Eps y Salud`);
            }

            console.log(`🔍 Selector encontrado para ${tipoMoneda}: ${this.elements[monedaSelectorKey]}`);
            const monedaLocator = this.getLocator(this.elements[monedaSelectorKey]);

            // Esperar que el botón sea visible y esté habilitado antes de hacer clic
            await monedaLocator.waitFor({ state: "visible", timeout: 5000 });
            await monedaLocator.scrollIntoViewIfNeeded();
            await this.page.waitForSelector(this.elements[monedaSelectorKey], { state: "visible", timeout: 5000 });
            await monedaLocator.isEnabled(); // Verifica si el botón está habilitado antes de hacer clic
            // Intentar hacer clic
            await monedaLocator.click({ force: true });
            console.log(`✅ Moneda ${tipoMoneda} Eps y Salud seleccionada correctamente`);

            // Navegar a la URL correspondiente
            const url = tipoMoneda.toUpperCase() === "USD"
                ? "https://somoscorredorescrt.pacificotest.com.pe/group/guest/pga/comisiones/v2/planes-salud/dolares"
                : "https://somoscorredorescrt.pacificotest.com.pe/group/guest/pga/comisiones/v2/planes-salud/soles";
                
            await this.page.goto(url);
            console.log(`✅ Navegado a la URL: ${url}`);
        } catch (error) {
            console.error(`❌ Error al seleccionar la moneda ${tipoMoneda}: ${error}`);
            throw new Error(`No se pudo seleccionar la moneda ${tipoMoneda} en Eps y Salud`);
        }
    }

async descargarArchivo(nombreArchivo: string, tipoArchivo: string, moneda: string) {
    console.log(`🎯 Buscando archivo: ${nombreArchivo} en formato ${tipoArchivo} y moneda ${moneda}`);
    // Espera a que la página esté cargada y se muestre la tabla
    await this.page.waitForLoadState("networkidle");

    // Construir los XPaths dinámicos para PDF y Excel basados en el nombreArchivo, tipoArchivo y moneda
    const selector = `//a[@class='arrow-download-table' and @data-numpreliqui='${nombreArchivo}' and @data-tipoarc='${tipoArchivo}']`;

    // Obtener el locator
    const archivoLocator = this.page.locator(selector);

    // Verificar y descargar el archivo
    if (await archivoLocator.count() > 0 && await archivoLocator.isVisible()) {
        console.log(`✅ Se encontró el enlace ${tipoArchivo.toUpperCase()} para el archivo ${nombreArchivo} y moneda ${moneda}. Iniciando descarga...`);
        await archivoLocator.click({ force: true });
    } else {
        console.warn(`⚠ No se encontró el enlace ${tipoArchivo.toUpperCase()} para el archivo ${nombreArchivo} y moneda ${moneda}.`);
    }
}

}