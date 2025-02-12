# DBD-Exercise-2025

## 练习1 数据库准备实验

### 环境准备

- **安装 MySQL 8.4.4**（或当时的LTS 版本即可）  
  下载地址：[MySQL 官网](https://dev.mysql.com/downloads/mysql/)
- **安装 SQLite**  
  下载地址：[SQLite 安装教程](https://www.runoob.com/sqlite/sqlite-installation.html)

### 问题与SQL文件（`dev.json`）

每个条目包含以下字段：

- **`question`**：自然语言问题（如“查询英语非官方语言的国家的平均寿命”）
- **`query`**：对应的完整SQL查询语句
- **`db_id`**：关联的数据库ID
- **`sql`**：解析后的SQL结构（嵌套字典，包含`SELECT`、`WHERE`等子句的抽象表示）
- **附加字段**：

  ```json
  {
    "question_toks": ["What", "is", "average", "life", ...],  // 分词后的问题文本
    "query_toks": ["SELECT", "avg", "(", "LifeExpectancy", ...]  // 分词后的SQL词元
  }

1. 在spider/selected_questions.json中有三道题目。先自行完成，随后使用AI工具生成SQL，指出使用的AI工具，以及如何构建SQL语句，以及构建多少种SQL，提供**过程中使用的对话和总体时间**。

    ```
    正确与否需要比较benchmark，执行命令：
    python evaluation.py --dev ..spider_data/dev.json --selected selected_questions.json --db ./spider_data/database --table ./spider_data/tables.json
    ```

### 导入movielens表

2. 创建一个数据库，名称为学号，下载<https://files.grouplens.org/datasets/movielens/ml-latest.zip>并将 ratings.csv、movies.csv 和 links.csv 文件导入到数据库中。
3. 连接 MySQL 数据库。
4. 查看 ratings、movies、links表中的前五条数据，并查询每个表行数。
5. 计算ratings打分之和。
6. 计算movies有几部2023年的电影。
7. 对links的imdbId计算该列值的 MD5。

## 练习2

SQL Exercise练习，完成5道题目。

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

------

预计阅读代码时间：80分钟

预计编码时间：30分钟

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

### 实验背景和目的

数据库管理系统通常会采用一些复杂的机制以确保在各种情况下数据不丢失且保持一致性。本次实验聚焦于实现一个简化的数据库日志管理系统，模拟数据库事务的处理过程以及日志的记录和恢复，帮助理解数据库事务和日志管理的基本原理。

假设我们正在开发一个简单的内存数据库系统，该系统主要用于存储键值对（key - value）数据。为了确保数据库在各种异常情况下的数据完整性，我们需要实现以下几个关键功能：

1. **数据库事务管理**：本实验不涉及并发事务。在一个事务中用户会执行一系列的操作（插入、更新、删除数据），这些操作需要作为一个原子操作来处理，即要么全部成功（提交事务），要么全部失败（回滚事务）。
2. **写前日志（WAL）机制**：写前日志（Write - Ahead Logging，WAL）是一种常用的数据库日志策略，用于确保在数据写入磁盘之前，先将相关的日志记录写入磁盘。本实验日志是内存日志，无需考虑磁盘持久化。
3. **事务恢复**：在系统崩溃后，需要能够利用日志文件进行数据恢复，以确保数据库的数据一致性。
4. （附加）**事务保存点**：保存点（Savepoint）是一个在事务执行过程中设置的标记点。在事务的部分失败时，可以回滚到保存点而不是完全回滚整个事务。

### 实验代码架构

**Database类**

简单的内存数据库，用于存储 `key-value` 数据，涉及数据变更时需要调用其内方法。

**Operation类**

表示事务中的单个操作，本次实验支持`INSERT`、`UPDATE`、`DELETE`操作。一个事务会涉及一个或多个操作。

**WAL类**

简单的写前日志管理，将不同类型操作记录到日志中。

**Transaction类**

处理事务中各个操作的执行以及事物的执行与回滚，并管理事务的保存点。

**LogFile**

RedoLog、UndoLog分别是一条redo/undo日志记录，而RedoLogFile、UndoLogFile作为redo/undo日志的抽象化，充当日志文件角色，持有各自日志记录的集合。

**test**

提供了12个测试用例。

### 实验任务

1. 实现Transaction类中的execute和rollback方法。其中execute负责执行`INSERT`、`UPDATE`、`DELETE`三种操作，并生成每个操作对应的redo、undo日志记录并利用WAL类进行持久化。rollback则需要利用undo日志进行事务的回滚操作。
2. 实现WAL类中的recover方法。该方法模拟在系统崩溃情况下利用redo日志进行数据恢复的操作。
3. **附加**：可调整日志文件类中的存储结构以帮助你更方便地实现上述任务。

### 实验评分

满分：100

本次试验共提供12个测试用例，其中前10个涉及任务1、2、3，每个占10分。

最后两个涉及保存点功能，为附加题，可加分。

### 实验提交方式

请打包提交源码和在test下运行**TransactionTest**测试的结果截图。

### 注意事项与提示

1. 本次试验不涉及并发，且仅涉及INSERT、UPDATE、DELETE三种操作。
2. 执行DELETE操作时需要将对应的键值对删除而不是仅仅将value设为null，对INSERT操作的回滚亦然。
3. 事务只有在回滚或提交后才会结束活动状态，仅仅保存点回滚不会结束。
4. 请仔细阅读日志和日志文件类，其中RedoLogFile类中的addInitial方法代表redo日志存在初始记录。

## 练习7 AI4DB程序开发实验

使用Vanna和Google gemini来去编写一个AI4DB的程序

------

### **实验目的**

1. **了解大语言模型（LLM）在 SQL 生成中的应用**：通过使用 Vanna 和 Google Gemini，学生将了解如何利用大语言模型生成 SQL 查询语句。
2. **掌握 AI4DB 的基本概念**：通过实验，学生将理解 AI 在数据库领域的应用
3. **培养大模型训练与分析能力**：学生将一个未进行任何训练的模型逐步演变到进阶训练的模型，通过对执行语句以及性能分析，再与当今市面上的大模型进行对比，理解大语言模型的训练过程，以及对模型训练重要性的理解

------

### **实验要求**

**编程语言**：Python

**工具与库：**

- Vanna：用于 SQL 生成和训练
- Google Gemini：作为大语言模型生成 SQL

**数据库**：MySQL

**实验环境：**

- 安装MySQL数据库
- 安装 Python 3.8 及以上版本
- 安装所需的 Python 库

**实验报告和代码**：

- 实验报告需包括实验过程、实验结果、结果分析和总结
- 实验报告需附上运行结果截图

------

### **实验过程**

***PS：提问的问题在所需资源中！***

1. **实验准备**
   - 安装所需的 Python 库 (提供所需要的requirements.txt)
   - 配置数据库信息，包括创建数据库，导入数据
2. **实验步骤**
   - **步骤 1：原始模型测试**
     1. 连接调用Vanna和Gemini，参照资源中调用Vanna和Genimi的代码
     2. 使用未经训练的 Vanna 和 Google Gemini 生成 SQL 语句，参照相关响应对话函数
     3. 记录生成的 SQL 语句及其执行时间
     4. 将相关执行信息截图放入实验报告中
   - **步骤 2：基础查询训练模型**
     - 基本查询，仅对Vanna模型进行简单的SELECT语句训练（SELECT），将实验材料中的basic_train.json文件中的数据导入到模型中进行训练，使用训练后的模型生成SQL语句并记录生成的SQL语句及其执行时间，将执行结果截图放入到实验报告中
   - **步骤 3：进阶查询训练模型**
     - 进阶查询，对Vanna模型进行多种查询方式的训练，让模型理解连接查询，子查询等概念（WHERE、ORDER BY、GROUP BY、JOIN 等），将实验材料中的advanced_train.json文件中的数据导入到模型中进行训练，使用训练后的模型生成SQL语句并记录生成的SQL语句及其执行时间，将执行结果截图放入到实验报告中
   - **步骤 4：函数应用训练模型**
     - 函数应用，让模型了解数据库函数的用法（CASE WHEN、聚合函数等），将实验材料中的function_train.json文件中的数据导入到模型中进行训练，使用训练后的模型生成SQL语句并记录生成的SQL语句及其执行时间，将执行结果截图放入到实验报告中
   - **步骤 5：结果分析**
     - 分析训练数据对模型生成 SQL 语句的影响
     - 对比不同层次训练数据对模型性能的提升
     - 对比最终训练的程序与 Gemini 成熟模型的差别
   - **步骤6：撰写实验报告**

------

### **考核标准**

实验总分为 100 分，分为以下四个层次：

1. **基础完成（30 分）**
   - 完成实验环境的搭建和配置
   - 成功调用 Google Gemini 生成 SQL 语句
   - 成功调用初始 Vanna 模型
   - 记录并提交未经训练的模型生成的 SQL 语句及其执行时间
2. **训练模型，每个阶段的训练占比10分（30 分）**
   - 根据提供的训练数据文件进行训练
   - 成功训练 Vanna 模型
   - 记录并提交训练后的模型生成的 SQL 语句及其执行时间
3. **结果对比与分析（20 分）**
   - 对比自己的 Vanna 模型与 Google Gemini 模型生成 SQL 语句的准确性和差异
   - 对比训练前后生成的 SQL 语句的准确性
   - 对比训练前后 SQL 语句的执行时间
   - 分析训练数据对模型性能的影响
4. **实验报告与总结（20 分）**
   - 提交完整的实验报告和代码，实验报告包括实验过程、实验结果和结果分析

------

### 实验提交方式

打包提交源码和相应实验报告，实验报告模板参考实验材料中的**实验报告模板.docx**

### **所需资源**

**Vanna使用方法**：<https://vanna.ai/docs/>

**获取gemini的api_key方法**：<https://7b5krb21xg.apifox.cn/>

**所需要提问的问题：**

```python
test_question=["查询每部电影的平均评分，并按评分从高到低排序", "查询每个用户的评分数量，并按评分数量从高到低排序", "查询每部电影的标签数量，并按标签数量从高到低排序", "查询评分最高的 10 部电影，并显示电影标题和平均评分", "查询评分次数最多的 10 部电影，并显示电影标题和评分次数", "查询每部电影的最高评分和最低评分，并显示电影标题", "查询每部电影的相关性最高的标签，并显示电影标题和标签名称", "查询每个用户的平均评分与总体平均评分的差异，并按差异从高到低排序", "查询每部电影的类型分布，并统计每种类型的电影数量", "查询每部电影的标签数量与评分数量的比值，并按比值从高到低排序"]
```

**调用 Vanna 如下：**

```python
from vanna.remote import VannaDefault

# MY_VANNA_AIP_KEY和MY_VANNA_MODEL 是指自己的api_key和自定义的vanna_model
MY_VANNA_API_KEY = ""
MY_VANNA_MODEL = ""
vn = VannaDefault(api_key=MY_VANNA_API_KEY, model=MY_VANNA_MODEL)
# 之后可以使用vn来去调用Vanna的服务
vn.train() # 训练函数
vn.generate_sql(question) # 使用 Vanna 生成SQL语句，其中question是要提问的问题
```

**调用Gemini 如下：**

```python
import google.generativeai as genai

# GEMINI_API_KEY和GEMINI_MODEL 是指自己申请的Google Gemini的api_key 和 model
GEMINI_API_KEY = ''
GEMINI_MODEL = ''
genai.configure(api_key=GEMINI_API_KEY)
gemini_model = genai.GenerativeModel(GEMINI_MODEL)
# 之后可以调用genimi的相关函数
prompt = f"Generate an SQL query for the following question: {question}" # 自定义prompt
response = gemini_model.generate_content(prompt) # 向genimi发送问题，返回问题回答
sql_gemini = response.text.strip() # 将genimi响应的SQL转换成字符串
```

**读取json文件**：

```python
import json
with open(file_path, 'r', encoding='utf-8') as file:
 data = json.load(file)
```

**使用 Vanna 进行训练：**

```python
# 常用API 
vn.train(ddl="CREATE TABLE my_table (id INT, name TEXT)") # 使系统了解可用的表、列和数据类型
vn.train(documentation="我们的业务将XYZ定义为ABC") # 让 LLM 了解用户问题的上下文
vn.train(
    question="我们顾客的平均年龄是多少?", 
    sql="SELECT AVG(age) FROM customers"
)
vn.train(ddl="CREATE TABLE my_table (id INT, name VARCHAR(20), age INT)", question="我们顾客的平均年龄是多少?", sql="SELECT AVG(age) FROM customers") 
# 推荐使用第三种和第四种
```

其中”ddl” 描述表结构，”question”描述提问问题，”sql” 描述针对该问题对应的SQL语句

**连接数据库，运行SQL语句并记录执行效率的流程：**

```python
# 连接数据库
import time
import pymysql
connection = pymysql.connect(**db_config) # db_config 存放数据库相关信息，包括host,user,password,database 例如:db_config = {"host": "localhost", "user": "user", "password": "1234", "database": "mydb"}
cursor = connection.cursor()

# 记录开始时间 利用time函数

# 执行SQL
cursor.execute(sql)
# 获取查询结果
result = cursor.fetchall()

# 记录结束时间

# 计算查询时间

# 关闭连接 使用connect和cursor的close函数
```
