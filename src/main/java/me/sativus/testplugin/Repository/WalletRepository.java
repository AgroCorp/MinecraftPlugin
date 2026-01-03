package me.sativus.testplugin.Repository;

import me.sativus.testplugin.DAO.Wallet;

public class WalletRepository extends BaseRepository<Wallet> {
    public WalletRepository() {
        super(Wallet.class);
    }

}
