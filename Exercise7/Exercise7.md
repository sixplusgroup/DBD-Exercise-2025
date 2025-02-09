### **练习7 基于大语言模型的 SQL 生成**

使用Vanna和Google gemini来去编写一个AI4DB的程序

---

### **实验目的**

1. 了解大语言模型（LLM）在 SQL 生成中的应用：通过使用 Vanna 和 Google Gemini，学生将了解如何利用大语言模型生成 SQL 查询语句。

2. 掌握 AI4DB 的基本概念：通过实验，学生将理解 AI 在数据库领域的应用

3. 培养实验设计与分析能力：学生将一个未进行任何训练的模型逐步演变到进阶训练的模型，通过对执行语句以及性能分析，再与当今市面上的大模型进行对比，理解大语言模型的训练过程，以及对模型训练重要性的理解

---

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

---

## **实验过程**

1. **实验准备**
   - 安装所需的 Python 库 (提供所需要的requirements.txt)
   - 配置数据库信息，包括创建数据库，导入数据
2. **实验步骤**
   - **步骤 1：原始模型测试**
     - 使用未经训练的 Vanna 和 Google Gemini 生成 SQL 语句
     - 记录生成的 SQL 语句及其执行时间
     - 分析生成的 SQL 语句的准确性和性能
   - **步骤 2：训练模型**
     - 提供不同层次的训练数据：
       1. 基本查询，仅对Vanna模型进行简单的SELECT语句训练（SELECT）
       2. 进阶查询，对Vanna模型进行多种查询方式的训练，让模型理解连接查询，子查询等概念（WHERE、ORDER BY、GROUP BY、JOIN 等）
       3. 函数应用，让模型了解数据库函数的用法（CASE WHEN、聚合函数等）
   - **步骤 3：训练后模型测试**
     - 使用训练后的模型生成 SQL 语句
     - 记录生成的 SQL 语句及其执行时间
     - 对比训练前后的 SQL 生成结果和执行性能
   - **步骤 4：结果分析**
     - 分析训练数据对模型生成 SQL 语句的影响
     - 对比不同层次训练数据对模型性能的提升
     - 对比最终训练的程序与 Gemini 成熟模型的差别 

---

## **考核标准**

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

 

### **所需资源**

**调用 Vanna 如下：**

```python
from vanna.remote import VannaDefault

vn = VannaDefault(api_key=MY_VANNA_API_KEY, model=MY_VANNA_MODEL)
```

 

**调用Gemini 如下：**

```python
import google.generativeai as genai

genai.configure(api_key=GEMINI_API_KEY)

gemini_model = genai.GenerativeModel(GEMINI_MODEL)
```

 

**使用 Vanna 生成SQL语句：**

```python
vn.generate_sql(question) # 其中question是要提问的问题
```

 

**使用Gemini 生成SQL语句：**

```python
prompt = f"Generate an SQL query for the following question: {question}"

response = gemini_model.generate_content(prompt)

sql_gemini = response.text.strip()
```

其中 prompt 是自定义提示词，使用generate_content方法与Gemini进行对话得到响应

结果，再对响应结果进行处理，将其处理为字符串

 

**使用 Vanna 进行训练：**

```python
for data in training_data:

    vn.train(ddl=data["ddl"], question=data["question"], sql=data["sql"])
```

其中”ddl” 描述表结构，”question”描述提问问题，”sql” 描述针对该问题对应的SQL语句

数据集链接：https://files.grouplens.org/datasets/movielens/ml-latest.zip
