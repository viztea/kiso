## Table of Contents

- kiso/core: common utilities
- kiso/log: logging api
- kiso/io: i/o structures
- kiso/iort: i/o runtime
- kiso/http: http client and server powered by iort and io
- kiso/tracing: multiplatform opentelemetry tracing api
- kiso/ext/koin: [koin](https://insert-koin.io) utilities

## Versions

```kotlin 
repositories {
  maven("https://maven.dimensional.fun/releases")
}
```

- `kiso-core` 0.0.3 [api reference](https://maven.dimensional.fun/javadoc/releases/gay/vzt/kiso/kiso-core/0.0.3/raw/index.html)

## Opinionated

If you've used or browsed this repo at all, you may have noticed the use of snake case instead of the kotlin standard of
camel case.

This is a pretty opinionated library, and this is the boldest of my opinions. I simply prefer snake case, and
if you have a problem with that (and have a legitimate reason to use my library), I'm open to feedback.
