package util;

import java.security.MessageDigest;
import java.util.regex.Pattern;

public class ValidationUtil {

    // 1. Tên: Dùng chuẩn \p{L} của Java để nhận diện TOÀN BỘ chữ cái Tiếng Việt có dấu. Cho phép khoảng trắng. Độ dài từ 2 đến 100.
    private static final String NAME_REGEX = "^[\\p{L}\\s]{2,100}$";
    
    // 2. Email: Cho phép dấu +, ., _, %. Ép buộc phần tên email (trước @) phải từ 3 ký tự trở lên (Chặn 1@gmail.com). Hỗ trợ domain.co.uk
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]{3,64}@[A-Za-z0-9.-]+\\.[A-Za-z]{2,10}$";
    
    // 3. Số điện thoại: 10-11 số VN
    private static final String PHONE_REGEX = "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$";
    
    // 4. Mật khẩu MẠNH: 
    // - (?=.*[a-z]): Có ít nhất 1 chữ thường
    // - (?=.*[A-Z]): Có ít nhất 1 chữ hoa
    // - (?=.*\d): Có ít nhất 1 số
    // - (?=.*[^a-zA-Z0-9]): Có ít nhất 1 ký tự đặc biệt
    // - [\x21-\x7E]{8,50}: Dài từ 8-50 ký tự, CHỈ CHO PHÉP ASCII (Không dấu cách, không Tiếng Việt)
    private static final String PASS_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9])[\\x21-\\x7E]{8,50}$";
    
    // 5. Địa chỉ
    private static final String ADDRESS_REGEX = "^[a-zA-Z0-9À-ỹ\\s,./-]{0,255}$";

    public static boolean isValidName(String name) {
        return name != null && Pattern.matches(NAME_REGEX, name);
    }

    public static boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && Pattern.matches(PHONE_REGEX, phone);
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && Pattern.matches(PASS_REGEX, password);
    }
    
    public static boolean isSafeAddress(String address) {
        if (address == null || address.trim().isEmpty()) return true; 
        return Pattern.matches(ADDRESS_REGEX, address);
    }

    // Hash Mật Khẩu (Giữ nguyên)
    public static String hashPassword(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}