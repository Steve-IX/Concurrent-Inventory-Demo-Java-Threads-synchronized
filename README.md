# Concurrent-Inventory Demo — Java Threads & `synchronized`

This repository shows how to build a **race-free, thread-driven inventory counter** in pure Java.
It is a compact but complete example of:

* spawning any number of worker threads at runtime,
* guaranteeing correctness with Java’s built-in `synchronized` keyword + `Thread.join`,
* producing fully deterministic console output—even when hundreds of operations interleave.&#x20;

---

## Problem statement

You have one shared integer representing the size of a warehouse inventory. Two kinds of operations must be supported concurrently:

| Operation  | Effect        | Concurrency rule                                                             |
| ---------- | ------------- | ---------------------------------------------------------------------------- |
| **Add**    | `inventory++` | May run in parallel with other adds/removes, but updates must appear atomic. |
| **Remove** | `inventory--` | Same guarantee as above; final total must equal *adds – removes*.            |

The application starts with **inventory = 0** and accepts **two command-line arguments**:

```text
java InventoryMain <adds> <removes>
```

* `<adds>`   Number of add operations (one thread each).
* `<removes>` Number of remove operations (one thread each).&#x20;

Example:

```bash
java InventoryMain 5 10
```

launches **15 threads** (5 adders, 10 removers). When all finish, the program must have printed every step and end with:

```
Final inventory size = -5
```

---

## Repository layout

```
.
├── InventoryMain.java   # program entry point
├── Inventory.java       # holds the shared counter, exposes sync'd mutators
├── AddWorker.java       # Runnable that performs one add operation
├── RemoveWorker.java    # Runnable that performs one remove operation
└── README.md            # this file
```

> **No external libraries are required.** The entire solution compiles and runs with the JDK alone; only core-language constructs (`synchronized`, `join`) are used.&#x20;

---

## Building and running

```bash
# 1. Compile
javac InventoryMain.java

# 2. Run with desired workload
java InventoryMain 50 20      # 50 adds, 20 removes
```

You should see an interleaved sequence such as:

```
Added.   Inventory size = 1
Removed. Inventory size = 0
Removed. Inventory size = -1
...
Final inventory size = 30
```

While the order of individual messages varies run-to-run, **the final total is always adds − removes**.

---

## How it works

| Component                    | Responsibility                                                                                                                                                                            |
| ---------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `Inventory`                  | Holds the `private int count`. Both `add()` and `remove()` are declared `synchronized`, ensuring that only one thread can mutate or read `count` at a time.                               |
| `AddWorker` / `RemoveWorker` | Simple `Runnable`s that call `inventory.add()` or `inventory.remove()` exactly once, then print the new size.                                                                             |
| `InventoryMain`              | *Parses arguments → spawns threads → starts them → `join`s every thread → prints the final result.* It never touches `count` directly, so all access passes through the critical section. |

**Why only `synchronized` + `join`?**
They demonstrate Java’s native monitor-based locking clearly. More sophisticated primitives (locks, latches, atomics) are deliberately excluded to keep the focus on core language tools.&#x20;

---

## Extending the demo

* **Stress tests** – loop `InventoryMain` inside a shell script with thousands of operations to confirm robustness.
* **Timing metrics** – record start/stop times in each worker to analyse contention.
* **Multiple item types** – replace the single counter with a `Map<Item, Integer>`; synchronise at the map-level or with finer-grained locks.

---
