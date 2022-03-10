> 遵循编译原理主要过程实现1+1=2的Demo原型项目

# 项目简介
遵循编译原理主要过程实现1+1=2的Demo原型项目，该项目并非炫技（打印1+1的结果搞得这么复杂），只是用于简单展示：1、词法分析器；2、语法分析器；3、脚本编译器；4、编译执行器，四个过程，因为他们是实现自定义脚本引擎的基础。基于这种方式，后续扩展条件（if/else），循环（for/while），其他类型（map，对象，list,set……），其他语法特性（import，匿名函数）等都是完全支持的，那么支持了常用数据类型和语法之后，称之其为自定义脚本引擎就名副其实了。

# 过程简介

## 1、词法分析器（开发完成） 
  1、扫描源代码文本，从左到右扫描文本，把文本拆成一些单词。  
  2、分析出拆出来的单词是什么：关键字、标识符、符号、，注释……，其结果产物为Token。

上面的话不太好懂，举个栗子：
```
var a = 1 + 1;  // Comment

var ：关键字
a ： 标识符
= ： 符号
1 ： 数字
// Comment ： 注释

```

测试代码：    
```
public static void main(String[] args) throws Exception {
        String strScript = "var a = 1 + 1; // Comment";
        ScriptTokenRegister tokenRegister = new ScriptTokenRegister();
        List<Token> tokens = tokenRegister.getTokens(strScript);
        tokens.forEach(token->{
            System.out.println(token.toString());
        });
    }
```

分词结果如下（注释由于对脚本执行无意义，直接丢弃处理）：

```
Token{value='var', lineNo=0, offset=0, type='Keyword'}
Token{value='a', lineNo=0, offset=4, type='Identifier'}
Token{value='=', lineNo=0, offset=6, type='SingleSymbol'}
Token{value='1', lineNo=0, offset=8, type='Integer'}
Token{value='+', lineNo=0, offset=10, type='SingleSymbol'}
Token{value='1', lineNo=0, offset=12, type='Integer'}
Token{value=';', lineNo=0, offset=13, type='SingleSymbol'}
```

## 2、语法分析器（开发完成） 
  1、token序列会经过语法解析器识别出文本中的各类短语。  
  2、根据语言的文法规则（rules of grammar）输出解析树，这棵树（AST：抽象语法树）是对代码的树形描述。文法就是用简单的符号解决了无穷语言的有穷表述问题。举个栗子：5 + （12 * 1）根据对应的文法生成的解析树为：  
