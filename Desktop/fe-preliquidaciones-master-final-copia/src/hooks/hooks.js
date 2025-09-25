"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g = Object.create((typeof Iterator === "function" ? Iterator : Object).prototype);
    return g.next = verb(0), g["throw"] = verb(1), g["return"] = verb(2), typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (g && (g = 0, op[0] && (_ = 0)), _) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
Object.defineProperty(exports, "__esModule", { value: true });
var cucumber_1 = require("@cucumber/cucumber");
var fs = require("fs-extra");
var fixture_1 = require("./fixture");
var browser_manager_1 = require("../util/browser-manager");
var env_1 = require("../config/env");
var winston_1 = require("winston");
var logger_1 = require("../config/logger");
var browser;
var context;
var resultPath = './test-results';
var videoPath;
(0, cucumber_1.setDefaultTimeout)(60 * 1000);
// Configuración inicial antes de todas las pruebas
(0, cucumber_1.BeforeAll)(function () {
    return __awaiter(this, void 0, void 0, function () {
        var browserType, headlessOption;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    (0, env_1.getEnv)(process.env.npm_config_env || 'qa');
                    browserType = process.env.npm_config_tbrowser || process.env.BROWSER || 'chrome';
                    headlessOption = process.env.HEADLESS === 'true';
                    console.log('=====================================');
                    console.log('Environment: ', process.env.ENV);
                    console.log('Headless: ', headlessOption);
                    console.log('Browser: ', browserType);
                    console.log('=====================================');
                    return [4 /*yield*/, (0, browser_manager_1.launchBrowser)(browserType, headlessOption)];
                case 1:
                    browser = _a.sent();
                    return [2 /*return*/];
            }
        });
    });
});
// Configuración antes de cada escenario sin autenticación
(0, cucumber_1.Before)({ tags: 'not @auth' }, function (_a) {
    return __awaiter(this, arguments, void 0, function (_b) {
        var scenarioName, _c, _d, page;
        var pickle = _b.pickle;
        return __generator(this, function (_e) {
            switch (_e.label) {
                case 0:
                    scenarioName = pickle.name + pickle.id;
                    _d = (_c = browser).newContext;
                    return [4 /*yield*/, configureContextOptions(scenarioName, false)];
                case 1: return [4 /*yield*/, _d.apply(_c, [_e.sent()])];
                case 2:
                    context = _e.sent();
                    return [4 /*yield*/, startTracing(scenarioName, pickle)];
                case 3:
                    _e.sent();
                    return [4 /*yield*/, context.newPage()];
                case 4:
                    page = _e.sent();
                    fixture_1.fixture.setPage(page);
                    fixture_1.fixture.logger = (0, winston_1.createLogger)((0, logger_1.options)(scenarioName));
                    return [2 /*return*/];
            }
        });
    });
});
// Configuración antes de cada escenario con autenticación
(0, cucumber_1.Before)({ tags: '@auth' }, function (_a) {
    return __awaiter(this, arguments, void 0, function (_b) {
        var scenarioName, _c, _d, page;
        var pickle = _b.pickle;
        return __generator(this, function (_e) {
            switch (_e.label) {
                case 0:
                    scenarioName = pickle.name + pickle.id;
                    _d = (_c = browser).newContext;
                    return [4 /*yield*/, configureContextOptions(scenarioName, true)];
                case 1: return [4 /*yield*/, _d.apply(_c, [_e.sent()])];
                case 2:
                    context = _e.sent();
                    return [4 /*yield*/, startTracing(scenarioName, pickle)];
                case 3:
                    _e.sent();
                    return [4 /*yield*/, context.newPage()];
                case 4:
                    page = _e.sent();
                    fixture_1.fixture.setPage(page);
                    fixture_1.fixture.logger = (0, winston_1.createLogger)((0, logger_1.options)(scenarioName));
                    return [2 /*return*/];
            }
        });
    });
});
// Acción después de cada paso sin autenticación
(0, cucumber_1.AfterStep)({ tags: 'not @auth' }, function (_a) {
    return __awaiter(this, arguments, void 0, function (_b) {
        var pickle = _b.pickle, result = _b.result;
        return __generator(this, function (_c) {
            switch (_c.label) {
                case 0:
                    if (!(process.env.SCREENSHOT_EVERY_STEP === 'true' || (result === null || result === void 0 ? void 0 : result.status) === cucumber_1.Status.FAILED)) return [3 /*break*/, 2];
                    return [4 /*yield*/, takeScreenshotAndAttach(fixture_1.fixture.page, pickle, this.attach)];
                case 1:
                    _c.sent();
                    _c.label = 2;
                case 2:
                    if (!((result === null || result === void 0 ? void 0 : result.status) === cucumber_1.Status.FAILED)) return [3 /*break*/, 4];
                    return [4 /*yield*/, handleTraceAndVideo(fixture_1.fixture.page, pickle, this.attach)];
                case 3:
                    _c.sent();
                    _c.label = 4;
                case 4: return [2 /*return*/];
            }
        });
    });
});
// Acción después de cada paso con autenticación
(0, cucumber_1.AfterStep)({ tags: '@auth' }, function (_a) {
    return __awaiter(this, arguments, void 0, function (_b) {
        var pickle = _b.pickle, result = _b.result;
        return __generator(this, function (_c) {
            switch (_c.label) {
                case 0:
                    if (!(process.env.SCREENSHOT_EVERY_STEP === 'true' || (result === null || result === void 0 ? void 0 : result.status) === cucumber_1.Status.FAILED)) return [3 /*break*/, 2];
                    return [4 /*yield*/, takeScreenshotAndAttach(fixture_1.fixture.page, pickle, this.attach)];
                case 1:
                    _c.sent();
                    _c.label = 2;
                case 2:
                    if (!((result === null || result === void 0 ? void 0 : result.status) === cucumber_1.Status.FAILED)) return [3 /*break*/, 4];
                    return [4 /*yield*/, handleTraceAndVideo(fixture_1.fixture.page, pickle, this.attach)];
                case 3:
                    _c.sent();
                    _c.label = 4;
                case 4: return [2 /*return*/];
            }
        });
    });
});
// Acción después de cada escenario
(0, cucumber_1.After)(function (_a) {
    return __awaiter(this, arguments, void 0, function (_b) {
        var result = _b.result;
        return __generator(this, function (_c) {
            switch (_c.label) {
                case 0: return [4 /*yield*/, fixture_1.fixture.page.close()];
                case 1:
                    _c.sent();
                    return [4 /*yield*/, context.close()];
                case 2:
                    _c.sent();
                    if (!(process.env.RECORD_VIDEO === 'true' && (result === null || result === void 0 ? void 0 : result.status) == cucumber_1.Status.FAILED)) return [3 /*break*/, 4];
                    return [4 /*yield*/, this.attach(fs.readFileSync(videoPath), 'video/webm')];
                case 3:
                    _c.sent();
                    _c.label = 4;
                case 4: return [2 /*return*/];
            }
        });
    });
});
// Acción después de todas las pruebas
(0, cucumber_1.AfterAll)(function () {
    return __awaiter(this, void 0, void 0, function () {
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0: return [4 /*yield*/, browser.close()];
                case 1:
                    _a.sent();
                    return [2 /*return*/];
            }
        });
    });
});
// Función para iniciar el trazado
function startTracing(scenarioName, pickle) {
    return __awaiter(this, void 0, void 0, function () {
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    if (!(process.env.RECORD_LOGS === 'true')) return [3 /*break*/, 2];
                    return [4 /*yield*/, context.tracing.start({
                            name: scenarioName,
                            title: pickle.name,
                            sources: true,
                            screenshots: true,
                            snapshots: true
                        })];
                case 1:
                    _a.sent();
                    _a.label = 2;
                case 2: return [2 /*return*/];
            }
        });
    });
}
// Función para configurar las opciones del contexto
function configureContextOptions(scenarioName, isAuth) {
    var contextOptions = {};
    if (isAuth) {
        contextOptions.storageState = 'resources/auth/auth.json';
    }
    if (process.env.RECORD_HAR === 'true') {
        contextOptions.recordHar = { path: "".concat(resultPath, "/har/").concat(scenarioName, ".har") };
    }
    if (process.env.RECORD_VIDEO === 'true') {
        contextOptions.recordVideo = { dir: "".concat(resultPath, "/videos/").concat(scenarioName) };
    }
    return contextOptions;
}
// Función para tomar una captura de pantalla y adjuntarla
function takeScreenshotAndAttach(page, pickle, attach) {
    return __awaiter(this, void 0, void 0, function () {
        var screenshotDir, screenshotPath, img;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    screenshotDir = "".concat(resultPath, "/screenshots");
                    fs.ensureDirSync(screenshotDir); //👈 Asegura que la carpeta exista
                    screenshotPath = "".concat(resultPath, "/screenshots/").concat(pickle.name, "_").concat(Date.now(), ".png");
                    return [4 /*yield*/, page.screenshot({ path: screenshotPath, type: 'png' })];
                case 1:
                    img = _a.sent();
                    return [4 /*yield*/, attach(img, 'image/png')];
                case 2:
                    _a.sent();
                    return [2 /*return*/];
            }
        });
    });
}
// Función para manejar el trazado y el video
function handleTraceAndVideo(page, pickle, attach) {
    return __awaiter(this, void 0, void 0, function () {
        var tracePath;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    tracePath = "".concat(resultPath, "/trace/").concat(pickle.id, ".zip");
                    if (!(process.env.RECORD_LOGS === 'true')) return [3 /*break*/, 3];
                    return [4 /*yield*/, context.tracing.stop({ path: tracePath })];
                case 1:
                    _a.sent();
                    return [4 /*yield*/, attach("Trace file: ".concat(tracePath), 'text/html')];
                case 2:
                    _a.sent();
                    _a.label = 3;
                case 3:
                    if (!(process.env.RECORD_VIDEO === 'true')) return [3 /*break*/, 5];
                    return [4 /*yield*/, page.video().path()];
                case 4:
                    videoPath = _a.sent();
                    _a.label = 5;
                case 5: return [2 /*return*/];
            }
        });
    });
}
