package com.proriberaapp.ribera.utils.pdfTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.proriberaapp.ribera.Api.controllers.client.dto.ReportOfKitchenBdDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.ReportOfKitchenDto;

public class ReportKitchenPdf {
    private List<ReportOfKitchenBdDto> reportOfKitchenDto;

    public ReportKitchenPdf(List<ReportOfKitchenBdDto> reportOfKitchenDto) {
        this.reportOfKitchenDto = reportOfKitchenDto;
    }

    public String toTemplate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");

        String currentDateHour = dateFormat.format(new Date());
        String currentDate = date.format(new Date());
        String templateItem = """
                                <tr>
                            <td>%number_room</td>
                            <td>%checkin</td>
                            <td>%checkout</td>
                            <td>%number_adults</td>
                            <td>%adultsmayor</td>
                            <td>%adultsextra</td>
                            <td>%number_children</td>
                            <td>%number_infants</td>
                            <td>%alimentation</td>
                        </tr>
                """;

        ReportOfKitchenBdDto reportOfKitchenEmpty = new ReportOfKitchenBdDto();
        reportOfKitchenEmpty.setNumberadults(0);
        reportOfKitchenEmpty.setNumberadultsmayor(0);
        reportOfKitchenEmpty.setNumberadultsextra(0);
        reportOfKitchenEmpty.setNumberchildren(0);
        reportOfKitchenEmpty.setNumberinfants(0);
        reportOfKitchenEmpty.setTotalbreakfast(0);
        reportOfKitchenEmpty.setTotallunch(0);
        reportOfKitchenEmpty.setTotaldinner(0);
        reportOfKitchenEmpty.setTotalperson(0);
        ReportOfKitchenBdDto reportOfKitchenDtoTotal = this.reportOfKitchenDto.stream().reduce(
                reportOfKitchenEmpty,
                (a, b) -> {
                    a.setNumberadults(a.getNumberadults() + b.getNumberadults());
                    a.setNumberadultsmayor(a.getNumberadultsmayor() + b.getNumberadultsmayor());
                    a.setNumberadultsextra(a.getNumberadultsextra() + b.getNumberadultsextra());
                    a.setNumberchildren(a.getNumberchildren() + b.getNumberchildren());
                    a.setNumberinfants(a.getNumberinfants() + b.getNumberinfants());

                    return a;
                });
        String templateTotal = """
                <tr>
                <td colspan="3" class="principal">Total</td>
                <td>%number_adults</td>
                <td>%adultsmayor</td>
                <td>%adultsextra</td>
                <td>%number_children</td>
                <td>%number_infants</td>
                <td></td>
                </tr>
                """;
        String valueTemplateItem = this.reportOfKitchenDto.stream()
                .map(d -> templateItem.replace("%number_room", d.getRoomnumber())
                        .replace("%checkin", d.getCheckin())
                        .replace("%checkout", d.getCheckout())
                        .replace("%number_adults", d.getNumberadults().toString())
                        .replace("%adultsmayor", d.getNumberadultsmayor().toString())
                        .replace("%adultsextra", d.getNumberadultsextra().toString())
                        .replace("%number_children", d.getNumberchildren().toString())
                        .replace("%number_infants", d.getNumberinfants().toString())
                        .replace("%alimentation", d.isIsalimentation() ? "Si" : "No"))
                .reduce("", String::concat)
                .concat(templateTotal.replace("%number_adults", reportOfKitchenDtoTotal.getNumberadults().toString())
                        .replace("%adultsmayor", reportOfKitchenDtoTotal.getNumberadultsmayor().toString())
                        .replace("%adultsextra", reportOfKitchenDtoTotal.getNumberadultsextra().toString())
                        .replace("%number_children", reportOfKitchenDtoTotal.getNumberchildren().toString())
                        .replace("%number_infants", reportOfKitchenDtoTotal.getNumberinfants().toString()));

