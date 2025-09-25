const fs = require('fs');
const path = require('path');

const cucumberReportPath = path.resolve(__dirname, '../test-results/cucumber_report.json');
const serenityOutputDir = path.resolve(__dirname, '../test-results'); // sin crear subcarpetas
const screenshotsDir = path.resolve(__dirname, '../test-results/screenshots');

const cucumberData = JSON.parse(fs.readFileSync(cucumberReportPath, 'utf8'));
const serenityResults = [];

function toSerenityStatus(status) {
  switch (status) {
    case 'passed': return 'SUCCESS';
    case 'failed': return 'FAILURE';
    case 'skipped': return 'SKIPPED';
    default: return 'UNKNOWN';
  }
}

function buildTestStep(step, index, scenarioName) {
  const status = toSerenityStatus(step.result.status);
  const screenshotFilename = `${scenarioName.replace(/[^a-zA-Z0-9]/g, '_')}_step${index + 1}.png`;
  const screenshotPath = path.join(screenshotsDir, screenshotFilename);

  const stepData = {
    number: `${index + 1}`,
    description: step.name,
    startTime: new Date().toISOString(),
    duration: step.result.duration || 0,
    result: status,
    children: [],
    evidence: [],
    logs: [`Ejecución de paso: ${step.name}`]
  };

  // Si existe una captura de pantalla, agregarla como evidencia
  if (fs.existsSync(screenshotPath)) {
    stepData.evidence.push(screenshotPath);
  }

  // Capturar evidencia desde embeddings (cucumber-html-reporter style)
  if (step.embeddings && step.embeddings.length > 0) {
    step.embeddings.forEach(embed => {
      if (embed.mime_type && embed.mime_type.includes("image") && embed.data) {
        stepData.evidence.push(`data:${embed.mime_type};base64,${embed.data}`);
      }
    });
  }

  return stepData;
}

cucumberData.forEach((feature, featureIndex) => {
  if (!feature.elements) return;

  feature.elements.forEach((scenario, scenarioIndex) => {
    const testSteps = scenario.steps.map((step, i) => 
      buildTestStep(step, i, scenario.name)
    );

    serenityResults.push({
      name: scenario.name,
      title: scenario.name,
      description: scenario.description || '',
      userStory: {
        id: feature.id || `feature-${featureIndex + 1}`,
        storyName: feature.name,
        path: feature.uri || '',
        type: 'feature',
      },
      testSource: 'Cucumber',
      result: toSerenityStatus(
        scenario.steps.some(s => s.result.status === 'failed') ? 'failed' : 'passed'
      ),
      testSteps: testSteps,
      startTime: new Date().toISOString(),
      duration: testSteps.reduce((acc, step) => acc + (step.duration || 0), 0),
      manual: false,
      testType: 'automated'
    });
  });
});

// Cambia el nombre del archivo generado a Miresultadoserenity.json
const serenityOutputPath = path.join(serenityOutputDir, 'Miresultadoserenity.json');
// Escribir el archivo serenity.json
fs.writeFileSync(serenityOutputPath, JSON.stringify(serenityResults, null, 2), 'utf8');
console.log('✅ Archivo serenity.json generado correctamente.');