![IMG](https://s1.ax1x.com/2022/03/09/bfZqwn.png)  

* 语法规则由配置文件表达，这样做的好处是在实现脚本多语言时，只需要定义新文法配置文件，然后写对应的statementHandler即可。目前我们的脚本语法选择高度贴合JavaScript，如果未来想搞套其他语言的语法实现则会相对方便（例如.Net支持 C#、VB.Net、JS.Net 3种语法，但编译后生成的中间语言相同）。
* 语法规则配置文件（上述的文法规则：rules of grammar）本身由语法分析器自身完成解析，语法规则配置文件如下，格式为一行一条数据，每行格式为：[规则名称]：规则表达式。规则表达式中\?\*\+等符号含义同正则表达式对应含义（? 零次或一次；* 零次或多次；+ 一次或多次）
```
root : statementList? ;

statementList : statement+ ;

statement : variableStatement
    | expressionStatement
    ;

variableStatement : variableDeclarationList ';' ;

variableDeclarationList : 'var' variableDeclaration (',' variableDeclaration)*;

variableDeclaration : Identifier ('=' singleExpression)? ;

expressionStatement :  expressionSequence ';' ;

expressionSequence : singleExpression (',' singleExpression)* ;

arguments : '('(argument (',' argument)* ','?)?')' ;

argument : singleExpression | Identifier ;

singleExpression
    : singleExpression arguments                                            # ArgumentsExpression
    | singleExpression ('+' | '-') singleExpression                         # AdditiveExpression
    | singleExpression '=' singleExpression                                 # AssignmentExpression
    | singleExpression assignmentOperator singleExpression                  # AssignmentOperatorExpression
    | Identifier                                                            # IdentifierExpression
    | literal                                                               # LiteralExpression
    ;

assignmentOperator : '*=' | '/=' | '%=' | '+=' | '-=' | '&=' | '^=' | '|=';

literal : Null  | Boolean | String | MultiLineString | numericLiteral ;

numericLiteral : Float | Integer;
```

## 3、编译器
  1、根据AST生成四元式。  
  2、序列化四元式（有一定压缩优化，如果满大街的很长的标识符，显然很容易对其优化），由于元组的编解码不是想阐述的核心内容，目前demo中没有实现完全（利用业务时间分享点东西也不容易）。
  关于四元式举个栗子（当然现在大家基本上都是用带返回的用三元式去搞）：
```
5 + 6 -> 四元式:

5 ： p1
6 ： p2
+ ： operator
11 ： result
```

## 4、执行器  
  1、 简单来说就是按照顺序执行四元式数组，当然还有jump。执行层面基本上就2种情况，顺序执行以及执行到某个位置之后跳到另外的位置。 我们可以想想for循环转换为do while、可以想想if/else的执行，基本上很多的语法都是在基础语法之上包装出来的“语法糖”。    
  2、 由于脚本引擎基于JAVA实现，运行时执行效率只能做到接近JVM，原则上可以做到1个量级左右的差别，运行时尽量避免String.Equal之类的东西，以及本身逻辑尽量避免无谓的性能损耗。这里只是一个原型Demo，在执行器中原始的用：operator.equals("xxx")，显然是不合适的。
  
# 测试代码及结果
测试代码：
```
public class ExpressionEngine {
    private ASTPattern astPattern;
    private ScriptTokenRegister tokenRegister;

    public ExpressionEngine() throws Exception {
        tokenRegister = new ScriptTokenRegister();
        astPattern = ASTPattern.compileWithFile("AstParser.conf");
    }

    /**
     * apply
     */
    public void apply(String script) throws Exception {
        List<Token> tokens = tokenRegister.getTokens(script);   // 1.词法分析
        System.out.println("---------Tokens-----------");
        tokens.forEach(token -> {
            System.out.println(token.toFullString());
        });

        System.out.println("---------AST Result-----------");   // 2.语法分析
        ASTMatcher astResult = astPattern.match(tokens);
        System.out.println(astResult.toString());


        System.out.println("---------Tuples-----------");
        MethodStack methodStack = AbstractStatementHandle.parseMethodStack(astResult);  // 3.根据AST生成四元式
        Arrays.asList(methodStack.getTuples()).forEach(tuple -> {
            System.out.println(tuple.toString());
        });

        System.out.println("---------apply-----------");
        TupleExecutor executor = new TupleExecutor(methodStack);
        executor.apply(null);       // 4.四元式执行器
    }

    public static void main(String[] args) throws Exception {
        String script = "var a = 1 + 1; printl(a);";
        ExpressionEngine expEngine = new ExpressionEngine();
        expEngine.apply(script);
  
```

执行结果：
```
---------Tokens-----------
Token{value='var', lineNo=0, offset=0, type='Keyword'}
Token{value='a', lineNo=0, offset=4, type='Identifier'}
Token{value='=', lineNo=0, offset=6, type='SingleSymbol'}
Token{value='1', lineNo=0, offset=8, type='Integer'}
Token{value='+', lineNo=0, offset=10, type='SingleSymbol'}
Token{value='1', lineNo=0, offset=12, type='Integer'}
Token{value=';', lineNo=0, offset=13, type='SingleSymbol'}
Token{value='printl', lineNo=0, offset=15, type='Identifier'}
Token{value='(', lineNo=0, offset=21, type='SingleSymbol'}
Token{value='a', lineNo=0, offset=22, type='Identifier'}
Token{value=')', lineNo=0, offset=23, type='SingleSymbol'}
Token{value=';', lineNo=0, offset=24, type='SingleSymbol'}
---------AST Result-----------
'root':['statementList':['statement':['variableStatement':['variableDeclarationList':[var, 'variableDeclaration':[a, =, 'singleExpression#AdditiveExpression':['singleExpression#LiteralExpression':['literal':['numericLiteral':[1]]], +, 'singleExpression#LiteralExpression':['literal':['numericLiteral':[1]]]]]], ;]], 'statement':['expressionStatement':['expressionSequence':['singleExpression#ArgumentsExpression':['singleExpression#IdentifierExpression':[printl], 'arguments':[(, 'argument':['singleExpression#IdentifierExpression':[a]], )]]], ;]]]]
---------Tuples-----------
QuaternionTuple{p1=1, p2=1, op=+, result=#0, end=false, lineNo=0}
QuaternionTuple{p1=a, p2=#0, op=:=, result=a, end=true, lineNo=0}
FunctionTuple{name='printl', arguments=[a], result=#1}
---------apply-----------
2
```

