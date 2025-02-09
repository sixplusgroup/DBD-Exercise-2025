# DBD-Exercise-2025

## 练习1 数据库准备实验

0. 安装MySQL环境，选择8.0.41 <https://dev.mysql.com/downloads/mysql/>
1. [https://yale-lily.github.io/spider](https://yale-lily.github.io/spider)
2. 根据SQL benchmark出1-2题，先自己完成，使用AI工具生成SQL，指出使用的AI工具，以及如何构建SQL语句，以及构建多少种SQL。比较benchmark(开学前写完)，改错，获取最终答案，提供过程中使用的对话和总体时间。
导入表
3. 创建一个数据库，名称为提供的编号，并将 ratings.csv、movies.csv 和 links.csv 文件导入到数据库中
4. 查看 ratings、movies、 links表中所有数据，并查询该表行数（可分两个 SQL 语句）。
5. 完成数据检查，保证数据导入成功。

## 练习2

待定，如SQL Exercise练习等

## 练习3 B+树实验手册

### 实验目的

- **理解数据结构**：通过手动编写 B+ 树代码，学生可以更深入地理解 B+ 树这种数据结构的原理和工作方式，包括节点的分裂、合并，键的插入和删除等操作。

- **学习算法设计**：编写 B+ 树代码可以帮助学生学习如何设计高效的数据结构操作算法，包括查找、插入和删除等操作，以及如何处理树的平衡和维护。

- **实践和应用**：通过实际编写 B+ 树代码，学生可以将理论知识应用到实践中，加深对数据结构和算法的理解，并培养解决问题的能力。

### 实验介绍

在本次的实验中，你需要在学习并掌握B+树的相关知识后，阅读并理解所给出的代码框架，根据B+树的特性完成相关代码的补充。代码框架的介绍如下：

- **BPlusTree**：代表整个B+树的类，主要包括B+树的**Degree**（阶数）属性和对B+树节点的插入、删除、查询节点的方法。
- **BPlusTreeNode**：抽象类，由以下两种具体类负责实现。
- **BPlusTreeNonLeafNode**：具体实现B+树中的非叶节点，主要包括B+树非叶节点的插入、删除、查询、合并、分裂的方法。其还包含两部分信息：**Entry**作为索引键用于索引数据，**Child**作为指针指向其子节点。
- **BPlusTreeLeafNode**：具体实现B+树中的叶节点，主要包括B+树非叶节点的插入、删除、查询、合并、分裂的方法。其还包含两部分信息：**Entry**作为索引键用于索引数据，**Data**作为指针指向具体数据，**Next**作为指针指向该叶节点的后一个叶节点。
- **RemoveResult**：仅在删除时负责通知父节点是否删除成功，以及子节点删除完成之后是否发生下溢。

对于叶节点和非叶节点，需要完成：

1. 范围查询方法：B+树的范围查询指给定查询上限Entry K1，查询下限Entry K2, 找到B+树叶节点Entry满足在 [K1, k2) 范围内。

   ```java
   public List<E> rangeQuery(K startInclude, K endExclude){}
   ```

2. 等值查询方法：B+树的等值查询指找到B+树叶节点Entry与查询Entry完全相等所对应的数据。

   ```Java
   private int getEqualEntryIndex(K entry){}
   ```

3. 插入方法：向B+树中插入新的节点，在该方法中仅考虑不会触发节点溢出而分裂节点的情况，若触发则调用下面的方法。

   ```Java
   public BPlusTreeNode insert(K entry, E value){}
   ```

4. 上溢分裂方法：向B+树中插入新的节点而导致节点溢出时需要按规则进行分裂操作。

   ```java
   private BPlusTreeLeafNode split(){}
   ```

5. 借调方法：当出现节点不够删除时，向左右邻居借节点。

   ```java
   public void borrow(BPlusTreeNode neighbor, K parentEntry, boolean isLeft){}
   ```

6. 删除方法：删除一个节点。

   ```java
   public RemoveResult remove(K entry) {};
   public RemoveResult remove(K entry, E value) {};
   ```

7. 特别地，对于非叶节点，你还需要额外处理删除节点时可能发生的下溢：

   ```java
   private void handleUnderflow(BPlusTreeNode childNode, int childIndex, int entryIndex) {}
   ```

### 评分方法

本次实验共计100分，共20个测试用例，每通过一个计5分。

20个测试用例会以不同的难度梯度呈现，其中：

- 10分：范围查询方法、等值查询方法、借调方法
- 15分：插入方法、删除方法
- 20分：上溢分裂方法，下溢合并方法

## 练习4：数据库开发实验，每道题都要记录自己的完成时间

作业需要在给定数据集上，构建数据库表结构，并在数据库上完成指定的查询，并比较查询的结果、效率以及数据库之间的差异。
请完成作业，并将 SQL 语句（文本格式必须，可以附加截图）和结果截图（包含运行时间）形成一份 word 文档，并提交。
备注：请标明题号，涉及到 SQL 语句的都需要截图，同时附上 SQL 语句。

MySQL 部分：

1. 查询 id 为学号后三位的用户评价过的电影名称
2. 查询 movieId 为学号后三位的影片的平均分（只要平均分，不要多做操作）
3. 查询 userId 为学号后三位的用户给出所有评价的平均分并保留两位小数
4. 查询评价过至少 X 部电影的用户数（X 为学号后三位）
5. 查询 id 为学号后三位的用户评价的最近 5 部电影的平均分（最近 5 部是指最近的 5 个时间戳，可能不止 5 部电影）（保留两位小数）
6. 评分为 5 且评价数最多的电影名查询（需要考虑评价数最多的电影不止一部的情况）
7. 查询用户 1024 最喜欢的（即评分最高）电影 id、名称和评分信息及其所在IMDB 评分系统中的记录 ID，并按照时间升序排序（连表查询，时间指时间戳，不是 title 中的）。
8. 实现某次评分记录（userId 为 1024，movieId 为 110）的逻辑删除。

A班人工完成奇数题，B班人工完成偶数题。
A班偶数题使用AI生成，B班奇数题使用AI生成。

插入优化：

1. 在你的 MySQL 创建名为 dbcourse 的库， 然后执行给定的 ``InsertTable.java`` 文件，创建表并插入数据。 在控制台打印建表的 SQL语句以及插入数据所用时间 （秒） ，并提交截图。
2. 执行给定的 ``InsertTableUUID.java`` 文件，创建表并插入数据。在控制台打印建表的 SQL语句以及插入数据所用时间（秒），并提交截图。然后比较和第一题插入数据所用时间的差别，并分析造成这种现象的原因。

查询优化：

1. 编写 java 文件，执行查询语句：
``select id, age from person where age=30``,在控制台打印该 SQL语句以及查询数据所用时间（毫秒），并提交截图。
2. 在 person 表的 age 列上创建索引，并提交截图。然后重新运行 java 程序，并提交控制台打印的截图。
3. 将 java 文件中的查询 SQL语句改为``select id, age from person where age!=30``，然后重新运行 java 程序，并提交控制台打印的截图。试分析为什么该条语句的执行时间相比第二题创建索引后的执行时间，有较大差别。

更新优化：

1. 将java文件中的SQL语句改为 ``update person set age=18 where name like"abc%"``，然后重新运行 java 程序，并提交控制台打印的截图。
2. 在 person 表的 name 列上创建索引，并提交截图。将 java 文件中的SQL语句改为 ``update person set age=19 where name like "abc%"``， 然后重新运行 java 程序，并提交控制台打印的截图。
3. 将 java 文件中的SQL语句 为 ``update person set age=20 where name like"%abc%"``，然后重新运行 java 程序，并提交控制台打印的截图。试分析为什么该条语句的执行时间相比第二题创建索引后的执行时间，有较大差别。

A班人工完成查询优化题，B班人工完成更新优化题。
A班更新优化题使用AI生成，B班查询优化题使用AI生成。

## 练习5 MVCC实验

内容：用 Java 模拟实现简单的 MVCC，包括版本控制、事务、读写、提交与回滚、日志等。

形式：给大框架，核心代码缺失，进行代码填空。提供手册和代码注释进行引导。

目标：有助于学生进一步理解 MVCC，体会利用 MVCC 机制解决脏读和不可重复读问题。

### 整体介绍

#### Data 类

数据内容 + 版本号，令版本号从 0 开始。

#### VersionChain 类

持有 Data 的列表，记录 Data 的变更。当数据内容更新时，列表新增一个 Data（新数据内容 + 新版本号）。

VersionChain 相当于变量，Data 相当于该变量的值。比如变量 A 的初始值为 20 ，某事务将其改写为 21 ，再改写为 23，最后提交。那么 VersionChain 中的 Data 列表为：`{{object = 20, version = 0}, {object = 21, version = 1}, {object = 23, version = 2}}`。

**重点：currentVersion 和 newestRightVersion 的区别，详情见注释。**

#### TransactionManager 类

事务管理器，负责处理事务的开始、提交和回滚，管理快照等等。

这个类非常重要，更多介绍见代码注释。

注：出于简单，本实验中的事务只会读取同一个 VersionChain，也就是只有一个“变量”，不涉及多个 VersionChain 的情况。

#### ReadWriteHandler 类

完成读、写操作。

- 本实验中读操作的实现遵循 **REPEATABLE-READ(可重复读)** 的要求：在同一个事务中，多次读取同一数据将返回第一次读取时的结果（除非数据是被事务本身所修改），即使其他事务在此期间对数据进行了修改并提交。
- 具体通过“快照”的方式实现：如果是事务首次读取数据，建立快照；否则，从快照中读取。
- 将 mvcc 和上述 REPEATABLE-READ(可重复读) 的要求相结合，可以避免 **脏读** 和 **不可重复读**。

#### WalManager 类

日志管理，功能包括日志记录、提交/回滚时刷盘。

#### LockManager 类

用于在写操作前上锁，写操作后释放锁。

### 你的任务

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

### 本实验中 MVCC 对于脏读、丢失修改、不可重复读的作用

参考链接：[MySQL常见面试题总结 | JavaGuide](https://javaguide.cn/database/mysql/mysql-questions-01.html)

#### 脏读

一个事务读取数据并且对数据进行了修改，这个修改对其他事务来说是可见的，即使当前事务没有提交。这时另外一个事务读取了这个还未提交的数据，但第一个事务突然回滚，导致数据并没有被提交到数据库，那第二个事务读取到的就是脏数据，这也就是脏读的由来。

例如：事务 1 读取某表中的数据 A=20，事务 1 修改 A=A-1，事务 2 读取到 A = 19,事务 1 回滚导致对 A 的修改并未提交到数据库， A 的值还是 20。

![脏读](https://javaguide.cn/assets/concurrency-consistency-issues-dirty-reading-C1rL9lNt.png)

本实验中，将 mvcc 和 REPEATABLE-READ(可重复读) 的要求相结合，可以避免 **脏读**。对应的测试用例为 `testDirtyRead()` 。

#### 丢失修改

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

#### 不可重复读

指在一个事务内多次读同一数据。在这个事务还没有结束时，另一个事务也访问该数据。那么，在第一个事务中的两次读数据之间，由于第二个事务的修改导致第一个事务两次读取的数据可能不太一样。这就发生了在一个事务内两次读到的数据是不一样的情况，因此称为不可重复读。

例如：事务 1 读取某表中的数据 A=20，事务 2 也读取 A=20，事务 1 修改 A=A-1，事务 2 再次读取 A =19，此时读取的结果和第一次读取的结果不同。

![不可重复读](https://javaguide.cn/assets/concurrency-consistency-issues-unrepeatable-read-RYuQTZvh.png)

本实验中，将 mvcc 和 REPEATABLE-READ(可重复读) 的要求相结合，可以避免 **不可重复读**。对应的测试用例为 `testUnrepeatableRead()` 。

### 附录——AtomicInteger 介绍

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

### 评分标准

按满分 100 分计算。

总共七个测试用例，`testRead()` `testWrite()`  `testLog()` `testStatus()` 四个用例各占 25 分。 `testDirtyRead()`  `testUnrepeatableRead()`  `testLostToModify()` 三个用例不占分，意在帮助同学们更好的理解 MVCC 与并发事务带来的问题之间的关系。

## 练习6 数据库日志管理实验

### 实验目的

1. **理解和实现数据库事务**：学习如何使用 Java 实现数据库事务的基本操作，支持事务的提交、回滚功能。
2. **实现日志管理系统**：理解写前日志（WAL）机制的工作原理，学会如何实现 `Redo` 和 `Undo`。
3. **支持事务恢复**：通过日志文件，支持事务崩溃恢复。

### 实验代码架构

**Database类**

简单的内存数据库，用于存储 `key-value` 数据

**Operation类**

表示事务中的单个操作，本次实验支持`INSERT`、`UPDATE`、`DELETE`操作。

**WAL类**

简单的写前日志管理，记录不同类型操作并确保日志的持久性。

**Transaction类**

处理事务中各个操作的执行以及事物的执行与回滚。

**LogFile**

RedoLog、UndoLog分别是一条redo/undo日志记录，而RedoLogFile、UndoLogFile作为redo/undo日志的抽象化，充当日志文件角色，持有各自日志记录的集合。

### 实验任务

1. 实现Transaction类中的execute和rollback方法。其中execute负责执行`INSERT`、`UPDATE`、`DELETE`三种操作，并生成每个操作对应的redo、undo日志记录并利用WAL类进行持久化。rollback则需要利用undo日志进行事务的回滚操作。
2. 实现WAL类中的recover方法。该方法模拟在系统崩溃情况下利用redo日志进行数据恢复的操作。
3. **附加**：可调整日志文件类中的存储结构以帮助你更方便地实现上述任务。

## 练习7 AI4DB程序开发实验

使用Vanna和Google gemini来去编写一个AI4DB的程序

1. 先通过原始的Vanna和Google gemini在不经过训练的情况下询问题目，得到生成的查询SQL语句，之后记录执行该查询语句的查询用时
2. 对模型进行不同程度的训练，提供不同层次的训练集，包括：
   - SQL基本查询（SELECT）
   - 进阶查询：条件查询（WHERE），排序查询（ORDER BY），分组查询（GROUP BY），筛选分组查询（GROUP BY + HAVING），连接查询（JOIN），子查询（非关联子查询，关联子查询，条件查询，FROM子句中使用子查询），合并结果集（UNION），分页查询（LIMIT）
   - 函数应用：CASE WHEN查询语句，SQL中基本函数
   使用vanna.train(ddl, question,sql)的方式进行训练
3. 根据三种层次进行训练，依次再去问同样的问题，查看生成的查询语句与之前原始语句有什么不同，并记录执行该查询语句的查询用时，进行对比，体会不同训练的层度训练模型前后的性能提升
4. 完成实验报告

提供一个开源数据集，数据集可以采用开源数据例如movielens中ml-latest-small，Netflix，Retailrocket等，用于后续实验操作

题目均需要用自然语言对话程序，从而获得查询结果，得到操作所需要时间，并进行逐步优化SQL语句，记录操作时间，进而实现从自然语言到SQL语言的转化

最终提供实验报告（代码、训练过程、最终执行结果、优化后结果）
