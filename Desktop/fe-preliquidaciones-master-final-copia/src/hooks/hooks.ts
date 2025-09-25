import { Before, BeforeAll, After, AfterAll, Status, AfterStep,setDefaultTimeout } from '@cucumber/cucumber';
import { Browser, BrowserContext, Page } from '@playwright/test';
import { Pickle } from '@cucumber/messages';
import * as fs from 'fs-extra';

import { fixture } from './fixture';
import { launchBrowser } from '../util/browser-manager';
import { getEnv } from '../config/env';
import { createLogger } from 'winston';
import { options } from '../config/logger';

let browser: Browser;
let context: BrowserContext;
const resultPath = './test-results';
let videoPath: string;

setDefaultTimeout(60 * 1000);

// Configuración inicial antes de todas las pruebas
BeforeAll(async function () {
    getEnv(process.env.npm_config_env || 'qa');
    const browserType =  process.env.npm_config_tbrowser  || process.env.BROWSER || 'chrome';
    let headlessOption: boolean = process.env.HEADLESS === 'true';
    console.log('=====================================');
    console.log('Environment: ', process.env.ENV);
    console.log('Headless: ', headlessOption);
    console.log('Browser: ', browserType);
    console.log('=====================================');
    browser = await launchBrowser(browserType, headlessOption);
});

// Configuración antes de cada escenario sin autenticación
Before({tags:'not @auth'},async function ({ pickle }) {
    const scenarioName = pickle.name + pickle.id;
    context = await browser.newContext(await configureContextOptions(scenarioName, false));
    await startTracing(scenarioName, pickle);
    const page = await context.newPage();
    fixture.setPage(page);
    fixture.logger = createLogger(options(scenarioName));
});

// Configuración antes de cada escenario con autenticación
Before({ tags: '@auth'}, async function ({ pickle }) {
    const scenarioName = pickle.name + pickle.id;
    context = await browser.newContext(await configureContextOptions(scenarioName, true));
    await startTracing(scenarioName, pickle);
    const page = await context.newPage();
    fixture.setPage(page);
    fixture.logger = createLogger(options(scenarioName));
});

// Acción después de cada paso sin autenticación
AfterStep({ tags: 'not @auth' }, async function ({ pickle, result }) {
    if (process.env.SCREENSHOT_EVERY_STEP === 'true' || result?.status === Status.FAILED) {
        await takeScreenshotAndAttach(fixture.page, pickle, this.attach);
    }

    if (result?.status === Status.FAILED) {
        await handleTraceAndVideo(fixture.page, pickle, this.attach);
    }
});



// Acción después de cada paso con autenticación
AfterStep({ tags: '@auth' }, async function ({ pickle, result }) {
    if (process.env.SCREENSHOT_EVERY_STEP === 'true' || result?.status === Status.FAILED) {
        await takeScreenshotAndAttach(fixture.page, pickle, this.attach);
    }

    if (result?.status === Status.FAILED) {
        await handleTraceAndVideo(fixture.page, pickle, this.attach);
    }
});



// Acción después de cada escenario
After(async function ({result }) {
    await fixture.page.close();
    await context.close();
    
    if (process.env.RECORD_VIDEO === 'true' && result?.status == Status.FAILED){
        await this.attach(fs.readFileSync(videoPath),'video/webm');
    }  
});

// Acción después de todas las pruebas
AfterAll(async function() {
    await browser.close();
});

// Función para iniciar el trazado
async function startTracing(scenarioName: string, pickle:Pickle) {
    if (process.env.RECORD_LOGS === 'true') {
        await context.tracing.start({
            name: scenarioName,
            title: pickle.name,
            sources: true,
            screenshots: true,
            snapshots: true
        });
    }
}

// Función para configurar las opciones del contexto
function configureContextOptions(scenarioName: string, isAuth:boolean): any {
    let contextOptions: any = {};

    if (isAuth) {
        contextOptions.storageState = 'resources/auth/auth.json';
    }

    if (process.env.RECORD_HAR === 'true') {
        contextOptions.recordHar = { path: `${resultPath}/har/${scenarioName}.har` };
    }

    if (process.env.RECORD_VIDEO === 'true') {
        contextOptions.recordVideo = { dir: `${resultPath}/videos/${scenarioName}` };
    }

    return contextOptions;
}

// Función para tomar una captura de pantalla y adjuntarla
async function takeScreenshotAndAttach(page: Page, pickle: Pickle, attach: any) {
    const screenshotDir = `${resultPath}/screenshots`; //agregado
    fs.ensureDirSync(screenshotDir); //👈 Asegura que la carpeta exista

    const screenshotPath = `${resultPath}/screenshots/${pickle.name}_${Date.now()}.png`;
    const img = await page.screenshot({ path: screenshotPath, type: 'png' });
    await attach(img, 'image/png');
}

// Función para manejar el trazado y el video
async function handleTraceAndVideo(page: Page, pickle:Pickle, attach:any) {
    const tracePath = `${resultPath}/trace/${pickle.id}.zip`;

    if (process.env.RECORD_LOGS === 'true') {
        await context.tracing.stop({ path: tracePath });
        await attach(`Trace file: ${tracePath}`, 'text/html');
    }

    if (process.env.RECORD_VIDEO === 'true') {
        videoPath = await page.video().path();
    }
}