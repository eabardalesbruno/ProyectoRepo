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
exports.launchBrowser = launchBrowser;
exports.createContext = createContext;
exports.closeBrowser = closeBrowser;
var test_1 = require("@playwright/test");
var dotenv = require("dotenv");
// Carga las variables de entorno desde el archivo .env.qa
dotenv.config({ path: ".env.qa" });
var browser = null;
// Función para obtener las opciones de lanzamiento del navegador
function getLaunchOptions(browserType, headless) {
    var useZapProxy = process.env.USE_ZAP_PROXY === "true";
    var launchOptions = {
        headless: headless,
        args: [
            '--ignore-certificate-errors', // Ignorar errores de certificado
        ],
    };
    if (useZapProxy) {
        console.log("⚡ Usando el proxy de OWASP ZAP");
        launchOptions.proxy = { server: "http://localhost:9199" };
    }
    else {
        console.log("🚀 Ejecutando sin proxy");
    }
    return launchOptions;
}
// Función para lanzar el navegador
function launchBrowser(browserType, headless) {
    return __awaiter(this, void 0, void 0, function () {
        var launchOptions, _a;
        return __generator(this, function (_b) {
            switch (_b.label) {
                case 0:
                    launchOptions = getLaunchOptions(browserType, headless);
                    _a = browserType.toLowerCase();
                    switch (_a) {
                        case "chromium": return [3 /*break*/, 1];
                        case "chrome": return [3 /*break*/, 1];
                        case "firefox": return [3 /*break*/, 3];
                        case "webkit": return [3 /*break*/, 5];
                    }
                    return [3 /*break*/, 7];
                case 1: return [4 /*yield*/, test_1.chromium.launch(launchOptions)];
                case 2:
                    browser = _b.sent();
                    return [3 /*break*/, 8];
                case 3: return [4 /*yield*/, test_1.firefox.launch(launchOptions)];
                case 4:
                    browser = _b.sent();
                    return [3 /*break*/, 8];
                case 5: return [4 /*yield*/, test_1.webkit.launch(launchOptions)];
                case 6:
                    browser = _b.sent();
                    return [3 /*break*/, 8];
                case 7: throw new Error("Browser type \"".concat(browserType, "\" no soportado."));
                case 8: return [2 /*return*/, browser];
            }
        });
    });
}
// Función para crear un contexto del navegador
function createContext() {
    return __awaiter(this, void 0, void 0, function () {
        return __generator(this, function (_a) {
            if (!browser) {
                throw new Error("El navegador no está inicializado. Llama a launchBrowser primero.");
            }
            return [2 /*return*/, browser.newContext({ ignoreHTTPSErrors: true })];
        });
    });
}
// Función para cerrar el navegador
function closeBrowser() {
    return __awaiter(this, void 0, void 0, function () {
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    if (!browser) return [3 /*break*/, 2];
                    return [4 /*yield*/, browser.close()];
                case 1:
                    _a.sent();
                    browser = null;
                    _a.label = 2;
                case 2: return [2 /*return*/];
            }
        });
    });
}
