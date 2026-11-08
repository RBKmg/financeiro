package com.ricardo.financeiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RestController
public class DebugController {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @GetMapping("/debug/mappings")
    public String mappings() {

        return handlerMapping.getHandlerMethods()
                .entrySet()
                .stream()
                .filter(entry ->
                    entry.getKey().toString().contains("/categorias"))
                .map(entry ->
                    entry.getKey() + " -> " + entry.getValue())
                .sorted()
                .collect(java.util.stream.Collectors.joining("\n"));
    }
}
