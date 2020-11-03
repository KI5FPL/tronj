package com.github.ki5fpl.tronj.contract;

import com.github.ki5fpl.tronj.proto.Common.SmartContract.ABI;
import com.github.ki5fpl.tronj.client.TronClient;
import com.google.protobuf.ByteString;

public class Contract {
    private String originAddr = "";
    private String cntrAddr = "";
    private ABI abi;
    private ByteString bytecode;
    private long callValue = 0;
    private long userResourcePercent = 100;
    private String name;
    private long originEnergyLimit = 1;
    private ByteString codeHash = null;
    private ByteString trxHash = null;
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

    public Contract(Builder builder) {
        this.originAddr = builder.originAddr;
        this.cntrAddr = builder.cntrAddr;
        this.abi = builder.abi;
        this.bytecode = builder.bytecode;
        this.callValue = builder.callValue;
        this.userResourcePercent = builder.userResourcePercent;
        this.name = builder.name;
        this.originEnergyLimit = builder.originEnergyLimit;
        this.client = builder.client;
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

    //contract builder
    public static class Builder {
        private String originAddr = "";
        private String cntrAddr = "";
        private ABI abi;
        private ByteString bytecode;
        private long callValue = 0;
        private long userResourcePercent = 100;
        private String name;
        private long originEnergyLimit = 1;
        private ByteString codeHash = null;
        private ByteString trxHash = null;
        private TronClient client;

        public Builder setOriginAddr(String originAddr) {
            this.originAddr = originAddr;
            return this;
        }

        public Builder setCntrAddr(String cntrAddr) {
            this.cntrAddr = cntrAddr;
            return this;
        }

        public Builder setAbi(ABI abi) {
            this.abi = abi;
            return this;
        }

        public Builder setBytecode(ByteString bytecode) {
            this.bytecode = bytecode;
            return this;
        }

        public Builder setCallValue(long callValue) {
            this.callValue = callValue;
            return this;
        }

        public Builder setUserResourcePercent(long userResourcePercent) {
            this.userResourcePercent = userResourcePercent;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setOriginEnergyLimit(long originEnergyLimit) {
            this.originEnergyLimit = originEnergyLimit;
            return this;
        }

        public Builder setClient(TronClient client) {
            this.client = client;
            return this;
        }

        public Builder build() {
            return new Contract(this);
        }
    }
}