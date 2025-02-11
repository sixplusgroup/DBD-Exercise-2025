import org.example.Database;
import org.example.Operation;
import org.example.Transaction;
import org.example.WAL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    private WAL wal;

    @BeforeEach
    public void setup() throws Exception {
        // 创建一个新的 WAL（Write-Ahead Log）和事务对象
        wal = new WAL();
    }

    @Test
    public void testInsertCommit() throws Exception {
        Transaction transaction = new Transaction("T1",wal);
        // 执行插入操作
        Operation insertOp = new Operation("INSERT", "key1", "value1");
        transaction.execute(insertOp);

        // 提交事务
        transaction.commit();

        // 验证数据库中是否包含插入的数据
        assertEquals("value1", Database.getData("key1"));
    }

    @Test
    public void testUpdateCommit() throws Exception {
        Transaction transaction = new Transaction("T1",wal);
        // 先插入一条数据
        Operation insertOp = new Operation("INSERT", "key1", "value1");
        transaction.execute(insertOp);

        // 更新该数据
        Operation updateOp = new Operation("UPDATE", "key1", "value2");
        transaction.execute(updateOp);

        // 提交事务
        transaction.commit();

        // 验证数据库中是否正确更新数据
        assertEquals("value2", Database.getData("key1"));
    }

    @Test
    public void testDeleteCommit() throws Exception {
        Transaction transaction = new Transaction("T1",wal);
        // 先插入一条数据
        Operation insertOp = new Operation("INSERT", "key1", "value1");
        transaction.execute(insertOp);

        // 删除该数据
        Operation deleteOp = new Operation("DELETE", "key1", null);
        transaction.execute(deleteOp);

        // 提交事务
        transaction.commit();

        // 验证数据库中是否已删除数据
        assertNull(Database.getData("key1"));
    }

    @Test
    public void testRollback() throws Exception {
        Transaction transaction = new Transaction("T1",wal);
        // 先插入一条数据
        Operation insertOp = new Operation("INSERT", "key1", "value1");
        transaction.execute(insertOp);

        // 执行回滚操作
        transaction.rollback();

        // 验证回滚后，数据库中应该没有该数据
        assertNull(Database.getData("key1"));
    }

    @Test
    public void testRollbackUpdate() throws Exception {
        Transaction transaction1 = new Transaction("T1",wal);
        // 插入数据并更新
        Operation insertOp = new Operation("INSERT", "key1", "value1");
        transaction1.execute(insertOp);

        transaction1.commit();

        Transaction transaction2 = new Transaction("T2",wal);

        Operation updateOp = new Operation("UPDATE", "key1", "value2");
        transaction2.execute(updateOp);

        // 执行回滚操作
        transaction2.rollback();

        // 验证回滚后数据恢复为插入时的状态
        assertEquals("value1", Database.getData("key1"));
    }

    @Test
    public void testRedoLog1() throws Exception {
        Transaction transaction = new Transaction("T1",wal);
        // 执行操作并提交事务
        Operation insertOp = new Operation("INSERT", "key1", "value1");
        transaction.execute(insertOp);
        transaction.commit();

        // 模拟崩溃后恢复数据库
        Database.getDown();
        wal.recover(); // 恢复日志

        // 验证数据是否恢复
        assertEquals("value1", Database.getData("key1"));
        assertEquals("1", Database.getData("k99"));
    }

    @Test
    public void testRedoLog2() throws Exception {
        // 模拟崩溃后恢复数据库
        Database.getDown();
        wal.recover(); // 恢复日志

        assertEquals("1", Database.getData("k99"));
    }

    @Test
    public void testUndoLog() throws Exception {
        Transaction transaction = new Transaction("T1",wal);
        // 插入数据
        Operation insertOp = new Operation("INSERT", "key1", "value1");
        transaction.execute(insertOp);

        // 回滚事务
        transaction.rollback();

        // 验证数据已回滚，删除操作已撤销
        assertNull(Database.getData("key1"));
    }

    @Test
    public void testMultipleOperations() throws Exception {
        Transaction transaction1 = new Transaction("T1",wal);
        // 插入、更新、删除操作
        Operation insertOp = new Operation("INSERT", "key1", "value1");
        transaction1.execute(insertOp);

        Operation updateOp = new Operation("UPDATE", "key1", "value2");
        transaction1.execute(updateOp);

        transaction1.commit();

        Transaction transaction2 = new Transaction("T2",wal);

        Operation deleteOp = new Operation("DELETE", "key1", null);
        transaction2.execute(deleteOp);

        // 回滚事务
        transaction2.rollback();

        // 验证回滚后，数据应恢复为T1提交时的状态
        assertEquals("value2", Database.getData("key1"));
    }

    @Test
    public void testMultipleOperations2() throws Exception {


        Transaction transaction1 = new Transaction("T1",wal);
        // 插入、更新、删除操作
        Operation insertOp = new Operation("INSERT", "key1", "value1");
        transaction1.execute(insertOp);

        Operation updateOp = new Operation("UPDATE", "key1", "value5");
        transaction1.execute(updateOp);

        transaction1.commit();

        //崩溃
        Database.getDown();
        wal.recover();

        Transaction transaction2 = new Transaction("T2",wal);

        Operation deleteOp = new Operation("DELETE", "key1", null);
        transaction2.execute(deleteOp);

        // 回滚事务
        transaction2.rollback();

        // 验证回滚后，数据应恢复为T1提交时的状态
        assertEquals("value5", Database.getData("key1"));
        assertEquals("1", Database.getData("k99"));
    }

    @Test
    public void testSavepointInsertUpdateRollback() throws Exception {
        Transaction transaction = new Transaction("T1", wal);
        // 插入一条数据
        Operation insertOp = new Operation("INSERT", "key1", "value1");
        transaction.execute(insertOp);

        // 设置保存点
        transaction.setSavepoint();

        // 更新数据
        Operation updateOp = new Operation("UPDATE", "key1", "value2");
        transaction.execute(updateOp);

        // 回滚到保存点
        transaction.rollbackToSavepoint();

        // 验证数据恢复到保存点状态
        assertEquals("value1", Database.getData("key1"));
    }

    @Test
    public void testSavepointMultipleOperationsRollback() throws Exception {
        Transaction transaction = new Transaction("T1", wal);
        // 插入一条数据
        Operation insertOp1 = new Operation("INSERT", "key1", "value1");
        transaction.execute(insertOp1);
        // 更新数据
        Operation updateOp = new Operation("UPDATE", "key1", "value2");
        transaction.execute(updateOp);

        // 设置保存点
        transaction.setSavepoint();

        // 继续操作，插入另一条数据
        Operation insertOp2 = new Operation("INSERT", "key2", "value3");
        transaction.execute(insertOp2);

        // 回滚到保存点
        transaction.rollbackToSavepoint();

        // 验证数据恢复到保存点状态
        assertEquals("value2", Database.getData("key1"));
        assertNull(Database.getData("key2"));
    }
}

