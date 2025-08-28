package com.br.keyroom.repository;

import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Repository
public class PwnedSecretRepository {
    public boolean isPasswordPwned(String prefix, String suffix) {
        try {
            URL url = new URL("https://api.pwnedpasswords.com/range/" + prefix);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length > 1 && parts[0].equalsIgnoreCase(suffix)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // Log exception
        }
        return false;
    }
}
