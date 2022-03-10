package cn.bossfridy.protype.expression.runtime;

import cn.bossfridy.protype.expression.rval.*;
import cn.bossfridy.protype.expression.token.Token;
import cn.bossfridy.protype.expression.tuple.FunctionTuple;
import cn.bossfridy.protype.expression.tuple.QuaternionTuple;
import cn.bossfridy.protype.expression.tuple.Tuple;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 元组执行器
 */
public class TupleExecutor implements BiFunction {
    public TupleExecutor parent = null;
    public MethodStack methodStack;
    public RVariable prevResult = null;
    public int lineNo = 0;
    public Map<String, RVariable> tempVariablePool = new HashMap<>();   // 临时变量池
    public RuntimeVariablePool variablePool = new RuntimeVariablePool();    // 变量池

    public TupleExecutor(MethodStack methodStack) {
        this.parent = null;
        this.methodStack = methodStack;
    }

    public TupleExecutor(TupleExecutor parent, MethodStack methodStack) {
        this.parent = parent;
        this.methodStack = methodStack;
    }

    @Override
    public RVariable apply(RVariable[] args) throws Exception {
        if (args != null) {
            Iterator<Token> iterator = methodStack.getParams().iterator();
            for (int i = 0; i < args.length && iterator.hasNext(); i++) {
                variablePool.createVariable(iterator.next().value, args[i]);
            }
        }

        execute();
        return prevResult;
    }

    public RVariable getVal(Token token) throws Exception {
        if (token.type.equals("Identifier")) {
            RVariable variable = null;
            if (token.value.startsWith("#")) {
                variable = tempVariablePool.get(token.value);
            } else {
                TupleExecutor tempExecuteSequence = this;
                do {
                    if (tempExecuteSequence.variablePool.containsKey(token.value)) {
                        variable = tempExecuteSequence.variablePool.get(token.value);
                        break;
                    } else {
                        tempExecuteSequence = tempExecuteSequence.parent;
                    }
                } while (tempExecuteSequence != null);
            }

            if (variable != null) {
                if (variable.getValue().value() instanceof TupleExecutor) {
                    TupleExecutor sequence = (TupleExecutor) variable.getValue().value();
                    sequence.lineNo = 0;
                }

                return variable;
            }

            return new RVariable(new UndefinedRValue());
        }

        return RValueFactory.getRVariable(token);
    }

    /**
     * 执行元组
     */
    private void execute() throws Exception {
        while (lineNo < methodStack.getTuples().length) {
            Tuple tuple = methodStack.getTuples()[lineNo];
            if (tuple instanceof QuaternionTuple) {
                QuaternionTuple quaternionTuple = (QuaternionTuple) tuple;
                if (quaternionTuple.getOperator().equals("=") || quaternionTuple.getOperator().equals(":=")) {
                    prevResult = RValueFactory.getRVariable(getVal(quaternionTuple.getP2()));
                } else {
                    RVariable p1 = getVal(quaternionTuple.getP1());
                    RVariable p2 = getVal(quaternionTuple.getP2());
                    prevResult = p1.operate(quaternionTuple.getOperator(), p2);
                }

                if (quaternionTuple.getOperator().equals(":=")) {
                    if (variablePool.containsCurrentKey(quaternionTuple.getResult().value)) {
                        throw new RuntimeException();
                    }
                    variablePool.createVariable(quaternionTuple.getResult().value, prevResult);
                } else {
                    RVariable result = getVal(quaternionTuple.getResult());
                    if (result.getValue().type() != BaseRValue.UNDEFINED) {
                        // 先不考虑该情况
                    } else {
                        if (quaternionTuple.getResult().value.startsWith("#")) {
                            tempVariablePool.put(quaternionTuple.getResult().value, prevResult);
                        } else if (quaternionTuple.getOperator().equals("=")) {
                            variablePool.createVariable(quaternionTuple.getResult().value, prevResult);
                        }
                    }
                }

                lineNo++;
                continue;
            }

            if (tuple instanceof FunctionTuple) {
                FunctionTuple functionTuple = (FunctionTuple) tuple;
                List<Token> tokens = functionTuple.getArguments();
                RVariable rVariable = getVal(functionTuple.getFunction());
                if (rVariable.getValue().type() == BaseRValue.UNDEFINED) {
                    rVariable = InnerMethodMap.getFunction(functionTuple.getFunction().value);
                }

                if (rVariable == null || !(rVariable.getValue().value() instanceof BiFunction)) {
                    throw new RuntimeException();
                }

                BiFunction method = ((FunctionRValue) rVariable.getValue()).value();
                RVariable[] values = new RVariable[tokens.size()];
                for (int i = 0; i < tokens.size(); i++) {
                    values[i] = RValueFactory.getRVariable(getVal(tokens.get(i)));
                }

                prevResult = method.apply(values);
                if (prevResult != null) {
                    if (functionTuple.getResult().value.startsWith("#")) {
                        tempVariablePool.put(functionTuple.getResult().value, prevResult);
                    } else {
                        variablePool.createVariable(functionTuple.getResult().value, prevResult);
                    }
                }

                lineNo++;
                continue;
            }

            throw new Exception("unsupported tuple type! " + tuple.getClass().getSimpleName());
        }
    }
}
