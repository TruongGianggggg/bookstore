package org.example.cuahangsach.service;



import org.example.cuahangsach.model.VNPAYConfig;
import org.example.cuahangsach.model.VNPAYUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VNPAYService {

    public String createPaymentUrl(long amount, String ipAddress) {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", VNPAYConfig.VNP_TMNCODE);
        vnpParams.put("vnp_Amount", String.valueOf(amount * 100)); // VNPay yêu cầu số tiền phải tính bằng đồng
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));
        vnpParams.put("vnp_OrderInfo", "Payment for order " + vnpParams.get("vnp_TxnRef"));
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", VNPAYConfig.VNP_RETURNURL);
        vnpParams.put("vnp_IpAddr", ipAddress);
        vnpParams.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        // Tạo URL thanh toán
        return VNPAYUtils.buildPaymentUrl(vnpParams);
    }
}

