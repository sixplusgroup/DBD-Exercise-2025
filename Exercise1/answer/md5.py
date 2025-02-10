import pymysql
import hashlib

def hash_numeric_column(
    host: str,
    user: str,
    password: str,
    db_name: str,
    table_name: str,
    primary_key: str,
    numeric_column: str,
    batch_size: int = 10000
) -> str:
    """
    对某表的一个数值列分批计算哈希，保证行顺序稳定（按 primary_key 排序）。
    返回最终 MD5。
    """
    connection = pymysql.connect(
        host=host,
        user=user,
        password=password,
        db=db_name,
        charset='utf8mb4',
        cursorclass=pymysql.cursors.DictCursor
    )
    md5_obj = hashlib.md5()
    
    try:
        with connection.cursor() as cursor:
            # 排序很关键，避免行顺序不同导致哈希结果不同
            sql = f"""
                SELECT {primary_key}, {numeric_column}
                FROM {table_name}
                ORDER BY {primary_key} ASC
            """
            cursor.execute(sql)
            
            while True:
                rows = cursor.fetchmany(batch_size)
                if not rows:
                    break
                for row in rows:
                    pk_val = row[primary_key]
                    num_val = row[numeric_column]
                    # 拼接为统一的字符串格式: 主键:数值
                    # 如果数值包含小数，要确保格式一致，比如固定小数位
                    data_str = f"{pk_val}:{num_val}"
                    md5_obj.update(data_str.encode('utf-8'))
                    
    finally:
        connection.close()

    return md5_obj.hexdigest()

# 示例调用
if __name__ == "__main__":
    HOST = '127.0.0.1'
    USER = 'root'
    PASSWORD = '123456'
    DB_NAME = 'mydb'
    TABLE_NAME = 'mytable'
    PRIMARY_KEY = 'id'
    NUMERIC_COLUMN = 'value_col'

    source_hash = hash_numeric_column(
        host=HOST,
        user=USER,
        password=PASSWORD,
        db_name=DB_NAME,
        table_name=TABLE_NAME,
        primary_key=PRIMARY_KEY,
        numeric_column=NUMERIC_COLUMN
    )
    print("源端哈希:", source_hash)

    # 传输后，在目标库(或同库的备份表)计算再对比：
    # 这里示例中写同样的连接信息，你根据实际换成目标库就好
    dest_hash = hash_numeric_column(
        host=HOST,
        user=USER,
        password=PASSWORD,
        db_name=DB_NAME,
        table_name=TABLE_NAME,
        primary_key=PRIMARY_KEY,
        numeric_column=NUMERIC_COLUMN
    )
    print("目标端哈希:", dest_hash)

    if source_hash == dest_hash:
        print("校验通过：数值列数据完全一致。")
    else:
        print("校验失败：哈希不一致，可能有数据被改动或丢失。")
