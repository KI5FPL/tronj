package com.github.ki5fpl.tronj.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ki5fpl.tronj.abi.FunctionEncoder;
import com.github.ki5fpl.tronj.abi.TypeReference;
import com.github.ki5fpl.tronj.abi.datatypes.*;
import com.github.ki5fpl.tronj.abi.datatypes.generated.Bytes10;
import com.github.ki5fpl.tronj.abi.datatypes.generated.Uint256;
import com.github.ki5fpl.tronj.abi.datatypes.generated.Uint32;
import com.github.ki5fpl.tronj.api.GrpcAPI.EmptyMessage;
import com.github.ki5fpl.tronj.client.TronClient;
import com.github.ki5fpl.tronj.proto.Chain.Transaction;
import com.github.ki5fpl.tronj.proto.Contract.TriggerSmartContract;
import com.github.ki5fpl.tronj.proto.Response.BlockExtention;
import com.github.ki5fpl.tronj.proto.Response.TransactionExtention;
import com.github.ki5fpl.tronj.proto.Response.TransactionReturn;
import com.github.ki5fpl.tronj.utils.Base58Check;
import com.google.protobuf.ByteString;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

public class ClientTest {
    @Test
    public void testGetNowBlockQuery() {
        TronClient client = TronClient.ofShasta("3333333333333333333333333333333333333333333333333333333333333333");
        BlockExtention block = client.blockingStub.getNowBlock2(EmptyMessage.newBuilder().build());

        System.out.println(block.getBlockHeader());
        assertTrue(block.getBlockHeader().getRawDataOrBuilder().getNumber() > 0);
    }

    @Test
    public void testSendTrc20Transaction() {
        TronClient client = TronClient.ofNile("3333333333333333333333333333333333333333333333333333333333333333");

        byte[] rawFrom = Base58Check.base58ToBytes("TJRabPrwbZy45sbavfcjinPJC18kjpRTv8");
        // JST of Nile testnet
        byte[] rawContract = Base58Check.base58ToBytes("TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3");

        // transfer(address,uint256) returns (bool)
        Function trc20Transfer = new Function("transfer",
            Arrays.asList(new Address("TVjsyZ7fYF3qLF6BQgPmTEZy1xrNNyVAAA"),
                new Uint256(BigInteger.valueOf(10).multiply(BigInteger.valueOf(10).pow(18)))),
            Arrays.asList(new TypeReference<Bool>() {}));

        String encodedHex = FunctionEncoder.encode(trc20Transfer);
        byte[] rawData = Hex.decode(encodedHex);

        TriggerSmartContract trigger = TriggerSmartContract.newBuilder()
                                           .setOwnerAddress(ByteString.copyFrom(rawFrom))
                                           .setContractAddress(ByteString.copyFrom(rawContract))
                                           .setData(ByteString.copyFrom(rawData))
                                           .build();

        System.out.println("trigger:\n" + trigger);

        TransactionExtention txnExt = client.blockingStub.triggerContract(trigger);
        System.out.println("txn id => " + Hex.toHexString(txnExt.getTxid().toByteArray()));

        Transaction signedTxn = client.signTransaction(txnExt);

        System.out.println(signedTxn.toString());
        TransactionReturn ret = client.blockingStub.broadcastTransaction(signedTxn);
        System.out.println("======== Result ========\n" + ret.toString());
    }

    @Test
    public void testGenerateAddress() {
        TronClient.generateAddress();
    }
}
