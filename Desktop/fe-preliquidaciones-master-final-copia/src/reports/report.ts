import { UploadQMetry } from './upload';
import report from 'multiple-cucumber-html-reporter';
import * as os from 'os';

const executionDate = new Date();

const browserNames: { [key: string]: string } = {
  'chromium': 'Chrome',
  'firefox': 'Firefox',
  'webkit': 'Safari'
};

let browserType = process.env.BROWSER_TYPE || 'chromium';
browserType = browserNames[browserType] || browserType;

let platform = os.type();
const platformNames: { [key: string]: string } = {
  'Darwin': 'osx',
  'Win32': 'windows',
  'Linux': 'linux'
};
platform = platformNames[platform] || platform;

report.generate({
  jsonDir: './test-results/',
  reportPath: './test-results/cucumber-html-reporte/',
  reportName: 'Playwright Test',
  pageTitle: 'Login Azure B2C - Mi espacio Pacifico',
  displayDuration: false,
  disableLog: true,
  metadata: {
    browser: {
      name: browserType,
      version: 'none',
    },
    platform: {
      name: platform,
      version: os.release(),
    },
  },
  customData: {
    title: 'Run Info',
    data: [
      { label: 'Project', value: 'Login Azure B2C' },
      { label: 'Release', value: '1.0.0' },
      { label: 'Execution Date', value: executionDate.toISOString() },
    ],
  },
});

(async function upload() {
  try {
    const uploadQMetry = new UploadQMetry();
    await uploadQMetry.uploadQMetry();
    console.log('Upload to QMetry completed successfully.');
  } catch (error) {
    console.error('Error during upload to QMetry:', error);
  }
})();