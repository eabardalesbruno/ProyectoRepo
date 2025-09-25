import { Then } from "@cucumber/cucumber";
import { fixture } from "../../src/hooks/fixture";  // Asegúrate de que fixture tiene la página Playwright
import { ComisionesPage } from "../pages/comisionesPage";
import { expect } from "@playwright/test";


Then('Seleccionar la opción Comisiones Preliquidaciones Generales', async function () {
    const comisionesPage = new ComisionesPage(fixture.page);
    await comisionesPage.seleccionarComisionesPreliquidacionesGenerales();
});

Then('Seleccionar la opción Comisiones Preliquidaciones Seguros de Vida', async function () {
    const comisionesPage = new ComisionesPage(fixture.page);
    await comisionesPage.seleccionarComisionesPreliquidacionesSegurosVida();
});

Then('Seleccionar la opción Comisiones Preliquidaciones Eps y Salud', async function () {
    const comisionesPage = new ComisionesPage(fixture.page);
    await comisionesPage.seleccionarComisionesPreliquidacionesEpsySalud();
});


Then('Seleccionar tipo de moneda valor {string} Generales', async function (tipoMoneda) {
    const comisionesPage = new ComisionesPage(fixture.page);
    await comisionesPage.seleccionarTipodeMonedaGenerales(tipoMoneda);
   });

Then('Seleccionar tipo de moneda valor {string} Seguros de Vida', async function (tipoMoneda) {
    const comisionesPage = new ComisionesPage(fixture.page);
    await comisionesPage.seleccionarTipodeMonedaSegurosdeVida(tipoMoneda);

});

Then('Seleccionar tipo de moneda valor {string} Eps y Salud', async function (tipoMoneda) {
    const comisionesPage = new ComisionesPage(fixture.page);
    await comisionesPage.seleccionarTipodeMonedaEpsySalud(tipoMoneda);
});

Then('Descargar Archivo pdf {string} excel {string} en moneda {string}', async function (nombreArchivoPDF, nombreArchivoExcel, moneda) {
    const comisionesPage = new ComisionesPage(fixture.page);
    await comisionesPage.descargarArchivo(nombreArchivoPDF, 'pdf', moneda);
    await comisionesPage.descargarArchivo(nombreArchivoExcel, 'excel', moneda);
});