package com.example.chatapp.activities;

import java.util.HashMap;
import java.util.Map;

public class DoSProtection {

    private static final long REQUEST_INTERVAL = 10000; // Intervalle de temps en millisecondes (10 secondes ici)
    private static final int MAX_REQUESTS_PER_IP = 5; // Nombre maximal de requêtes autorisées dans l'intervalle

    private Map<String, Long> requestCountMap = new HashMap<>();

    public boolean isAllowed(String ipAddress) {
        long currentTime = System.currentTimeMillis();

        // Vérifier si l'adresse IP est dans la carte
        if (requestCountMap.containsKey(ipAddress)) {
            // L'adresse IP est présente dans la carte, vérifier l'intervalle de temps
            long lastRequestTime = requestCountMap.get(ipAddress);
            if (currentTime - lastRequestTime < REQUEST_INTERVAL) {
                // L'intervalle de temps n'est pas écoulé, vérifier le nombre de requêtes
                int requestCount = getRequestCount(ipAddress);
                if (requestCount >= MAX_REQUESTS_PER_IP) {
                    // Bloquer l'accès si le nombre de requêtes dépasse la limite
                    return false;
                }
            } else {
                // L'intervalle de temps est écoulé, réinitialiser le compteur
                resetRequestCount(ipAddress);
            }
        }

        // Enregistrer la nouvelle requête
        recordRequest(ipAddress, currentTime);

        // L'accès est autorisé
        return true;
    }

    private int getRequestCount(String ipAddress) {
        return Math.toIntExact(requestCountMap.getOrDefault(ipAddress, Long.valueOf(0)));
    }

    private void resetRequestCount(String ipAddress) {
        requestCountMap.put(ipAddress, 1L);
    }

    private void recordRequest(String ipAddress, long currentTime) {
        requestCountMap.put(ipAddress, currentTime);
    }
}