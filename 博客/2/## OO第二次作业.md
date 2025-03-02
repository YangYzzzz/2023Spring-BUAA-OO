## 三.第二次作业分析 ##
### 1.问题分析 ###
本次作业相较上次增添了几个新功能，支持**自定义函数因子**和**三角函数因子**的解析计算，并支持**嵌套括号**。新增添文法规则大致如下；
>变量因子 →→ 幂函数 | 三角函数 | 自定义函数调用
三角函数 →→ 'sin' '(' 因子 ')' [指数] | 'cos' '(' 因子 ')' [指数]
自定义函数定义 →→ 自定义函数名 '(' 自变量 [',' 自变量 [',' 自变量]] ')' '=' 函数表达式 例如 f(x)=x+1
自定义函数调用 →→ 自定义函数名 '(' 因子 [',' 因子 [',' 因子]] ')'
自定义函数名 →→ 'f' | 'g' | 'h'
函数表达式 →→ 表达式

首先我们可以轻松解决嵌套括号的问题，按照上一次作业所示架构，已经可以支持嵌套括号的运算，可能使用正则表达式分析法的同学在面临这个问题时会显得更为麻烦？
接着我们来思考三角函数的实现，笔者认为有关三角函数的处理是本次作业(乃至本单元)最为核心且困难的实现。在第一次作业中，我们将每一个表达式，项和各种因子看作是多个单项式集合组成的多项式，而每个单项式仅用**5**个变量即可被完全描述。而本次新增了三角函数，以上概念明显不再可行，但就如何存储一个“基本表达式”而言仍能发现有相通之处。笔者将每一个“单项式”(因为单项式类经过以下修改，将不再是狭义上的单项式)新增一个属性，内容为HashMap<Poly, Integer>,其中HashMap中每一个键值对代表着一个三角函数因子， 具体意义为**Poly**:存储的是该三角函数中含有的因子toPoly后的多项式Poly形式 **Integer**:存储的是该三角函数因子的指数，同时令正数为sin，负数为cos，这点很有创造性，使用一个HashMap即可存储任何形式的三角函数，而通过这一键值对即可完全描述出一个三角函数因子。而Poly多项式类属性不变，仍然是ArrayList<Unit>。拿$4*x**2*y*sin(x)*cos((4*z))$来说，前面单项式存储部分不变，后面跟着两个三角函数因子，使用HashMap对其指数和内部因子统一存储即可。以上讨论的是三角函数计算层面的实现，而解析表达式层面将在下文论述。
最后来考虑自定义函数的实现。我们可以新建一个CustomSet类来存储定义，具体存储其函数名，形参，函数表达式等属性。接着在我们解析表达式时会遇到自定义函数调用，笔者在此采用的是方法是**将其仍看作是一个因子解析进表达式树之中，而在自底向上toPoly时再将定义的函数与解析的函数相匹配，并进行形参到实参的带入**，这样做的好处是能在解析时专注解析，使目标表达式能按照文法完整的解析到表达式树之中，令人赏心悦目(也更简单)。

### 2.类分析
本次作业UML类图如图所示:
![UML类图](OO_U1_P1.png)
本次相较上次肉眼可见的复杂了许多(哭)，