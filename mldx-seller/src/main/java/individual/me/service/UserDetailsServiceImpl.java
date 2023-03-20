package individual.me.service;

import individual.me.domain.SecurityUser;
import individual.me.domain.Seller;
import individual.me.repository.SellerDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SellerDetailsRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Seller seller = repository.loadSellerBySellerName(username);
        if (null == seller) throw new UsernameNotFoundException("用户不存在");
        return new SecurityUser(seller);
    }
}
