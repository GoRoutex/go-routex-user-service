package vn.com.routex.hub.user.service.domain.merchant.port;


import vn.com.routex.hub.user.service.domain.merchant.model.Merchant;

public interface MerchantRepositoryPort {

    Merchant save(Merchant merchant);

    boolean existsByCode(String code);

    String generateMerchantCode();
}
