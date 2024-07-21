#include <iostream>
#include <vector>
#include <string>
#include <cmath>
#include <ctime>
#include <cstdlib>

using namespace std;

class RSA {
private:
    int bitSize;
    unsigned long long e, d, n;

    bool isPrime(int num) {
        if (num <= 1) return false;
        for (int i = 2; i <= sqrt(num); i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    unsigned long long generatePrime() {
        while (true) {
            unsigned long long randomNum = rand() % (1 << bitSize) + 1;
            if (isPrime(randomNum)) return randomNum;
        }
    }

    unsigned long long modExp(unsigned long long base, unsigned long long exp, unsigned long long mod) {
        unsigned long long result = 1;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exp /= 2;
        }
        return result;
    }

public:
    RSA(int bitSize) : bitSize(bitSize) {
        srand(time(0));
    }

    void setKeys() {
        unsigned long long prime1 = generatePrime();
        unsigned long long prime2 = generatePrime();
        while (prime1 == prime2) prime2 = generatePrime();

        cout << "Prime 1: " << prime1 << endl;
        cout << "Prime 2: " << prime2 << endl;

        n = prime1 * prime2;
        cout << "N: " << n << endl;

        unsigned long long phi = (prime1 - 1) * (prime2 - 1);
        e = 2;

        while (true) {
            if (gcd(e, phi) == 1) break;
            e++;
        }
        cout << "e: " << e << endl;

        d = modInverse(e, phi);
        cout << "d: " << d << endl;
    }

    unsigned long long gcd(unsigned long long a, unsigned long long b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    unsigned long long modInverse(unsigned long long a, unsigned long long m) {
        unsigned long long m0 = m;
        unsigned long long y = 0, x = 1;

        if (m == 1) return 0;

        while (a > 1) {
            unsigned long long q = a / m;
            unsigned long long t = m;

            m = a % m;
            a = t;
            t = y;

            y = x - q * y;
            x = t;
        }

        if (x < 0) x += m0;

        return x;
    }

    vector<unsigned long long> encoder(string message) {
        vector<unsigned long long> msg;
        for (char letter : message) {
            unsigned long long charValue = (unsigned long long)letter;
            unsigned long long encryptedChar = modExp(charValue, e, n);
            msg.push_back(encryptedChar);
        }
        return msg;
    }

    string decoder(vector<unsigned long long> encoded) {
        string s;
        for (unsigned long long encryptedChar : encoded) {
            unsigned long long decryptedChar = modExp(encryptedChar, d, n);
            s += (char)decryptedChar;
        }
        return s;
    }

    void StrengthTest(vector<unsigned long long> encoded, string original) {
        clock_t start = clock();
        breakEncryption(encoded, original);
        clock_t end = clock();
        cout << "Time taken: " << (double)(end - start) / CLOCKS_PER_SEC << " seconds" << endl;
    }

    void breakEncryption(vector<unsigned long long> cipher, string txt) {
        unsigned long long p = 2;
        while (p <= n) {
            if (n % p == 0) break;
            p++;
        }
        unsigned long long q = n / p;
        cout << "P: " << p << endl;
        cout << "Q: " << q << endl;

        unsigned long long phi = (p - 1) * (q - 1);
        unsigned long long k = 1;
        unsigned long long d;
        string s;
        while (true) {
            unsigned long long nm = 1 + k * phi;
            d = nm / e;
            s = decoder(cipher, d, n);
            if (s == txt) {
                cout << "d: " << d << endl;
                cout << "Text: " << s << endl;
                break;
            }
            k++;
        }
    }

    string decoder(vector<unsigned long long> encoded, unsigned long long d, unsigned long long n) {
        string s;
        for (unsigned long long encryptedChar : encoded) {
            unsigned long long decryptedChar = modExp(encryptedChar, d, n);
            s += (char)decryptedChar;
        }
        return s;
    }
};

int main() {
    RSA rsa(10); // Adjust bit size to 10 for easier demonstration
    rsa.setKeys();
    cout << "Enter text to be encrypted: ";
    string s;
    getline(cin, s);
    vector<unsigned long long> v = rsa.encoder(s);
    cout << "Encrypted message: ";
    for (unsigned long long elem : v) cout << elem << " ";
    cout << endl;
    cout << "Decrypted message: " << rsa.decoder(v) << endl;
    rsa.StrengthTest(v, s);
    return 0;
}
