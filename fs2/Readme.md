## FS2 Tutorial

**Fs2 provides an entirely functional approach to stream processing**

The fs2 streaming library is built on top of the Cats and Cats Effect libraries. 
However, we don’t need to specify them as direct dependencies in the sbt file since the 
two libraries are already contained in fs2.

### The FS2 library has two major capabilities:

- The ability to build arbitrarily complex streams, possibly with embedded effects.
- The ability to transform one or more streams using a small but powerful set of operations

### Resources
- [Functional, effectful, concurrent streams for Scala](https://fs2.io/#/)
- [Rock the JVM tutorial](https://blog.rockthejvm.com/fs2/)
