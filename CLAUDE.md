# scales-xml — repo guide

## What this is

`scales-xml` is a pure-Scala XML library (trees, paths, XPath-like navigation, pull/SAX
parsing, StAX/TrAX serialization, type-class-based XML comparison). Upstream is **abandoned**;
this is SRF's maintained fork. History: originally Scala 2.11 → ported to 2.12 → lifted to
2.13 → **now Scala 3** (this branch). There is **no cross-compilation** — the project targets
a single Scala version.

Branch convention here: `master` (base), `master-213` (the Scala 2.13 state), **`master-3`**
(the Scala 3 migration — current). The directory name still says `2-12` for historical
reasons; ignore it.

## Build & verify

- Compile everything (main + test): `sbt "Test/compile"`
- Run the tests: `sbt test`
- **sbt must be run with `dangerouslyDisableSandbox: true`** — the build fetches an AWS
  CodeArtifact auth token via the AWS SDK at load time, which the sandbox blocks. This is the
  only command that needs it. Authentication is wired in `project/build.sbt`
  (`awsCodeartifactCredentials`) against the private EAI CodeArtifact repo.
- Verification is compile-only + the single spec. Warnings are tolerated and intentionally
  ignored (`-Xfatal-warnings` is removed; `-source:3.0-migration` accepts legacy 2.x syntax as
  warnings). Do **not** treat warnings as failures.

## Dependencies

Versions are centrally managed by the SRF sbt plugin and referenced via `SrfPlugin.Deps.*` in
`build.sbt` (do not hardcode versions). Current: Scala `SrfPlugin.scala3Version` (3.7.4),
scalaz-core 7.3.8, scala-xml 2.3.0, specs2 4.20.8 (test), scala-collection-compat.

- **scalaz** is used only lightly: the `Equal` type-class (in `scales.xml.equals` and a few
  core collections), one `EphemeralStream` appender (`EphemeralAppender` in
  `utils/collection/IterableUtils.scala`), and some `syntax` helpers. The XML **tree is scales'
  own** `scales.utils.collection.Tree` — *not* `scalaz.Tree`.

## Architecture (single module, `src/main/scala/scales/...`)

- `scales.utils` — foundation: own `Tree`, `Path`, immutable-array collections, resources, io.
  - `utils.collection[.array|.path]`, `utils.resources`, `utils.io`
- `scales.xml` — the XML model and API:
  - `xml.parser.sax` (SAX `Handler`/`XmlParser`) and `xml.parser.pull` (pull parser:
    `XmlPull`, `PullIterator`) — parsing.
  - `xml.trax` — StAX/TrAX: `ScalesStreamReader` (an `XMLStreamReader` over a scales tree),
    `TraxSupport`, conversions. Used to feed scales trees into JAXP transformers.
  - `xml.serializers` — serialization/pretty-print.
  - `xml.equals` — type-class-based XML comparison (uses scalaz `Equal`).
  - `xml.dsl` — the tree-building DSL (`Elem(...) / (...)`, `~>`).
  - `xml.xpath` — path navigation / functions.
  - `xml.impl` — implicits, defaults, factories, `TreeProxies`.

## Gotchas

- **`ScalesStreamReader` + JDK transformers:** feeding a scales tree into a JAXP `Transformer`
  (e.g. pretty-printing via `xml.transform`) drives scales' `ScalesStreamReader`. Its `next()`
  must follow the StAX contract — `getEventType()` already reports `START_DOCUMENT` before
  iteration, so `next()` must return the first *real* event, not `START_DOCUMENT` again.
  Emitting `START_DOCUMENT` from the first `next()` makes modern JDKs (11+) throw
  `java.lang.InternalError: processing prolog event: 7` inside the JDK's `StAXStream2SAX`
  bridge. (This was a pre-existing fork bug, fixed during the Scala 3 migration.)
- The Scala 3 migration deliberately keeps changes minimal: procedure syntax got `: Unit =`,
  view bounds (`<%`) became implicit evidence params, two anonymous structural types became
  named classes (`EphemeralAppender`, `ElemMatcherExtractor`), and implicit `def`/`val`
  definitions got explicit result types (required by Scala 3). No logic was changed.
