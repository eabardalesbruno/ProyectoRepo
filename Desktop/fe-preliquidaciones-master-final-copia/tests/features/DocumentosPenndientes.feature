  @HP_Dpen1 @HP_Dpen2 @HP_Dpen3
Feature: Documentos Pendientes

  Este feature es responsable de validar la búsqueda estado de documentos pendientes y navegar al módulo de póliza.

  Background:

    Given Web was opened
    When Enter credentials of user "ebardale@emeal.nttdata.com" with password "398900BBe*"
      

  Scenario Outline: Buscar y seleccionar póliza en Documentos Pendientes

    Then Seleccionar la opción Estado de Cuenta
    Then Hacer clic en la opción Cuotas
    Then Escribir el número de póliza "<numeroPoliza>" en la caja de texto
    Then Hacer clic en el botón Buscar
    #Then Verificar que el número de póliza "<numeroPoliza>" fue encontrado
    #Then Seleccionar la póliza encontrada para ir al módulo Póliza
    #Then Selecionar  estado de cuenta documentos pendientes

    Examples:

      | numeroPoliza  |
      |   30410       |
    

 @HP_Dpen
Scenario: Seleccionar documentos pendientes
    Then Seleccionar la opción Estado de Cuenta pendientes
    Then Selecionar  estado de cuenta documentos pendientes
