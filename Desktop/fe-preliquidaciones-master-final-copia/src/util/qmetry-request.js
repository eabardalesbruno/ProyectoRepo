"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.createUploadConfig = createUploadConfig;
function createUploadConfig(environment, component, hu, todayYYYYMMDD, jiraId, plannedStartDate, plannedEndDate, entorno, quarter, sprint, squad, plataforma, testCycleFolderId, testCaseFolderId) {
    return {
        format: "cucumber",
        attachFile: true,
        isZip: false,
        environment: environment,
        matchTestSteps: true,
        fields: {
            testCycle: {
                components: [component],
                priority: "Medium",
                status: "Done",
                folderId: testCycleFolderId,
                summary: "".concat(hu, "_").concat(component, "_").concat(todayYYYYMMDD),
                description: "Automated generated Test Cycle",
                assignee: jiraId,
                reporter: jiraId,
                plannedStartDate: plannedStartDate,
                plannedEndDate: plannedEndDate,
                customFields: [
                    { name: "Entorno", value: entorno },
                    { name: "Quarter", value: quarter },
                    { name: "sprint (Personalizado)", value: sprint },
                    { name: "squad", value: squad },
                ],
            },
            testCase: {
                components: [component],
                priority: "Medium",
                status: "Done",
                folderId: testCaseFolderId,
                description: "Automated generated Test Case",
                assignee: jiraId,
                reporter: jiraId,
                customFields: [
                    { name: "Automatizable", value: "Si", cascadeValue: "Terminado" },
                    { name: "Tipo de Prueba", value: "Funcional" },
                ],
            },
            testCaseExecution: {
                comment: "Automated generated Test Execution",
                assignee: jiraId,
                customFields: [{ name: "Entorno", value: entorno }, { name: "Plataforma", value: plataforma }],
            },
        },
    };
}
