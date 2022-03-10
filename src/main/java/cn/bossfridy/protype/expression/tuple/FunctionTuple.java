package cn.bossfridy.protype.expression.tuple;

import cn.bossfridy.protype.expression.runtime.TupleExecutor;
import cn.bossfridy.protype.expression.token.Token;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FunctionTuple extends Tuple {
    @Getter
    @Setter
    private Token function;

    @Getter
    @Setter
    private List<Token> arguments = new ArrayList<>();

    @Getter
    @Setter
    private Token result;

    @Getter
    @Setter
    private boolean callNew = false;

    public FunctionTuple() {
        super("call");
        this.type = (byte) TupleType.FUNCTION_TUPLE.intValue();
    }

    /**
     * addArgument
     */
    public void addArgument(Token token) {
        arguments.add(token);
    }

    @Override
    protected void write(DataOutputStream dataOutputStream) throws IOException {
        function.encode(dataOutputStream);
        dataOutputStream.writeBoolean(callNew);
        dataOutputStream.writeInt(arguments.size());
        for (Token token : arguments) {
            token.encode(dataOutputStream);
        }
        result.encode(dataOutputStream);
    }

    @Override
    protected void read(DataInputStream dataInputStream) throws IOException {
        setFunction(new Token(dataInputStream));
        this.callNew = dataInputStream.readBoolean();
        int len = dataInputStream.readInt();
        List<Token> arguments = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            arguments.add(new Token(dataInputStream));
        }
        this.arguments = arguments;
        setResult(new Token(dataInputStream));
    }

    @Override
    protected void run(TupleExecutor tupleExecutor) throws Exception {

    }

    @Override
    public int getLineNo() {
        return function.getLineNo();
    }

    @Override
    public String toString() {
        return "FunctionTuple{" +
                "name='" + function + '\'' +
                ", arguments=" + arguments +
                ", result=" + result +
                "}";
    }
}
