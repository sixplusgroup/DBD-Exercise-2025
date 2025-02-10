# 练习5 MVCC实验手册

内容：用 Java 模拟实现简单的 MVCC，包括版本控制、事务、读写、提交与回滚、日志等。

形式：给大框架，核心代码缺失，进行代码填空。提供手册和代码注释进行引导。

目标：有助于学生进一步理解 MVCC，体会利用 MVCC 机制解决脏读和不可重复读问题。

## 整体介绍

### Data 类

数据内容 + 版本号，令版本号从 0 开始。

### VersionChain 类

持有 Data 的列表，记录 Data 的变更。当数据内容更新时，列表新增一个 Data（新数据内容 + 新版本号）。

VersionChain 相当于变量，Data 相当于该变量的值。比如变量 A 的初始值为 20 ，某事务将其改写为 21 ，再改写为 23，最后提交。那么 VersionChain 中的 Data 列表为：`{{object = 20, version = 0}, {object = 21, version = 1}, {object = 23, version = 2}}`。

**重点：currentVersion 和 newestRightVersion 的区别，详情见注释。**

### TransactionManager 类

事务管理器，负责处理事务的开始、提交和回滚，管理快照等等。

这个类非常重要，更多介绍见代码注释。

注：出于简单，本实验中的事务只会读取同一个 VersionChain，也就是只有一个“变量”，不涉及多个 VersionChain 的情况。

### ReadWriteHandler 类

完成读、写操作。

- 本实验中读操作的实现遵循 **REPEATABLE-READ(可重复读)** 的要求：在同一个事务中，多次读取同一数据将返回第一次读取时的结果（除非数据是被事务本身所修改），即使其他事务在此期间对数据进行了修改并提交。
- 具体通过“快照”的方式实现：如果是事务首次读取数据，建立快照；否则，从快照中读取。
- 将 mvcc 和上述 REPEATABLE-READ(可重复读) 的要求相结合，可以避免 **脏读** 和 **不可重复读**。

### WalManager 类

日志管理，功能包括日志记录、提交/回滚时刷盘。

### LockManager 类

用于在写操作前上锁，写操作后释放锁。

## 你的任务

代码填空，完成代码中的 TODO 部分，具体包括：

| Class              | Method                 |
| ------------------ | ---------------------- |
| VersionChain       | update                 |
| ReadWriteHandler   | read                   |
| TransactionManager | performUpdate          |
| TransactionManager | takeSnapshot           |
| TransactionManager | getDataByTransactionId |
| TransactionManager | commitTransaction      |
| TransactionManager | commitTransaction      |
| TransactionManager | rollbackTransaction    |
| WalManager         | beginTransaction       |
| WalManager         | logOperation           |

注：同学们不要担心，总共10个方法的代码填空看起来很多，但是本实验的重点在于理解 mvcc 机制以及阅读理解框架代码，实际代码量很小。

## 本实验中 MVCC 对于脏读、丢失修改、不可重复读的作用

