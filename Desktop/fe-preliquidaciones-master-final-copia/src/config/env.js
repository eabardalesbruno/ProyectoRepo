"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getEnv = void 0;
var dotenv = require("dotenv");
var getEnv = function (environment) {
    dotenv.config({
        override: true,
        path: "resources/.env.".concat(environment)
    });
};
exports.getEnv = getEnv;
