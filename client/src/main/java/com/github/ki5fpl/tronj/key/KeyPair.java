package com.github.ki5fpl.tronj.key;

import com.github.ki5fpl.tronj.api.WalletGrpc;
import com.github.ki5fpl.tronj.crypto.SECP256K1;
import com.github.ki5fpl.tronj.proto.Chain.Transaction;
import com.github.ki5fpl.tronj.proto.Response.TransactionExtention;
import com.github.ki5fpl.tronj.utils.Base58Check;
import com.google.protobuf.ByteString;
import org.apache.tuweni.bytes.Bytes32;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.util.encoders.Hex;

public class KeyPair {
  private final SECP256K1.KeyPair rawKeyPair;

  public KeyPair(SECP256K1.KeyPair innerKey) {
    rawKeyPair = innerKey;
  }

  public static KeyPair of(String hexPrivateKey) {
    SECP256K1.KeyPair rawKeyPair =
        SECP256K1.KeyPair.create(SECP256K1.PrivateKey.create(Bytes32.fromHexString(hexPrivateKey)));
    return new KeyPair(rawKeyPair);
  }

  public static KeyPair generate() {
    SECP256K1.KeyPair rawKeyPair = SECP256K1.KeyPair.generate();
    return new KeyPair(rawKeyPair);
  }

  public String toPublicKey() {
    SECP256K1.PublicKey pubKey = rawKeyPair.getPublicKey();
    return Hex.toHexString(pubKey.getEncoded());
  }

  public String toPrivateKey() {
    return Hex.toHexString(rawKeyPair.getPrivateKey().getEncoded());
  }

  public String toBase58CheckAddress() {
    SECP256K1.PublicKey pubKey = rawKeyPair.getPublicKey();
    Keccak.Digest256 digest = new Keccak.Digest256();
    digest.update(pubKey.getEncoded(), 0, 64);
    byte[] raw = digest.digest();
    byte[] rawAddr = new byte[21];
    rawAddr[0] = 0x41;
    System.arraycopy(raw, 12, rawAddr, 1, 20);

    return Base58Check.bytesToBase58(rawAddr);
  }

  public String toHexAddress() {
    SECP256K1.PublicKey pubKey = rawKeyPair.getPublicKey();
    Keccak.Digest256 digest = new Keccak.Digest256();
    digest.update(pubKey.getEncoded(), 0, 64);
    byte[] raw = digest.digest();
    byte[] rawAddr = new byte[21];
    rawAddr[0] = 0x41;
    System.arraycopy(raw, 12, rawAddr, 1, 20);

    return Hex.toHexString(rawAddr);
  }

  public Transaction sign(Transaction txn) {
    SHA256.Digest digest = new SHA256.Digest();
    digest.update(txn.getRawData().toByteArray());
    byte[] txid = digest.digest();
    SECP256K1.Signature sig = SECP256K1.sign(Bytes32.wrap(txid), rawKeyPair);
    Transaction signedTxn =
        txn.toBuilder().addSignature(ByteString.copyFrom(sig.encodedBytes().toArray())).build();
    return signedTxn;
  }

  public Transaction sign(TransactionExtention txnExt) {
    SECP256K1.Signature sig =
        SECP256K1.sign(Bytes32.wrap(txnExt.getTxid().toByteArray()), rawKeyPair);
    Transaction signedTxn = txnExt.getTransaction()
                                .toBuilder()
                                .addSignature(ByteString.copyFrom(sig.encodedBytes().toArray()))
                                .build();
    return signedTxn;
  }
}