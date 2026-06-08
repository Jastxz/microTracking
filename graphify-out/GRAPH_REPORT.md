# Graph Report - .  (2026-06-03)

## Corpus Check
- Corpus is ~6,768 words - fits in a single context window. You may not need a graph.

## Summary
- 56 nodes · 79 edges · 10 communities (4 shown, 6 thin omitted)
- Extraction: 95% EXTRACTED · 5% INFERRED · 0% AMBIGUOUS · INFERRED: 4 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Community 0|Community 0]]
- [[_COMMUNITY_Community 1|Community 1]]
- [[_COMMUNITY_Community 2|Community 2]]
- [[_COMMUNITY_Community 3|Community 3]]
- [[_COMMUNITY_Community 4|Community 4]]
- [[_COMMUNITY_Community 5|Community 5]]
- [[_COMMUNITY_Community 6|Community 6]]
- [[_COMMUNITY_Community 7|Community 7]]
- [[_COMMUNITY_Community 8|Community 8]]
- [[_COMMUNITY_Community 9|Community 9]]

## God Nodes (most connected - your core abstractions)
1. `TrackingService` - 7 edges
2. `TrackingController` - 5 edges
3. `EmailService` - 4 edges
4. `String` - 4 edges
5. `HttpServletRequest` - 3 edges
6. `ResponseEntity` - 3 edges
7. `PostMapping` - 3 edges
8. `VisitorData` - 3 edges
9. `Async` - 3 edges
10. `IpInfoResponse` - 3 edges

## Surprising Connections (you probably didn't know these)
- None detected - all connections are within the same source files.

## Import Cycles
- None detected.

## Communities (10 total, 6 thin omitted)

### Community 0 - "Community 0"
Cohesion: 0.26
Nodes (9): TrackingController, HttpServletRequest, PostMapping, ResponseEntity, String, TimeData, VisitorData, TrackingService (+1 more)

### Community 1 - "Community 1"
Cohesion: 0.36
Nodes (6): Async, IpInfoResponse, TrackingService, String, TimeData, VisitorData

### Community 2 - "Community 2"
Cohesion: 0.43
Nodes (3): Map, EmailService, String

### Community 3 - "Community 3"
Cohesion: 0.33
Nodes (3): EmailService, EmailServiceLocalTest, String

## Knowledge Gaps
- **8 isolated node(s):** `java.configuration.updateBuildConfiguration`, `java.compile.nullAnalysis.mode`, `String`, `String`, `IpInfoResponse` (+3 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **6 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `TrackingService` connect `Community 1` to `Community 3`?**
  _High betweenness centrality (0.089) - this node is a cross-community bridge._
- **What connects `java.configuration.updateBuildConfiguration`, `java.compile.nullAnalysis.mode`, `String` to the rest of the system?**
  _8 weakly-connected nodes found - possible documentation gaps or missing edges._