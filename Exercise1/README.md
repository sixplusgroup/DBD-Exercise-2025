## 练习1

### 环境准备

0. 安装MySQL环境，选择8.4.4 <https://dev.mysql.com/downloads/mysql/>（LTS版本真的变化很快）。安装SQLite<https://www.runoob.com/sqlite/sqlite-installation.html>

### 数据下载

1. <https://drive.google.com/file/d/1403EGqzIDoHMdQF4c9Bkyl7dZLZ5Wt6J/view?usp=sharing>下载spider数据集。

### 数据文件结构

#### 问题与SQL文件（`train.json` / `dev.json`）

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

2. 在spider/selected_questions.json中有三道题目。先自行完成，随后使用AI工具生成SQL，指出使用的AI工具，以及如何构建SQL语句，以及构建多少种SQL，提供**过程中使用的对话和总体时间**。

```
正确与否需要比较benchmark，执行命令：
python evaluation.py --dev ../spider_data/dev.json --selected selected_questions.json --db ../spider_data/database --table ../spider_data/tables.json
```

导入表

3. 创建一个数据库，名称为学号，下载<https://files.grouplens.org/datasets/movielens/ml-latest.zip>并将 ratings.csv、movies.csv 和 links.csv 文件导入到数据库中。
4. 连接 MySQL 数据库。
5. 查看 ratings、movies、links表中的前五条数据，并查询每个表行数。
6. 计算ratings打分之和。
7. 计算movies有几部2023年的电影。
8. 对links的imdbId计算该列值的 MD5。
