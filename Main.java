import java.util.*;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Main {
    static Scanner sc = new Scanner(System.in);

    BigInteger key1;
    BigInteger key2;
    BigInteger n;
    int bitSize;
    SecureRandom random = new SecureRandom();

    public BigInteger generatePrime() {
        bitSize = 34;// ?34
        while (true) {
            BigInteger randomBigInteger = new BigInteger(bitSize, random);
            if (randomBigInteger.isProbablePrime(15)) {
                return randomBigInteger;
            }
        }
    }

    public void setKeys() {
        BigInteger prime1 = generatePrime();
        BigInteger prime2 = generatePrime();
        while (prime1.equals(prime2)) {
            prime2 = generatePrime();

        }
        System.out.println("Prime 1 : " + prime1.toString());
        System.out.println("Prime 2 : " + prime2.toString());

        n = prime1.multiply(prime2);
        System.out.println("N: " + n.toString());
        BigInteger phi = (prime1.subtract(new BigInteger("1"))).multiply(prime2.subtract(new BigInteger("1")));
        BigInteger e = new BigInteger("2");

        while (true) {
            if ((e.gcd(phi)).compareTo(new BigInteger("1")) == 0)
                break;
            e = e.add(new BigInteger("1"));
        }
        System.out.println("e : " + e.toString());
        key1 = e;

        BigInteger d = e.modInverse(phi);
        System.out.println("d : " + d.toString());

        key2 = d;
    }

    public BigInteger encrypt(BigInteger message) {
        return message.modPow(key1, n);
    }

    public BigInteger decrypt(BigInteger encryptedText) {
        return encryptedText.modPow(key2, n);
    }

    public List<BigInteger> encoder(String message) {
        List<BigInteger> msg = new ArrayList<>();
        for (char letter : message.toCharArray()) {
            BigInteger charValue = BigInteger.valueOf((long) letter);
            BigInteger encryptedChar = encrypt(charValue);
            msg.add(encryptedChar);
        }
        return msg;
    }

    public String decoder(List<BigInteger> encoded) {
        StringBuilder s = new StringBuilder();
        for (BigInteger encryptedChar : encoded) {
            BigInteger decryptedChar = decrypt(encryptedChar);
            s.append((char) decryptedChar.longValue());
        }
        return s.toString();
    }

    // !!!BREAK
    public void StrengthTest() {
        setKeys();
        String s = "Checking The Strength of Algorithm";
        List<BigInteger> v = encoder(s);
        long startTime = System.currentTimeMillis();
        breakEncryption(n, key1, v, s);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

    }

    BigInteger decrypt(BigInteger encrpyted_text, BigInteger key2, BigInteger n) {
        BigInteger d = key2;
        BigInteger decrypted = encrpyted_text.modPow(d, n);
        return decrypted;
    }

    public String decoder(List<BigInteger> encoded, BigInteger d, BigInteger n) {
        StringBuilder s = new StringBuilder();
        for (BigInteger encryptedChar : encoded) {
            BigInteger decryptedChar = decrypt(encryptedChar, d, n);
            s.append((char) decryptedChar.longValue());
        }
        return s.toString();
    }

    void breakEncryption(BigInteger n, BigInteger e, List<BigInteger> cipher, String txt) {
        // ArrayList<BigInteger> pr = generatePrimes(n);
        // long p = pr.get(0);
        // long q = pr.get(1);
        // for(BigInteger i=new BigInteger("2");)
        System.out.println("N: " + n.toString());
        BigInteger p = new BigInteger("2");
        while (p.compareTo(n) <= 0) {
            if (n.remainder(p).equals(BigInteger.ZERO)) {
                break;
                // System.out.println("Prime divisor found: " + divisor);
                // number = number.divide(divisor);
            } else {
                p = p.add(BigInteger.ONE);
            }
        }
        System.out.println("P : " + p.toString());
        BigInteger q = n.divide(p);
        System.out.println("Q : " + q.toString());
        // q = n.divide(p);
        System.out.println(p + " " + q);
        // if (p == n || q == n) {
        // System.out.println("Error");
        // return;
        // }
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger k = new BigInteger("1");
        BigInteger d;
        String s;
        BigInteger nm;
        while (true) {

            nm = BigInteger.ONE.add(k.multiply(phi));
            d = nm.divide(e);
            System.out.println("d : " + d);
            s = decoder(cipher, d, n);
            if (s.equals(txt)) {
                System.out.println(d + " " + k);
                System.out.println("Text : " + s);
                break;
            }
            k = k.add(BigInteger.ONE);
        }
    }

    public static void main(String[] args) {
        Main rsa = new Main();
        rsa.StrengthTest();
        // rsa.setKeys();
        // /*
        // * String s = "Hi There";
        // * List<BigInteger> v = rsa.encoder(s);
        // * System.out.println("Encrypted Message : ");
        // * v.forEach((elem) -> System.out.print(elem + " "));
        // * System.out.println("\nDecrypted Message : ");
        // * String sh = rsa.decoder(v);
        // * System.out.println(sh);
        // */
        // // !Custom Check

        // System.out.println("Enter Text to be Encrypted : ");
        // String s = sc.nextLine();
        // List<BigInteger> v = rsa.encoder(s);
        // System.out.println("\nEncrypted Message : ");
        // v.forEach((elem) -> System.out.print(elem + " "));
        // System.out.println("\n\nDecrypted Message : ");
        // String sh = rsa.decoder(v);
        // rsa.StrengthTest(v);
        // System.out.println(sh);

    }

}