参考链接：[MySQL常见面试题总结 | JavaGuide](https://javaguide.cn/database/mysql/mysql-questions-01.html)

### 脏读

一个事务读取数据并且对数据进行了修改，这个修改对其他事务来说是可见的，即使当前事务没有提交。这时另外一个事务读取了这个还未提交的数据，但第一个事务突然回滚，导致数据并没有被提交到数据库，那第二个事务读取到的就是脏数据，这也就是脏读的由来。

例如：事务 1 读取某表中的数据 A=20，事务 1 修改 A=A-1，事务 2 读取到 A = 19,事务 1 回滚导致对 A 的修改并未提交到数据库， A 的值还是 20。

![脏读](https://javaguide.cn/assets/concurrency-consistency-issues-dirty-reading-C1rL9lNt.png)

本实验中，将 mvcc 和 REPEATABLE-READ(可重复读) 的要求相结合，可以避免 **脏读**。对应的测试用例为 `testDirtyRead()` 。

### 丢失修改

在一个事务读取一个数据时，另外一个事务也访问了该数据，那么在第一个事务中修改了这个数据后，第二个事务也修改了这个数据。这样第一个事务内的修改结果就被丢失，因此称为丢失修改。

例如：事务 1 读取某表中的数据 A=20，事务 2 也读取 A=20，事务 1 先修改 A=A-1，事务 2 后来也修改 A=A-1，最终结果 A=19，事务 1 的修改被丢失。

![丢失修改](https://javaguide.cn/assets/concurrency-consistency-issues-missing-modifications-D4pIxvwj.png)

本实验不能避免丢失修改，对应的测试用例为 `testLostToModify()`。要防止丢失修改，有以下措施：

1. **使用适当的隔离级别**：

   数据库系统提供了不同的事务隔离级别，如读未提交（Read Uncommitted）、读已提交（Read Committed）、可重复读（Repeatable Read）、序列化（Serializable）。其中，序列化是最高级别的隔离，它完全避免了丢失修改的问题，但可能导致较多的锁竞争和较低的并发性能。

2. **乐观并发控制（Optimistic Concurrency Control, OCC）**：

   乐观锁机制允许事务在执行过程中不加锁地读写数据，但在提交时检查是否有其他事务对相同的数据进行了修改。如果有冲突，则拒绝提交当前事务，并可能要求用户重试。

3. **悲观并发控制（Pessimistic Concurrency Control）**：

   悲观锁机制会在读取数据时就加上锁，防止其他事务修改该数据，直到当前事务完成。这确保了没有其他事务可以干扰，但是会降低系统的并发度。

### 不可重复读

指在一个事务内多次读同一数据。在这个事务还没有结束时，另一个事务也访问该数据。那么，在第一个事务中的两次读数据之间，由于第二个事务的修改导致第一个事务两次读取的数据可能不太一样。这就发生了在一个事务内两次读到的数据是不一样的情况，因此称为不可重复读。

例如：事务 1 读取某表中的数据 A=20，事务 2 也读取 A=20，事务 1 修改 A=A-1，事务 2 再次读取 A =19，此时读取的结果和第一次读取的结果不同。

![不可重复读](https://javaguide.cn/assets/concurrency-consistency-issues-unrepeatable-read-RYuQTZvh.png)

本实验中，将 mvcc 和 REPEATABLE-READ(可重复读) 的要求相结合，可以避免 **不可重复读**。对应的测试用例为 `testUnrepeatableRead()` 。

## 附录——AtomicInteger 介绍

`AtomicInteger` 是 Java 并发包 `java.util.concurrent.atomic` 中的一个类，它提供了一种基于原子操作的整数包装类。与普通的 `Integer` 不同，`AtomicInteger` 的所有实例方法都可以保证线程安全，并且它们的操作（如获取、设置、增加等）都是原子性的，即这些操作在多线程环境下不会被中断。

以下是 `AtomicInteger` 类的一些主要方法：

- `int get()`：获取当前值。
- `void set(int newValue)`：将当前值设为 `newValue`。
- `int getAndSet(int newValue)`：以原子方式将给定值设置为 `newValue`，并返回旧值。
- `int getAndIncrement()`：以原子方式将当前值加1，并返回旧值。
- `int incrementAndGet()`：以原子方式将当前值加1，并返回新值。
- `int getAndDecrement()`：以原子方式将当前值减1，并返回旧值。
- `int decrementAndGet()`：以原子方式将当前值减1，并返回新值。
- `int addAndGet(int delta)`：以原子方式将指定的值添加到当前值，并返回新值。
- `int getAndAdd(int delta)`：以原子方式将指定的值添加到当前值，并返回旧值。
- `boolean compareAndSet(int expect, int update)`：如果当前值等于预期值，则以原子方式将其设置为新值，并返回 `true`；否则返回 `false`。

`AtomicInteger` 使用了硬件级别的原子指令来实现高效并发访问，而不需要使用重量级的同步机制如 `synchronized`。因此，在某些情况下，它可以提供比传统锁更好的性能和可伸缩性。然而，当需要执行更复杂的复合操作时，可能还是需要借助显式的锁定或其他同步工具。

## 评分标准

按满分 100 分计算。

总共七个测试用例，`testRead()` `testWrite()`  `testLog()` `testStatus()` 四个用例各占 25 分。 `testDirtyRead()`  `testUnrepeatableRead()`  `testLostToModify()` 三个用例不占分，意在帮助同学们更好的理解 MVCC 与并发事务带来的问题之间的关系。