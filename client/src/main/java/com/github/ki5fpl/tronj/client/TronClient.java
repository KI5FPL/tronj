package com.github.ki5fpl.tronj.client;

import com.github.ki5fpl.tronj.api.WalletGrpc;
import com.github.ki5fpl.tronj.crypto.SECP256K1;
import com.github.ki5fpl.tronj.key.KeyPair;
import com.github.ki5fpl.tronj.proto.Chain.Transaction;
import com.github.ki5fpl.tronj.proto.Contract.TransferAssetContract;
import com.github.ki5fpl.tronj.proto.Contract.TransferContract;
import com.github.ki5fpl.tronj.proto.Response.TransactionExtention;
import com.github.ki5fpl.tronj.proto.Response.TransactionReturn;
import com.github.ki5fpl.tronj.utils.Base58Check;
import com.google.protobuf.ByteString;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import org.apache.tuweni.bytes.Bytes32;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.util.encoders.Hex;

public class TronClient {
  public final WalletGrpc.WalletBlockingStub blockingStub;
  public final long feeLimit = 10_000_000; // 10_TRX

  public TronClient(String grpcEndpoint) {
    ManagedChannel channel = ManagedChannelBuilder.forTarget(grpcEndpoint).usePlaintext().build();
    blockingStub = WalletGrpc.newBlockingStub(channel);
  }

  public TronClient(Channel channel) {
    blockingStub = WalletGrpc.newBlockingStub(channel);
  }

  public static TronClient ofMainnet() {
    return new TronClient("grpc.trongrid.io:50051");
  }

  public static TronClient ofShasta() {
    return new TronClient("grpc.shasta.trongrid.io:50051");
  }

  public static TronClient ofNile() {
    return new TronClient("47.252.19.181:50051");
  }

  public static KeyPair keyPairOfHex(String hexPrivateKey) {
    return KeyPair.of(hexPrivateKey);
  }

  public static String generateAddress() {
    // generate random address
    SECP256K1.KeyPair kp = SECP256K1.KeyPair.generate();

    SECP256K1.PublicKey pubKey = kp.getPublicKey();
    Keccak.Digest256 digest = new Keccak.Digest256();
    digest.update(pubKey.getEncoded(), 0, 64);
    byte[] raw = digest.digest();
    byte[] rawAddr = new byte[21];
    rawAddr[0] = 0x41;
    System.arraycopy(raw, 12, rawAddr, 1, 20);

    System.out.println("Base58Check: " + Base58Check.bytesToBase58(rawAddr));
    System.out.println("Hex Address: " + Hex.toHexString(rawAddr));
    System.out.println("Public Key:  " + Hex.toHexString(pubKey.getEncoded()));
    System.out.println("Private Key: " + Hex.toHexString(kp.getPrivateKey().getEncoded()));

    return Hex.toHexString(kp.getPrivateKey().getEncoded());
  }

  public static String toBase58Address(String address) {
    ByteString rawAddress = parseAddress(address);
    return Base58Check.bytesToBase58(rawAddress.toByteArray());
  }

  public static String toHexAddress(String address) {
    ByteString rawAddress = parseAddress(address);
    return Hex.toHexString(rawAddress.toByteArray());
  }

  public static ByteString parseAddress(String address) {
    if (address.startsWith("T")) {
      byte[] raw = Base58Check.base58ToBytes(address);
      return ByteString.copyFrom(raw);
    } else if (address.startsWith("41")) {
      byte[] raw = Hex.decode(address);
      return ByteString.copyFrom(raw);
    } else if (address.startsWith("0x")) {
      byte[] raw = Hex.decode(address.substring(2));
      return ByteString.copyFrom(raw);
    } else {
      throw new IllegalArgumentException("Invalid address: " + address);
    }
  }

  public static ByteString parseHex(String hexString) {
    byte[] raw = Hex.decode(hexString);
    return ByteString.copyFrom(raw);
  }

  public static String toHex(byte[] raw) {
    return Hex.toHexString(raw);
  }

  public static String toHex(ByteString raw) {
    return Hex.toHexString(raw.toByteArray());
  }

  public static String transactionIdOf(Transaction txn) {
    SHA256.Digest digest = new SHA256.Digest();
    digest.update(txn.getRawData().toByteArray());
    byte[] txid = digest.digest();
    return toHex(txid);
  }

