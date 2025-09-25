Feature: Creación de Empleado

@happyPath
Scenario: Crear un empleado con datos válidos
    Given que estoy en la página de OrangeHRM
    When ingreso mis credenciales username "Admin" y password "admin123"
    Then el inicio de sesión es satisfactorio