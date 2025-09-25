import { fixture } from "../../src/hooks/fixture";
import { documentospendientesPage } from "../pages/documentospendientesPage";
import { Then } from "@cucumber/cucumber";




Then('Seleccionar la opción Estado de Cuenta pendientes', async function () {
    const documentosPendientes = new documentospendientesPage(fixture.page);
    await documentosPendientes.seleccionarEstadoCuentaDocumentosPendientes();
});

Then('Selecionar  estado de cuenta documentos pendientes', async function () {
    const documentosPendientes = new documentospendientesPage(this.page);
    await documentosPendientes.seleccionarEstadoCuentaDocumentosPendientes();
});