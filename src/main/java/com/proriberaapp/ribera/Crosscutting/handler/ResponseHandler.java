package com.proriberaapp.ribera.Crosscutting.handler;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class ResponseHandler {

    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String STATUS_KEY = "status";
    private static final String SUCCESS_KEY = "success";
    private static final String DATA_KEY = "data";
    private static final String MESSAGE_KEY = "message";

    /**
     * Genera una respuesta para un Mono o Flux
     *
     * @param status Estado HTTP de la respuesta
     * @param data Datos de la operación (Mono o Flux)
     * @param result true si la operación fue exitosa, false en caso contrario
     * @return Mono<ResponseEntity<Object>> Respuesta formateada
     */
    @SuppressWarnings("unchecked")
    public static Mono<ResponseEntity<Object>> generateResponse(HttpStatus status, Object data, boolean result) {
        System.out.println(">>> [DEBUG] Entrando a ResponseHandler.generateResponse con status: " + status + ", success: " + result + ", data: " + data);
        if (data != null) {
            System.out.println(">>> [DEBUG] Tipo de data: " + data.getClass().getName());
            if (data instanceof reactor.core.publisher.Flux) {
                Flux<?> flux = (Flux<?>) data;
                flux = flux.doOnNext(item -> System.out.println(">>> [DEBUG] Flux emitió: " + item))
                        .doOnComplete(() -> System.out.println(">>> [DEBUG] Flux completado"))
                        .doOnError(e -> System.out.println(">>> [DEBUG] Flux error: " + e));
                // Para evitar consumir el flujo antes de tiempo, solo logueamos, no suscribimos aquí
                data = flux;
            } else if (data instanceof reactor.core.publisher.Mono) {
                Mono<?> mono = (Mono<?>) data;
                mono = mono.doOnNext(item -> System.out.println(">>> [DEBUG] Mono emitió: " + item))
                        .doOnSuccess(item -> System.out.println(">>> [DEBUG] Mono completado con: " + item))
                        .doOnError(e -> System.out.println(">>> [DEBUG] Mono error: " + e));
                data = mono;
            }
        }
        if (data instanceof Mono) {
            return generateMonoResponse(status, (Mono<Object>) data, result);
        } else if (data instanceof Flux) {
            return generateFluxResponse(status, (Flux<Object>) data, result);
        } else {
            return generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, new IllegalArgumentException("Unsupported data type"));
        }
    }

    /**
     * Genera una respuesta con paginación
     */
    public static <T> Mono<ResponseEntity<Object>> generateResponseWithPagination(HttpStatus status, Flux<T> data, boolean result, int page, int size, Long total) {
        return data.collectList().map(list -> {
            Map<String, Object> response = new HashMap<>();
            response.put("data", list);
            response.put("page", page);
            response.put("size", size);
            response.put("total", total);
            response.put("success", result);
            return new ResponseEntity<>(response, status);
        });
    }

    private static Mono<ResponseEntity<Object>> generateMonoResponse(HttpStatus status, Mono<Object> data, boolean result) {
        Map<String, Object> map = new HashMap<>();

        try {
            return data.flatMap(d -> {
                map.put(TIMESTAMP_KEY, formatTimestamp(new Date()));
                map.put(STATUS_KEY, status.value());
                map.put(SUCCESS_KEY, result);
                map.put(DATA_KEY, d);
                return Mono.just(new ResponseEntity<>(map, status));
            });
        } catch (Exception exception) {
            log.error("Error while building response", exception);
            map.clear();
            return generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception);
        }
    }

    private static Mono<ResponseEntity<Object>> generateFluxResponse(HttpStatus status, Flux<Object> data, boolean result) {
        Map<String, Object> map = new HashMap<>();

        try {
            return data.collectList().flatMap(list -> {
                map.put(TIMESTAMP_KEY, formatTimestamp(new Date()));
                map.put(STATUS_KEY, status.value());
                map.put(SUCCESS_KEY, result);
                map.put(DATA_KEY, list);
                return Mono.just(new ResponseEntity<>(map, status));
            });
        } catch (Exception exception) {
            log.error("Error while building response", exception);
            map.clear();
            return generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception);
        }
    }

    private static String formatTimestamp(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return dateFormat.format(date);
    }

    private static Mono<ResponseEntity<Object>> generateErrorResponse(HttpStatus status, Exception exception) {
        Map<String, Object> map = new HashMap<>();
        map.put(TIMESTAMP_KEY, new Date());
        map.put(STATUS_KEY, status.value());
        map.put(SUCCESS_KEY, false);
        map.put(MESSAGE_KEY, exception.getMessage());
        map.put(DATA_KEY, null);
        return Mono.just(new ResponseEntity<>(map, status));
    }

    /**
     * Genera una respuesta simple para objetos directos
     */
    public static Mono<ResponseEntity<Object>> generateResponseSimple(HttpStatus status, Object data, boolean result) {
        Map<String, Object> map = new HashMap<>();
        map.put(TIMESTAMP_KEY, formatTimestamp(new Date()));
        map.put(STATUS_KEY, status.value());
        map.put(SUCCESS_KEY, result);
        map.put(DATA_KEY, data);
        return Mono.just(new ResponseEntity<>(map, status));
    }
}
