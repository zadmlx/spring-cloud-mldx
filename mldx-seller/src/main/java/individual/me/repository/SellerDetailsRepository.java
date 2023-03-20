package individual.me.repository;

import individual.me.domain.Seller;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SellerDetailsRepository {

    Seller loadSellerBySellerName(String sellerName);
}
