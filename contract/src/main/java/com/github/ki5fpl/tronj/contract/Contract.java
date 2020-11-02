package com.github.ki5fpl.tronj.contract;

import com.github.ki5fpl.tronj.proto.Common.SmartContract.ABI;
import com.github.ki5fpl.tronj.client.TronClient;
import com.google.protobuf.ByteString;

public class Contract {
    private String originAddr;
    private String cntrAddr;
    private ABI abi;
    private ByteString bytecode;
    private long callValue;
    private long userResourcePercent;
    private String name;
    private long originEnergyLimit;
    private ByteString codeHash;
    private ByteString trxHash;
    private TronClient client;

    public Contract(String cntrAddr, ABI abi, ByteString bytecode, long userResourcePercent, String name, long originEnergyLimit, TronClient client) {
        this.cntrAddr = cntrAddr;
        this.abi = abi;
        this.bytecode = bytecode;
        this.userResourcePercent = userResourcePercent;
        this.name = name;
        this.originEnergyLimit = originEnergyLimit;
        this.client = client;
    }

    public String getOriginAddr() {
        return originAddr;
    }

    public void setOriginAddr(String originAddr) {
        this.originAddr = originAddr;
    }

    public String getCntrAddr() {
        return cntrAddr;
    }

    public void setCntrAddr(String cntrAddr) {
        this.cntrAddr = cntrAddr;
    }

    public ABI getAbi() {
        return abi;
    }

    public void setAbi(ABI abi) {
        this.abi = abi;
    }

    public ByteString getBytecode() {
        return bytecode;
    }

    public void setBytecode(ByteString bytecode) {
        this.bytecode = bytecode;
    }

    public long getCallValue() {
        return callValue;
    }

    public void setCallValue(long callValue) {
        this.callValue = callValue;
    }

    public long getUserResourcePercent() {
        return userResourcePercent;
    }

    public void setUserResourcePercent(long userResourcePercent) {
        this.userResourcePercent = userResourcePercent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOriginEnergyLimit() {
        return originEnergyLimit;
    }

    public void setOriginEnergyLimit(long originEnergyLimit) {
        this.originEnergyLimit = originEnergyLimit;
    }

    public ByteString getCodeHash() {
        return codeHash;
    }

    public void setCodeHash(ByteString codeHash) {
        this.codeHash = codeHash;
    }

    public ByteString getTrxHash() {
        return trxHash;
    }

    public void setTrxHash(ByteString trxHash) {
        this.trxHash = trxHash;
    }

    public TronClient getClient() {
        return client;
    }

    public void setClient(TronClient client) {
        this.client = client;
    }
}