        Integer totalBreakfast = this.reportOfKitchenDto.stream().mapToInt(ReportOfKitchenBdDto::getTotalbreakfast)
                .sum();
        Integer totalLunch = this.reportOfKitchenDto.stream().mapToInt(ReportOfKitchenBdDto::getTotallunch).sum();
        Integer totalDinner = this.reportOfKitchenDto.stream().mapToInt(ReportOfKitchenBdDto::getTotaldinner).sum();
        Integer totalHosted = this.reportOfKitchenDto.stream().mapToInt(ReportOfKitchenBdDto::getTotalperson).sum();
        Integer totalWithAlimentation = this.reportOfKitchenDto.stream()
                .filter(ReportOfKitchenBdDto::isIsalimentation).mapToInt(ReportOfKitchenBdDto::getTotalperson).sum();
        Integer totalWithoutAlimentation = this.reportOfKitchenDto.stream()
                .filter(d -> !d.isIsalimentation())
                .mapToInt(ReportOfKitchenBdDto::getTotalperson)
                .sum();
        String template = """
                <!DOCTYPE html>
                 <html lang="es">
                 <head>
                     <meta charset="UTF-8"/>
                     <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
                     <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                     <title>Reporte de Cocina</title>
                     <style>
                         body { font-family: Arial, sans-serif; font-size: 14px; }
                         table { width: 100%%; border-collapse: collapse; margin: 10px 0; }
                         th, td { border: 1px solid black; padding: 8px; text-align: center; }
                         .principal { background-color: #025928; color: white; }
                     </style>
                 </head>
                 <body>
                     <p><strong>HOTEL</strong></p>
                     <h1>REPORTE COCINA</h1>
                     <p><strong>Fecha:%date</strong></p>
                     <p><strong>Fecha y hora reporte:%current_date_hour</strong></p>
                                 <table>
                                        <tbody>
                                             <tr>
                                             <td class="principal">

                                             <strong>Total hospedados: %total_hosted</strong></td>
                                             </tr>
                                             <tr>
                                             <td class="principal">Total con alimentación: %total_alimentation</td>
                                             </tr>
                                             <tr>
                                             <td class="principal">Total sin alimentación: %total_with_out_alimentation</td>
                                             </tr>
                                             <tr>
                                                 <td>

                                                 <strong>Desayunos( %desayunos )</strong>
                                                    <strong>Almuerzos( %almuerzos )</strong>
                                                   <strong> Cenas( %cenas )</strong>
                                                  </td>
                                             </tr>
                                             </tbody>
                                    </table>

                     <table>
                     <thead>
                     <tr>
                     <th class="principal">Numero habitación</th>
                     <th class="principal">Check in</th>
                     <th class="principal">Check  out</th>
                     <th class="principal">Adultos</th>
                     <th class="principal">Adulto mayor</th>
                     <th class="principal">Adultos extra</th>
                     <th class="principal">Niños</th>
                     <th class="principal">Infantes</th>
                     <th class="principal">Alimentación</th>
                     </tr>
                     </thead>
                         <tbody>
                        %cuerpo_habitaciones
                         </tbody>
                     </table>

                 </body>
                 </html>
                """;
        return template.replace("%date",
                currentDate)
                .replace("%current_date_hour", currentDateHour)
                .replace("%cuerpo_habitaciones", valueTemplateItem)
                .replace("%total_hosted", totalHosted.toString())
                .replace("%total_alimentation", totalWithAlimentation.toString())
                .replace("%total_with_out_alimentation", totalWithoutAlimentation.toString())
                .replace("%desayunos", totalBreakfast.toString())
                .replace("%almuerzos", totalLunch.toString())
                .replace("%cenas", totalDinner.toString());

        /*
         * return String.format("""
         * 
         * """,
         * currentDate,
         * reportOfKitchenDto.getNumberAdults(),
         * reportOfKitchenDto.getNumberAdultMayor(),
         * reportOfKitchenDto.getNumberChildren(),
         * reportOfKitchenDto.getNumberBreakfast(),
         * reportOfKitchenDto.getNumberLunch(),
         * reportOfKitchenDto.getNumberDinner());
         */
    }

}
