interface CustomField {
  name: string;
  value: string;
  cascadeValue?: string;
}

interface TestEntity {
  components: string[];
  priority: string;
  status: string;
  folderId: string;
  description?: string;
  assignee: string;
  reporter: string;
  summary?: string;
  plannedStartDate?: string;
  plannedEndDate?: string;
  customFields: CustomField[];
}

interface UploadConfig {
  format: string;
  attachFile: boolean;
  isZip: false;
  environment: string;
  matchTestSteps: boolean;
  fields: {
    testCycle: TestEntity;
    testCase: TestEntity;
    testCaseExecution: {
      comment: string;
      assignee: string;
      customFields: CustomField[];
    };
  };
}

export function createUploadConfig(
  environment: string,
  component: string,
  hu: string,
  todayYYYYMMDD: string,
  jiraId: string,
  plannedStartDate: string,
  plannedEndDate: string,
  entorno: string,
  quarter: string,
  sprint: string,
  squad: string,
  plataforma: string,
  testCycleFolderId: string,
  testCaseFolderId: string,
): UploadConfig {
  return {
    format: "cucumber",
    attachFile: true,
    isZip: false,
    environment,
    matchTestSteps: true,
    fields: {
      testCycle: {
        components: [component],
        priority: "Medium",
        status: "Done",
        folderId:testCycleFolderId,
        summary: `${hu}_${component}_${todayYYYYMMDD}`,
        description: "Automated generated Test Cycle",
        assignee: jiraId,
        reporter: jiraId,
        plannedStartDate,
        plannedEndDate,
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
        folderId:testCaseFolderId,
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
        customFields: [{ name: "Entorno", value: entorno },{name:"Plataforma",value:plataforma}],
      },
    },
  };
}