package org.opentron.tronj.abi.datatypes.generated;

import java.util.List;
import org.opentron.tronj.abi.datatypes.StaticArray;
import org.opentron.tronj.abi.datatypes.Type;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.opentron.tronj.codegen.AbiTypesGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class StaticArray28<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray28(List<T> values) {
        super(28, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray28(T... values) {
        super(28, values);
    }

    public StaticArray28(Class<T> type, List<T> values) {
        super(type, 28, values);
    }

    @SafeVarargs
    public StaticArray28(Class<T> type, T... values) {
        super(type, 28, values);
    }
}
