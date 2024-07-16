package com.alura.challengejavalivros.interfaces;

public interface IconverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
