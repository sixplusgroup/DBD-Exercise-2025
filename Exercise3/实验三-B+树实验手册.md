# 练习3 B+树实验手册

## 实验目的

- **理解数据结构**：通过手动编写 B+ 树代码，学生可以更深入地理解 B+ 树这种数据结构的原理和工作方式，包括节点的分裂、合并，键的插入和删除等操作。

- **学习算法设计**：编写 B+ 树代码可以帮助学生学习如何设计高效的数据结构操作算法，包括查找、插入和删除等操作，以及如何处理树的平衡和维护。

- **实践和应用**：通过实际编写 B+ 树代码，学生可以将理论知识应用到实践中，加深对数据结构和算法的理解，并培养解决问题的能力。

## 实验须知

- 背景知识：学生需要提前了解并熟练掌握B+树的增删改查等原理。
- 阅读代码：需阅读代码行数约100行，耗时约五分钟。
- 编写代码：待填充代码行数约250行，耗时约两小时。

## 实验介绍

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

## 评分方法

本次实验共计100分，共6个测试用例

6个测试用例会以不同的难度梯度呈现，其中：

- 15分：范围查询方法、等值查询方法、插入方法、删除方法
- 20分：上溢分裂方法，下溢合并方法

## 提交方法

代码打包上传moodle并附上通过用例的截图
