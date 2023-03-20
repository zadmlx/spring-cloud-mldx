package individual.me.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    @Autowired
    private PasswordEncoder passwordEncoder;


    public boolean validate(String rawPassword,String jdbcPassword){
        return passwordEncoder.matches(rawPassword,jdbcPassword);
    }

    public String encode(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }
}
