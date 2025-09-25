import { estadoCuentaPage } from '../pages/estadoCuentaPage';
import { Then } from "@cucumber/cucumber";
import { fixture } from "../../src/hooks/fixture"; 

Then('Seleccionar la opción Estado de Cuenta', async function () {
    const estadoCuenta = new estadoCuentaPage(fixture.page);
    await estadoCuenta.seleccionarEstadoDeCuenta();
});

Then('Hacer clic en la opción Cuotas', async function () {
    const estadoCuenta = new estadoCuentaPage(fixture.page);
    await estadoCuenta.hacerClicEnCuotas();
});

Then('Escribir el número de póliza {string} en la caja de texto', async function (numeroPoliza) {
    const estadoCuenta = new estadoCuentaPage(fixture.page);
    await estadoCuenta.escribirNumeroPoliza(numeroPoliza);
});

Then('Hacer clic en el botón Buscar', async function () {
    const estadoCuenta = new estadoCuentaPage(fixture.page);
    await estadoCuenta.hacerClicEnBuscar();
});

Then('Verificar que el número de póliza {string} fue encontrado', async function (numeroPoliza) {
    const estadoCuenta = new estadoCuentaPage(fixture.page);
    await estadoCuenta.seleccionarPoliza(numeroPoliza);
});



Then('Seleccionar la póliza encontrada para ir al módulo Póliza', async function (numeroPoliza: string) {
    const estadoCuenta = new estadoCuentaPage(fixture.page);
    await estadoCuenta.seleccionarPoliza(numeroPoliza);
});