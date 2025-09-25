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
exports.UploadQMetry = void 0;
var qmetry_1 = require("../services/qmetry");
var jira_1 = require("../services/jira");
var qmetry_request_1 = require("../util/qmetry-request");
var date_fns_1 = require("date-fns");
var locale_1 = require("date-fns/locale");
var fs_1 = require("fs");
var env_1 = require("../config/env");
var minimist_1 = require("minimist");
var UploadQMetry = /** @class */ (function () {
    function UploadQMetry() {
    }
    UploadQMetry.prototype.uploadQMetry = function () {
        return __awaiter(this, void 0, void 0, function () {
            var args, env, jira, qmetry, jiraContextJwt, apiKey, responseGetApiKey, responsePostApiKey, _a, _b, USERSTORY, COMPONENTS, JIRAUSERID, PLATAFORMA, QUARTER, SPRINT, SQUAD, TESTCYCLEFOLDERID, TESTCASEFORLDERID, ENVIRONMENT, BROWSER, todayNow, startDate, todayYYYYMMDD, bodyRequest, responseNewUpload, url, trackingId, bodyUpload, cucumberJson, responseUpload, _c, _d;
            return __generator(this, function (_e) {
                switch (_e.label) {
                    case 0:
                        args = (0, minimist_1.default)(process.argv.slice(2));
                        env = args.env;
                        console.log("Uploading to QMetry");
                        console.log("Ambiente: " + env);
                        (0, env_1.getEnv)(env || "qa");
                        jira = new jira_1.Jira(process.env.JIRABASEURL, process.env.JIRAUSEREMAIL, process.env.JIRAAPITOKEN);
                        qmetry = new qmetry_1.QMetry(process.env.QMETRYBASEURL);
                        return [4 /*yield*/, jira.getJiraContextJwt()];
                    case 1:
                        jiraContextJwt = _e.sent();
                        if (!jiraContextJwt) {
                            console.error('Error getting Jira Context JWT - APIToken');
                            return [2 /*return*/];
                        }
                        apiKey = '';
                        return [4 /*yield*/, qmetry.getAPIKey(process.env.JIRAPROJECTID, jiraContextJwt)];
                    case 2:
                        responseGetApiKey = _e.sent();
                        if (!(responseGetApiKey.status === 200)) return [3 /*break*/, 4];
                        return [4 /*yield*/, responseGetApiKey.json()];
                    case 3:
                        apiKey = (_e.sent()).key;
                        return [3 /*break*/, 9];
                    case 4: return [4 /*yield*/, qmetry.postAPIKey(process.env.JIRAPROJECTID, jiraContextJwt)];
                    case 5:
                        responsePostApiKey = _e.sent();
                        if (!(responsePostApiKey.status === 200)) return [3 /*break*/, 7];
                        return [4 /*yield*/, responsePostApiKey.json()];
                    case 6:
                        apiKey = (_e.sent()).key;
                        return [3 /*break*/, 9];
                    case 7:
                        _a = printErrors;
                        return [4 /*yield*/, responsePostApiKey.text()];
                    case 8:
                        _a.apply(void 0, [_e.sent(), responsePostApiKey.status, "Error getting ApiKey Token"]);
                        return [2 /*return*/];
                    case 9:
                        _b = process.env, USERSTORY = _b.USERSTORY, COMPONENTS = _b.COMPONENTS, JIRAUSERID = _b.JIRAUSERID, PLATAFORMA = _b.PLATAFORMA, QUARTER = _b.QUARTER, SPRINT = _b.SPRINT, SQUAD = _b.SQUAD, TESTCYCLEFOLDERID = _b.TESTCYCLEFOLDERID, TESTCASEFORLDERID = _b.TESTCASEFORLDERID, ENVIRONMENT = _b.ENVIRONMENT, BROWSER = _b.BROWSER;
                        todayNow = new Date();
                        startDate = (0, date_fns_1.format)(todayNow, "dd/MMM/yyyy HH:mm", { locale: locale_1.enUS });
                        todayYYYYMMDD = (0, date_fns_1.format)(todayNow, "yyyyMMdd");
                        bodyRequest = (0, qmetry_request_1.createUploadConfig)(ENVIRONMENT, COMPONENTS, USERSTORY, todayYYYYMMDD, JIRAUSERID, startDate, startDate, BROWSER, QUARTER, SPRINT, SQUAD, PLATAFORMA, TESTCYCLEFOLDERID, TESTCASEFORLDERID);
                        return [4 /*yield*/, qmetry.generateNewUpload(apiKey, JSON.stringify(bodyRequest))];
                    case 10:
                        responseNewUpload = _e.sent();
                        url = '';
                        trackingId = '';
                        if (!(responseNewUpload.status == 200)) return [3 /*break*/, 17];
                        return [4 /*yield*/, responseNewUpload.json()];
                    case 11:
                        bodyUpload = (_e.sent());
                        url = bodyUpload.url;
                        trackingId = bodyUpload.trackingId;
                        return [4 /*yield*/, readJSONFile()];
                    case 12:
                        cucumberJson = _e.sent();
                        return [4 /*yield*/, qmetry.putUploadResult(url, apiKey, cucumberJson)];
                    case 13:
                        responseUpload = _e.sent();
                        if (!(responseUpload.status == 200)) return [3 /*break*/, 14];
                        console.log("trackingId:" + trackingId);
                        console.log("Upload Succsess");
                        return [3 /*break*/, 16];
                    case 14:
                        _c = printErrors;
                        return [4 /*yield*/, responseUpload.text()];
                    case 15:
                        _c.apply(void 0, [_e.sent(), responseUpload.status, "Error Automation-uploads"]);
                        _e.label = 16;
                    case 16: return [3 /*break*/, 19];
                    case 17:
                        _d = printErrors;
                        return [4 /*yield*/, responseNewUpload.text()];
                    case 18:
                        _d.apply(void 0, [_e.sent(), responseNewUpload.status, "Error new Importresult"]);
                        _e.label = 19;
                    case 19: return [2 /*return*/];
                }
            });
        });
    };
    return UploadQMetry;
}());
exports.UploadQMetry = UploadQMetry;
function readJSONFile() {
    return __awaiter(this, void 0, void 0, function () {
        var path, cucumberJson;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    path = './test-results/cucumber_report.json';
                    return [4 /*yield*/, fs_1.promises.readFile(path, 'utf8')];
                case 1:
                    cucumberJson = _a.sent();
                    return [2 /*return*/, cucumberJson];
            }
        });
    });
}
function printErrors(body, status, message) {
    return __awaiter(this, void 0, void 0, function () {
        return __generator(this, function (_a) {
            console.log("body" + body);
            console.log("status" + status);
            console.error(message);
            return [2 /*return*/];
        });
    });
}
