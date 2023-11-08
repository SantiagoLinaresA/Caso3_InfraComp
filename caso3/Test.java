import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest("aaaaaaaaaaaaaaaaaaaannv".getBytes());
        bytesToBinary(hashBytes);
    }
        

    private static String bytesToBinary(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        int count = 0;
        boolean counting = true;
        for (byte b : bytes) {
            for (int i = 7; i >= 0; i--) {
                int bit = (b >> i) & 1;
                if (bit == 0 && counting) count++;
                else counting = false;
                result.append((b >> i) & 1);
            }
        }
        System.out.println(count);
        return result.toString();
    }

    // private static void bytesToHex(byte[] bytes) {
    //     int zeros = 24;
    //     int count = 0;
    //     int i = 0;
    //     boolean flag = false;
    //     while (i < bytes.length && !flag){
    //         String hex = String.format("%02x", bytes[i++]);
    //         if (hex.charAt(0)!='0') flag = true;
    //         int j = 0;
    //         while (j < hex.length() && !flag) {
    //             char c = hex.charAt(j++);
    //             if (c == '0' || c == '1') {
    //                 count += (c == '0') ? 4 : 0;
    //             } else if (c == 1){
    //                 count += 3;
    //                 flag = true;
    //             }
    //             else if (c == '2' || c == '3' || c == '6' || c == '7') {
    //                 count += 2;
    //                 flag = true;
    //             } else if (c == '4' || c == '5' || c == 'c' || c == 'd') {
    //                 count += 1;
    //                 flag = true;
    //             }
    //             else{
    //                 flag = true;
    //             }
    //         }
    //     }
    //     System.out.println(count);
    //     System.out.println(zeros);
    //     System.out.println(count >= zeros);
    // }
}
