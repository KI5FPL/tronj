package org.opentron.tronj.abi.datatypes.generated;

import java.math.BigInteger;
import org.opentron.tronj.abi.datatypes.Uint;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.opentron.tronj.codegen.AbiTypesGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Uint40 extends Uint {
    public static final Uint40 DEFAULT = new Uint40(BigInteger.ZERO);

    public Uint40(BigInteger value) {
        super(40, value);
    }

    public Uint40(long value) {
        this(BigInteger.valueOf(value));
    }
}
