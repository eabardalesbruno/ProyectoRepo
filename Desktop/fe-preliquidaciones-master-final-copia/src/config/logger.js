"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.options = options;
var winston_1 = require("winston");
function options(scenarioName) {
    return {
        transports: [
            new winston_1.transports.File({
                filename: "test-results/logs/".concat(scenarioName, "/log.log"),
                level: 'info',
                format: winston_1.format.combine(winston_1.format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }), winston_1.format.align(), winston_1.format.printf(function (info) { return "".concat(info.level, ":").concat(info.timestamp, ": ").concat(info.message); }))
            })
        ]
    };
}
