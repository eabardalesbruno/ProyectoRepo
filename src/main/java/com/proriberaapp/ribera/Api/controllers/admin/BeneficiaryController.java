
package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.dto.InclubUserDto;

import com.proriberaapp.ribera.Domain.dto.BeneficiaryDto;
import com.proriberaapp.ribera.services.admin.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BeneficiaryController {
    // @GetMapping("/socios/{idUser}")
    // public Mono<InclubUserDto> getSocioById(@PathVariable Integer idUser) {
    //     return beneficiaryService.consultarSociosDesdeInclub("")
    //             .filter(socio -> socio.getIdUser() != null && socio.getIdUser().equals(idUser))
    //             .next();
    // }

    // Nuevo endpoint paginado y filtrado
    // Endpoint para obtener socios (con paginación y filtro)
    @GetMapping("/socios/page")
    public Flux<InclubUserDto> getSociosPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String nombre) {
        // Puedes implementar paginación real si lo necesitas
        return beneficiaryService.consultarSociosDesdeInclub(nombre)
                .skip(page * size)
                .take(size);
    }

    private final BeneficiaryService beneficiaryService;

    public static class SincronizarInclubRequest {
        public String username;
    }

    @PostMapping("/socios/search")
    public Flux<InclubUserDto> consultarSociosDesdeInclub(@RequestBody SincronizarInclubRequest request) {
        return beneficiaryService.consultarSociosDesdeInclub(request.username);
    }

    // Endpoint para obtener beneficiarios
    @GetMapping("/socios")
    public Flux<InclubUserDto> getAllSocios(@RequestParam(required = false) String nombre) {
        Flux<InclubUserDto> socios = beneficiaryService.consultarSociosDesdeInclub(nombre == null ? "" : nombre);
        return socios.doOnNext(socio -> System.out.println("[Controller] Socio recibido: " + socio))
                .doOnComplete(() -> System.out.println("[Controller] Fin de socios recibidos"));
    }

    @GetMapping("/socios/filter")
    public Flux<InclubUserDto> filterSocios(@RequestParam(required = false) String nombre) {
        return beneficiaryService.consultarSociosDesdeInclub(nombre);
    }

    // @GetMapping("/beneficiarios/{id}")
    // public Mono<BeneficiaryDto> getBeneficiaryById(@PathVariable Integer id) {
    // return beneficiaryService.getBeneficiaryById(id);
    // }

    @PostMapping("/beneficiarios")
    public Mono<BeneficiaryDto> createBeneficiary(@RequestBody BeneficiaryDto dto) {
        return beneficiaryService.createBeneficiary(dto);
    }

    @PutMapping("/beneficiarios/{id}")
    public Mono<BeneficiaryDto> updateBeneficiary(@PathVariable Integer id, @RequestBody BeneficiaryDto dto) {
        return beneficiaryService.updateBeneficiary(id, dto);
    }

    @PostMapping("/beneficiarios/{id}/checkin")
    public Mono<BeneficiaryDto> registrarCheckin(@PathVariable Integer id) {
        return beneficiaryService.registrarCheckin(id);
    }

    @DeleteMapping("/beneficiarios/{id}")
    public Mono<Void> deleteBeneficiary(@PathVariable Integer id) {
        return beneficiaryService.deleteBeneficiary(id);
    }

    // Endpoint para obtener beneficiarios por membresía
    @GetMapping("/membresias/{id}/beneficiarios")
    public Flux<BeneficiaryDto> getBeneficiariesByMembership(@PathVariable Integer id) {
        return beneficiaryService.getBeneficiariesByMembership(id);
    }

    // Endpoint para obtener membresías por socio
    @GetMapping("/socios/{idUser}/membresias")
    public Flux<com.proriberaapp.ribera.Domain.dto.MembershipResponse> getMembresiasByUser(
            @PathVariable Integer idUser) {
        String url = "https://adminpanelapi-dev.inclub.world/api/suscription/view/user/" + idUser;
        return beneficiaryService.getMembershipsByUser(url);
    }

    // Sugerencia: Endpoint para autocomplete de socios (puede ir en otro
    // controller)
    // @GetMapping("/socios/search")
    // public Flux<InclubUserDto> buscarSocios(@RequestParam String search) {
    // return beneficiaryService.buscarSocios(search);
    // }

    // Sugerencia: Endpoint para membresías por socio (puede ir en otro controller)
    // @GetMapping("/membresias/user/{idUser}")
    // public Flux<MembresiaDto> getMembresiasByUser(@PathVariable Integer idUser) {
    // return beneficiaryService.getMembresiasByUser(idUser);
    // }
}
