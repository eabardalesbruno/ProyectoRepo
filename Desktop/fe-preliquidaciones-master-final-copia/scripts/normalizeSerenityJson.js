const fs = require('fs');
const path = require('path');

// Ruta al archivo original
const inputPath = path.join(__dirname, '..', 'test-results', 'MiresultadoserenityConvert.json');

// Ruta al archivo de salida con el nombre esperado por Serenity (serenity.json)
const outputPath = path.join(__dirname, '..', 'test-results', 'serenity.json');

// Validar si existe el archivo original
if (!fs.existsSync(inputPath)) {
  console.error('❌ No se encontró el archivo Miresultadoserenity.json en test-results');
  process.exit(1);
}

// Leer el contenido del archivo original

const jsonData = fs.readFileSync(inputPath, 'utf8');

// Escribirlo como serenity.json (puedes modificar aquí si deseas hacer una normalización real)
fs.writeFileSync(outputPath, jsonData, 'utf8');

console.log('✅ Archivo serenity.json generado exitosamente en test-results');
