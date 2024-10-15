package org.example.cuahangsach.model;


import java.net.URLEncoder;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VNPAYUtils {

    public static String generateSecureHash(String secretKey, String data) {
        try {
            byte[] hmacData;
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(secretKeySpec);
            hmacData = mac.doFinal(data.getBytes());
            StringBuilder hash = new StringBuilder();
            for (byte b : hmacData) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating secure hash", e);
        }
    }

    public static String buildPaymentUrl(Map<String, String> vnpParams) {
        StringBuilder queryString = new StringBuilder();
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();

        try {
            for (String fieldName : fieldNames) {
                String value = vnpParams.get(fieldName);
                if (value != null && value.length() > 0) {
                    // Xây dựng chuỗi ký hash
                    hashData.append(fieldName).append("=").append(value).append("&");
                    // Xây dựng chuỗi query
                    queryString.append(URLEncoder.encode(fieldName, "UTF-8"))
                            .append("=")
                            .append(URLEncoder.encode(value, "UTF-8"))
                            .append("&");
                }
            }

            String rawHash = hashData.substring(0, hashData.length() - 1);
            String secureHash = generateSecureHash(VNPAYConfig.VNP_HASHSECRET, rawHash);
            String paymentUrl = VNPAYConfig.VNP_URL + "?" + queryString + "vnp_SecureHash=" + secureHash;
            return paymentUrl;
        } catch (Exception e) {
            throw new RuntimeException("Error building payment URL", e);
        }
    }
}

