package cn.bossfridy.protype.expression.rval;

import cn.bossfridy.protype.expression.token.Token;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StringRValue extends BaseRValue<String> {

    private String value;
    private Map<String, RVariable> methodMap = new HashMap<>();

    public StringRValue() {
        setType(STRING);
        initMethodMap();
    }

    public StringRValue(Token token) {
        setType(STRING);
        initMethodMap();
        if (token.getValue().startsWith("\"") && token.getValue().endsWith("\"")) {
            this.value = token.getValue().substring(1, token.getValue().length() - 1);
        } else if (token.getValue().startsWith("\'") && token.getValue().endsWith("\'")) {
            this.value = token.getValue().substring(1, token.getValue().length() - 1);
        } else if (token.getValue().startsWith("```") && token.getValue().endsWith("```")) {
            this.value = token.getValue().substring(3, token.getValue().length() - 3);
        } else {
            this.value = token.getValue();
        }
    }

    public StringRValue(String value) {
        this.value = value;
        setType(STRING);
        initMethodMap();
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringRValue that = (StringRValue) o;

        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public RVariable operate(String operator, RVariable b) {
        RValue variable = b.getValue();
        if (operator.equals("+")) {
            return new RVariable(new StringRValue(this.value + variable.toString()));
        }
        if (operator.equals("==")) {
            if (variable instanceof StringRValue) {
                return new RVariable(new BooleanRValue(this.value.equals(((StringRValue) variable).value)));
            } else {
                return new RVariable(new BooleanRValue(false));
            }
        }
        if (operator.equals("!=")) {
            return new RVariable(new BooleanRValue(!this.value.equals(variable.value())));
        }

        if (variable instanceof StringRValue) {
            String value = ((StringRValue) variable).value();

            if (value.equals("length")){
                return new RVariable(new LongRValue(this.value.length()));
            }

            return methodMap.get(value);
        }

        return new RVariable(new NullRValue());
    }

    @Override
    public Object toObject() {
        return value;
    }

    @Override
    public void encode(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(type());
        dataOutputStream.writeUTF(value());
    }

    @Override
    public void decode(DataInputStream dataInputStream) throws IOException {
        this.value = dataInputStream.readUTF();
    }

    @Override
    public String toString() {
        return value;
    }

    private void initMethodMap() {
        /**
        BiFunction toUpperCase = params ->
                RValueFactory.getRVariable(value.toUpperCase());
        methodMap.put("toUpperCase", new RVariable(new FunctionRValue(toUpperCase)));

        BiFunction toLowerCase = params ->
                RValueFactory.getRVariable(value.toLowerCase());
        methodMap.put("toLowerCase", new RVariable(new FunctionRValue(toLowerCase)));

        BiFunction size = params ->
                RValueFactory.getRVariable(value.length());
        methodMap.put("size", new RVariable(new FunctionRValue(size)));

        BiFunction charAt = params ->{
            long var = (long)params[0].toObject();
            return RValueFactory.getRVariable(String.valueOf(this.value.charAt((int)var)));
        };
        methodMap.put("charAt", new RVariable(new FunctionRValue(charAt)));

        BiFunction substring = params -> {
            long start = (long) params[0].toObject();
            long stop;
            if (params.length == 1){
                stop = value.length();
            }else{
                stop = (long) params[1].toObject();
            }
            return RValueFactory.getRVariable(this.value.substring((int)start, (int)stop));
        };

        methodMap.put("substring", new RVariable(new FunctionRValue(substring)));

        BiFunction split = params -> {
            String separator = params[0].toString();
            long limit;
            if (params.length == 1){
                limit = 0;
            }else{
                limit = (long) params[1].toObject();
            }

            return RValueFactory.getRVariable(Arrays.asList(this.value.split(separator, (int)limit)));
        };

        methodMap.put("split", new RVariable(new FunctionRValue(split)));

        BiFunction indexOf = params -> {
            String searchValue = params[0].toString();
            long fromIndex;
            if (params.length == 1){
                fromIndex = 0;
            }else{
                fromIndex = (long) params[1].toObject();
            }

            return RValueFactory.getRVariable(this.value.indexOf(searchValue, (int)fromIndex));
        };

        methodMap.put("indexOf", new RVariable(new FunctionRValue(indexOf)));

        BiFunction lastIndexOf = params -> {
            String searchValue =  params[0].toString();
            long fromIndex;
            if (params.length == 1){
                fromIndex = value.length();
            }else{
                fromIndex = (long) params[1].toObject();
            }

            return RValueFactory.getRVariable(this.value.lastIndexOf(searchValue, (int)fromIndex));
        };

        methodMap.put("lastIndexOf", new RVariable(new FunctionRValue(lastIndexOf)));

        BiFunction replace = params -> {
            String regex = params[0].toString();
            String replacement = params[1].toString();
            return RValueFactory.getRVariable(this.value.replace(regex, replacement));
        };

        methodMap.put("replace", new RVariable(new FunctionRValue(replace)));

        BiFunction iterator = params -> {
            LinkedList<RVariable> objects = new LinkedList<>();
            for (char ch : value.toCharArray()) {
                objects.add(new RVariable(new StringRValue(Character.toString(ch))));
            }

            ArrayRValue temp = new ArrayRValue(objects);
            temp.setIterator(temp.value().iterator());
            return new RVariable(temp);
        };

        methodMap.put("#iterator", new RVariable(new FunctionRValue(iterator)));
         **/
    }
}