  public static String transactionIdOf(TransactionExtention txnExt) {
    return transactionIdOf(txnExt.getTransaction());
  }

  public static Transaction signTransaction(TransactionExtention txnExt, KeyPair kp) {
    return kp.sign(txnExt);
  }

  public static Transaction signTransaction(Transaction txn, KeyPair kp) {
    return kp.sign(txn);
  }

  public void broadcastTransaction(Transaction signedTxn) {
    TransactionReturn ret = blockingStub.broadcastTransaction(signedTxn);
    if (ret.getResult() != true) {
      throw new RuntimeException(
          String.format("%s: %s", ret.getCode(), ret.getMessage().toStringUtf8()));
    }
  }

  public void broadcastTransaction(TransactionExtention signedTxnExt) {
    broadcastTransaction(signedTxnExt.getTransaction());
  }

  public static Transaction addMemoToTransaction(Transaction txn, String memo) {
    return txn.toBuilder()
        .setRawData(txn.getRawData().toBuilder().setData(ByteString.copyFromUtf8(memo)))
        .build();
  }

  public static Transaction addMemoToTransaction(TransactionExtention txnExt, String memo) {
    return addMemoToTransaction(txnExt.getTransaction(), memo);
  }

  public static Transaction setFeeLimitOfTransaction(Transaction txn, long feeLimit) {
    return txn.toBuilder().setRawData(txn.getRawData().toBuilder().setFeeLimit(feeLimit)).build();
  }

  public static Transaction setFeeLimitOfTransaction(TransactionExtention txnExt, long feeLimit) {
    return setFeeLimitOfTransaction(txnExt.getTransaction(), feeLimit);
  }

  private static ByteString calculateTransactionId(Transaction txn) {
    SHA256.Digest digest = new SHA256.Digest();
    digest.update(txn.getRawData().toByteArray());
    byte[] txid = digest.digest();
    return ByteString.copyFrom(txid);
  }

  private static void handleTransactionException(TransactionExtention txnExt) {
    if (txnExt.getResult().getResult() != true) {
      throw new IllegalArgumentException(String.format(
          "%s: %s", txnExt.getResult().getCode(), txnExt.getResult().getMessage().toStringUtf8()));
    }
  }

  public TransactionExtention transfer(String from, String to, long amount, String memo) {
    TransactionExtention txnExt = transfer(from, to, amount);
    Transaction txnWithMemo = addMemoToTransaction(txnExt.getTransaction(), memo);
    return txnExt.toBuilder()
        .setTransaction(txnWithMemo)
        .setTxid(calculateTransactionId(txnWithMemo))
        .build();
  }

  public TransactionExtention transfer(String from, String to, long amount) {
    ByteString from_ = parseAddress(from);
    ByteString to_ = parseAddress(to);

    TransferContract req = TransferContract.newBuilder()
                               .setOwnerAddress(from_)
                               .setToAddress(to_)
                               .setAmount(amount)
                               .build();

    TransactionExtention txnExt = blockingStub.createTransaction2(req);
    handleTransactionException(txnExt);
    return txnExt;
  }

  public TransactionExtention transferTrc10(
      String from, String to, int tokenId, long amount, String memo) {
    TransactionExtention txnExt = transferTrc10(from, to, tokenId, amount);
    Transaction txnWithMemo = addMemoToTransaction(txnExt.getTransaction(), memo);
    return txnExt.toBuilder()
        .setTransaction(txnWithMemo)
        .setTxid(calculateTransactionId(txnWithMemo))
        .build();
  }

  public TransactionExtention transferTrc10(String from, String to, int tokenId, long amount) {
    ByteString from_ = parseAddress(from);
    ByteString to_ = parseAddress(to);
    ByteString tokenId_ = ByteString.copyFrom(Integer.toString(tokenId).getBytes());

    TransferAssetContract req = TransferAssetContract.newBuilder()
                                    .setOwnerAddress(from_)
                                    .setToAddress(to_)
                                    .setAssetName(tokenId_)
                                    .setAmount(amount)
                                    .build();

    TransactionExtention txnExt = blockingStub.transferAsset2(req);
    handleTransactionException(txnExt);
    return txnExt;
  }
}
