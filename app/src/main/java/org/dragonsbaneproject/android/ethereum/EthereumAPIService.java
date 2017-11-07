package org.dragonsbaneproject.android.ethereum;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import de.petendi.ethereum.android.Contracts;
import de.petendi.ethereum.android.EthereumAndroid;
import de.petendi.ethereum.android.EthereumAndroidFactory;
import de.petendi.ethereum.android.EthereumNotInstalledException;
import de.petendi.ethereum.android.service.model.RpcCommand;
import de.petendi.ethereum.android.service.model.WrappedRequest;
import de.petendi.ethereum.android.service.model.WrappedResponse;

/**
 * Ethereum API Access
 *
 * https://medium.com/@pacs_IT/an-ethereum-api-for-android-app-developers-3f46b820f8f6
 * https://github.com/p-acs/ethereum-android-lib
 * https://github.com/ethereum/wiki/wiki/JSON-RPC
 */

public class EthereumAPIService extends IntentService {

    // Actions
    public static final String CHECK_BALANCE = "dragonsbane.ether.CHECK_BALANCE";
    public static final String BALANCE_RECEIVED = "dragonsbane.ether.BALANCE_RECEIVED";
    public static final String LIST_CONTRACTS = "dragonsbane.ether.LIST_CONTRACTS";
    public static final String CONTRACTS_FOUND = "dragonsbane.ether.CONTRACTS_FOUND";

    // Data
    public static final String ETHER_ACCOUNT = "dragonsbane.ether.ACCOUNT";
    public static final String ETHER_BALANCE = "dragonsbane.ether.BALANCE";
    public static final String CONTRACTS = "dragonsbane.ether.CONTRACTS";

    private EthereumAndroid ethereumAndroid;

    public EthereumAPIService() {
        super(EthereumAPIService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        switch(intent.getAction()) {
            case CHECK_BALANCE: { checkBalance(intent.getStringExtra(ETHER_ACCOUNT));break; }
            case LIST_CONTRACTS: { listContracts();break; }
            default: {
                Log.w(EthereumAPIService.class.getName(),"EthereumAPIService action not supported: "+intent.getAction());
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EthereumAndroidFactory ethereumAndroidFactory = new EthereumAndroidFactory(this);
        try {
            ethereumAndroid = ethereumAndroidFactory.create();
        } catch (EthereumNotInstalledException e) {
            //let the user install Ethereum
            ethereumAndroidFactory.showInstallationDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ethereumAndroid!=null)
            ethereumAndroid.release();
    }

    private void checkBalance(String ethereumAccount) {
        WrappedRequest request = new WrappedRequest();
        request.setCommand(RpcCommand.eth_getBalance.toString());
        request.setParameters(new String[]{ethereumAccount,"latest"});
        WrappedResponse response = ethereumAndroid.send(request);
        if(response.isSuccess()) {
            // Balance gets returned in hexadecimal format
            String balanceHex = (String)response.getResponse();
            // Convert to decimal
            int balance = Integer.parseInt(balanceHex, 16);
            Intent out = new Intent(BALANCE_RECEIVED);
            out.putExtra(ETHER_BALANCE, balance);
            LocalBroadcastManager.getInstance(this).sendBroadcast(out);
        } else {
            Log.w(EthereumAPIService.class.getName(),"Error retrieving Ether Balance: "+response.getErrorMessage());
        }
    }

    private void listContracts() {
        Contracts contracts = ethereumAndroid.contracts();
        // TODO:
    }
}
