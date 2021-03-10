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
import com.github.ki5fpl.tronj.crypto.SECP256K1;
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
    TronClient client = TronClient.ofShasta();
    BlockExtention block = client.blockingStub.getNowBlock2(EmptyMessage.newBuilder().build());

    System.out.println(block.getBlockHeader());
    assertTrue(block.getBlockHeader().getRawDataOrBuilder().getNumber() > 0);
  }

  @Test
  public void testTrxTransaction() {
    SECP256K1.KeyPair kp =
        TronClient.keyPairOfHex("3333333333333333333333333333333333333333333333333333333333333333");
    TronClient client = TronClient.ofNile();

    TransactionExtention txnExt =
        client.transfer("TJRabPrwbZy45sbavfcjinPJC18kjpRTv8", "TVjsyZ7fYF3qLF6BQgPmTEZy1xrNNyVAAA",
            10_000_000, "memo: this transaction is made in tronj testcase");
    System.out.println("txid: " + client.transactionIdOf(txnExt));

    Transaction signedTxn = client.signTransaction(txnExt, kp);

    System.out.println(signedTxn.toString());
    TransactionReturn ret = client.blockingStub.broadcastTransaction(signedTxn);
    System.out.println("======== Result ========\n" + ret.toString());
  }

  @Test
  public void testSendTrc20Transaction() {
    SECP256K1.KeyPair kp =
        TronClient.keyPairOfHex("3333333333333333333333333333333333333333333333333333333333333333");
    TronClient client = TronClient.ofNile();

    // transfer(address,uint256) returns (bool)
    Function trc20Transfer = new Function("transfer",
        Arrays.asList(new Address("TVjsyZ7fYF3qLF6BQgPmTEZy1xrNNyVAAA"),
            new Uint256(BigInteger.valueOf(10).multiply(BigInteger.valueOf(10).pow(18)))),
        Arrays.asList(new TypeReference<Bool>() {}));

    String encodedHex = FunctionEncoder.encode(trc20Transfer);

    TriggerSmartContract trigger =
        TriggerSmartContract.newBuilder()
            .setOwnerAddress(TronClient.parseAddress("TJRabPrwbZy45sbavfcjinPJC18kjpRTv8"))
            .setContractAddress(TronClient.parseAddress("TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3"))
            .setData(TronClient.parseHex(encodedHex))
            .build();

    System.out.println("trigger:\n" + trigger);

    TransactionExtention txnExt = client.blockingStub.triggerContract(trigger);
    Transaction txnWithFeeLimit = client.setFeeLimitOfTransaction(txnExt, 10_000_000);

    System.out.println("txid:\n" + client.transactionIdOf(txnWithFeeLimit));

    Transaction signedTxn = client.signTransaction(txnWithFeeLimit, kp);

    System.out.println(signedTxn.toString());
    TransactionReturn ret = client.blockingStub.broadcastTransaction(signedTxn);
    System.out.println("======== Result ========\n" + ret.toString());
  }

  @Test
  public void testGenerateAddress() {
    TronClient.generateAddress();
  }
}
