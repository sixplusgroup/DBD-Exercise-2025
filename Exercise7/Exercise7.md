## **练习7 基于大语言模型的 SQL 生成**

使用Vanna和Google gemini来去编写一个AI4DB的程序

------

### **实验目的**

1. **了解大语言模型（LLM）在 SQL 生成中的应用**：通过使用 Vanna 和 Google Gemini，学生将了解如何利用大语言模型生成 SQL 查询语句。
2. **掌握 AI4DB 的基本概念**：通过实验，学生将理解 AI 在数据库领域的应用
3. **培养大模型训练与分析能力**：学生将一个未进行任何训练的模型逐步演变到进阶训练的模型，通过对执行语句以及性能分析，再与当今市面上的大模型进行对比，理解大语言模型的训练过程，以及对模型训练重要性的理解

------

### **实验要求**

**编程语言：**Python

**工具与库：**

- Vanna：用于 SQL 生成和训练
- Google Gemini：作为大语言模型生成 SQL

**数据库：**MySQL

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

**所需要提问的问题：**

```python
test_question=["查询每部电影的平均评分，并按评分从高到低排序", "查询每个用户的评分数量，并按评分数量从高到低排序", "查询每部电影的标签数量，并按标签数量从高到低排序", "查询评分最高的 10 部电影，并显示电影标题和平均评分", "查询评分次数最多的 10 部电影，并显示电影标题和评分次数", "查询每部电影的最高评分和最低评分，并显示电影标题", "查询每部电影的相关性最高的标签，并显示电影标题和标签名称", "查询每个用户的平均评分与总体平均评分的差异，并按差异从高到低排序", "查询每部电影的 IMDb 和 TMDb 编号，并显示电影标题", "查询每部电影的标签数量与评分数量的比值，并按比值从高到低排序"]
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

