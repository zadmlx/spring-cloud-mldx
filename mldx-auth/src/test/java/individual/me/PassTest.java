package individual.me;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PassTest {
    public static void main(String[] args) {

        String pass = "123456";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(pass);
        System.out.println(encode);

        System.out.println("***************");
        BCryptPasswordEncoder p2 = new BCryptPasswordEncoder();

        String encoded = "$2a$10$y.RqP78.1RiBVlW9jCGRN.sGMMxjvzoXbvlQCnMza1ctD/u3CRfzO";
        boolean matches = p2.matches(pass, encoded);
        System.out.println(matches);

    }
}
