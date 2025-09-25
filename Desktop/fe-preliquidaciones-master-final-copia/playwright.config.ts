import { defineConfig, devices } from '@playwright/test';


export default defineConfig({
  testDir: './tests', 
  timeout: 2 * 60 * 1000,
  expect: {
    timeout: 60_000,
  },



  fullyParallel: false,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 1,
  workers: process.env.CI ? 1 : undefined,

  reporter: [
    ['list'], // Muestra los resultados en la consola
    ['json', { outputFile: 'test-results/results.json' }], // Guarda los resultados en test-results
    ['html', { outputFolder: 'playwright-reports', open: 'never' }], // Cambia la carpeta para los reportes HTML
  ],
 
  use: {
      
    headless: true,  
    actionTimeout: 60_000,  
    ignoreHTTPSErrors: true,  
    proxy: process.env.USE_ZAP_PROXY === 'true'
      ? { server: 'http://localhost:9199' } 
      : undefined,
    launchOptions: {
      args: [
        '--ignore-certificate-errors',  
      ],
    },
  },

  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
    {
      name: 'firefox',
      use: { ...devices['Desktop Firefox'] },
    },
    {
      name: 'webkit',
      use: { ...devices['Desktop Safari'] },
    },
  ],
});
