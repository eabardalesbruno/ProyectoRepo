  @HP_Emision
Feature: Preliquidaciones
 Este feature es responsable de válidar las descargas de las nuevas Preliquidaciones

//antes de correr el flujo con el comando npm test --tags="@HP_Emision1" --env="qa" --tbrowser="Chrome" para correr la prueba cambiar el broker o intermediario
 
  Background:

    Given Web was opened
    When Enter credentials of user "ebardale@emeal.nttdata.com" with password "398900BBe**"
      
  @HP_Emision1

  Scenario Outline: Descargas  Preliquidaciones Generales

    Then Seleccionar la opción Comisiones Preliquidaciones Generales
    Then Seleccionar tipo de moneda valor "<tipoMoneda>" Generales
    Then Descargar Archivo pdf "<nombreArchivo>" excel "<nombreArchivo>" en moneda "<tipoMoneda>"
    
    Examples:

       | #    | canal           | tipoSeguro | tipoMoneda   | MontoPago    | tipoArchivo     | nombreArchivo | codigoSistema | corredorId            | corredorNombre                                             | status_code |
       | 1    | portalcorredores|GEN         | USD          | 98.8958      | preliquidacion  | 5000000026130 | GW            | 305318                | SIEKMANN QUEVEDO DE GAMBIRAZIO ELKE KATHERIN               |    200      |
      # | 2    | portalcorredores|PEN         | PEN          | 23.069       | preliquidacion  | 5000000026238 | GW            | 305318                | SIEKMANN QUEVEDO DE GAMBIRAZIO ELKE KATHERIN               |    200      |
      # | 3    | portalcorredores|GEN         | USD          | 347.4274     | preliquidacion  | 5000000026233 | GW            | 305318                | SIEKMANN QUEVEDO DE GAMBIRAZIO ELKE KATHERIN               |    200      |

      
  @HP_Emision2

  Scenario Outline: Descargas  Preliquidaciones Seguros de Vida

    Then Seleccionar la opción Comisiones Preliquidaciones Seguros de Vida
    Then Seleccionar tipo de moneda valor "<tipoMoneda>" Seguros de Vida
    Then Descargar Archivo pdf "<nombreArchivo>" excel "<nombreArchivo>" en moneda "<tipoMoneda>"
 
    Examples:

     | #    | canal           | tipoSeguro | tipoMoneda   | MontoPago    | tipoArchivo     | nombreArchivo | codigoSistema | corredorId            | corredorNombre                                             | status_code |
    # | 1    | portalcorredores|GEN         | USD          | 98.8958      | preliquidacion  | 5000000026130 | GW            | 305318                | SIEKMANN QUEVEDO DE GAMBIRAZIO ELKE KATHERIN               |    200      |
     | 2    | portalcorredores|GEN         | PEN          | 23.069       | preliquidacion  | 5000000026238 | GW            | 305318                | SIEKMANN QUEVEDO DE GAMBIRAZIO ELKE KATHERIN               |    200      |
    # | 3    | portalcorredores|GEN         | USD          | 347.4274     | preliquidacion  | 5000000026233 | GW            | 305318                | SIEKMANN QUEVEDO DE GAMBIRAZIO ELKE KATHERIN               |    200      |


  @HP_Emision3

  Scenario Outline: Descargas  Preliquidaciones Eps y Salud 

    Then Seleccionar la opción Comisiones Preliquidaciones Eps y Salud
    Then Seleccionar tipo de moneda valor "<tipoMoneda>" Eps y Salud
    Then Descargar Archivo pdf "<nombreArchivo>" excel "<nombreArchivo>" en moneda "<tipoMoneda>"
 
    
    Examples:

      | #    | canal           | tipoSeguro | tipoMoneda   | MontoPago    | tipoArchivo     | nombreArchivo | codigoSistema | corredorId            | corredorNombre                                             | status_code |
     # | 1    | portalcorredores|GEN         | USD          | 98.8958      | preliquidacion  | 5000000026130 | GW            | 305318                | SIEKMANN QUEVEDO DE GAMBIRAZIO ELKE KATHERIN               |    200      |
     # | 2    | portalcorredores|GEN         | PEN          | 23.069       | preliquidacion  | 5000000026238 | GW            | 305318                | SIEKMANN QUEVEDO DE GAMBIRAZIO ELKE KATHERIN               |    200      |
      | 3    | portalcorredores|GEN         | USD          | 347.4274     | preliquidacion  | 5000000026233 | GW            | 305318                | SIEKMANN QUEVEDO DE GAMBIRAZIO ELKE KATHERIN               |    200      |
