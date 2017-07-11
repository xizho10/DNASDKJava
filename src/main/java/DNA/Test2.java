package DNA;

import DNA.Core.StateUpdateTransaction;
import DNA.Core.Transaction;
import DNA.Wallets.Account;
import DNA.Wallets.Contract;
import DNA.sdk.info.account.AccountAsset;
import DNA.sdk.info.account.AccountInfo;
import DNA.sdk.wallet.UserWalletManager;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by zx on 2017/7/11.
 */
public class Test2 {

    public static void main(String[] args) throws Exception {
//        String jsonMessage = "{\"hello\":\"world\",\"Data\":10}";
//        JSONObject string_to_json = JSONObject.parseObject(jsonMessage);
        //JSONObject json_to_data = string_to_json.t;
//        System.out.println(string_to_json);
//        System.exit(0);
        // 打开账户管理器
        String path = "wallet.dat";
        String url = "http://139.196.115.69:20334";
        String accessToken = "";                // 非必需项，如开启OAuth认证，则需要填写
        UserWalletManager wm = UserWalletManager.getWallet(path, url, accessToken);
        Contract[] contracts = wm.getWallet().loadContracts();
        if (contracts.length == 1) {
            List<String> list = wm.createAccount(2);
            String addr = wm.createAccount(Helper.hexToBytes("d46f336479abfef0fa4bcfbaa0268138b39fe5e55fc0f2b529ca2e8eb2ba9d91"));//pubkey 02d1708024559a9a83028c25c7f0bf12b0a01ea8ea9cff077748bc54d624f62f65
            System.out.println("stateupdater addr:"+addr);
            contracts = wm.getWallet().loadContracts();
        }
       //  System.exit(0);
        System.out.println("Addr Num:" + contracts.length);

        AccountInfo acct1 = wm.getAccountInfo(contracts[0].address());
        AccountInfo acct2 = wm.getAccountInfo(contracts[1].address());
        AccountInfo acct3 = wm.getAccountInfo(contracts[2].address());
        AccountInfo acct4 = wm.getAccountInfo(contracts[3].address());

        for (Contract c : contracts) {
            AccountInfo acct = wm.getAccountInfo(c.address());
            AccountAsset asset = wm.getAccountAsset(acct.address);
            System.out.println(acct.address + "   " + asset.canUseAssets);
        }
        //System.exit(0);

        String issuer = acct1.address;    // 资产发行者地址
        String name = (new Date()).toString();            // 资产名称
        long amount = 10000;        // 资产数量
        String desc = "hh";            // 描述
        String controller = acct1.address;    // 资产控制者地址
        int precision = 8;            // 精度
        boolean rr = false;
        Transaction tx;
        String txHex;

        name = "{\"Date\":\""+(new Date()).toString()+"\",\"Data\":10}";
        //状态更新
        Account acc = wm.getAccount(contracts[3].address());
        System.out.println("StateUpdateTransaction pubkey:" + acct4.pubkey);
        tx = wm.createStateUpdate("ts01","ts02",name,acc.publicKey);
        txHex = wm.signTx(tx);
        rr = wm.sendTx(txHex);
        System.out.println("StateUpdateTransaction:" + rr + ",txid:" + tx.hash().toString());
        //System.exit(0);

        //注册
        tx = wm.createRegTx(acct1.address, name, amount, desc, acct1.address, precision);
        txHex = wm.signTx(tx);
        rr = wm.sendTx(txHex);
        Thread.sleep(7000);
        System.out.println("RegTx:" + rr + ",txid:" + tx.hash().toString());

        String assetid = tx.hash().toString();

        //签发
        tx = wm.createIssTx(acct1.address, assetid, 100, acct2.address, desc);
        txHex = wm.signTx(tx);
        rr = wm.sendTx(txHex);
        Thread.sleep(7000);
        System.out.println("IssTx1:" + rr + ",txid:" + tx.hash().toString());

        //签发
        tx = wm.createIssTx(acct1.address, assetid, 100, acct3.address, desc);
        txHex = wm.signTx(tx);
        rr = wm.sendTx(txHex);
        Thread.sleep(7000);
        System.out.println("IssTx2:" + rr + ",txid:" + tx.hash().toString());

        //System.exit(0);

        //转移
        tx = wm.createTrfTx(acct2.address, assetid, 10, acct3.address, desc);
        txHex = wm.signTx(tx);
        rr = wm.sendTx(txHex);
        Thread.sleep(7000);
        System.out.println("TrfTx:" + rr + ",txid:" + tx.hash().toString());




    }
}
