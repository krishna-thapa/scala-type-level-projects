## Learn Doobie

doobie is a pure functional JDBC layer for Scala and Cats. 
It is not an ORM, nor is it a relational algebra; it simply provides a functional way to 
construct programs (and higher-level libraries) that use JDBC. 
For common use cases doobie provides a minimal but expressive high-level API.

**It provides a higher-level API on top of JDBC, using an effectful style through the Cats 
and Cats Effect libraries.**

Doobie is a library that lives in the Cats ecosystem

Doobie defines all its most essential types as instances of the Free monad.
A free monad is a construction which allows you to build a monad from any Functor. Like other monads, it is a pure way to represent and manipulate computations.

### What is doobie?
- 
- doobie is a pure-functional JDBC layer for Scala.
- doobie provides low-level access to everything in java.sql (as of Java 8), allowing you to write any JDBC program in a pure functional style.

We can say doobie is just a FP wrapper of JDBC, 
it just help you translate the FP style code to java.sql code.

`doobie-hikari` supply another implementation of Transactor which support to manage the connection pool.
In simple terms doobie translate all the models in java.sql to corresponding Free Monad, 
and use these Free Monad to compose program, then interpret the program to real java.sql as needed.

### Resources
- [doobie](https://tpolecat.github.io/doobie/)
- [Rock the JVM tutorial](https://blog.rockthejvm.com/doobie/)
- [Understanding Doobie](https://blog.shangjiaming.com/scala%20tutorial/doobie/)
